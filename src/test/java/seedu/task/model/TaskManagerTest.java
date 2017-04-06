package seedu.task.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.task.model.TaskManager;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.Task;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.testutil.TypicalTestTasks;

public class TaskManagerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final TaskManager taskManager = new TaskManager();

	@Test
	public void constructor() {
		assertEquals(Collections.emptyList(), taskManager.getTaskList());
		assertEquals(Collections.emptyList(), taskManager.getTagList());
	}

	@Test
	public void resetData_null_throwsAssertionError() {
		thrown.expect(AssertionError.class);
		taskManager.resetData(null);
	}

	@Test
	public void resetData_withValidReadOnlyTaskManager_replacesData() {
		TaskManager newData = new TypicalTestTasks().getTypicalTaskManager();
		taskManager.resetData(newData);
		assertEquals(newData, taskManager);
	}

	@Test
	public void resetData_withDuplicateTasks_throwsAssertionError() {
		TypicalTestTasks td = new TypicalTestTasks();
		// Repeat td.alice twice
		List<Task> newTasks = Arrays.asList(new Task(td.apples), new Task(td.apples));
		List<Tag> newTags = td.apples.getTags().asObservableList();
		TaskManagerStub newData = new TaskManagerStub(newTasks, newTags);

		thrown.expect(AssertionError.class);
		taskManager.resetData(newData);
	}

	@Test
	public void resetData_withDuplicateTags_throwsAssertionError() {
		TaskManager typicalTaskManager = new TypicalTestTasks().getTypicalTaskManager();
		List<ReadOnlyTask> newPersons = typicalTaskManager.getTaskList();
		List<Tag> newTags = new ArrayList<>(typicalTaskManager.getTagList());
		// Repeat the first tag twice
		newTags.add(newTags.get(0));
		TaskManagerStub newData = new TaskManagerStub(newPersons, newTags);

		thrown.expect(AssertionError.class);
		taskManager.resetData(newData);
	}

	/**
	 * A stub ReadOnlyTaskManager whose persons and tags lists can violate
	 * interface constraints.
	 */
	private static class TaskManagerStub implements ReadOnlyTaskManager {
		private final ObservableList<ReadOnlyTask> tasks = FXCollections.observableArrayList();
		private final ObservableList<Tag> tags = FXCollections.observableArrayList();

		TaskManagerStub(Collection<? extends ReadOnlyTask> tasks, Collection<? extends Tag> tags) {
			this.tasks.setAll(tasks);
			this.tags.setAll(tags);
		}

		@Override
		public ObservableList<ReadOnlyTask> getTaskList() {
			return tasks;
		}

		@Override
		public ObservableList<Tag> getTagList() {
			return tags;
		}
	}

}
