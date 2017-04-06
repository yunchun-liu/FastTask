package guitests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.task.model.task.ReadOnlyTask;

public class SelectCommandTest extends TaskManagerGuiTest {

	@Test
	public void selectPerson_nonEmptyList() {

		assertSelectionInvalid(10); // invalid index
		assertNoPersonSelected();

		assertSelectionSuccess(1); // first person in the list
		int personCount = td.getTypicalTasks().length;
		assertSelectionSuccess(personCount); // last person in the list
		int middleIndex = personCount / 2;
		assertSelectionSuccess(middleIndex); // a person in the middle of the
												// list

		assertSelectionInvalid(personCount + 1); // invalid index
		assertPersonSelected(middleIndex); // assert previous selection remains

		/*
		 * Testing other invalid indexes such as -1 should be done when testing
		 * the SelectCommand
		 */
	}

	@Test
	public void selectPerson_emptyList() {
		commandBox.runCommand("clear");
		assertListSize(0);
		assertSelectionInvalid(1); // invalid index
	}

	private void assertSelectionInvalid(int index) {
		commandBox.runCommand("select " + index);
		assertResultMessage("The person index provided is invalid");
	}

	private void assertSelectionSuccess(int index) {
		commandBox.runCommand("select " + index);
		assertResultMessage("Selected Person: " + index);
		assertPersonSelected(index);
	}

	private void assertPersonSelected(int index) {
		assertEquals(taskListPanel.getSelectedPersons().size(), 1);
		ReadOnlyTask selectedPerson = taskListPanel.getSelectedPersons().get(0);
		assertEquals(taskListPanel.getPerson(index - 1), selectedPerson);
		// TODO: confirm the correct page is loaded in the Browser Panel
	}

	private void assertNoPersonSelected() {
		assertEquals(taskListPanel.getSelectedPersons().size(), 0);
	}

}
