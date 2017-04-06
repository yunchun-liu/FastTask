package seedu.task.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.events.model.TaskManagerChangedEvent;
import seedu.task.commons.events.storage.DataSavingExceptionEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.UserPrefs;

/**
 * Manages storage of TaskManager data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

	private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
	private TaskManagerStorage taskManagerStorage;
	private UserPrefsStorage userPrefsStorage;

	public StorageManager(TaskManagerStorage taskManagerStorage, UserPrefsStorage userPrefsStorage) {
		super();
		this.taskManagerStorage = taskManagerStorage;
		this.userPrefsStorage = userPrefsStorage;
	}

	public StorageManager(String taskManagerFilePath, String userPrefsFilePath) {
		this(new XmlTaskManagerStorage(taskManagerFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
	}

	// ================ UserPrefs methods ==============================

	@Override
	public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
		return userPrefsStorage.readUserPrefs();
	}

	@Override
	public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
		userPrefsStorage.saveUserPrefs(userPrefs);
	}

	// ================ Task Manager methods ==============================

	@Override
	public String getTaskManagerFilePath() {
		return taskManagerStorage.getTaskManagerFilePath();
	}

	@Override
	public Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException {
		return readTaskManager(taskManagerStorage.getTaskManagerFilePath());
	}

	@Override
	public Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws DataConversionException, IOException {
		logger.fine("Attempting to read data from file: " + filePath);
		return taskManagerStorage.readTaskManager(filePath);
	}

	@Override
	public void saveTaskManager(ReadOnlyTaskManager taskManager) throws IOException {
		saveTaskManager(taskManager, taskManagerStorage.getTaskManagerFilePath());
	}

	@Override
	public void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
		logger.fine("Attempting to write to data file: " + filePath);
		taskManagerStorage.saveTaskManager(taskManager, filePath);
	}

	@Override
	@Subscribe
	public void handleTaskManagerChangedEvent(TaskManagerChangedEvent event) {
		logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
		try {
			saveTaskManager(event.data);
		} catch (IOException e) {
			raise(new DataSavingExceptionEvent(e));
		}
	}

	@Override
	public void setPathName(String pathName) {
		taskManagerStorage.setPathName(pathName);
	}

}
