package seedu.task.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.CommandResult;
import seedu.task.logic.commands.UpdateBackupCommand;
import seedu.task.logic.commands.exceptions.CommandException;
import seedu.task.logic.parser.Parser;
import seedu.task.model.Model;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
	private final Logger logger = LogsCenter.getLogger(LogicManager.class);

	private final Model model;
	private final Parser parser;

	public LogicManager(Model model, Storage storage) {
		this.model = model;
		this.parser = new Parser();
	}

	@Override
	public CommandResult execute(String commandText) throws CommandException {
		logger.info("----------------[USER COMMAND][" + commandText + "]");
		Command command = parser.parseCommand(commandText);
		command.setData(model);
		if (command.isUndoable()) {
			UpdateBackupCommand ubc = new UpdateBackupCommand();
			ubc.setData(model);
			ubc.execute();
		}
		return command.execute();
	}

	@Override
	public ObservableList<ReadOnlyTask> getFilteredTaskList() {
		return model.getFilteredTaskList();
	}
}
