package seedu.task.logic.commands;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.task.logic.commands.exceptions.CommandException;
import seedu.task.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be
 * executed.
 */
public abstract class Command {
	protected Model model;

	/**
	 * Constructs a feedback message to summarize an operation that displayed a
	 * listing of tasks.
	 *
	 * @param displaySize
	 *            used to generate summary
	 * @return summary message for persons displayed
	 */
	public static String getMessageForTaskListShownSummary(int displaySize) {
		return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
	}

	/**
	 * Raises an event to indicate an attempt to execute an incorrect command
	 */
	protected void indicateAttemptToExecuteIncorrectCommand() {
		EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(this));
	}

	/**
	 * Executes the command and returns the result message.
	 *
	 * @return feedback message of the operation result for display
	 * @throws CommandException
	 *             If an error occurs during command execution.
	 */
	public abstract CommandResult execute() throws CommandException;

	/**
	 * Provides any needed dependencies to the command. Commands making use of
	 * any of these should override this method to gain access to the
	 * dependencies.
	 */
	public void setData(Model model) {
		this.model = model;
	}

	public boolean isUndoable() {
		return false;
	}

}
