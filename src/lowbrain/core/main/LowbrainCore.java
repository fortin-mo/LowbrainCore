package lowbrain.core.main;

import java.util.*;

import lowbrain.core.commun.Settings;
import lowbrain.core.events.ArmorEquipListener;
import lowbrain.core.events.CoreListener;
import lowbrain.core.handlers.CommandHandler;
import lowbrain.core.handlers.ConfigHandler;
import lowbrain.core.handlers.PlayerHandler;
import lowbrain.core.rpg.LowbrainSkill;
import lowbrain.library.fn;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import lowbrain.core.commun.*;
import org.jetbrains.annotations.Contract;

/**
 * LowbrainCore plugin class
 * @author lowbrain
 *
 */
public class LowbrainCore extends JavaPlugin {

	private static LowbrainCore instance;

	private HashMap<String,ItemRequirements> itemsRequirements;
	private HashMap<String,LowbrainSkill> skills;
	private PlayerHandler playerHandler;
	private ConfigHandler configHandler;

	private static final String CLASSES_V = "1.0";
	private static final String CONFIG_V = "1.0";
	private static final String ITEMS_REQUIREMENTS_V = "1.0";
	private static final String MOBS_XP_V = "1.0";
	private static final String POWERS_V = "1.0";
	private static final String RACES_V = "1.0";
	private static final String SKILLS_V = "1.0";
	private static final String PARAMETERS_V = "1.0";


	public boolean useHolographicDisplays = false;
	public boolean useArmorEquipEvent = false;
	public boolean useLowbrainMoblevel = false;
	public boolean useParties = false;
	public boolean useLowbrainItems = false;

    private NamespacedKey namespacedKey;

	@Contract(pure = true)
    public static LowbrainCore getInstance(){
		return instance;
	}

	/**
	 * called when the plugin is initially enabled
	 */
	@Override
    public void onEnable() {
		try {
			instance = this;

			playerHandler = new PlayerHandler(this);

			namespacedKey = new NamespacedKey(this, "LowbrainCore");

            configHandler = new ConfigHandler(this).load();

			validateConfigVersion();

			useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
			log("HologramDisplays is "+(useHolographicDisplays ? "enabled" : "disabled")+" !");

			useArmorEquipEvent = Bukkit.getPluginManager().isPluginEnabled("ArmorEquipEvent");
            log("ArmorEquipEvent is "+ (useArmorEquipEvent ? "enabled" : "disabled") +" !");

			useParties = Settings.getInstance().isGroupXpEnableParties() && Bukkit.getPluginManager().isPluginEnabled("Parties");
            log("Parties is "+ (useParties ? "enabled" : "disabled") +" !");

			useLowbrainItems = Bukkit.getPluginManager().isPluginEnabled("LowbrainItems");
            log("LowbrainItems is "+ (useLowbrainItems ? "enabled" : "disabled") +" !");

			if(!evaluateFunctions()){
				this.warn("[ERROR] functions in config file are not correctly formatted !!!");
				this.warn("[ERROR] LowbrainCore cannot load !");
                getServer().getPluginManager().disablePlugin(this);
				return;
			}

			getServer().getPluginManager().registerEvents(new CoreListener(this), this);

			if(useArmorEquipEvent)
				getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);

			this.getCommand("lbcore").setExecutor(new CommandHandler(this));

			log(getDescription().getVersion() + " enabled!");

			if(Settings.getInstance().isAutoSave()) {
				Bukkit.getServer().getScheduler().runTaskTimer((Plugin) this, new Runnable() {
					@Override
					public void run() {
						saveData();
					}
				}, 0, Settings.getInstance().getSaveInterval() * 20);
			}

		} catch (Exception e){
			e.printStackTrace();
		}
    }

    @Override
    public void onDisable() {
    	saveData();
		Bukkit.getServer().getScheduler().cancelTasks(this);
    }

	/**
	 * save all player data
	 */
	public void saveData(){
		this.getPlayerHandler().saveData();
		debugInfo("Data saved correctly");
	}

	/**
	 * log debug message
	 * @param msg message
	 */
	public void debugInfo(Object msg) {
	    if(Settings.getInstance().isDebug())
	        this.getLogger().info("[DEBUG]: " + msg);
    }

	/**
	 * log debug message has waning
	 * @param msg message
	 */
	public void debugWarning(Object msg) {
		if(Settings.getInstance().isDebug())
			this.getLogger().warning("[DEBUG]: " + msg);
	}

    /**
     * log warn messages
     * @param msg message
     */
	public void warn(Object msg) {
	    this.getLogger().warning("" + msg);
    }

    /**
     * log message
     * @param msg
     */
    public void log(Object msg) {
        this.getLogger().info("" + msg);
    }

	/**
	 * evaluate all string functions from config file
	 * @return false if one function fails
	 */
    private boolean evaluateFunctions(){
    	List<String> functions = new ArrayList<String>();

		functionsLookup(configHandler.config(),functions);

		for (String key: configHandler.powers().getKeys(false))
			functionsLookup(configHandler.powers().getConfigurationSection(key),functions);

		boolean succeed = true;
		try{
			for (String func: functions)
				fn.eval(Helper.FormatStringWithValues(func.split(","),null));
		}
		catch(Exception e){
			succeed = false;
		}

    	return succeed;
	}

	/**
	 * go through all the section in the config file and check for function
	 * this way, if a function does not compute correctly (not well formated)
	 * it will be displayed in the console so the admin can modify it
	 * @param start first configuration section to recurse into
	 * @param functions list of found function
	 */
	private void functionsLookup(ConfigurationSection start, List<String> functions){
		if(start == null){
			this.warn("Could not find settings !");
			return;
		}
		for (String key: start.getKeys(false)) {
			if(key.equals("function") && !fn.StringIsNullOrEmpty(start.getString(key)))
				functions.add(start.getString(key));
			else if(start.getConfigurationSection(key) != null)
				functionsLookup(start.getConfigurationSection(key),functions);

		}
	}

	/**
	 * load skill from skills.yml
	 */
	private void loadSkills(){
		this.skills = new HashMap<>();
		for (String skillName: configHandler.skills().getKeys(false)) {
			if(configHandler.skills().getBoolean(skillName + ".enable"))
				this.skills.put(skillName,new LowbrainSkill(skillName));

		}
	}

	/**
	 * load item requirements from itemrequirements.yml
	 */
	private void loadItemsRequirements(){
		this.itemsRequirements = new HashMap<String,ItemRequirements>();
		for (String n:configHandler.itemsRequirements().getKeys(false)) {
			ItemRequirements i = new ItemRequirements(n);

			ConfigurationSection sec = configHandler.itemsRequirements().getConfigurationSection(n);

			for (String r: sec.getKeys(false))
				i.getRequirements().put(r,sec.getInt(r));

			this.itemsRequirements.put(n,i);
		}
	}

	/**
	 * validate config file versions
	 */
	private void validateConfigVersion(){
		ConfigurationSection versions = configHandler.config().getConfigurationSection("versions");

		if(versions == null)
		    return;

		boolean valid = true;

		if(!versions.getString("config","").equals(CONFIG_V)){
			this.warn("config.yml is outdated");
			valid = false;
		}
		if(!versions.getString("classes","").equals(CLASSES_V)){
            this.warn("classes.yml is outdated");
			valid = false;
		}
		if(!versions.getString("itemsrequirements","").equals(ITEMS_REQUIREMENTS_V)){
            this.warn("itemsrequirements.yml is outdated");
			valid = false;
		}
		if(!versions.getString("mobsxp","").equals(MOBS_XP_V)){
            this.warn("mobsxp.yml is outdated");
			valid = false;
		}
		if(!versions.getString("powers","").equals(POWERS_V)){
            this.warn("powers.yml is outdated");
			valid = false;
		}
		if(!versions.getString("races","").equals(RACES_V)){
            this.warn("races.yml is outdated");
			valid = false;
		}
		if(!versions.getString("skills","").equals(SKILLS_V)){
            this.warn("skills.yml is outdated");
			valid = false;
		}
		if(!versions.getString("parameters","").equals(PARAMETERS_V)){
            this.warn("parameters files are outdated");
			valid = false;
		}

		if(!valid){
            this.warn("Your config files are outdated");
            this.warn("To update : Make a copy of your current .yml files in the plugin directory");
            this.warn("Then delete all of them and reload your server.");
		}

	}

    /**
     * get the list of loaded item requirements
     * @return item requirements
     */
	public HashMap<String, ItemRequirements> getItemsRequirements() {
		return itemsRequirements;
	}

    /**
     * get the list of loaded skills
     * @return skills
     */
	public HashMap<String, LowbrainSkill> getSkills() {
		return skills;
	}

    /**
     * get PlayerHandler instance
     * @return playerHandler
     */
	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

    /**
     * get Configuration handler
     * @return this.configHandler
     */
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    /**
     * get the lowbrain namespaced key
     * @return namespaceKey
     */
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    /**
	 * inner class for item requirements
	 */
	public class ItemRequirements {
		private String name;
		private HashMap<String,Integer> requirements = new HashMap<String,Integer>();
		public ItemRequirements(String name){
			this.name = name;
		}


		public String getName() {
			return name;
		}

		public HashMap<String, Integer> getRequirements() {
			return requirements;
		}
	}
}

