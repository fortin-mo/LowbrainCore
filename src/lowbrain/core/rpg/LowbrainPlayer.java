package lowbrain.core.rpg;
import lowbrain.core.commun.Settings;
import lowbrain.core.commun.Helper;
import lowbrain.core.config.Internationalization;
import lowbrain.core.main.LowbrainCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import lowbrain.core.events.CoreListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LowbrainPlayer {
	private Player player;
	private int reputation = 0;
	private int strength = 0;
	private int intelligence = 0;
	private int dexterity = 0;
	private int vitality = 0;
	private int defence = 0;
	private float nextLvl = 0;
	private String className = "";
	private int magicResistance = 0;
	private boolean classIsSet = false;
	private int points = 0;
	private int skillPoints = 0;
	private float experience = 0;
	private int lvl = 0;
	private int kills = 0;
	private int deaths = 0;
	private float maxMana = 0;
	private float currentMana = 0;
	private BukkitTask manaRegenTask = null;
	private LowbrainClass lowbrainClass = null;
	private LowbrainRace lowbrainRace = null;
	private String raceName = "";
	private boolean raceIsSet = false;
	private int agility = 0;
	private boolean showStats = true;
	private HashMap<String,Integer> mobKills;
	private HashMap<String,LowbrainSkill> skills;
	private HashMap<String,LowbrainPower> powers;
	private String currentSkill;
	private Multipliers multipliers;

	private Scoreboard scoreboard;

	//========================================================= CONSTRUCTOR====================================
	/**
	 * contruct player with bukkit.Player
	 * will retrive the player's information using his uuid in
     * the forder ../data/uuid.yml
	 * @param p Bukkit.Player
	 */
	public LowbrainPlayer(Player p){
		player = p;
		initializePlayer();
	}

	/**
	 * initialize individual player scoreboard for stats
     */
	private void initializeScoreBoard(){
			if(scoreboard != null) return; // already initialize
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

			Objective objectiveInfo = scoreboard.registerNewObjective("Info", "dummy");
			objectiveInfo.setDisplayName("Info");
			if(showStats){
				objectiveInfo.setDisplaySlot(DisplaySlot.SIDEBAR);
			}
			else objectiveInfo.setDisplaySlot(DisplaySlot.PLAYER_LIST);

			this.getPlayer().setScoreboard(scoreboard);
	}

	/**
	 * hide or show scoreboard stats
	 * @param show
     */
	public void toggleScoreboard(boolean show){
		if(scoreboard == null) return;
		this.showStats = show;
		Objective info = scoreboard.getObjective("Info");
		if(showStats){
			info.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		else info.setDisplaySlot(DisplaySlot.PLAYER_LIST);
	}

	/**
	 * hide or show (toggle) scoreboard stats
     */
	public void toggleScoreboard(){
		if(scoreboard == null) return;
		this.showStats = !this.showStats;
		Objective info = scoreboard.getObjective("Info");
		if(showStats){
			info.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		else info.setDisplaySlot(DisplaySlot.PLAYER_LIST);
	}

	/**
	 * refresh individual player scoreboard stats
     */
	private void refreshScoreBoard(){
		if(scoreboard != null){
			Objective info = scoreboard.getObjective("Info");
			info.getScore(ChatColor.GREEN + "CURRENT MANA: ").setScore((int)this.currentMana);
			info.getScore(ChatColor.GREEN + "MANA %: ").setScore((int)(this.currentMana / this.maxMana * 100));
			info.getScore(ChatColor.GREEN + "LEVEL: ").setScore(this.lvl);
			info.getScore(ChatColor.GREEN + "XP: ").setScore((int)this.experience);
			info.getScore(ChatColor.GREEN + "NEXT LEVEL IN: ").setScore((int)(this.nextLvl - this.experience));
			info.getScore(ChatColor.GREEN + "POINTS: ").setScore(this.points);
			info.getScore(ChatColor.GREEN + "SKILL POINTS: ").setScore(this.skillPoints);
			info.getScore(ChatColor.GREEN + "KILLS: ").setScore(this.kills);

			Objective level = scoreboard.getObjective("Level");
			//level.getScore(this.getPlayer()).setScore(this.lvl);
		}
	}

	/**
	 * read player config yml to initialize current player
	 */
	private void initializePlayer(){
		if(CoreListener.plugin == null){
			return;
		}

		File userdata = new File(CoreListener.plugin.getDataFolder(), File.separator + "PlayerDB");
        File f = new File(userdata, File.separator + player.getUniqueId() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

		this.skills = new HashMap<String,LowbrainSkill>();
		this.mobKills = new HashMap<String,Integer>();
		this.powers = new HashMap<String,LowbrainPower>();

		//When the player file is created for the first time...
		if (!f.exists()) {
			playerData.createSection("class");
			playerData.set("class.is_set", false);
			playerData.set("class.name", "");

			playerData.createSection("race");
			playerData.set("race.is_set", false);
			playerData.set("race.name", "");

			playerData.createSection("stats");
			playerData.set("stats.vitality", 0);
			playerData.set("stats.lvl", 1);
			playerData.set("stats.strength", 0);
			playerData.set("stats.intelligence", 0);
			playerData.set("stats.dexterity", 0);
			playerData.set("stats.defence", 0);
			playerData.set("stats.agility",0);
			playerData.set("stats.magic_resistance", 0);
			playerData.set("stats.points", getSettings().getStartingPoints());
			playerData.set("stats.experience", 0);
			playerData.set("stats.next_lvl", getSettings().getFirstLvlExp());
			playerData.set("stats.kills",0);
			playerData.set("stats.deaths",0);
			playerData.set("stats.current_mana",0);
			playerData.set("stats.skill_points", getSettings().getStartingSkillPoints());
			playerData.set("stats.current_skill","");
			playerData.set("stats.reputation", 0);

			playerData.createSection("mob_kills");

			playerData.createSection("skills");

			for (LowbrainSkill skill :
					LowbrainCore.getInstance().getSkills().values()) {
				playerData.set("skills." + skill.getName(),0);
			}


			playerData.createSection("settings");
			playerData.set("settings.show_stats",true);

			try {
				playerData.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		classIsSet = playerData.getBoolean("class.is_set",false);
		className = playerData.getString("class.name","");

		raceIsSet = playerData.getBoolean("race.is_set",false);
		raceName = playerData.getString("race.name","");

        strength = playerData.getInt("stats.strength",0);
        intelligence = playerData.getInt("stats.intelligence",0);
        vitality = playerData.getInt("stats.vitality",0);
        defence = playerData.getInt("stats.defence",0);
        dexterity = playerData.getInt("stats.dexterity",0);
		magicResistance = playerData.getInt("stats.magic_resistance",0);
        experience = (float)playerData.getDouble("stats.experience",0);
        points = playerData.getInt("stats.points",0);
		skillPoints = playerData.getInt("stats.skill_points",0);
        lvl = playerData.getInt("stats.lvl",0);
        nextLvl = (float)playerData.getDouble("stats.next_lvl",0);
		kills = playerData.getInt("stats.kills",0);
		deaths = playerData.getInt("stats.deaths",0);
		currentMana = (float)playerData.getDouble("stats.current_mana",0);
		agility = playerData.getInt("stats.agility",0);
		reputation = playerData.getInt("stats.reputation, 0");

		ConfigurationSection skillsSection = playerData.getConfigurationSection("skills");

		//in case of new skills added
		this.skills = (HashMap<String, LowbrainSkill>) LowbrainCore.getInstance().getSkills().clone();

		if(skillsSection != null){
			for (String skill :
					skillsSection.getKeys(false)) {
				this.skills.put(skill,new LowbrainSkill(skill,skillsSection.getInt(skill)));
			}
		}

		currentSkill = this.skills.containsKey(playerData.getString("stats.current_skill")) ? playerData.getString("stats.current_skill") : "";

		ConfigurationSection mob = playerData.getConfigurationSection("mob_kills");
		if(mob != null){
			for (String key :mob.getKeys(false)) {
				this.mobKills.put(key,mob.getInt(key));
			}
		}


		showStats = playerData.getBoolean("settings.show_stats");

		this.lowbrainClass = new LowbrainClass(className);
		this.lowbrainRace = new LowbrainRace(raceName);

		initializePowers();

		start();
	}

    /**
     * initialize player's powers
     */
	private void initializePowers(){
		this.powers = new HashMap<>();
		if(classIsSet && this.lowbrainClass != null ){
			for (String powa : this.lowbrainClass.getPowers()) {
				this.powers.put(powa,new LowbrainPower(powa));
			}
		}
		if(raceIsSet && this.lowbrainRace != null ){
			for (String powa : this.lowbrainRace.getPowers()){
				this.powers.put(powa,new LowbrainPower(powa));
			}
		}
	}

	private void start(){
		if(classIsSet && raceIsSet && this.lowbrainClass != null && this.lowbrainRace != null){
			initializeScoreBoard();
			validateEquippedArmor();
			attributeHasChanged();
			setDisplayName();
			startManaRegenTask();
		}
	}

	//==========================================================END OF CONSTRUCTOR=============================

	//====================================================== USEFULL ==========================================

	/**
	 * return 0 if equals, -1 if lower or not equals, +1 if higher
	 * @param n name of the attribute
	 * @param v value to compare
     * @return 0 if equals, -1 if lower or not equals, +1 if higher
     */
	public int compareAttributesByName(String n, Object v){
		try {
			int v1 = -1;
			int v2 = -1;

			switch (n.toLowerCase()){
				case "strength":
				case "str":
					v1 = this.getStrength();
					v2 = (int)v;
					break;
				case "intelligence":
				case "intel":
					v1 = this.getIntelligence();
					v2 = (int)v;
					break;
				case "dexterity":
				case "dext":
					v1 = this.getDexterity();
					v2 = (int)v;
					break;
				case "defence":
				case "def":
					v1 = this.getDefence();
					v2 = (int)v;
					break;
				case "agility":
				case "agi":
					v1 = this.getAgility();
					v2 = (int)v;
					break;
				case "magic_resistance":
				case "magicresistance":
				case "mr":
					v1 = this.getMagicResistance();
					v2 = (int)v;
					break;
				case "vitality":
				case "vit":
					v1 = this.getVitality();
					v2 = (int)v;
					break;
				case "level":
				case "lvl":
					v1 = this.getLvl();
					v2 = (int)v;
					break;
				case "class":
					if(this.getLowbrainClass() != null){
						return this.getLowbrainClass().getName().equals(v) ? 0 : -1;
					}else return -1;
				case "race":
					if(this.getLowbrainRace() != null){
						return this.getLowbrainRace().getName().equals(v) ? 0 : -1;
					}else return -1;
				case "kills":
				case "kill":
					v1 = this.getKills();
					v2 = (int)v;
					break;
				case "deaths":
				case "death":
					v1 = this.getDeaths();
					v2 = (int)v;
					break;
				default:
					return -1;
			}

			return v1-v2;

		}catch (Exception e){
			e.printStackTrace();
			return -1;
		}

	}

	/***
	 * validate if player can wear equipped armor
	 */
	public void validateEquippedArmor(){
		if(!canEquipItem(this.getPlayer().getInventory().getHelmet())){
			this.getPlayer().getInventory().addItem(this.getPlayer().getInventory().getHelmet());
			this.getPlayer().getInventory().setHelmet(null);
		}
		if(!canEquipItem(this.getPlayer().getInventory().getChestplate())){
			this.getPlayer().getInventory().addItem(this.getPlayer().getInventory().getChestplate());
			this.getPlayer().getInventory().setChestplate(null);
		}
		if(!canEquipItem(this.getPlayer().getInventory().getLeggings())){
			this.getPlayer().getInventory().addItem(this.getPlayer().getInventory().getLeggings());
			this.getPlayer().getInventory().setLeggings(null);
		}
		if(!canEquipItem(this.getPlayer().getInventory().getBoots())){
			this.getPlayer().getInventory().addItem(this.getPlayer().getInventory().getBoots());
			this.getPlayer().getInventory().setBoots(null);
		}
		if(!canEquipItem(this.getPlayer().getInventory().getItemInOffHand())){
			this.getPlayer().getInventory().addItem(this.getPlayer().getInventory().getItemInOffHand());
			this.getPlayer().getInventory().setItemInOffHand(null);
		}
	}

	/**
	 * can this player wear a specific set of armor
	 * @param item
	 * @return
     */
	public boolean canEquipItem(ItemStack item){
		if(item == null) return true;

		String name = "";

		if(item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null){//custom items
			name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
		}else{//vanilla items
			name = item.getType().name().toLowerCase();
		}

		LowbrainCore.ItemRequirements i = LowbrainCore.getInstance().getItemsRequirements().get(name);
		if(i == null) return true;
		return meetRequirements(i.getRequirements());
	}

	/**
	 * can this player wear a specific set of armor
	 * @param item
	 * @return a string with the requirements that failed... empty if they all passed
	 */
	public String canEquipItemString(ItemStack item){
		String msg = "";
		if(item == null) return msg;
		String name = "";

		if(item.getItemMeta().getDisplayName() != null){//custom items
			name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
		}else{//vanilla items
			name = item.getType().name().toLowerCase();
		}

		LowbrainCore.ItemRequirements i = LowbrainCore.getInstance().getItemsRequirements().get(name);
		if(i == null)return msg;
		return meetRequirementsString(i.getRequirements());
	}

	/**
	 * check if a player passes a list of requirements
	 * @param requirements
	 * @return true if passes, false if not
	 */
	public boolean meetRequirements(Map<String,Integer> requirements){
		if(requirements == null)return true;
		for(Map.Entry<String, Integer> r : requirements.entrySet()) {
			String n = r.getKey().toLowerCase();
			int v = r.getValue();
			if(this.compareAttributesByName(n,v) < 0){
				return false;
			}
		}
		return true;
	}

	/**
	 * check if a player passes a list of requirements
	 * @param requirements
	 * @return a string of failed requirements, empty string if passes
	 */
	public String meetRequirementsString(Map<String,Integer> requirements){
		String msg = "";
		if(requirements == null)return msg;
		for(Map.Entry<String, Integer> r : requirements.entrySet()) {
			String n = r.getKey().toLowerCase();
			int v = r.getValue();
			if(this.compareAttributesByName(n,v) < 0){
				msg += " " + n + ":" + v;
			}
		}
		return msg;
	}

	/**
	 * get the value of an attribute by name
	 * @param n the name of the attribute
	 * @return getAttribute with 0 as default value
	 */
	public int getAttribute(String n){
		return getAttribute(n, 0);
	}

	/**
	 * get the value of an attribute by name
	 * @param n the name of the attribute
	 * @param d default value in cause of not found
	 * @return value
	 */
	public int getAttribute(String n, int d){
		if(Helper.StringIsNullOrEmpty(n))return d;

		switch (n.toLowerCase()){
			case "level":
			case "lvl":
				return this.getLvl();

			case "intelligence":
			case "intel":
				return this.getIntelligence();

			case "agility":
			case "agi":
				return this.getAgility();

			case "vitality":
			case "vit":
				return this.getVitality();

			case "strength":
			case "str":
				return this.getStrength();

			case "dexterity":
			case "dext":
				return this.getDexterity();

			case "defence":
			case "def":
				return this.getDefence();

			case "magic_resistance":
			case "magicresistance":
			case "mr":
				return this.getMagicResistance();
			case "kills":
			case "kill":
				return this.getKills();

			case "deaths":
			case "death":
				return this.getDeaths();

            case "reputation" :
            case "rep" :
                return this.getReputation();
		}
		return d;
	}

	/**
	 * cast a spell
	 * @param name name of the spell
	 * @param to rpgPlayer you wish to cast the spell to.. if null will cast to self
     * @return true if the cast has succeeded
     */
	public boolean castSpell(String name, LowbrainPlayer to){

		LowbrainPower powa = this.powers.get(name);

		if(powa == null){
			sendMessage(Internationalization.getInstance().getString("you_cannot_cast_this_spell"));
			return false;
		}

		if(powa.Cast(this,to == null ? this : to)){
			refreshScoreBoard();
			return true;
		}
		return false;
	}
	
	/**
	 * save player current data in yml
	 */
	public void saveData(){
		try {
	        File userdata = new File(CoreListener.plugin.getDataFolder(), File.separator + "PlayerDB");
	        File f = new File(userdata, File.separator + this.player.getUniqueId() + ".yml");
	        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

            playerData.set("class.is_set", this.classIsSet);
            playerData.set("class.name", this.className);

			playerData.set("race.is_set", this.raceIsSet);
			playerData.set("race.name", this.raceName);
            
            playerData.set("stats.vitality",this.vitality);
            playerData.set("stats.lvl", this.lvl);
            playerData.set("stats.strength", this.strength);
            playerData.set("stats.intelligence", this.intelligence);
            playerData.set("stats.dexterity", this.dexterity);
			playerData.set("stats.magic_resistance",this.magicResistance);
            playerData.set("stats.defence", this.defence);
			playerData.set("stats.agility",this.agility);
            playerData.set("stats.points", this.points);
            playerData.set("stats.experience", this.experience);
			playerData.set("stats.next_lvl", this.nextLvl);
			playerData.set("stats.kills",this.kills);
			playerData.set("stats.deaths",this.deaths);
			playerData.set("stats.current_mana", this.currentMana);
			playerData.set("stats.skill_points", this.skillPoints);
			playerData.set("stats.reputation", this.reputation);

			for(Map.Entry<String, Integer> r : this.mobKills.entrySet()) {
				String n = r.getKey();
				int v = r.getValue();
				playerData.set("mob_kills." + n,v);
			}

			playerData.set("stats.current_skill",this.currentSkill);

			for(Map.Entry<String, LowbrainSkill> s : this.skills.entrySet()) {
				String n = s.getKey();
				int v = s.getValue().getCurrentLevel();
				playerData.set("skills." + n,v);
			}

			playerData.set("settings.show_stats",showStats);

            
            playerData.save(f);
		} catch (Exception e) {
			CoreListener.plugin.getLogger().info(e.getMessage());// TODO: handle exception
		}
	}

	/**
	 * disconnect the player and stop all tasks
     * will save his data
     */
	public void disconnect(){
		stopManaRegenTask();
		this.saveData();
	}

	/***
	 * reload everything when config are reloaded
	 */
	public void reload(){
		saveData();
		stopManaRegenTask();
		attributeHasChanged();
		validateEquippedArmor();
		startManaRegenTask();
	}

	/**
	 * level up add one level... increment player points
	 */
	public void levelUP(){
		if((getSettings().getMaxLvl() < 0 || this.lvl < getSettings().getMaxLvl())){
			this.lvl += 1;

			addBonusAttributes(1);

			this.addPoints(getSettings().getPointsPerLvl());

			if(this.lvl % getSettings().getSkillPointsLevelInterval() == 0){
				this.addSkillPoints(getSettings().getSkillPointsPerInterval());
			}

			double lvlExponential = getSettings().getParameters().getNextLvlMultiplier();
			this.nextLvl += this.nextLvl * lvlExponential;
			setDisplayName();

			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()); //restore health on level up
			this.currentMana = this.maxMana;//restore maxMana on level up
			sendMessage(Internationalization.getInstance().getString("level_up") + " " + this.lvl);
			this.getPlayer().getWorld().playSound(this.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,0);
		}
	}

	/**
	 * reset player class and race
	 * @param c class name
	 * @param r race name
     */
	public void reset(String c, String r){
		if(getSettings().isAllowStatsReset()){
			setClass(c,true);
			setRace(r, true);
		}
	}

	/**
	 * reset all stats of the player
	 * @param override by pass settings restriction
	 */
	public void resetAll(boolean override){
		if(getSettings().isAllowCompleteReset() || override){
			// reset stats
			strength = 0;
			intelligence = 0;
			vitality = 0;
			defence = 0;
			dexterity = 0;
			magicResistance = 0;
			classIsSet = false;
			className = "";
			raceIsSet = false;
			lowbrainClass = null;
			lowbrainRace = null;
			raceName = "";
			experience = 0;
			points = getSettings().getStartingPoints();
			lvl = 1;
			nextLvl = getSettings().getFirstLvlExp();
			kills = 0;
			deaths = 0;
			currentMana = 0;
			agility = 0;

			// reset generic attributes with original minecraft attributes
			this.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(2);
			this.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
			this.getPlayer().setWalkSpeed(0.2F);
			this.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
			this.getPlayer().getAttribute(Attribute.GENERIC_LUCK).setBaseValue(0);

			// stop mana regen task
			this.stopManaRegenTask();

			// set all skill level to zero
			for (String k : this.getSkills().keySet()) {
				this.getSkills().get(k).setCurrentLevel(0);
			}

			// reset mob kills count
			for (String k : this.getMobKills().keySet()) {
				this.getMobKills().put(k,0);
			}

			setDisplayName();
			sendMessage(Internationalization.getInstance().getString("stats_reset"));
		}
		else{
			sendMessage(Internationalization.getInstance().getString("not_allowed_to_reset_stats"));
		}
	}

    public void sendMessage(String msg){
        sendMessage(msg,ChatColor.GREEN,"");
    }

    public void sendMessage(String msg, ChatColor color){
        sendMessage(msg, color,"");
    }

    public void sendMessage(String msg, String prefix){
        sendMessage(msg,ChatColor.GREEN, prefix);
    }

    public void sendMessage(String msg, ChatColor color, String prefix){
        this.getPlayer().sendMessage(prefix + color + msg);
    }

	//=================================================== END OF USEFULL =====================================

	//=====================================================  ADD AND SETTER METHODS =================================

	/**
	 * add experience to current player
	 * @param exp
	 */
	public void addExp(double exp){
		this.experience += exp;
		refreshScoreBoard();
		if(this.experience >= nextLvl){
			this.levelUP();
		}
		else if (this.experience < 0 ){
			this.experience = 0;
		}
	}

	/**
	 * set current player experience
	 * @param experience
	 */
	public void setExperience(float experience) {
		this.experience = experience;
	}
	
	/**
	 * set current player strength
	 * @param strength
	 */
	public void setStrength(int strength) {
		this.strength = strength;
		if(getSettings().getMaxStats() >=0 && this.strength > getSettings().getMaxStats())this.strength = getSettings().getMaxStats();
		else if(this.strength < 0) this.strength = 0;
		attributeHasChanged();
	}
	
	/**
	 * add strength to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addStrength(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().getMaxStats();
		if(nb == 0){
			return;
		}
		else if(!getSettings().isAllowDeductionPoints() && nb < 0){
			sendMessage(Internationalization.getInstance().getString("not_allowed_to_deduct_attributes_point"));
			return;
		}
		else if (getSettings().isAllowDeductionPoints() && nb < 0 && this.strength == 0){
			sendMessage(Internationalization.getInstance().getString("cannot_deduct_anymore_point"));
			return;
		}
		else if(usePoints && this.points >= nb){
			int oldStrength = this.strength;
			this.strength += nb;
			if(maxStats >= 0 && this.strength > maxStats){
				this.strength = maxStats;
			}
			else if(this.strength < 0){
				this.strength = 0;
			}
			
			int dif = this.strength - oldStrength;
			
			this.points -= dif;
			if(callChange) attributeHasChanged();
			sendMessage(Internationalization.getInstance().getString("strength_incremented_by") + " " + dif);
		}
		else if(!usePoints){
			this.strength += nb;
			if(maxStats >= 0 && this.strength > maxStats){
				this.strength = maxStats;
				sendMessage(Internationalization.getInstance().getString("strength_set_to")+ " " + maxStats);
			}else {
				sendMessage(Internationalization.getInstance().getString("strength_incremented_by") + " " + nb);
			}
			if(callChange) attributeHasChanged();
		}
		else{
			sendMessage(Internationalization.getInstance().getString("not_enough_points"),ChatColor.RED);
			return;
		}
	}
	
	/**
	 * set intelligence of current player
	 * @param intelligence
	 */
	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
		if(getSettings().getMaxStats() >=0 && this.intelligence > getSettings().getMaxStats())this.intelligence = getSettings().getMaxStats();
		else if(this.intelligence < 0) this.intelligence = 0;
		attributeHasChanged();
	}
	
	/**
	 * add intelligence to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addIntelligence(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().getMaxStats();
		if(nb == 0){
			return;
		}
		else if(!getSettings().isAllowDeductionPoints() && nb < 0){
			sendMessage(Internationalization.getInstance().getString("not_allowed_to_deduct_attributes_point"));
			return;
		}
		else if (getSettings().isAllowDeductionPoints() && nb < 0 && this.intelligence == 0){
			sendMessage(Internationalization.getInstance().getString("cannot_deduct_anymore_point"));
			return;
		}
		else if(usePoints && this.points >= nb){
			int oldIntelligence = this.intelligence;
			this.intelligence += nb;
			if(maxStats >= 0 && this.intelligence > maxStats){
				this.intelligence = maxStats;
			}
			else if(this.intelligence < 0){
				this.intelligence = 0;
			}
			int dif = this.intelligence - oldIntelligence;
			
			this.points -= dif;
			if(callChange) attributeHasChanged();
			sendMessage(Internationalization.getInstance().getString("intelligence_incremented_by") + " " + dif);

		}
		else if(!usePoints){
			this.intelligence += nb;
			if(maxStats >= 0 && this.intelligence > maxStats){
				this.intelligence = maxStats;
				sendMessage(Internationalization.getInstance().getString("intelligence_set_to") + " " + maxStats);
			}
			else{
				sendMessage(Internationalization.getInstance().getString("intelligence_incremented_by") + " " + nb);
			}
			if(callChange) attributeHasChanged();
		}
		else{
			sendMessage(Internationalization.getInstance().getString("not_enough_points"),ChatColor.RED);
			return;
		}
	}
	
	/**
	 * set dexterity of current player
	 * @param dexterity
	 */
	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
		if(getSettings().getMaxStats() >=0 && this.dexterity > getSettings().getMaxStats())this.dexterity = getSettings().getMaxStats();
		else if(this.dexterity < 0) this.dexterity = 0;
		attributeHasChanged();
	}
	
	/**
	 * add dexterity to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addDexterity(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().getMaxStats();
		if(nb == 0){
			return;
		}
		else if(!getSettings().isAllowDeductionPoints() && nb < 0){
			sendMessage(Internationalization.getInstance().getString("not_allowed_to_deduct_attributes_point"));
			return;
		}
		else if (getSettings().isAllowDeductionPoints() && nb < 0 && this.dexterity == 0){
			sendMessage(Internationalization.getInstance().getString("cannot_deduct_anymore_point"));
			return;
		}
		else if(usePoints && this.points >= nb){
			int oldDexterity = this.dexterity;
			this.dexterity += nb;
			if(maxStats >= 0 && this.dexterity > maxStats){
				this.dexterity = maxStats;
			}
			else if(this.dexterity < 0 ){
				this.dexterity = 0;
			}
			int dif = this.dexterity - oldDexterity;
			
			this.points -= dif;
			if(callChange) attributeHasChanged();
			sendMessage(Internationalization.getInstance().getString("dexterity_incremented_by") + " " + dif);
		}
		else if(!usePoints){
			this.dexterity += nb;
			if(maxStats >= 0 && this.dexterity > maxStats){
				this.dexterity = maxStats;
				sendMessage(Internationalization.getInstance().getString("dexterity_set_to") + " " + maxStats);
			}else {
				sendMessage(Internationalization.getInstance().getString("dexterity_incremented_by") + " " + nb);
			}
			if(callChange) attributeHasChanged();
		}
		else{
			sendMessage(Internationalization.getInstance().getString("not_enough_points"),ChatColor.RED);
			return;
		}
	}
	
	/**
	 * set vitality of current player
	 * @param vitality
	 */
	public void setVitality(int vitality) {
		this.vitality = vitality;
		if(getSettings().getMaxStats() >=0 && this.vitality > getSettings().getMaxStats())this.vitality = getSettings().getMaxStats();
		else if(this.vitality < 0) this.vitality = 0;
		setPlayerMaxHealth();
	}

	/**
	 * add vitality to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addVitality(int nb, boolean usePoints, boolean callChange){
		int maxStats = getSettings().getMaxStats();
		if(nb == 0){
			return;
		}
		else if(!getSettings().isAllowDeductionPoints() && nb < 0){
			sendMessage(Internationalization.getInstance().getString("not_allowed_to_deduct_attributes_point"));
			return;
		}
		else if (getSettings().isAllowDeductionPoints() && nb < 0 && this.vitality == 0){
			sendMessage(Internationalization.getInstance().getString("cannot_deduct_anymore_point"));
			return;
		}
		else if(usePoints && this.points >= nb){
			int oldHealth = this.vitality;
			this.vitality += nb;
			if(maxStats >= 0 && this.vitality > maxStats){
				this.vitality = maxStats;
			}
			else if (this.vitality < 0 )this.vitality = 0;

			int dif = this.vitality - oldHealth;
			
			this.points -= dif;
			if(callChange) attributeHasChanged();
			sendMessage(Internationalization.getInstance().getString("vitality_incremented_by") + " " + dif);
		}
		else if(!usePoints){
			this.vitality += nb;
			if(maxStats >= 0 && this.vitality > maxStats){
				this.vitality = maxStats;
				sendMessage(Internationalization.getInstance().getString("vitality_set_to") + " " + maxStats);
			}else{
				sendMessage(Internationalization.getInstance().getString("vitality_incremented_by") + " " + nb);
			}
			if(callChange) attributeHasChanged();
		}
		else{
			sendMessage(Internationalization.getInstance().getString("not_enough_points"),ChatColor.RED);
			return;
		}
	}
	
	/**
	 * set defence of current player
	 * @param defence
	 */
	public void setDefence(int defence) {
		this.defence = defence;
		if(getSettings().getMaxStats() >=0 && this.defence > getSettings().getMaxStats())this.defence = getSettings().getMaxStats();
		else if(this.defence < 0) this.defence = 0;
		attributeHasChanged();
	}

	/**
	 * add defence to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addDefence(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().getMaxStats();

		if(nb == 0){
			return;
		}
		else if(!getSettings().isAllowDeductionPoints() && nb < 0){
			sendMessage(Internationalization.getInstance().getString("not_allowed_to_deduct_attributes_point"));
			return;
		}
		else if (getSettings().isAllowDeductionPoints() && nb < 0 && this.defence == 0){
			sendMessage(Internationalization.getInstance().getString("cannot_deduct_anymore_point"));
			return;
		}
		else if(usePoints && this.points >= nb){
			int oldDefence = this.defence;
			this.defence += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.defence = maxStats;
			}
			else if (this.defence < 0 ) this.defence = 0;
			
			double dif = this.defence - oldDefence;
			
			this.points -= dif;
			if(callChange) attributeHasChanged();
			sendMessage(Internationalization.getInstance().getString("defence_incremented_by") + " " + dif);
		}
		else if(!usePoints){
			this.defence += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.defence = maxStats;
				sendMessage(Internationalization.getInstance().getString("defence_set_to") + " " + maxStats);
			}else{
				sendMessage(Internationalization.getInstance().getString("defence_incremented_by") + " " + nb);
			}
			if(callChange) attributeHasChanged();
		}
		else{
			sendMessage(Internationalization.getInstance().getString("not_enough_points"),ChatColor.RED);
			return;
		}
	}

	/**
	 * set player magic resitance
	 * @param magicResistance
     */
	public void setMagicResistance(int magicResistance) {
		this.magicResistance = magicResistance;
		if(getSettings().getMaxStats() >=0 && this.magicResistance > getSettings().getMaxStats())this.magicResistance = getSettings().getMaxStats();
		else if(this.magicResistance < 0) this.magicResistance = 0;
		attributeHasChanged();
	}

	/**
	 * add magic resistance to current player
	 * @param nb
	 * @param usePoints
     */
	public void addMagicResistance(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().getMaxStats();

		if(nb == 0){
			return;
		}
		else if(!getSettings().isAllowDeductionPoints() && nb < 0){
			sendMessage(Internationalization.getInstance().getString("not_allowed_to_deduct_attributes_point"));
			return;
		}
		else if (getSettings().isAllowDeductionPoints() && nb < 0 && this.magicResistance == 0){
			sendMessage(Internationalization.getInstance().getString("cannot_deduct_anymore_point"));
			return;
		}
		else if(usePoints && this.points >= nb){
			int oldMagicResistance = this.defence;
			this.magicResistance += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.magicResistance = maxStats;
			}
			else if (this.magicResistance < 0)this.magicResistance = 0;

			double dif = this.magicResistance - oldMagicResistance;
			if(callChange) attributeHasChanged();
			this.points -= dif;

			sendMessage(Internationalization.getInstance().getString("magicresist_incremented_by") + " " + dif);
		}
		else if(!usePoints){
			this.magicResistance += nb;
			if(maxStats >= 0 && this.magicResistance > maxStats){
				this.magicResistance = maxStats;
				sendMessage(Internationalization.getInstance().getString("magicresist_set_to") + " " + maxStats);
			} else{
				sendMessage(Internationalization.getInstance().getString("magicresist_incremented_by") + " " + nb);
			}
			if(callChange) attributeHasChanged();
		}
		else{
			sendMessage(Internationalization.getInstance().getString("not_enough_points"),ChatColor.RED);
			return;
		}
	}

	/**
	 * set player agility
	 * @param agility
     */
	public void setAgility(int agility) {
		this.agility = agility;
		if(getSettings().getMaxStats() >=0 && this.agility > getSettings().getMaxStats())this.agility = getSettings().getMaxStats();
		else if(this.agility < 0) this.agility = 0;
		attributeHasChanged();
	}

	/**
	 * add agility to the player
	 * @param nb
	 * @param usePoints
	 */
	public void addAgility(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().getMaxStats();

		if(nb == 0){
			return;
		}
		else if(!getSettings().isAllowDeductionPoints() && nb < 0){
			sendMessage(Internationalization.getInstance().getString("not_allowed_to_deduct_attributes_point"));
			return;
		}
		else if (getSettings().isAllowDeductionPoints() && nb < 0 && this.agility == 0){
			sendMessage(Internationalization.getInstance().getString("cannot_deduct_anymore_point"));
			return;
		}
		else if(usePoints && this.points >= nb){
			int oldAgility = this.agility;
			this.agility += nb;
			if(maxStats >= 0 && this.agility > maxStats){
				this.agility = maxStats;
			}
			else if (this.agility < 0) this.agility = 0;

			double dif = this.agility - oldAgility;
			this.points -= dif;
			if(callChange) attributeHasChanged();
			sendMessage(Internationalization.getInstance().getString("agility_incremented_by") + " " + dif);
		}
		else if(!usePoints){
			this.agility += nb;
			if(maxStats >= 0 && this.agility > maxStats){
				this.agility = maxStats;
				sendMessage(Internationalization.getInstance().getString("agility_set_to") + " " + maxStats);
			}else{
				sendMessage(Internationalization.getInstance().getString("agility_incremented_by") + " " + nb);
			}
			if(callChange) attributeHasChanged();
		}
		else{
			sendMessage(Internationalization.getInstance().getString("not_enough_points"),ChatColor.RED);
			return;
		}
	}
	
	/**
	 * set number of points of current player
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
		if(this.points < 0)this.points = 0;
	}

	public void setSkillPoints(int pts){
		this.skillPoints = pts;
		if(this.skillPoints < 0)this.skillPoints = 0;
	}
	
	/**
	 * add points to current player
	 * @param nbPoints
	 */
	public void addPoints(int nbPoints){
		this.points += nbPoints;
		if(this.points < 0) this.points = 0;
	}

	/**
	 * set player level. will not add points
	 * @param lvl
	 */
	public void setLvl(int lvl, boolean ajust) {
		addLevel(lvl-this.lvl, ajust);
	}
	
	/**
	 * add lvl to current player. will add points as well
	 * @param nbLvl
	 */
	public void addLevel(int nbLvl, boolean ajust){
		int oldLvl = this.lvl;
		this.lvl += nbLvl;
		int maxLvl = getSettings().getMaxLvl();
		int nbPointsPerLevel = getSettings().getPointsPerLvl();
		
		if(maxLvl > 0 && this.lvl > maxLvl)this.lvl= maxLvl;
		else if (this.lvl <= 0) this.lvl = 1;
		
		int dif = this.lvl - oldLvl;
		this.setDisplayName();
		if(ajust){
			this.points += dif * nbPointsPerLevel;
			if(this.points < 0) this.points = 0;

			addBonusAttributes(dif);
		}
	}

	/**
	 * add killds
	 * @param kills number of kills to add
     */
	public void addKills(int kills) {
		this.kills += kills;
	}

	public void setKills(int kills){this.kills = kills;}

	public void setDeaths(int deaths){this.deaths = deaths;}

	/**
	 * add deaths
	 * @param deaths number of deaths to add
     */
	public void addDeaths(int deaths) {
		this.deaths += deaths;
		if(Settings.getInstance().isHardCoreEnable() && this.deaths >= Settings.getInstance().getHardCoreMaxDeaths()){
			this.sendMessage(Internationalization.getInstance().getString("player_dies_on_hardcore_mode"));
			this.resetAll(true);
		}
	}

	public int getMagicResistance() {
		return magicResistance;
	}

	/**
	 * set player current mana
	 * @param currentMana
     */
	public void setCurrentMana(float currentMana) {
		this.currentMana = currentMana;
	}

	/**
	 * set current player race
	 * @param n name of the race
	 * @param override override current configuration (can reset race, can switch race)
     */
	public void setRace(String n, boolean override){
		if(!raceIsSet || override){
			this.lowbrainRace = new LowbrainRace(n);

			this.defence += lowbrainRace.getDefence();
			this.dexterity += lowbrainRace.getDexterity();
			this.intelligence += lowbrainRace.getIntelligence();
			this.strength += lowbrainRace.getStrength();
			this.vitality += lowbrainRace.getVitality();
			this.magicResistance += lowbrainRace.getMagicResistance();
			this.agility += lowbrainRace.getAgility();

			this.raceName = n;
			this.experience = 0;
			this.nextLvl = getSettings().getFirstLvlExp();
			this.lvl = 1;
			sendMessage(Internationalization.getInstance().getString("set_race_and_class") + " " + lowbrainRace.getName());
			this.raceIsSet = true;
			initializePowers();
			start();
		}
		else if(getSettings().isCanSwitchRace()){
			if(this.raceName == n){
				sendMessage(Internationalization.getInstance().getString("set_race_and_class_same") + " " + lowbrainRace.getName());
				return;
			}

			LowbrainRace newRace = new LowbrainRace(n);
			this.raceIsSet = true;

			this.defence -= lowbrainRace.getDefence();
			this.dexterity -= lowbrainRace.getDexterity();
			this.intelligence -= lowbrainRace.getIntelligence();
			this.strength -= lowbrainRace.getStrength();
			this.vitality -= lowbrainRace.getVitality();
			this.agility -= lowbrainRace.getAgility();

			addBonusAttributes(this.lvl * -1 - 1);

			this.defence += newRace.getDefence();
			this.dexterity += newRace.getDexterity();
			this.intelligence += newRace.getIntelligence();
			this.strength += newRace.getStrength();
			this.vitality += newRace.getVitality();
			this.agility += newRace.getAgility();
			this.raceName = n;
			this.lowbrainRace = newRace;

			addBonusAttributes(this.lvl -1);
			sendMessage(Internationalization.getInstance().getString("set_race_and_class") + " "  + newRace.getName());
			initializePowers();
			start();
		}
		else{
			sendMessage(Internationalization.getInstance().getString("cant_switch_race"));
		}
		this.raceIsSet = true;
	}

	/**
	 * set current player class
	 * @param n class name
	 * @param override override current configuration (can reset class, can switch class)
     */
	public void setClass(String n, boolean override){
		if(!classIsSet || override){
			this.lowbrainClass = new LowbrainClass(n);

			this.defence = lowbrainClass.getDefence();
			this.dexterity = lowbrainClass.getDexterity();
			this.intelligence = lowbrainClass.getIntelligence();
			this.strength = lowbrainClass.getStrength();
			this.vitality = lowbrainClass.getVitality();
			this.magicResistance = lowbrainClass.getMagicResistance();
			this.agility = lowbrainClass.getAgility();

			this.className = n;
			this.experience = 0;
			this.nextLvl = getSettings().getFirstLvlExp();
			this.lvl = 1;
			sendMessage(Internationalization.getInstance().getString("set_race_and_class") + " "  + lowbrainClass.getName());
			this.classIsSet = true;

			initializePowers();
			start();
		}
		else if(getSettings().isCanSwitchClass()){
			if(this.className == n){
				sendMessage(Internationalization.getInstance().getString("set_race_and_class_same") + " " + lowbrainClass.getName());
				return;
			}

			this.classIsSet = true;
			LowbrainClass newClass = new LowbrainClass(n);

			this.defence -= lowbrainClass.getDefence();
			this.dexterity -= lowbrainClass.getDexterity();
			this.intelligence -= lowbrainClass.getIntelligence();
			this.strength -= lowbrainClass.getStrength();
			this.vitality -= lowbrainClass.getVitality();
			this.agility -= lowbrainClass.getAgility();

			addBonusAttributes(this.lvl * -1 - 1);

			this.defence += newClass.getDefence();
			this.dexterity += newClass.getDexterity();
			this.intelligence += newClass.getIntelligence();
			this.strength += newClass.getStrength();
			this.vitality += newClass.getVitality();
			this.agility += newClass.getAgility();
			this.className = n;
			this.lowbrainClass = newClass;

			addBonusAttributes(this.lvl -1);

			initializePowers();
			start();
			sendMessage(Internationalization.getInstance().getString("set_race_and_class") + " "  + newClass.getName());
		}
		else{
			sendMessage(Internationalization.getInstance().getString("cant_switch_class"));
		}
		this.classIsSet = true;
	}

	/**
	 * set current skill
	 * @param n name of the skill
	 */
	public void setCurrentSkill(String n) {
		if(this.skills.containsKey(n)) {
			if(this.skills.get(n).getCurrentLevel() == 0){
				this.sendMessage(Internationalization.getInstance().getString("cannot_use_skill"),ChatColor.RED);
				return;
			}
			this.currentSkill = n;
		}
		else{
			this.sendMessage(Internationalization.getInstance().getString("no_such_skill"),ChatColor.RED);
		}
	}

	/**
	 * add skill points
	 * @param skillPoints the number to add
	 */
	public void addSkillPoints(int skillPoints) {
		this.skillPoints += skillPoints;
		if(this.skillPoints < 0) this.skillPoints = 0;
	}

	/**
	 * upgrade skill
	 * @param n name of the skill
	 */
	public void upgradeSkill(String n){
		if(!LowbrainCore.getInstance().getSkills().containsKey(n)){
			this.sendMessage(Internationalization.getInstance().getString("no_such_skill"),ChatColor.RED);
			return;
		}

		LowbrainSkill s = this.skills.get(n);
		if(!s.isEnable()){
			this.sendMessage(Internationalization.getInstance().getString("skill_is_disabled"),ChatColor.RED);
			return;
		}

		if(s.getMaxLevel() <= s.getCurrentLevel()){
			this.sendMessage(Internationalization.getInstance().getString("skill_fully_upgraded"),ChatColor.RED);
			return;
		}

		String msg = "";
		for(Map.Entry<String, Integer> r : s.getBaseRequirements().entrySet()) {
			String requirement = r.getKey().toLowerCase();
			int value = s.operation(r.getValue(),s.getRequirementsOperationValue(),s.getRequirementsOperation());
			if(this.compareAttributesByName(requirement,value) < 0){
				msg += " " + n + ":" + value;
			}
		}

		if(!Helper.StringIsNullOrEmpty(msg)){
			this.sendMessage(Internationalization.getInstance().getString("skill_requirement_to_high") + " " + msg,ChatColor.RED);
			return;
		}

		if(s.getSkillpointsCost() > this.skillPoints){
			this.sendMessage(Internationalization.getInstance().getString("this_skills_require") + " "
					+ s.getBaseSkillpointsCost() + " " + Internationalization.getInstance().getString("points_to_upgrade"),ChatColor.RED);
			return;
		}

		s.addLevel(1);
		this.addSkillPoints(- s.getSkillpointsCost());

	}

	public void addReputation(int n) {
	    this.reputation += n;
    }

    public void setReputation(int n) {
	    this.reputation = n;
    }

	//=============================================== END OF ADD AND SETTER ===============================

	//================================================ GETTER ==============================================

	/**
	 * return formatted player's information as string
	 * @return
     */
	public String toString(){
		if(classIsSet && raceIsSet) {
			String s = "Level : " + lvl + "\n";
			s += "Class : " + getClassName() + "\n";
			s += "Race : " + getRaceName() + "\n";
			s += "Defence : " + defence + "\n";
			s += "Strength : " + strength + "\n";
			s += "Health : " + vitality + "\n";
			s += "Dexterity : " + dexterity + "\n";
			s += "Intelligence : " + intelligence + "\n";
			s += "Magic Resistance : " + magicResistance + "\n";
			s += "Agility : " + agility + "\n";
			s += "Kills : " + kills + "\n";
			s += "Deaths : " + deaths + "\n";
			s += "Points : " + points + "\n";
			s += "Skill points: " + skillPoints + "\n";
			s += "Experience : " + experience + "\n";
			s += "Next lvl in : " + (nextLvl - experience) + " xp" + "\n";

			s += "Attack speed : " + this.getAttackSpeed()+ "\n";
			s += "Movement speed : " + this.getMovementSpeed()+ "\n";
			s += "Mana regen : " + this.getMultipliers().getPlayerManaRegen()+ "\n";
			s += "Max maxMana : " + this.getMaxMana()+ "\n";
			s += "Max vitality : " + this.getMaxHealth()+ "\n";
			s += "Luck : " + this.getLuck()+ "\n";
			s += "Knockback resistance : " + this.getKnockBackResistance()+ "\n";

			s += "Powers : ";
			for (LowbrainPower powa :
					this.powers.values()) {
				s += powa.getName() + ", ";
			}
			s += "\n";

			return s;
		}
		return "You must set your class and your race first !";
	}

    /**
     * get the player LowbrainRace object
     * @return
     */
	public LowbrainRace getLowbrainRace() {
		return lowbrainRace;
	}

    /**
     * get the player LowbrainClass object
     * @return
     */
	public LowbrainClass getLowbrainClass(){ return lowbrainClass; }

    /**
     * get the player race name
     * @return
     */
	public String getRaceName() {
		return raceName;
	}

    /**
     * get if the player has set his race
     * @return
     */
	public boolean isRaceIsSet() {
		return raceIsSet;
	}

	/**
	 * return current bukkit.Player
	 * @return
	 */
	public Player getPlayer(){
		return this.player;
	}

	/**
	 * return experience needed for next level
	 * @return
	 */
	public float getNextLvl(){
		return this.nextLvl;
	}

    /**
     * get strength attribute
     * @return strength
     */
	public int getStrength() {
		return strength;
	}

    /**
     * get intelligence attribute
     * @return intelligence
     */
	public int getIntelligence() {
		return intelligence;
	}

    /**
     * get dexterity attribute
     * @return dexterity
     */
	public int getDexterity() {
		return dexterity;
	}

    /**
     * get vitality attribute
     * @return vitality
     */
	public int getVitality() {
		return vitality;
	}

    /**
     * get defence attribute
     * @return defence
     */
	public int getDefence() {
		return defence;
	}

    /**
     * get the player class name
     * @return class name
     */
	public String getClassName() {
		return className;
	}

    /**
     * get if the player has sets his class
     * @return class is set
     */
	public boolean isClassIsSet() {
		return classIsSet;
	}

    /**
     * get the current amount of points
     * @return points
     */
	public int getPoints() {
		return points;
	}

    /**
     * get the current experience
     * @return experience
     */
	public float getExperience() {
		return experience;
	}

    /**
     * get the level
     * @return current level
     */
	public int getLvl() {
		return lvl;
	}

    /**
     * get the max mana
     * @return maximum mana
     */
	public float getMaxMana() {
		return maxMana;
	}

    /**
     * get the current amount of mana
     * @return current mana
     */
	public float getCurrentMana() {
		return currentMana;
	}

    /**
     * get the number of deaths
     * @return deaths
     */
	public int getDeaths() {
		return this.deaths;
	}

    /**
     * get agility attribute
     * @return agility
     */
	public int getAgility() {
		return agility;
	}

    /**
     * get the number of kills
     * @return kills
     */
	public int getKills() {
		return kills;
	}

    /**
     * get player luck from generic attribute
     * @return luck
     */
	public double getLuck(){
		return this.getPlayer().getAttribute(Attribute.GENERIC_LUCK).getBaseValue();
	}

    /**
     * get player attack speed from generic attribute
     * @return attack speed
     */
	public double getAttackSpeed(){
		return this.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue();
	}

	public double getMaxHealth() {
		return this.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
	}

    /**
     * get player knockback resistance from generic attribute
     * @return
     */
	public double getKnockBackResistance(){
		return this.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue();
	}

    /**
     * get player movement speed from generic attribute
     * @return
     */
	public double getMovementSpeed(){
		return this.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
	}

    /**
     * return the list of mob the player killed (type)
     * @return HashMap<String, Integer> list of mob kills
     */
	public HashMap<String, Integer> getMobKills() {
		return mobKills;
	}

    /**
     * return the list of skill
     * @return HashMap<String, LowbrainSkill> list of skills
     */
	public HashMap<String, LowbrainSkill> getSkills() {
		return skills;
	}

    /**
     * get the current player LowbrainSkill object
     * @return current skill
     */
	public LowbrainSkill getCurrentSkill() {
		return this.skills.get(this.currentSkill);
	}

    /**
     * get the current amount of skill points
     * @return skill points
     */
	public int getSkillPoints() {
		return this.skillPoints;
	}

    /**
     * return the list of powers
     * @return list of powers
     */
    public HashMap<String, LowbrainPower> getPowers() {
        return powers;
    }

    public int getReputation() {
        return this.reputation;
    }

	//================================================= END OF GETTER =======================================

	//============================================PRIVATE MEHODES FOR PLAYER ATTRIBUTES======================

    /**
     * set the player generic attribute of attack speed using his attributes
     */
	private void setAttackSpeed(){
		if(getSettings().getParameters().getPlayerAttributes().getAttackSpeed().isEnabled()) {
			this.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(this.getMultipliers().getPlayerAttackSpeed());
		}
	}

    /**
     * set the player generic attribute of knockback resistance using his attributes
     */
	private void setKnockBackResistance(){
		if(getSettings().getParameters().getPlayerAttributes().getKnockbackResistance().isEnabled()) {
			this.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(this.getMultipliers().getPlayerKnockbackResistance());
		}
	}

    /**
     * set the player generic attribute of movement speed using his attributes
     */
	private void setMovementSpeed(){
		if(getSettings().getParameters().getPlayerAttributes().getMovementSpeed().isEnabled()){
			//this.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
			this.getPlayer().setWalkSpeed(this.getMultipliers().getPlayerMovementSpeed());
		}

	}

    /**
     * set the player generic attribute of luck using his attributes
     */
    private void setLuck(){
        if(getSettings().getParameters().getPlayerAttributes().getLuck().isEnabled()){
            this.getPlayer().getAttribute(Attribute.GENERIC_LUCK).setBaseValue(this.getMultipliers().getPlayerLuck());
        }
    }

	/**
	 * set player maximum vitality based on his attributes
	 */
	private void setPlayerMaxHealth(){
		if(getSettings().getParameters().getPlayerAttributes().getTotalHealth().isEnabled()) {
			this.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getMultipliers().getPlayerMaxHealth());
		}
	}

	/**
	 * reset player maxMana
	 */
	private void setMana() {
		if(getSettings().getParameters().getPlayerAttributes().getTotalMana().isEnabled()) {
			this.maxMana = this.getMultipliers().getPlayerMaxMana();
		}
	}

	/**
	 * set player display name with current level
	 */
	private void setDisplayName(){
		if(classIsSet && raceIsSet) {
			String prefix = ChatColor.GREEN + "[" + this.lowbrainClass.getTag() + "] ";
			String suffix = ChatColor.GOLD + "[" + this.lvl + "]";
			String name =  ChatColor.WHITE + getPlayer().getName();
			getPlayer().setCustomName(prefix + name + suffix);
			getPlayer().setDisplayName(prefix + name + suffix);
		}
		else {
			getPlayer().setCustomName(getPlayer().getName());
			getPlayer().setDisplayName(getPlayer().getName());
		}
	}

	//==================================================END OF PRIVATE METHODES==============================

	//=============================================PRIVATE METHODS HELPERS==================================

	/**
	 * reset player generic attributes when changes are made
     */
	private void attributeHasChanged(){
		if(classIsSet && raceIsSet) {
			if(multipliers == null){
				multipliers = new Multipliers(this);
			}
			else {
				multipliers.update();
			}

			setPlayerMaxHealth();
			setLuck();
			setMana();
			setKnockBackResistance();
			setAttackSpeed();
			setMovementSpeed();
			refreshScoreBoard();
		}
	}

	/**
	 * regenerate player maxMana based on player intelligence
     */
	private void regenMana(){
		if(currentMana == maxMana){
			return;
		}
		if(getSettings().getParameters().getPlayerAttributes().getManaRegen().isEnabled()){
			float regen = this.getMultipliers().getPlayerManaRegen();
			this.currentMana += regen;
			if(this.currentMana > maxMana)this.currentMana = maxMana;
			refreshScoreBoard();
		}
	}

	/**
	 * start a new maxMana regeneration task on the server
     */
	private void startManaRegenTask(){
		if(this.manaRegenTask == null) {
			this.manaRegenTask = CoreListener.plugin.getServer().getScheduler().runTaskTimer(CoreListener.plugin, new Runnable() {
				@Override
				public void run() {
					regenMana();
				}
			}, 0, getSettings().getManaRegenInterval() * 20);
			CoreListener.plugin.debugInfo("Start regen maxMana task !");
		}
	}

	/**
	 * stop maxMana regeneration task on server
     */
	private void stopManaRegenTask(){
		if (CoreListener.plugin != null && this.manaRegenTask != null){
			CoreListener.plugin.getServer().getScheduler().cancelTask(this.manaRegenTask.getTaskId());
			this.manaRegenTask = null;
		}
	}

    /**
     * return the instance of setting
     * @return setting
     */
	private Settings getSettings(){
		return Settings.getInstance();
	}

    /**
     * increment all attributes from bonus attributes
     * @param nb increment by nb
     */
	private void addBonusAttributes(int nb){
		for (String attribute :
				this.lowbrainRace.getBonusAttributes()) {
			switch (attribute){
				case "vitality":
					addVitality(nb,false,false);
					break;
				case "strength":
					addStrength(nb,false,false);
					break;
				case "intelligence":
					addIntelligence(nb,false,false);
					break;
				case "dexterity":
					addDexterity(nb,false,false);
					break;
				case "magic_resistance":
					addMagicResistance(nb,false,false);
					break;
				case "defence":
					addDefence(nb,false,false);
					break;
				case "agility":
					addAgility(nb,false,false);
					break;
				case "all":
					addVitality(nb,false,false);
					addDefence(nb,false,false);
					addMagicResistance(nb,false,false);
					addDexterity(nb,false,false);
					addIntelligence(nb,false,false);
					addVitality(nb,false,false);
					addStrength(nb,false,false);
					addAgility(nb, false,false);
					break;
			}
		}
		
		attributeHasChanged();
	}

    /**
     * get multiplier class object
     * @return multiplier object
     */
	public Multipliers getMultipliers() {
		return multipliers;
	}

	//===============================================END OF PRIVATE METHODES HELPER===============================

}

