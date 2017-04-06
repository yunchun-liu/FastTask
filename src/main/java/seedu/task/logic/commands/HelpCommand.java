package seedu.task.logic.commands;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.events.ui.ShowHelpRequestEvent;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

	public static final String COMMAND_WORD = "help";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n" + "Example: "
			+ COMMAND_WORD;

	public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

	@Override
	public CommandResult execute() {
		EventsCenter.getInstance().post(new ShowHelpRequestEvent());
		return new CommandResult(SHOWING_HELP_MESSAGE);
	}
}
