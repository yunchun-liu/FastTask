package seedu.task.logic.commands;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.events.ui.JumpToListRequestEvent;
import seedu.task.logic.commands.exceptions.CommandException;
import seedu.task.model.task.ReadOnlyTask;

/**
 * Selects a task identified using it's last displayed index from the task
 * manager.
 */
public class SelectCommand extends Command {

	public final int targetIndex;

	public static final String COMMAND_WORD = "google";

	public static final String MESSAGE_USAGE = COMMAND_WORD
			+ ": Googles the task identified by the index number used in the last task listing.\n"
			+ "Parameters: INDEX (must be a positive integer)\n" + "Example: " + COMMAND_WORD + " 1";

	public static final String MESSAGE_SELECT_TASK_SUCCESS = "Googled Task: %1$s";

	public SelectCommand(int targetIndex) {
		this.targetIndex = targetIndex;
	}

	@Override
	public CommandResult execute() throws CommandException {

		UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

		if (lastShownList.size() < targetIndex) {
			throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
		}

		EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex - 1));
		return new CommandResult(String.format(MESSAGE_SELECT_TASK_SUCCESS, targetIndex));

	}

}
