package lowbrain.core.commands;

import lowbrain.library.command.Command;
import org.bukkit.command.CommandSender;

public class StatsCommand extends Command{
    public StatsCommand() {
        super("stats");
    }

    @Override
    public CommandStatus execute(CommandSender who, String[] args, String cmd) {
        return null;
    }
}
