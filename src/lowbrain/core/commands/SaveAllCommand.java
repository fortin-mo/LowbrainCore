package lowbrain.core.commands;

import lowbrain.library.command.Command;
import org.bukkit.command.CommandSender;

public class SaveAllCommand extends Command{
    public SaveAllCommand() {
        super("saveall");
    }

    @Override
    public CommandStatus execute(CommandSender who, String[] args, String cmd) {
        return null;
    }
}
