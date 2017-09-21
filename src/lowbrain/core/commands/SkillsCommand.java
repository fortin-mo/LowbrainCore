package lowbrain.core.commands;

import lowbrain.library.command.Command;
import org.bukkit.command.CommandSender;

public class SkillsCommand extends Command{
    public SkillsCommand() {
        super("skills");
    }

    @Override
    public CommandStatus execute(CommandSender who, String[] args, String cmd) {
        return null;
    }
}
