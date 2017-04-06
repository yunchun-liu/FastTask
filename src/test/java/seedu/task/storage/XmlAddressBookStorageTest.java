package seedu.task.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.FileUtil;
import seedu.task.model.TaskManager;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.task.Task;
import seedu.task.storage.XmlTaskManagerStorage;
import seedu.task.testutil.TypicalTestTasks;

public class XmlAddressBookStorageTest {
	private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlTaskManagerStorageTest/");

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void readTaskManager_nullFilePath_assertionFailure() throws Exception {
		thrown.expect(AssertionError.class);
		readTaskManager(null);
	}

	private java.util.Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws Exception {
		return new XmlTaskManagerStorage(filePath).readTaskManager(addToTestDataPathIfNotNull(filePath));
	}

	private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
		return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER + prefsFileInTestDataFolder : null;
	}

	@Test
	public void read_missingFile_emptyResult() throws Exception {
		assertFalse(readTaskManager("NonExistentFile.xml").isPresent());
	}

	@Test
	public void read_notXmlFormat_exceptionThrown() throws Exception {

		thrown.expect(DataConversionException.class);
		readTaskManager("NotXmlFormatAddressBook.xml");

		/*
		 * IMPORTANT: Any code below an exception-throwing line (like the one
		 * above) will be ignored. That means you should not have more than one
		 * exception test in one method
		 */
	}

	@Test
	public void readAndSaveTaskManager_allInOrder_success() throws Exception {
		String filePath = testFolder.getRoot().getPath() + "TempTaskManager.xml";
		TypicalTestTasks td = new TypicalTestTasks();
		TaskManager original = td.getTypicalTaskManager();
		XmlTaskManagerStorage xmlTaskManagerStorage = new XmlTaskManagerStorage(filePath);

		// Save in new file and read back
		xmlTaskManagerStorage.saveTaskManager(original, filePath);
		ReadOnlyTaskManager readBack = xmlTaskManagerStorage.readTaskManager(filePath).get();
		assertEquals(original, new TaskManager(readBack));

		// Modify data, overwrite exiting file, and read back
		original.addJobTask(new Task(td.zoo));
		original.removeTask(new Task(td.yam));
		xmlTaskManagerStorage.saveTaskManager(original, filePath);
		readBack = xmlTaskManagerStorage.readTaskManager(filePath).get();
		assertEquals(original, new TaskManager(readBack));

		// Save and read without specifying file path
		original.addJobTask(new Task(td.cereals));
		xmlTaskManagerStorage.saveTaskManager(original); // file path not
															// specified
		readBack = xmlTaskManagerStorage.readTaskManager().get(); // file path
																	// not
																	// specified
		assertEquals(original, new TaskManager(readBack));

	}

	@Test
	public void saveAddressBook_nullAddressBook_assertionFailure() throws IOException {
		thrown.expect(AssertionError.class);
		saveAddressBook(null, "SomeFile.xml");
	}

	private void saveAddressBook(ReadOnlyTaskManager addressBook, String filePath) throws IOException {
		new XmlTaskManagerStorage(filePath).saveTaskManager(addressBook, addToTestDataPathIfNotNull(filePath));
	}

	@Test
	public void saveAddressBook_nullFilePath_assertionFailure() throws IOException {
		thrown.expect(AssertionError.class);
		saveAddressBook(new TaskManager(), null);
	}

}
