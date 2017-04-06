package seedu.task.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.task.commons.core.FileNameHandler;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.FileUtil;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.logic.commands.PathCommand;
import seedu.task.model.task.TaskPath;
import seedu.task.logic.parser.PathCommandParser;

/**
 * A class to access TaskManager data stored as an xml file on the hard disk.
 */
public class XmlTaskManagerStorage implements TaskManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskManagerStorage.class);

    private String filePath;

    public XmlTaskManagerStorage(String filePath) {
	this.filePath = filePath;
    }

    public String getTaskManagerFilePath() {
	return FileNameHandler.getFileName();
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException {
	return readTaskManager(filePath);
    }
    /**
     * Similar to {@link #readTaskManager()}
     * @param filePath
     *            location of the data. Cannot be null
     * @throws DataConversionException
     *             if the file is not in the correct format.
     */
    public Optional<ReadOnlyTaskManager> readTaskManager(String filePath)
	    throws DataConversionException, FileNotFoundException {
	assert filePath != null;

	File taskManagerFile = new File(filePath);

	if (!taskManagerFile.exists()) {
	    logger.info("Task Manager file " + taskManagerFile + " not found");
	    return Optional.empty();
	}

	ReadOnlyTaskManager taskManagerOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

	return Optional.of(taskManagerOptional);
    }

    @Override
    public void saveTaskManager(ReadOnlyTaskManager taskManager) throws IOException {
	saveTaskManager(taskManager, filePath);
    }

    /**
     * Similar to {@link #saveTaskManager(ReadOnlyTaskManager)}
     * @param filePath
     *            location of the data. Cannot be null
     */
    public void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
	assert taskManager != null;
	assert filePath != null;

	// if(PathCommand.getPath() != null){
	// filePath = PathCommand.getPath();
	// }
	// filePath = "/Users/jlevy/";

	File file = new File(filePath);
	FileUtil.createIfMissing(file);
	XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskManager(taskManager));
    }

    @Override
    public void setPathName(String pathName) {
	filePath = pathName;
    }

}
