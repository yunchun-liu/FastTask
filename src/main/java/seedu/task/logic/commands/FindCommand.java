package seedu.task.logic.commands;

import java.util.Set;

/**
 * Finds and lists all tasks in task manager whose name contains any of the
 * argument keywords. Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

	public static final String COMMAND_WORD = "find";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose names contain any of "
			+ "the specified keywords (not case-sensitive) and displays them as a list.\n"
			+ "Parameters: KEYWORD [MORE_KEYWORDS]...\n" + "Example: " + COMMAND_WORD + "Groceries";

	private final Set<String> keywords;

	public FindCommand(Set<String> keywords) {
		this.keywords = keywords;
	}

	@Override
	public CommandResult execute() {
		model.updateFilteredTaskList(keywords);
		return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
	}

}
