package lowbrain.mcrpg.main;

import java.io.File;
import java.util.*;

import lowbrain.mcrpg.rpg.RPGPlayer;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import lowbrain.mcrpg.commun.*;
import org.bukkit.scoreboard.*;


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
	public static FileConfiguration weaponsConfig;
	public static boolean useHolographicDisplays;

	private File classf, racef, powerf, weaponsf;
	/**
	 * called when the plugin is initially enabled
	 */
	@Override
    public void onEnable() {

		this.getLogger().info("Loading LowbrainMCRPG.jar");

		InitialisingConfigFile();
		useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
		String enabled = useHolographicDisplays ? "enabled" : "disabled";
		debugMessage("HologramDisplays is "+enabled+" !");

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


		ScoreboardManager manager = Bukkit.getScoreboardManager();

    }

    private void InitialisingConfigFile(){
		this.saveDefaultConfig();

		config = new Config(this.getConfig());

		classf = new File(getDataFolder(),"classes.yml");
		racef = new File(getDataFolder(),"races.yml");
		powerf = new File(getDataFolder(),"powers.yml");
		weaponsf = new File(getDataFolder(),"weaponsarmor.yml");

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
		if (!weaponsf.exists()) {
			powerf.getParentFile().mkdirs();
			saveResource("weaponsarmor.yml", false);
		}

		classesConfig = new YamlConfiguration();
		powersConfig = new YamlConfiguration();
		racesConfig = new YamlConfiguration();
		weaponsConfig = new YamlConfiguration();

		try {
			classesConfig.load(classf);
			powersConfig.load(powerf);
			racesConfig.load(racef);
			weaponsConfig.load(weaponsf);
			createCustomWeapons();
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

	private boolean createCustomWeapons(){
		try {
			for (String weapon :
					weaponsConfig.getKeys(false)) {

				if(weaponsConfig.getBoolean(weapon +".enable")){
					Material material = Material.valueOf(weaponsConfig.getString(weapon +".material"));
					if(material == null){
						this.getLogger().info("Material for " + weapon + " could not found !");
						return false;
					}
					ItemStack customWeapon = new ItemStack(material, 1);
					ItemMeta ESmeta = customWeapon.getItemMeta();

					ChatColor color = ChatColor.getByChar(weaponsConfig.getString(weapon +".display_color"));
					if(color == null){
						this.getLogger().info("Color for " + weapon + " could not found !");
						return false;
					}
					ESmeta.setDisplayName(color + weaponsConfig.getString(weapon + ".name"));
					customWeapon.setItemMeta(ESmeta);

					ConfigurationSection attributes = weaponsConfig.getConfigurationSection(weapon + ".attributes");
					if(attributes == null){
						this.getLogger().info("Missing attributes section for " + weapon);
						return false;
					}
					net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(customWeapon);
					NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
					NBTTagList modifiers = new NBTTagList();

					for (String attribute :
							attributes.getKeys(false)) {
						NBTTagCompound modifier = new NBTTagCompound();
						modifier.set("AttributeName", new NBTTagString("generic." + attributes.getString(attribute +".attribute_name")));
						modifier.set("Name", new NBTTagString(attributes.getString(attribute +".name")));
						modifier.set("Amount", new NBTTagDouble(attributes.getDouble(attribute +".amount")));
						modifier.set("Operation", new NBTTagInt(attributes.getInt(attribute +".operation")));
						modifier.set("UUIDLeast", new NBTTagInt(894654));
						modifier.set("UUIDMost", new NBTTagInt(2872));

						String slots = attributes.getString(attribute +".slots");
						if(slots.length() > 0){
							modifier.set("Slot", new NBTTagString(slots));
						}

						modifiers.add(modifier);
					}

					ConfigurationSection enchts = weaponsConfig.getConfigurationSection(weapon + ".enchantments");
					NBTTagList enchModifiers = new NBTTagList();
					if (enchts != null) {
						for (String ench :
								enchts.getKeys(false)) {
							NBTTagCompound modifier = new NBTTagCompound();
							modifier.set("id", new NBTTagInt(attributes.getInt(ench +".id")));
							modifier.set("lvl", new NBTTagInt(attributes.getInt(ench +".lvl")));

							enchModifiers.add(modifier);
						}
					}

					if(!modifiers.isEmpty()) {
						compound.set("AttributeModifiers", modifiers);
					}
					compound.set("ench", enchModifiers);

					if(!modifiers.isEmpty() || !enchModifiers.isEmpty()){
						nmsStack.setTag(compound);
						customWeapon = CraftItemStack.asBukkitCopy(nmsStack);
					}

					int durability = weaponsConfig.getInt(weapon +".durability");
					if(durability > 0){
						customWeapon.setDurability((short)(customWeapon.getType().getMaxDurability() - durability));
					}


					ShapedRecipe customRecipe = new ShapedRecipe(customWeapon);

					ConfigurationSection recipeSection = weaponsConfig.getConfigurationSection(weapon + ".recipe");
					if(recipeSection == null){
						this.getLogger().info("Missing recipe section for " + weapon);
						return false;
					}

					String[] shape = recipeSection.getString("shape").split(",");
					if(shape.length != 3){
						this.getLogger().info("Wrong recipe shape format for " + weapon);
						return false;
					}

					customRecipe.shape(shape[0].trim().replace("-"," "),shape[1].trim().replace("-"," "),shape[2].trim().replace("-"," "));

					for (String ingredient:
						 recipeSection.getStringList("ingredients")) {
						String[] i = ingredient.split(",");
						if(i.length != 2){
							this.getLogger().info("Wrong recipe ingedient format for " + weapon);
							return false;
						}
						if(i[0].length() > 1){
							this.getLogger().info("Ingredient format for " + weapon + " !. Must be a single caracter before comma");
							return false;
						}

						Material mat = Material.getMaterial(i[1].trim());
						if(mat == null){
							this.getLogger().info("Ingredient material for " + weapon + " could not found !");
							return false;
						}
						customRecipe.setIngredient(i[0].trim().charAt(0),mat);
					}
					Bukkit.addRecipe(customRecipe);
				}
			}

		}catch (Exception e){
			e.printStackTrace();
			this.getLogger().info(e.getMessage());
			return false;
		}
		return true;
	}
}

