package seedu.task.storage;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.XmlUtil;

/**
 * Stores task manager data in an XML file
 */
public class XmlFileStorage {
	/**
	 * Saves the given task manager data to the specified file.
	 */
	public static void saveDataToFile(File file, XmlSerializableTaskManager taskManager) throws FileNotFoundException {
		try {
			XmlUtil.saveDataToFile(file, taskManager);
		} catch (JAXBException e) {
			assert false : "Unexpected exception " + e.getMessage();
		}
	}

	/**
	 * Returns task manager in the file or an empty task manager
	 */
	public static XmlSerializableTaskManager loadDataFromSaveFile(File file)
			throws DataConversionException, FileNotFoundException {
		try {
			return XmlUtil.getDataFromFile(file, XmlSerializableTaskManager.class);
		} catch (JAXBException e) {
			throw new DataConversionException(e);
		}
	}

}
