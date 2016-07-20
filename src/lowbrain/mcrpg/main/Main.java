package lowbrain.mcrpg.main;

import java.io.File;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import lowbrain.mcrpg.commun.*;


/**
 * Main plugin class
 * @author lowbrain
 *
 */
public class Main extends JavaPlugin {

	public static Map<UUID, RPGPlayer> connectedPlayers = new HashMap<UUID, RPGPlayer>();
	public static Config config;
	public FileConfiguration classesConfig;
	public FileConfiguration racesConfig;
	public FileConfiguration powersConfig;

	private File classf, racef, powerf;
	/**
	 * called when the plugin is initially enabled
	 */
	@Override
    public void onEnable() {

		this.getLogger().info("Loading LowbrainMCRPG.jar");

		InitialisingConfigFile();

        PlayerListener playerListener = new PlayerListener(this);

	    getServer().getPluginManager().registerEvents(playerListener, this);
	    this.getCommand("mcrpg").setExecutor(new RPGCommand(this));
	    this.getLogger().info("[LowbrainMCRPG] " + getDescription().getVersion() + " enabled!");

		if(this.config.auto_save) {
			Bukkit.getServer().getScheduler().runTaskTimer((Plugin) this, new Runnable() {
				@Override
				public void run() {
					SaveData();
				}
			}, 0, config.save_interval * 20);
		}
    }

    private void InitialisingConfigFile(){
		this.saveDefaultConfig();

		config = new Config(this.getConfig());

		classf = new File(getDataFolder(),"classes.yml");
		racef = new File(getDataFolder(),"races.yml");
		powerf = new File(getDataFolder(),"powers.yml");

		if (!classf.exists()) {
			classf.getParentFile().mkdirs();
			saveResource("classes.yml", false);
		}
		if (!racef.exists()) {
			racef.getParentFile().mkdirs();
			saveResource("races.yml", false);
		}
		if (!powerf.exists()) {
			powerf.getParentFile().mkdirs();
			saveResource("powers.yml", false);
		}

		classesConfig = new YamlConfiguration();
		powersConfig = new YamlConfiguration();
		racesConfig = new YamlConfiguration();

		try {
			classesConfig.load(classf);
			powersConfig.load(powerf);
			racesConfig.load(racef);
		}catch (Exception e){
			e.printStackTrace();
		}


	}
   
    @Override
    public void onDisable() {
    	SaveData();
		Bukkit.getServer().getScheduler().cancelTasks(this);
    }

    public void SaveData(){
		connectedPlayers.forEach((uuid, rpgPlayer) -> rpgPlayer.SaveData());
		debugMessage("Data saved correctly");
	}

	public void debugMessage(Object msg){
	    if(this.config.debug){
	        this.getLogger().info("[DEBUG] : " + msg);
        }
    }
}

