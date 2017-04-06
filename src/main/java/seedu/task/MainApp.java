package seedu.task;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.task.commons.core.Config;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.FileNameHandler;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.Version;
import seedu.task.commons.events.storage.LoadDataEvent;
import seedu.task.commons.events.ui.ChangePathNameEvent;
import seedu.task.commons.events.ui.ExitAppRequestEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ConfigUtil;
import seedu.task.commons.util.StringUtil;
import seedu.task.logic.Logic;
import seedu.task.logic.LogicManager;
import seedu.task.model.TaskManager;
import seedu.task.model.Model;
import seedu.task.model.ModelManager;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.UserPrefs;
import seedu.task.model.util.SampleDataUtil;
import seedu.task.storage.Storage;
import seedu.task.storage.StorageManager;
import seedu.task.ui.Ui;
import seedu.task.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {
	private static final Logger logger = LogsCenter.getLogger(MainApp.class);

	public static final Version VERSION = new Version(1, 0, 0, true);

	protected Ui ui;
	protected Logic logic;
	protected Storage storage;
	protected Model model;
	protected Config config;
	protected UserPrefs userPrefs;

	@Override
	public void init() throws Exception {
		logger.info("=============================[ Initializing Task Manager ]===========================");
		super.init();

		config = initConfig(getApplicationParameter("config"));
		storage = new StorageManager(config.getTaskManagerFilePath(), config.getUserPrefsFilePath());

		userPrefs = initPrefs(config);

		initLogging(config);

		model = initModelManager(storage, userPrefs);

		logic = new LogicManager(model, storage);

		ui = new UiManager(logic, config, userPrefs);

		initEventsCenter();
	}

	private String getApplicationParameter(String parameterName) {
		Map<String, String> applicationParameters = getParameters().getNamed();
		return applicationParameters.get(parameterName);
	}

	private Model initModelManager(Storage storage, UserPrefs userPrefs) {
		Optional<ReadOnlyTaskManager> taskManagerOptional;
		ReadOnlyTaskManager initialData;
		try {
			taskManagerOptional = storage.readTaskManager();
			if (!taskManagerOptional.isPresent()) {
				logger.info("Data file not found. Will be starting with a sample Task Manager");
			}
			initialData = taskManagerOptional.orElseGet(SampleDataUtil::getSampleTaskManager);
		} catch (DataConversionException e) {
			logger.warning("Data file not in the correct format. Will be starting with an empty Task Manager");
			initialData = new TaskManager();
		} catch (IOException e) {
			logger.warning("Problem while reading from the file. Will be starting with an empty Task Manager");
			initialData = new TaskManager();
		}

		return new ModelManager(initialData, userPrefs);
	}

	private void initLogging(Config config) {
		LogsCenter.init(config);
	}

	protected Config initConfig(String configFilePath) {
		Config initializedConfig;
		String configFilePathUsed;

		configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

		if (configFilePath != null) {
			logger.info("Custom Config file specified " + configFilePath);
			configFilePathUsed = configFilePath;
		}

		logger.info("Using config file : " + configFilePathUsed);

		try {
			Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
			initializedConfig = configOptional.orElse(new Config());
		} catch (DataConversionException e) {
			logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
					+ "Using default config properties");
			initializedConfig = new Config();
		}

		// Update config file in case it was missing to begin with or there are
		// new/unused fields
		try {
			ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
		} catch (IOException e) {
			logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
		}
		return initializedConfig;
	}

	protected UserPrefs initPrefs(Config config) {
		assert config != null;

		String prefsFilePath = config.getUserPrefsFilePath();
		logger.info("Using prefs file : " + prefsFilePath);

		UserPrefs initializedPrefs;
		try {
			Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
			initializedPrefs = prefsOptional.orElse(new UserPrefs());
		} catch (DataConversionException e) {
			logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
					+ "Using default user prefs");
			initializedPrefs = new UserPrefs();
		} catch (IOException e) {
			logger.warning("Problem while reading from the file. Will be starting with an empty Task Manager");
			initializedPrefs = new UserPrefs();
		}

		// Update prefs file in case it was missing to begin with or there are
		// new/unused fields
		try {
			storage.saveUserPrefs(initializedPrefs);
		} catch (IOException e) {
			logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
		}

		return initializedPrefs;
	}

	private void initEventsCenter() {
		EventsCenter.getInstance().registerHandler(this);
	}

	@Override
	public void start(Stage primaryStage) {
		logger.info("Starting Task Manager " + MainApp.VERSION);
		ui.start(primaryStage);
	}

	@Override
	public void stop() {
		logger.info("============================ [ Stopping Task Manager ] =============================");
		ui.stop();
		try {
			storage.saveUserPrefs(userPrefs);
		} catch (IOException e) {
			logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
		}
		Platform.exit();
		System.exit(0);
	}

	@Subscribe
	public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
		logger.info(LogsCenter.getEventHandlingLogMessage(event));
		this.stop();
	}

	// @@author A0163845X
	@Subscribe
	public void handleChangePathNameEvent(ChangePathNameEvent event) {
		System.out.println("working cpne");
		storage.setPathName(event.getPathName());
		FileNameHandler.setFileName(event.getPathName());
		try {
			storage.saveTaskManager(model.getTaskManager());
		} catch (IOException e) {
			System.out.println("Unexpected IOE in main app");
			storage.setPathName(FileNameHandler.DEFAULT_FILEPATH);
			FileNameHandler.setFileName(FileNameHandler.DEFAULT_FILEPATH);
		}
	}

	// @@author

	// @@author A0163845X
	@Subscribe
	public void handleLoadDataEvent(LoadDataEvent event) {
		System.out.println("working lde");
		for (int i = 0; i < 4; i++) {
			try {
				StorageManager temp = new StorageManager(event.getPathName(), config.getUserPrefsFilePath());
				System.out.println(temp.equals(storage));
				if (!temp.readTaskManager().isPresent()) {
					throw new Exception();
				}
				model.setTaskManager(temp.readTaskManager());
				FileNameHandler.setFileName(event.getPathName());
			} catch (Exception e) {
				System.out.println("Unexpected exception");
				storage.setPathName(FileNameHandler.DEFAULT_FILEPATH);
				FileNameHandler.setFileName(FileNameHandler.DEFAULT_FILEPATH);
			}
		}
	}
	// @@author

	public static void main(String[] args) {
		launch(args);
	}
}
