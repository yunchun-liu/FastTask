package guitests.guihandles;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import guitests.GuiRobot;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seedu.task.TestApp;
import seedu.task.model.task.Task;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.testutil.TestUtil;

/**
 * Provides a handle for the panel containing the person list.
 */
public class PersonListPanelHandle extends GuiHandle {

	public static final int NOT_FOUND = -1;
	public static final String CARD_PANE_ID = "#cardPane";

	private static final String PERSON_LIST_VIEW_ID = "#personListView";

	public PersonListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
		super(guiRobot, primaryStage, TestApp.APP_TITLE);
	}

	public List<ReadOnlyTask> getSelectedPersons() {
		ListView<ReadOnlyTask> personList = getListView();
		return personList.getSelectionModel().getSelectedItems();
	}

	public ListView<ReadOnlyTask> getListView() {
		return getNode(PERSON_LIST_VIEW_ID);
	}

	/**
	 * Returns true if the list is showing the person details correctly and in
	 * correct order.
	 * 
	 * @param persons
	 *            A list of person in the correct order.
	 */
	public boolean isListMatching(ReadOnlyTask... persons) {
		return this.isListMatching(0, persons);
	}

	/**
	 * Returns true if the list is showing the person details correctly and in
	 * correct order.
	 * 
	 * @param startPosition
	 *            The starting position of the sub list.
	 * @param persons
	 *            A list of person in the correct order.
	 */
	public boolean isListMatching(int startPosition, ReadOnlyTask... persons) throws IllegalArgumentException {
		if (persons.length + startPosition != getListView().getItems().size()) {
			throw new IllegalArgumentException(
					"List size mismatched\n" + "Expected " + (getListView().getItems().size() - 1) + " persons");
		}
		assertTrue(this.containsInOrder(startPosition, persons));
		for (int i = 0; i < persons.length; i++) {
			final int scrollTo = i + startPosition;
			guiRobot.interact(() -> getListView().scrollTo(scrollTo));
			guiRobot.sleep(200);
			if (!TestUtil.compareCardAndTask(getPersonCardHandle(startPosition + i), persons[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Clicks on the ListView.
	 */
	public void clickOnListView() {
		Point2D point = TestUtil.getScreenMidPoint(getListView());
		guiRobot.clickOn(point.getX(), point.getY());
	}

	/**
	 * Returns true if the {@code persons} appear as the sub list (in that
	 * order) at position {@code startPosition}.
	 */
	public boolean containsInOrder(int startPosition, ReadOnlyTask... persons) {
		List<ReadOnlyTask> personsInList = getListView().getItems();

		// Return false if the list in panel is too short to contain the given
		// list
		if (startPosition + persons.length > personsInList.size()) {
			return false;
		}

		// Return false if any of the persons doesn't match
		for (int i = 0; i < persons.length; i++) {
			if (!personsInList.get(startPosition + i).getTaskName().fullTaskName
					.equals(persons[i].getTaskName().fullTaskName)) {
				return false;
			}
		}

		return true;
	}

	public TaskCardHandle navigateToTask(String name) {
		guiRobot.sleep(500); // Allow a bit of time for the list to be updated
		final Optional<ReadOnlyTask> person = getListView().getItems().stream()
				.filter(p -> p.getTaskName().fullTaskName.equals(name)).findAny();
		if (!person.isPresent()) {
			throw new IllegalStateException("Name not found: " + name);
		}

		return navigateToPerson(person.get());
	}

	/**
	 * Navigates the listview to display and select the person.
	 */
	public TaskCardHandle navigateToPerson(ReadOnlyTask person) {
		int index = getPersonIndex(person);

		guiRobot.interact(() -> {
			getListView().scrollTo(index);
			guiRobot.sleep(150);
			getListView().getSelectionModel().select(index);
		});
		guiRobot.sleep(100);
		return getPersonCardHandle(person);
	}

	/**
	 * Returns the position of the person given, {@code NOT_FOUND} if not found
	 * in the list.
	 */
	public int getPersonIndex(ReadOnlyTask targetPerson) {
		List<ReadOnlyTask> personsInList = getListView().getItems();
		for (int i = 0; i < personsInList.size(); i++) {
			if (personsInList.get(i).getTaskName().equals(targetPerson.getTaskName())) {
				return i;
			}
		}
		return NOT_FOUND;
	}

	/**
	 * Gets a person from the list by index
	 */
	public ReadOnlyTask getPerson(int index) {
		return getListView().getItems().get(index);
	}

	public TaskCardHandle getPersonCardHandle(int index) {
		return getPersonCardHandle(new Task(getListView().getItems().get(index)));
	}

	public TaskCardHandle getPersonCardHandle(ReadOnlyTask person) {
		Set<Node> nodes = getAllCardNodes();
		Optional<Node> personCardNode = nodes.stream()
				.filter(n -> new TaskCardHandle(guiRobot, primaryStage, n).isSameTask(person)).findFirst();
		if (personCardNode.isPresent()) {
			return new TaskCardHandle(guiRobot, primaryStage, personCardNode.get());
		} else {
			return null;
		}
	}

	protected Set<Node> getAllCardNodes() {
		return guiRobot.lookup(CARD_PANE_ID).queryAll();
	}

	public int getNumberOfPeople() {
		return getListView().getItems().size();
	}
}
