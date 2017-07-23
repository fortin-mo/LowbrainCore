package lowbrain.core.main;

import java.util.*;

import lowbrain.core.commun.Settings;
import lowbrain.core.config.*;
import lowbrain.core.events.ArmorEquipListener;
import lowbrain.core.events.CoreListener;
import lowbrain.core.rpg.LowbrainSkill;
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

			this.getLogger().info("Loading LowbrainCore.jar");

			playerHandler = new PlayerHandler(this);

			namespacedKey = new NamespacedKey(this, "LowbrainCore");

			InitialisingConfigFile();

			validateConfigVersion();

			useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
			debugInfo("HologramDisplays is "+(useHolographicDisplays ? "enabled" : "disabled")+" !");

			useArmorEquipEvent = Bukkit.getPluginManager().isPluginEnabled("ArmorEquipEvent");
			debugInfo("ArmorEquipEvent is "+ (useArmorEquipEvent ? "enabled" : "disabled") +" !");

			useParties = Settings.getInstance().isGroupXpEnableParties() && Bukkit.getPluginManager().isPluginEnabled("Parties");
			debugInfo("Parties is "+ (useParties ? "enabled" : "disabled") +" !");

			useLowbrainItems = Bukkit.getPluginManager().isPluginEnabled("LowbrainItems");
            debugInfo("LowbrainItems is "+ (useLowbrainItems ? "enabled" : "disabled") +" !");

			if(!evaluateFunctions()){
				this.getLogger().info("[ERROR] functions in config file and not correctly formated !!!");
				this.getLogger().info("[ERROR] LowbrainCore.jar cannot load !");
				this.onDisable();
				return;
			}

			getServer().getPluginManager().registerEvents(new CoreListener(this), this);

			if(useArmorEquipEvent){
				getServer().getPluginManager().registerEvents(new ArmorEquipListener(this), this);
			}
			this.getCommand("lbcore").setExecutor(new CommandHandler(this));
			this.getLogger().info("[LowbrainCore] " + getDescription().getVersion() + " enabled!");

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

	/**
	 * first load config file
	 */
	private void InitialisingConfigFile(){
		try {
			Config.getInstance();
			Classes.getInstance();
			ItemsRequirements.getInstance();
			MobsXP.getInstance();
			Powers.getInstance();
			Races.getInstance();
			Skills.getInstance();
			Settings.getInstance();

			loadSkills();
			loadItemsRequirements();
		}catch (Exception e){
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
	 * log message
	 * @param msg
	 */
	public void debugInfo(Object msg){
	    if(Settings.getInstance().isDebug())
	        this.getLogger().info("[Lowbrain Core] : " + msg);
    }

	/**
	 * log message has waning
	 * @param msg
	 */
	public void debugWarning(Object msg){
		if(Settings.getInstance().isDebug())
			this.getLogger().warning("[DEBUG] : " + msg);
	}

	/**
	 * reload config
	 */
    public void reloadConfig(){
    	Classes.reload();
		Config.reload();
		ItemsRequirements.reload();
		MobsXP.reload();
		Powers.reload();
		Skills.reload();
		Races.reload();
		Settings.reload();
		getPlayerHandler().reload();
	}

	/**
	 * evaluate all string functions from config file
	 * @return false if one function fails
	 */
    private boolean evaluateFunctions(){
    	List<String> functions = new ArrayList<String>();

		recursiveConfigFunctionSearch(Config.getInstance(),functions);
		for (String key: Powers.getInstance().getKeys(false)
			 ) {
			recursiveConfigFunctionSearch(Powers.getInstance().getConfigurationSection(key),functions);
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

	/**
	 * go through all the section in the config file and check for function
	 * this way, if a function does not compute correctly (not well formated)
	 * it will be displayed in the console so the admin can modify it
	 * @param start
	 * @param functions
	 */
	private void recursiveConfigFunctionSearch(ConfigurationSection start, List<String> functions){
		if(start == null){
			this.debugInfo("Could not find settings !");
			return;
		}
		for (String key: start.getKeys(false)) {
			if(key.equals("function") && !Helper.StringIsNullOrEmpty(start.getString(key))){
				functions.add(start.getString(key));
			}
			else if(start.getConfigurationSection(key) != null){
				recursiveConfigFunctionSearch(start.getConfigurationSection(key),functions);
			}
		}
	}

	/**
	 * load skill from skills.yml
	 */
	private void loadSkills(){
		this.skills = new HashMap<>();
		for (String skillName :
				Skills.getInstance().getKeys(false)) {
			if(Skills.getInstance().getBoolean(skillName + ".enable")){
				this.skills.put(skillName,new LowbrainSkill(skillName));
			}
		}
	}

	/**
	 * load item requirements from itemrequirements.yml
	 */
	private void loadItemsRequirements(){
		this.itemsRequirements = new HashMap<String,ItemRequirements>();
		for (String n:ItemsRequirements.getInstance().getKeys(false)) {
			ItemRequirements i = new ItemRequirements(n);

			ConfigurationSection sec = ItemsRequirements.getInstance().getConfigurationSection(n);

			for (String r: sec.getKeys(false)) {
				i.getRequirements().put(r,sec.getInt(r));
			}
			this.itemsRequirements.put(n,i);
		}
	}

	/**
	 * validate config file versions
	 */
	private void validateConfigVersion(){
		ConfigurationSection versions = Config.getInstance().getConfigurationSection("versions");

		if(versions == null)return;

		boolean valid = true;

		if(!versions.getString("config","").equals(CONFIG_V)){
			this.getLogger().warning("config.yml is outdated");
			valid = false;
		}
		if(!versions.getString("classes","").equals(CLASSES_V)){
			this.getLogger().warning("classes.yml is outdated");
			valid = false;
		}
		if(!versions.getString("itemsrequirements","").equals(ITEMS_REQUIREMENTS_V)){
			this.getLogger().warning("itemsrequirements.yml is outdated");
			valid = false;
		}
		if(!versions.getString("mobsxp","").equals(MOBS_XP_V)){
			this.getLogger().warning("mobsxp.yml is outdated");
			valid = false;
		}
		if(!versions.getString("powers","").equals(POWERS_V)){
			this.getLogger().warning("powers.yml is outdated");
			valid = false;
		}
		if(!versions.getString("races","").equals(RACES_V)){
			this.getLogger().warning("races.yml is outdated");
			valid = false;
		}
		if(!versions.getString("skills","").equals(SKILLS_V)){
			this.getLogger().warning("skills.yml is outdated");
			valid = false;
		}
		if(!versions.getString("parameters","").equals(PARAMETERS_V)){
			this.getLogger().warning("parameters files are outdated");
			valid = false;
		}

		if(!valid){
			this.getLogger().warning("Your config files are outdated");
			this.getLogger().warning("To update : Make a copy of your current .yml files in the plugin directory");
			this.getLogger().warning("Then delete all of them and reload your server.");
		}

	}

	public HashMap<String, ItemRequirements> getItemsRequirements() {
		return itemsRequirements;
	}

	public HashMap<String, LowbrainSkill> getSkills() {
		return skills;
	}

	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

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

