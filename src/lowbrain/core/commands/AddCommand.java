package lowbrain.core.commands;

import lowbrain.library.command.Command;
import org.bukkit.command.CommandSender;

public class AddCommand extends Command{
    public AddCommand() {
        super("set");
    }

    @Override
    public CommandStatus execute(CommandSender who, String[] args, String cmd) {
        return null;
    }
}
