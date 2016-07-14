package lowbrain.mcrpg.main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RPGCommand implements CommandExecutor{
	private final Main plugin;
	
	public RPGCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Called when the plugin receice a command
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	return true;
    }
}
