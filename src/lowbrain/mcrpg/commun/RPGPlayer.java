package lowbrain.mcrpg.commun;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import lowbrain.mcrpg.main.PlayerListener;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class RPGPlayer {
	private Player player;
	private int strength = 0;
	private int intelligence = 0;
	private int dexterity = 0;
	private int health = 0;
	private int defence = 0;
	private float nextLvl = 0;
	private String className = "";
	private int magicResistance = 0;
	private boolean classIsSet = false;
	private int points = 0;
	private float experience = 0;
	private int lvl = 0;
	private int kills = 0;
	private int deaths = 0;
	private float mana = 0;
	private float currentMana = 0;
	private BukkitTask manaRegenTask = null;
	private RPGClass rpgClass = null;
	private RPGRace rpgRace = null;
	private String raceName = "";
	private boolean raceIsSet = false;
	private int agility = 0;

	//========================================================= CONSTRUCTOR====================================
	/**
	 * contruct player with bukkit.Player
	 * @param p
	 */
	public RPGPlayer(Player p){
		player = p;
		InitialisePlayer();
	}
	
	/**
	 * read player config yml to initialize current player
	 */
	private void InitialisePlayer(){
		
		if(PlayerListener.plugin == null){
			return;
		}

		File userdata = new File(PlayerListener.plugin.getDataFolder(), File.separator + "PlayerDB");
        File f = new File(userdata, File.separator + player.getUniqueId() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

		//When the player file is created for the first time...
		if (!f.exists()) {
			playerData.createSection("class");
			playerData.set("class.is_set", false);
			playerData.set("class.name", "");

			playerData.createSection("race");
			playerData.set("race.is_set", false);
			playerData.set("race.name", "");

			playerData.createSection("stats");
			playerData.set("stats.health", 0);
			playerData.set("stats.lvl", 1);
			playerData.set("stats.strength", 0);
			playerData.set("stats.intelligence", 0);
			playerData.set("stats.dexterity", 0);
			playerData.set("stats.defence", 0);
			playerData.set("stats.magic_resistance", 0);
			playerData.set("stats.points", getSettings().starting_points);
			playerData.set("stats.experience", 0);
			playerData.set("stats.next_lvl",getSettings().first_lvl_exp);
			playerData.set("stats.kills",0);
			playerData.set("stats.deaths",0);
			playerData.set("stats.current_mana",0);
			playerData.set("stats.agility",0);
			try {
				playerData.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        strength = playerData.getInt("stats.strength");
        intelligence = playerData.getInt("stats.intelligence");
        health = playerData.getInt("stats.health");
        defence = playerData.getInt("stats.defence");
        dexterity = playerData.getInt("stats.dexterity");
		magicResistance = playerData.getInt("stats.magic_resistance");
        classIsSet = playerData.getBoolean("class.is_set");
        className = playerData.getString("class.name");
		raceIsSet = playerData.getBoolean("race.is_set");
		raceName = playerData.getString("race.name");
        experience = (float)playerData.getDouble("stats.experience");
        points = playerData.getInt("stats.points");
        lvl = playerData.getInt("stats.lvl");
        nextLvl = (float)playerData.getDouble("stats.next_lvl");
		kills = playerData.getInt("stats.kills");
		deaths = playerData.getInt("stats.deaths");
		currentMana = (float)playerData.getDouble("stats.current_mana");
		agility = playerData.getInt("stats.agility");

		this.rpgClass = new RPGClass(className);
		this.rpgRace = new RPGRace(raceName);

		AttributeHasChanged();
		setDisplayName();
		StartManaRegenTask();

	}
	//==========================================================END OF CONSTRUCTOR=============================

	//====================================================== USEFULL ==========================================

	/**
	 * cast a spell
	 * @param name name of the spell
	 * @param to rpgPlayer you wish to cast the spell to.. if null will cast to self
     * @return
     */
	public boolean castSpell(String name, RPGPlayer to){

		RPGPower powa = this.rpgClass.getPowers().containsKey(name)
				? this.rpgClass.getPowers().get(name)
				: this.rpgRace.getPowers().containsKey(name)
				? this.rpgRace.getPowers().get(name)
				: null;

		if(powa == null){
			SendMessage("You can't cast this spell !");
			return false;
		}

		if(to != null && powa.getCast_range() == 0){
			SendMessage("You cannot cast this spell on others !");
			return false;
		}
		if(to != null && powa.getCast_range() > 0){
			double x = this.getPlayer().getLocation().getX() - to.getPlayer().getLocation().getX();
			double y = this.getPlayer().getLocation().getY() - to.getPlayer().getLocation().getY();
			double z = this.getPlayer().getLocation().getZ() - to.getPlayer().getLocation().getZ();

			double distance = Math.sqrt(x*x + y*y + z*z);

			if(powa.getMana() < distance){
				SendMessage("The player is to far away ! Range : " + powa.getCast_range() + "/" + distance);
				return false;
			}
		}

		if(this.lvl < powa.getMinLevel()){
			SendMessage("You are to low level to cast this spell ! LVL required : " + powa.getMinLevel());
			return false;
		}
		if(this.intelligence < powa.getMinIntelligence()){
			SendMessage("Insufficient intelligence ! " + powa.getMinIntelligence() + "/" + this.intelligence);
			return false;
		}
		if(this.currentMana < powa.getMana()){
			SendMessage("Insufficient mana ! " + powa.getMana() + "/" + this.currentMana);
			return false;
		}

		this.currentMana -= powa.getMana();
		return powa.Cast(this,to);
	}
	
	/**
	 * save player current data in yml
	 */
	public void SaveData(){
		try {
	        File userdata = new File(PlayerListener.plugin.getDataFolder(), File.separator + "PlayerDB");
	        File f = new File(userdata, File.separator + this.player.getUniqueId() + ".yml");
	        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

            playerData.set("class.is_set", this.classIsSet);
            playerData.set("class.name", this.className);

			playerData.set("race.is_set", this.raceIsSet);
			playerData.set("race.name", this.raceName);
            
            playerData.set("stats.health",this.health);
            playerData.set("stats.lvl", this.lvl);
            playerData.set("stats.strength", this.strength);
            playerData.set("stats.intelligence", this.intelligence);
            playerData.set("stats.dexterity", this.dexterity);
			playerData.set("stats.magic_resistance",this.magicResistance);
            playerData.set("stats.defence", this.defence);
            playerData.set("stats.points", this.points);
            playerData.set("stats.experience", this.experience);
			playerData.set("stats.next_lvl", this.nextLvl);
			playerData.set("stats.kills",kills);
			playerData.set("stats.deaths",deaths);
			playerData.set("stats.current_mana", currentMana);
			playerData.set("stats.agility",agility);
            
            playerData.save(f);
		} catch (Exception e) {
			PlayerListener.plugin.getLogger().info(e.getMessage());// TODO: handle exception
		}
	}

	/**
	 * player diconnect
     */
	public void Disconnect(){
		StopManaRegenTask();
		this.SaveData();
	}

	/**
	 * level up add one level... increment player points
	 */
	public void levelUP(){
		double maxLevel = getSettings().max_lvl;
		if((maxLevel < 0 || this.lvl < maxLevel)){
			this.lvl += 1;

			AddBonusAttributes(1);

			points += getSettings().points_per_lvl;
			double lvlExponential = getSettings().math.next_lvl_multiplier;
			this.nextLvl += this.nextLvl * lvlExponential;
			setDisplayName();
		}
		player.setHealth(player.getMaxHealth()); //restore health on level up
		this.currentMana = this.mana;//restore mana on level up
		SendMessage("LEVEL UP !!!! You are now lvl " + this.lvl);
	}

	public void reset(String c, String r){
		if(getSettings().allow_stats_reset){
			setClass(c,true);
			setRace(r, true);
		}
	}

	public void resetAll(){
		if(getSettings().allow_complete_reset){
			strength = 0;
			intelligence = 0;
			health = 0;
			defence = 0;
			dexterity = 0;
			magicResistance = 0;
			classIsSet = false;
			className = "";
			raceIsSet = false;
			rpgClass = null;
			rpgRace = null;
			raceName = "";
			experience = 0;
			points = getSettings().starting_points;
			lvl = 1;
			nextLvl = getSettings().first_lvl_exp;
			kills = 0;
			deaths = 0;
			currentMana = 0;
			agility = 0;

			AttributeHasChanged();
			setDisplayName();
			SendMessage("Your stats have been reset to zero !");
		}
		else{
			SendMessage("You are not allowed to completely reset your stats !");
		}
	}

	//=================================================== END OF USEFULL =====================================

	//=====================================================  ADD AND SETTER METHODS =================================

	/**
	 * add experience to current player
	 * @param exp
	 */
	public void addExp(double exp){
		this.experience += exp;
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
		if(getSettings().max_stats >=0 && this.strength > getSettings().max_stats)this.strength = getSettings().max_stats;
		else if(this.strength < 0) this.strength = 0;
		AttributeHasChanged();
	}
	
	/**
	 * add strength to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addStrength(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().max_stats;
		if(nb == 0){
			return;
		}
		else if(!getSettings().allow_deduction_points && nb < 0){
			SendMessage("You are not allowed to deduct attribute points !");
			return;
		}
		else if (getSettings().allow_deduction_points && nb < 0 && this.strength == 0){
			SendMessage("You cannot deduct any more points !");
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
			if(callChange)AttributeHasChanged();
			SendMessage("Strength incremented by " + dif);
		}
		else if(!usePoints){
			this.strength += nb;
			if(maxStats >= 0 && this.strength > maxStats){
				this.strength = maxStats;
				SendMessage("Strength set to " + maxStats);
			}else {
				SendMessage("Strength incremented by " + nb);
			}
			if(callChange)AttributeHasChanged();
		}
		else{
			this.ErrorMessageNotEnoughPoints();
			return;
		}
	}
	
	/**
	 * set intelligence of current player
	 * @param intelligence
	 */
	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
		if(getSettings().max_stats >=0 && this.intelligence > getSettings().max_stats)this.intelligence = getSettings().max_stats;
		else if(this.intelligence < 0) this.intelligence = 0;
		AttributeHasChanged();
	}
	
	/**
	 * add intelligence to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addIntelligence(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().max_stats;
		if(nb == 0){
			return;
		}
		else if(!getSettings().allow_deduction_points && nb < 0){
			SendMessage("You are not allowed to deduct attribute points !");
			return;
		}
		else if (getSettings().allow_deduction_points && nb < 0 && this.intelligence == 0){
			SendMessage("You cannot deduct any more points !");
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
			if(callChange)AttributeHasChanged();
			SendMessage("Intelligence incremented by " + dif);

		}
		else if(!usePoints){
			this.intelligence += nb;
			if(maxStats >= 0 && this.intelligence > maxStats){
				this.intelligence = maxStats;
				SendMessage("Intelligence set to " + maxStats);
			}
			else{
				SendMessage("Intelligence incremented by " + nb);
			}
			if(callChange)AttributeHasChanged();
		}
		else{
			this.ErrorMessageNotEnoughPoints();
			return;
		}
	}
	
	/**
	 * set dexterity of current player
	 * @param dexterity
	 */
	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
		if(getSettings().max_stats >=0 && this.dexterity > getSettings().max_stats)this.dexterity = getSettings().max_stats;
		else if(this.dexterity < 0) this.dexterity = 0;
		AttributeHasChanged();
	}
	
	/**
	 * add dexterity to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addDexterity(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().max_stats;
		if(nb == 0){
			return;
		}
		else if(!getSettings().allow_deduction_points && nb < 0){
			SendMessage("You are not allowed to deduct attribute points !");
			return;
		}
		else if (getSettings().allow_deduction_points && nb < 0 && this.dexterity == 0){
			SendMessage("You cannot deduct any more points !");
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
			if(callChange)AttributeHasChanged();
			SendMessage("Dexterity incremented by " + dif);
		}
		else if(!usePoints){
			this.dexterity += nb;
			if(maxStats >= 0 && this.dexterity > maxStats){
				this.dexterity = maxStats;
				SendMessage("Dexterity set to " + maxStats);
			}else {
				SendMessage("Dexterity incremented by " + nb);
			}
			if(callChange)AttributeHasChanged();
		}
		else{
			this.ErrorMessageNotEnoughPoints();
			return;
		}
	}
	
	/**
	 * set health of current player
	 * @param health
	 */
	public void setHealth(int health) {
		this.health = health;
		if(getSettings().max_stats >=0 && this.health > getSettings().max_stats)this.health = getSettings().max_stats;
		else if(this.health < 0) this.health = 0;
		setPlayerMaxHealth();
	}

	/**
	 * add health to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addHealth(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().max_stats;
		if(nb == 0){
			return;
		}
		else if(!getSettings().allow_deduction_points && nb < 0){
			SendMessage("You are not allowed to deduct attribute points !");
			return;
		}
		else if (getSettings().allow_deduction_points && nb < 0 && this.health == 0){
			SendMessage("You cannot deduct any more points !");
			return;
		}
		else if(usePoints && this.points >= nb){
			int oldHealth = this.health;
			this.health += nb;
			if(maxStats >= 0 && this.health > maxStats){
				this.health = maxStats;
			}
			else if (this.health < 0 )this.health = 0;

			int dif = this.health - oldHealth;
			
			this.points -= dif;
			if(callChange)AttributeHasChanged();
			SendMessage("Health incremented by " + dif);
		}
		else if(!usePoints){
			this.health += nb;
			if(maxStats >= 0 && this.health > maxStats){
				this.health = maxStats;
				SendMessage("Health set to " + maxStats);
			}else{
				SendMessage("Health incremented by " + nb);
			}
			if(callChange)AttributeHasChanged();
		}
		else{
			this.ErrorMessageNotEnoughPoints();
			return;
		}
	}
	
	/**
	 * set defence of current player
	 * @param defence
	 */
	public void setDefence(int defence) {
		this.defence = defence;
		if(getSettings().max_stats >=0 && this.defence > getSettings().max_stats)this.defence = getSettings().max_stats;
		else if(this.defence < 0) this.defence = 0;
		AttributeHasChanged();
	}

	/**
	 * add defence to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addDefence(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().max_stats;

		if(nb == 0){
			return;
		}
		else if(!getSettings().allow_deduction_points && nb < 0){
			SendMessage("You are not allowed to deduct attribute points !");
			return;
		}
		else if (getSettings().allow_deduction_points && nb < 0 && this.defence == 0){
			SendMessage("You cannot deduct any more points !");
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
			if(callChange)AttributeHasChanged();
			SendMessage("Defence incremented by " + dif);
		}
		else if(!usePoints){
			this.defence += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.defence = maxStats;
				SendMessage("Defence set to " + maxStats);
			}else{
				SendMessage("Defence incremented by " + nb);
			}
			if(callChange)AttributeHasChanged();
		}
		else{
			this.ErrorMessageNotEnoughPoints();
			return;
		}
	}

	/**
	 * set player magic resitance
	 * @param magicResistance
     */
	public void setMagicResistance(int magicResistance) {
		this.magicResistance = magicResistance;
		if(getSettings().max_stats >=0 && this.magicResistance > getSettings().max_stats)this.magicResistance = getSettings().max_stats;
		else if(this.magicResistance < 0) this.magicResistance = 0;
		AttributeHasChanged();
	}

	/**
	 * add magic resistance to current player
	 * @param nb
	 * @param usePoints
     */
	public void addMagicResistance(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().max_stats;

		if(nb == 0){
			return;
		}
		else if(!getSettings().allow_deduction_points && nb < 0){
			SendMessage("You are not allowed to deduct attribute points !");
			return;
		}
		else if (getSettings().allow_deduction_points && nb < 0 && this.magicResistance == 0){
			SendMessage("You cannot deduct any more points !");
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
			if(callChange)AttributeHasChanged();
			this.points -= dif;

			SendMessage("Magic Resistance incremented by " + dif);
		}
		else if(!usePoints){
			this.magicResistance += nb;
			if(maxStats >= 0 && this.magicResistance > maxStats){
				this.magicResistance = maxStats;
				SendMessage("Magic Resistance set to " + maxStats);
			} else{
				SendMessage("Magic Resistance incremented by " + nb);
			}
			if(callChange)AttributeHasChanged();
		}
		else{
			this.ErrorMessageNotEnoughPoints();
			return;
		}
	}

	/**
	 * set player agility
	 * @param agility
     */
	public void setAgility(int agility) {
		this.agility = agility;
		if(getSettings().max_stats >=0 && this.agility > getSettings().max_stats)this.agility = getSettings().max_stats;
		else if(this.agility < 0) this.agility = 0;
		AttributeHasChanged();
	}

	/**
	 * add agility to the player
	 * @param nb
	 * @param usePoints
	 */
	public void addAgility(int nb, boolean usePoints,boolean callChange){
		int maxStats = getSettings().max_stats;

		if(nb == 0){
			return;
		}
		else if(!getSettings().allow_deduction_points && nb < 0){
			SendMessage("You are not allowed to deduct attribute points !");
			return;
		}
		else if (getSettings().allow_deduction_points && nb < 0 && this.agility == 0){
			SendMessage("You cannot deduct any more points !");
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
			if(callChange)AttributeHasChanged();
			SendMessage("Agility incremented by " + dif);
		}
		else if(!usePoints){
			this.agility += nb;
			if(maxStats >= 0 && this.agility > maxStats){
				this.agility = maxStats;
				SendMessage("Agility set to " + maxStats);
			}else{
				SendMessage("Agility incremented by " + nb);
			}
			if(callChange)AttributeHasChanged();
		}
		else{
			this.ErrorMessageNotEnoughPoints();
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
		int maxLvl = getSettings().max_lvl;
		int nbPointsPerLevel = getSettings().points_per_lvl;
		
		if(maxLvl > 0 && this.lvl > maxLvl)this.lvl= maxLvl;
		else if (this.lvl <= 0) this.lvl = 1;
		
		int dif = this.lvl - oldLvl;
		this.setDisplayName();
		if(ajust){
			this.points += dif * nbPointsPerLevel;
			if(this.points < 0) this.points = 0;

			AddBonusAttributes(dif);
		}
	}

	public void addKills(int kills) {
		this.kills += kills;
	}

	public void addDeaths(int deaths) {
		this.deaths += deaths;
	}

	public int getMagicResistance() {
		return magicResistance;
	}

	public void setCurrentMana(float currentMana) {
		this.currentMana = currentMana;
	}

	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}

	public void setRace(String n, boolean override){
		if(!raceIsSet || override){
			this.rpgRace = new RPGRace(n);

			this.defence += rpgRace.getDefence();
			this.dexterity += rpgRace.getDexterity();
			this.intelligence += rpgRace.getIntelligence();
			this.strength += rpgRace.getStrength();
			this.health += rpgRace.getHealth();
			this.magicResistance += rpgRace.getMagicResistance();
			this.agility += rpgRace.getAgility();

			this.raceName = n;
			this.experience = 0;
			this.nextLvl = getSettings().first_lvl_exp;
			this.lvl = 1;
			SendMessage("You are now a " + rpgRace.getName());
			this.raceIsSet = true;
			AttributeHasChanged();
		}
		else if(getSettings().can_switch_race){
			if(this.raceName == n){
				SendMessage("You are already a " + rpgRace.getName());
				return;
			}

			RPGRace newRace = new RPGRace(n);
			this.raceIsSet = true;

			this.defence -= rpgRace.getDefence();
			this.dexterity -= rpgRace.getDexterity();
			this.intelligence -= rpgRace.getIntelligence();
			this.strength -= rpgRace.getStrength();
			this.health -= rpgRace.getHealth();
			this.agility -= rpgRace.getAgility();

			AddBonusAttributes(this.lvl * -1 - 1);

			this.defence += newRace.getDefence();
			this.dexterity += newRace.getDexterity();
			this.intelligence += newRace.getIntelligence();
			this.strength += newRace.getStrength();
			this.health += newRace.getHealth();
			this.agility += newRace.getAgility();
			this.raceName = n;
			this.rpgRace = newRace;

			AddBonusAttributes(this.lvl -1);

			SendMessage("You are now a " + newRace.getName());
		}
		else{
			SendMessage("You cannot switch race !");
		}
		this.raceIsSet = true;
	}

	/**
	 * Set class of the current player (add default class attributes)
	 */
	public void setClass(String n, boolean override){
		if(!classIsSet || override){
			this.rpgClass = new RPGClass(n);

			this.defence += rpgClass.getDefence();
			this.dexterity += rpgClass.getDexterity();
			this.intelligence += rpgClass.getIntelligence();
			this.strength += rpgClass.getStrength();
			this.health += rpgClass.getHealth();
			this.magicResistance += rpgClass.getMagicResistance();
			this.agility += rpgClass.getAgility();

			this.className = n;
			this.experience = 0;
			this.nextLvl = getSettings().first_lvl_exp;
			this.lvl = 1;
			SendMessage("You are now a " + rpgClass.getName());
			this.classIsSet = true;
			AttributeHasChanged();
		}
		else if(getSettings().can_switch_class){
			if(this.className == n){
				SendMessage("You are already a " + rpgClass.getName());
				return;
			}

			this.classIsSet = true;
			RPGClass newClass = new RPGClass(n);

			this.defence -= rpgClass.getDefence();
			this.dexterity -= rpgClass.getDexterity();
			this.intelligence -= rpgClass.getIntelligence();
			this.strength -= rpgClass.getStrength();
			this.health -= rpgClass.getHealth();
			this.agility -= rpgClass.getAgility();

			AddBonusAttributes(this.lvl * -1 - 1);

			this.defence += newClass.getDefence();
			this.dexterity += newClass.getDexterity();
			this.intelligence += newClass.getIntelligence();
			this.strength += newClass.getStrength();
			this.health += newClass.getHealth();
			this.agility += newClass.getAgility();
			this.className = n;
			this.rpgClass = newClass;

			AddBonusAttributes(this.lvl -1);

			SendMessage("You are now a " + newClass.getName());
		}
		else{
			SendMessage("You cannot switch class !");
		}
		this.classIsSet = true;
	}

	public void setRaceIsSet(boolean raceIsSet) {
		this.raceIsSet = raceIsSet;
	}

	//=============================================== END OF ADD AND SETTER ===============================

	//================================================ GETTER ==============================================

	public String toString(){
		if(classIsSet && raceIsSet) {
			String s = "Level: " + lvl + "\n";
			s += "Class: " + getClassName() + "\n";
			s += "Race: " + getRaceName() + "\n";
			s += "Defence: " + defence + "\n";
			s += "Strength: " + strength + "\n";
			s += "Health: " + health + "\n";
			s += "Dexterity: " + dexterity + "\n";
			s += "Intelligence: " + intelligence + "\n";
			s += "Magic Resistance: " + magicResistance + "\n";
			s += "Agility: " + agility + "\n";
			s += "Kills: " + kills + "\n";
			s += "Deaths: " + deaths + "\n";
			s += "Points left: " + points + "\n";
			s += "Experience: " + experience + "\n";
			s += "Next lvl in: " + (nextLvl - experience) + " xp" + "\n";

			s += "Attack speed : " + this.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue()+ "\n";
			s += "Movement speed : " + this.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue()+ "\n";
			s += "Mana regen : " + PlayerListener.getPlayerManaRegen(this)+ "\n";
			s += "Max mana : " + this.getMana()+ "\n";
			s += "Max health : " + this.getPlayer().getMaxHealth()+ "\n";
			s += "Luck : " + this.getPlayer().getAttribute(Attribute.GENERIC_LUCK).getBaseValue()+ "\n";
			s += "Knockback resistance : " + this.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue()+ "\n";

			s += "Powers: ";
			for (RPGPower powa :
					rpgClass.getPowers().values()) {
				s += powa.getName() + ", ";
			}
			for (RPGPower powa :
					rpgRace.getPowers().values()) {
				s += powa.getName() + ", ";
			}
			s += "\n";

			return s;
		}
		return "You must set your class and your race first !";
	}

	public RPGRace getRpgRace() {
		return rpgRace;
	}

	public RPGClass getRpgClass(){ return rpgClass; }

	public String getRaceName() {
		return raceName;
	}

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

	public int getStrength() {
		return strength;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public int getDexterity() {
		return dexterity;
	}

	public int getHealth() {
		return health;
	}

	public int getDefence() {
		return defence;
	}

	public String getClassName() {
		return className;
	}

	public boolean isClassIsSet() {
		return classIsSet;
	}

	public int getPoints() {
		return points;
	}

	public float getExperience() {
		return experience;
	}

	public int getLvl() {
		return lvl;
	}

	public float getMana() {
		return mana;
	}

	public float getCurrentMana() {
		return currentMana;
	}

	public int getDeaths() {
		return this.deaths;
	}

	public int getAgility() {
		return agility;
	}

	public int getKills() {
		return kills;
	}

	//================================================= END OF GETTER =======================================

	//============================================PRIVATE MEHODES FOR PLAYER ATTRIBUTES======================

	private void setAttackSpeed(){
		if(getSettings().math.attribute_attack_speed_enable) {
			this.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(PlayerListener.getPlayerAttackSpeed(this));
		}
	}

	private void setKnockBackResistance(){
		if(getSettings().math.attribute_knockback_resistance_enable) {
			this.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(PlayerListener.getPlayerKnockbackResistance(this));
		}
	}

	private void setMovementSpeed(){
		if(getSettings().math.attribute_movement_speed_enable){
			//this.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
			this.getPlayer().setWalkSpeed(PlayerListener.getPlayerMovementSpeed(this));
		}

	}

	/**
	 * set player maximum health based on rpg player health points
	 */
	private void setPlayerMaxHealth(){
		if(getSettings().math.attribute_total_health_enable) {
			this.getPlayer().setMaxHealth(PlayerListener.getPlayerMaxHealth(this));
		}
	}

	/**
	 * reset player mana
	 */
	private void setMana() {
		if(getSettings().math.attribute_total_mana_enable) {
			this.mana = PlayerListener.getPlayerMaxMana(this);
		}
		//this.mana = (float)Gradient(this.rpgRace.getMax_mana(),this.rpgRace.getBase_mana())
		//		* this.intelligence * getSettings().math.attribute_total_mana_intelligence
		//		+ this.rpgRace.getBase_mana();
	}

	/**
	 * set player display name with current level
	 */
	private void setDisplayName(){
		if(classIsSet && raceIsSet) {
			getPlayer().setDisplayName("[" + this.rpgClass.getTag() + "] " + getPlayer().getName() + " [" + this.lvl + "]");
		}
	}

	private void setLuck(){
		if(getSettings().math.attribute_luck_enable){
			this.getPlayer().getAttribute(Attribute.GENERIC_LUCK).setBaseValue(PlayerListener.getPlayerLuck(this));
		}
	}

	//==================================================END OF PRIVATE METHODES==============================

	//=============================================PRIVATE METHODS HELPERS==================================

	/**
	 * reset player generic attributes when changes are made
     */
	private void AttributeHasChanged(){
		if(classIsSet && raceIsSet) {
			setPlayerMaxHealth();
			setLuck();
			setMana();
			setKnockBackResistance();
			setAttackSpeed();
			setMovementSpeed();
		}
	}

	/**
	 * send error message to current player
	 */
	private void ErrorMessageNotEnoughPoints(){
		SendMessage("Not enough points !");
		SendMessage("You currently have " + this.points + " points");
	}

	/**
	 * regenerate player mana based on player intelligence
     */
	private void RegenMana(){
		if(currentMana == mana){
			return;
		}
		if(getSettings().math.attribute_mana_regen_enable){
			float regen = PlayerListener.getPlayerManaRegen(this);
			this.currentMana += regen;
			if(this.currentMana > mana)this.currentMana = mana;
		}
	}

	/**
	 * start a new mana regeneration task on the server
     */
	private void StartManaRegenTask(){
		this.manaRegenTask = PlayerListener.plugin.getServer().getScheduler().runTaskTimer(PlayerListener.plugin, new Runnable() {
			@Override
			public void run() {
				RegenMana();
			}
		}, 0, getSettings().mana_regen_interval * 20);

		PlayerListener.plugin.debugMessage("Start regen mana task !");
	}

	/**
	 * stop mana regeneration task on server
     */
	private void StopManaRegenTask(){
		if (PlayerListener.plugin != null && this.manaRegenTask != null){
			PlayerListener.plugin.getServer().getScheduler().cancelTask(this.manaRegenTask.getTaskId());
		}
	}

	private Config getSettings(){
		return PlayerListener.plugin.config;
	}

	public void SendMessage(String msg){
		SendMessage(msg,ChatColor.GREEN,"");
	}

	public void SendMessage(String msg, ChatColor color){
		SendMessage(msg, color,"");
	}

	public void SendMessage(String msg, String prefix){
		SendMessage(msg,ChatColor.GREEN, prefix);
	}

	public void SendMessage(String msg, ChatColor color, String prefix){
		this.getPlayer().sendMessage(prefix + color + msg);
	}

	private void AddBonusAttributes(int nb){
		for (String attribute :
				this.rpgClass.getBonusAttributes()) {
			switch (attribute){
				case "health":
					addHealth(nb,false,false);
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
					addHealth(nb,false,false);
					addDefence(nb,false,false);
					addMagicResistance(nb,false,false);
					addDexterity(nb,false,false);
					addIntelligence(nb,false,false);
					addHealth(nb,false,false);
					addStrength(nb,false,false);
					addAgility(nb, false,false);
					break;
			}
		}

		for (String attribute :
				this.rpgRace.getBonusAttributes()) {
			switch (attribute){
				case "health":
					addHealth(nb,false,false);
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
					addHealth(nb,false,false);
					addDefence(nb,false,false);
					addMagicResistance(nb,false,false);
					addDexterity(nb,false,false);
					addIntelligence(nb,false,false);
					addHealth(nb,false,false);
					addStrength(nb,false,false);
					addAgility(nb, false,false);
					break;
			}
		}
		
		AttributeHasChanged();
	}

	//===============================================END OF PRIVATE METHODES HELPER===============================
	
}

