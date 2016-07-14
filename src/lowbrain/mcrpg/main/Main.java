package lowbrain.mcrpg.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lowbrian.mcrpg.commun.RPGPlayer;

/**
 * Main plugin class
 * @author lowbrain
 *
 */
public class Main extends JavaPlugin {
	 
	FileConfiguration config;
	PlayerListener playerListener = new PlayerListener(this);
	public static List<RPGPlayer> lstPlayer = new ArrayList<RPGPlayer>();
	
	/**
	 * called when the plugin is initially enabled
	 */
	@Override
    public void onEnable() {
		this.getLogger().info("Loading LowbrainMCRPG.jar");
		this.config = this.getConfig();
	    this.saveDefaultConfig();
	    getServer().getPluginManager().registerEvents(this.playerListener, this);
	    this.getCommand("rpg").setExecutor(new RPGCommand(this));
	    this.getLogger().info("[LowbrainMCRPG] " + getDescription().getVersion() + " enabled!");
    }
   
    @Override
    public void onDisable() {
       this.config = null;
    }
}

