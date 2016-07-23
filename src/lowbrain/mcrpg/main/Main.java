package lowbrain.mcrpg.main;

import java.io.File;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
	public Config config;
	public static FileConfiguration classesConfig;
	public static FileConfiguration racesConfig;
	public static FileConfiguration powersConfig;

	private File classf, racef, powerf;
	/**
	 * called when the plugin is initially enabled
	 */
	@Override
    public void onEnable() {

		this.getLogger().info("Loading LowbrainMCRPG.jar");

		InitialisingConfigFile();

		if(!evatulateFunction()){
			this.getLogger().info("[ERROR] functions in config file and not correctly formated !!!");
			this.getLogger().info("[ERROR] LowbrainMCRPG.jar cannot load !");
			this.onDisable();
			return;
		}

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

    private boolean evatulateFunction(){
    	List<String> functions = new ArrayList<String>();
		recursiveConfigFunctionSearch(this.getConfig().getConfigurationSection("settings"),functions);

		for (String key: this.powersConfig.getKeys(false)
			 ) {
			recursiveConfigFunctionSearch(this.powersConfig.getConfigurationSection(key),functions);
		}


		boolean succeed = true;
		try{
			for (String funct :
					functions) {
				Helper.eval(Helper.FormatStringWithValues(funct.split(","),null));
			}
		}
		catch(Exception e){
			succeed = false;
		}

    	return succeed;
	}

	private void recursiveConfigFunctionSearch(ConfigurationSection start, List<String> functions){
		for (String key: start.getKeys(false)) {
			if(key.equals("function") && !Helper.StringIsNullOrEmpty(start.getString(key))){
				functions.add(start.getString(key));
			}
			else if(start.getConfigurationSection(key) != null){
				recursiveConfigFunctionSearch(start.getConfigurationSection(key),functions);
			}
		}
	}
}

