package lowbrain.core.commands;

import lowbrain.library.command.Command;
import org.bukkit.command.CommandSender;

public class ResetCommand extends Command{
    public ResetCommand() {
        super("reset");
    }

    @Override
    public CommandStatus execute(CommandSender who, String[] args, String cmd) {
        return null;
    }
}
