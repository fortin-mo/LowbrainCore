package lowbrain.mcrpg.commun;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import lowbrain.mcrpg.main.PlayerListener;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RPGPlayer {
	private Player player;
	private int strength = 0;
	private int intelligence = 0;
	private int dexterity = 0;
	private int health = 0;
	private int defence = 0;
	private double nextLvl = 0;
	private int idClass = 0;
	private int magicResistance = 0;
	private boolean classIsSet = false;
	private int points = 0;
	private double experience = 0;
	private int lvl = 0;
	private int kills = 0;
	private int deaths = 0;
	private double mana = 0;
	private double currentMana = 0;
	private BukkitTask manaRegenTask = null;
	private RPGClass rpgClass = null;
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
			playerData.createSection("Class");
			playerData.set("Class.isSet", false);
			playerData.set("Class.id", "");

			playerData.createSection("Stats");
			playerData.set("Stats.health", 0);
			playerData.set("Stats.lvl", 1);
			playerData.set("Stats.strength", 0);
			playerData.set("Stats.intelligence", 0);
			playerData.set("Stats.dexterity", 0);
			playerData.set("Stats.defence", 0);
			playerData.set("Stats.magicResistance", 0);
			playerData.set("Stats.points", getSettings().starting_points);
			playerData.set("Stats.experience", 0);
			playerData.set("Stats.nextLvl",getSettings().first_lvl_exp);
			playerData.set("Stats.kills",0);
			playerData.set("Stats.deaths",0);
			playerData.set("Stats.currentMana",0);
			playerData.set("Stats.agility",0);
			try {
				playerData.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        strength = playerData.getInt("Stats.strength");
        intelligence = playerData.getInt("Stats.intelligence");
        health = playerData.getInt("Stats.health");
        defence = playerData.getInt("Stats.defence");
        dexterity = playerData.getInt("Stats.dexterity");
		magicResistance = playerData.getInt("Stats.magicResistance");
        classIsSet = playerData.getBoolean("Class.isSet");
        idClass = playerData.getInt("Class.id");
        experience = playerData.getDouble("Stats.experience");
        points = playerData.getInt("Stats.points");
        lvl = playerData.getInt("Stats.lvl");
        nextLvl = playerData.getDouble("Stats.nextLvl");
		kills = playerData.getInt("Stas.kills");
		deaths = playerData.getInt("Stats.deaths");
		currentMana = playerData.getDouble("Stats.currentMana");
		agility = playerData.getInt("Stats.agility");

		this.rpgClass = new RPGClass(idClass);
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
		RPGPowers powa = this.rpgClass.getPowers().get(name);
		if(powa == null){
			SendMessage("You can't cast this spell !");
			return false;
		}

		if(to != null && powa.getRange() == 0){
			SendMessage("You cannot cast this spell on others !");
			return false;
		}
		if(to != null && powa.getRange() > 0){
			double x = this.getPlayer().getLocation().getX() - to.getPlayer().getLocation().getX();
			double y = this.getPlayer().getLocation().getY() - to.getPlayer().getLocation().getY();
			double z = this.getPlayer().getLocation().getZ() - to.getPlayer().getLocation().getZ();

			double distance = Math.sqrt(x*x + y*y + z*z);

			if(powa.getMana() < distance){
				SendMessage("The player is to far away ! Range : " + powa.getRange() + "/" + distance);
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

            playerData.set("Class.isSet", this.classIsSet);
            playerData.set("Class.id", this.idClass);
            
            playerData.set("Stats.health",this.health);
            playerData.set("Stats.lvl", this.lvl);
            playerData.set("Stats.strength", this.strength);
            playerData.set("Stats.intelligence", this.intelligence);
            playerData.set("Stats.dexterity", this.dexterity);
			playerData.set("Stats.magicResistance",this.magicResistance);
            playerData.set("Stats.defence", this.defence);
            playerData.set("Stats.points", this.points);
            playerData.set("Stats.experience", this.experience);
			playerData.set("Stats.nextLvl", this.nextLvl);
			playerData.set("Stats.kills",kills);
			playerData.set("Stats.deaths",deaths);
			playerData.set("Stat.currentMana", currentMana);
			playerData.set("Stats.agility",agility);
            
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

			for (String attribute :
					this.rpgClass.getBonusAttributes()) {
				switch (attribute){
					case "health":
						addHealth(1,false);
						break;
					case "strength":
						addStrength(1,false);
						break;
					case "intelligence":
						addIntelligence(1,false);
						break;
					case "dexterity":
						addDexterity(1,false);
						break;
					case "magicResistance":
						addMagicResistance(1,false);
						break;
					case "defence":
						addDefence(1,false);
						break;
					case "agility":
						addAgility(1,false);
						break;
					case "all":
						addHealth(1,false);
						addDefence(1,false);
						addMagicResistance(1,false);
						addDexterity(1,false);
						addIntelligence(1,false);
						addHealth(1,false);
						addStrength(1,false);
						addAgility(1, false);
						break;
				}
			}


			points += getSettings().points_per_lvl;
			double lvlExponential = getSettings().math.next_lvl_multiplier;
			this.nextLvl += this.nextLvl * lvlExponential;
			setDisplayName();
		}
		player.setHealth(player.getMaxHealth()); //restore health on level up
		this.currentMana = this.mana;//restore mana on level up
		SendMessage("LEVEL UP !!!! You are now lvl " + this.lvl);
	}

	public void reset(int idClass){
		if(getSettings().allow_stats_reset){
			SetClass(idClass,true);
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
	}

	/**
	 * Set class of the current player (add default class attributes)
	 * @param id
	 */
	public void SetClass(int id, boolean override){
		if(!classIsSet || override){
			this.rpgClass = new RPGClass(id);
			this.defence = rpgClass.getDefence();
			this.dexterity = rpgClass.getDexterity();
			this.intelligence = rpgClass.getIntelligence();
			this.strength = rpgClass.getStrength();
			this.health = rpgClass.getHealth();
			this.magicResistance = rpgClass.getMagicResistance();
			this.agility = rpgClass.getAgility();
			this.idClass = id;
			this.experience = 0;
			this.nextLvl = getSettings().first_lvl_exp;
			this.lvl = 1;
			SendMessage("You are now a " + rpgClass.getName());
			AttributeHasChanged();
		}
		else if(getSettings().can_switch_class){
			if(this.idClass == id){
				SendMessage("You are already a " + rpgClass.getName());
				return;
			}

			RPGClass newClass = new RPGClass(id);

			this.defence -= rpgClass.getDefence();
			this.dexterity -= rpgClass.getDexterity();
			this.intelligence -= rpgClass.getIntelligence();
			this.strength -= rpgClass.getStrength();
			this.health -= rpgClass.getHealth();
			this.agility -= rpgClass.getAgility();

			this.defence += newClass.getDefence();
			this.dexterity += newClass.getDexterity();
			this.intelligence += newClass.getIntelligence();
			this.strength += newClass.getStrength();
			this.health += newClass.getHealth();
			this.agility += newClass.getAgility();
			this.idClass = id;
			this.rpgClass = newClass;
			AttributeHasChanged();
			SendMessage("You are now a " + newClass.getName());
		}
		else{
			SendMessage("You cannot switch class !");
		}
		this.classIsSet = true;
	}

	/**
	 * set current player experience
	 * @param experience
	 */
	public void setExperience(double experience) {
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
	public void addStrength(int nb, boolean usePoints){
		int maxStats = getSettings().max_stats;
		int oldStrength = this.strength;
		if(usePoints && this.points >= nb){
			this.strength += nb;
			if(maxStats >= 0 && this.strength > maxStats){
				this.strength = maxStats;
			}
			
			double dif = Math.abs(oldStrength - this.strength);
			
			this.points -= dif;
			AttributeHasChanged();
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
			AttributeHasChanged();
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
	public void addIntelligence(int nb, boolean usePoints){
		int maxStats = getSettings().max_stats;
		int oldIntelligence = this.intelligence;
		if(usePoints && this.points >= nb){
			this.intelligence += nb;
			if(maxStats >= 0 && this.intelligence > maxStats){
				this.intelligence = maxStats;
			}
			
			double dif = Math.abs(oldIntelligence - this.intelligence);
			
			this.points -= dif;
			AttributeHasChanged();
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
			AttributeHasChanged();
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
	public void addDexterity(int nb, boolean usePoints){
		int maxStats = getSettings().max_stats;
		int oldDexterity = this.dexterity;
		if(usePoints && this.points >= nb){
			this.dexterity += nb;
			if(maxStats >= 0 && this.dexterity > maxStats){
				this.dexterity = maxStats;
			}
			
			double dif = Math.abs(oldDexterity - this.dexterity);
			
			this.points -= dif;
			AttributeHasChanged();
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
			AttributeHasChanged();
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
	public void addHealth(int nb, boolean usePoints){
		int maxStats = getSettings().max_stats;
		int oldHealth = this.health;
		if(usePoints && this.points >= nb){
			this.health += nb;
			if(maxStats >= 0 && this.health > maxStats){
				this.health = maxStats;
			}
			
			double dif = Math.abs(oldHealth - this.health);
			
			this.points -= dif;
			AttributeHasChanged();
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
			AttributeHasChanged();
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
	public void addDefence(int nb, boolean usePoints){
		int maxStats = getSettings().max_stats;
		int oldDefence = this.defence;
		if(usePoints && this.points >= nb){
			this.defence += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.defence = maxStats;
			}
			
			double dif = Math.abs(oldDefence - this.defence);
			
			this.points -= dif;
			AttributeHasChanged();
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
			AttributeHasChanged();
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
	public void addMagicResistance(int nb, boolean usePoints){
		int maxStats = getSettings().max_stats;
		int oldMagicResistance = this.defence;
		if(usePoints && this.points >= nb){
			this.magicResistance += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.magicResistance = maxStats;
			}

			double dif = Math.abs(oldMagicResistance - this.defence);
			AttributeHasChanged();
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
			AttributeHasChanged();
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
	public void addAgility(int nb, boolean usePoints){
		int maxStats = getSettings().max_stats;
		int oldAgility = this.agility;
		if(usePoints && this.points >= nb){
			this.agility += nb;
			if(maxStats >= 0 && this.agility > maxStats){
				this.agility = maxStats;
			}

			double dif = Math.abs(oldAgility - this.agility);
			this.points -= dif;
			AttributeHasChanged();
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
			AttributeHasChanged();
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

			for (String attribute :
					this.rpgClass.getBonusAttributes()) {
				switch (attribute){
					case "health":
						addHealth(dif,false);
						break;
					case "strength":
						addStrength(dif,false);
						break;
					case "intelligence":
						addIntelligence(dif,false);
						break;
					case "dexterity":
						addDexterity(dif,false);
						break;
					case "magicResistance":
						addMagicResistance(dif,false);
						break;
					case "defence":
						addDefence(dif,false);
						break;
					case "agility":
						addAgility(dif,false);
						break;
					case "all":
						addHealth(dif,false);
						addDefence(dif,false);
						addMagicResistance(dif,false);
						addDexterity(dif,false);
						addIntelligence(dif,false);
						addHealth(dif,false);
						addStrength(dif,false);
						addAgility(dif, false);
						break;
				}
			}
			
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

	public void setCurrentMana(double currentMana) {
		this.currentMana = currentMana;
	}

	//=============================================== END OF ADD AND SETTER ===============================

	//================================================ GETTER ==============================================

	public String toString(){
		String s = "Level: " + lvl + "\n";
		s += "Class: " + getClassName() + "\n";
		s += "Defence: " + defence + "\n";
		s += "Strength: " + strength + "\n";
		s += "Health: " + health + "\n";
		s += "Dexterity: " + dexterity + "\n";
		s += "Intelligence: " + intelligence + "\n";
		s += "Magic Resistance: " + magicResistance + "\n";
		s += "Kills: " + kills + "\n";
		s += "Deaths: " + deaths + "\n";
		s += "Points left: " + points + "\n";
		s += "Experience: " + experience + "\n";
		s += "Next lvl in: " + (nextLvl - experience) + " xp" + "\n";

		return s;
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
	public double getNextLvl(){
		return this.nextLvl;
	}

	public double getStrength() {
		return strength;
	}

	public double getIntelligence() {
		return intelligence;
	}

	public double getDexterity() {
		return dexterity;
	}

	public double getHealth() {
		return health;
	}

	public double getDefence() {
		return defence;
	}

	public int getIdClass() {
		return idClass;
	}

	public boolean isClassIsSet() {
		return classIsSet;
	}

	public int getPoints() {
		return points;
	}

	public double getExperience() {
		return experience;
	}

	public int getLvl() {
		return lvl;
	}

	public String getClassName(){
		RPGClass rc = new RPGClass(this.idClass);
		return rc.getName();
	}

	public double getMana() {
		return mana;
	}

	public double getCurrentMana() {
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
		double value = Gradient(getSettings().math.attribute_attack_speed_maximum, getSettings().math.attribute_attack_speed_minimum)
				* (agility * getSettings().math.attribute_attack_speed_agility
				+ dexterity * getSettings().math.attribute_attack_speed_dexterity
				+ strength * getSettings().math.attribute_attack_speed_strength)
				+ getSettings().math.attribute_attack_speed_minimum;
		this.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(value);
	}

	private void setKnockBackResistance(){
		double value = Gradient(getSettings().math.attribute_knockback_resistance_maximum,getSettings().math.attribute_knockback_resistance_minimum)
				* (strength * getSettings().math.attribute_knockback_resistance_strength
				+ defence * getSettings().math.attribute_knockback_resistance_defence
				+ dexterity * getSettings().math.attribute_knockback_resistance_dexterity
				+ agility * getSettings().math.attribute_knockback_resistance_agility);
		this.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(value);
	}

	private void setFollowRange(){
		double value = Gradient(getSettings().math.attribute_mob_follow_range_maximum,getSettings().math.attribute_mob_follow_range_minimum)
				* (agility * getSettings().math.attribute_mob_follow_range_agility
				+ intelligence * getSettings().math.attribute_mob_follow_range_intelligence
				+ dexterity * getSettings().math.attribute_mob_follow_range_dexterity)
				+ getSettings().math.attribute_mob_follow_range_minimum;
		this.getPlayer().getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(value);
	}

	private void setMovementSpeed(){
		double value = Gradient(getSettings().math.attribute_movement_speed_maximum,getSettings().math.attribute_movement_speed_minimum)
				* (agility * getSettings().math.attribute_movement_speed_agility
				+ dexterity * getSettings().math.attribute_movement_speed_dexterity)
				+ getSettings().math.attribute_movement_speed_minimum;
		this.getPlayer().setWalkSpeed((float)value);
	}

	/**
	 * set player maximum health based on rpg player health points
	 */
	private void setPlayerMaxHealth(){
		double value = Gradient(getSettings().math.attribute_total_health_maximum,getSettings().math.attribute_total_health_minimum)
				* health * getSettings().math.attribute_total_health_health
				+ getSettings().math.attribute_total_health_minimum;
		player.setMaxHealth(value);
	}

	/**
	 * reset player mana
	 */
	private void setMana() {
		this.mana = Gradient(getSettings().math.attribute_total_mana_maximum,getSettings().math.attribute_total_mana_minimum)
				* this.intelligence * getSettings().math.attribute_total_mana_intelligence
				+ getSettings().math.attribute_total_mana_minimum;
	}

	/**
	 * set player display name with current level
	 */
	private void setDisplayName(){
		getPlayer().setDisplayName(getPlayer().getName() + " [" + this.lvl + "]" );
	}

	private void setLuck(){
		double value = Gradient(getSettings().math.attribute_luck_maximum,getSettings().math.attribute_luck_minimum)
				* (agility * getSettings().math.attribute_luck_agility
				+ intelligence * getSettings().math.attribute_luck_intelligence
				+ dexterity * getSettings().math.attribute_luck_dexterity);
		this.getPlayer().getAttribute(Attribute.GENERIC_LUCK).setBaseValue(value);
	}

	//==================================================END OF PRIVATE METHODES==============================

	//=============================================PRIVATE METHODS HELPERS==================================

	/**
	 * reset player generic attributes when changes are made
     */
	private void AttributeHasChanged(){
		setPlayerMaxHealth();
		setLuck();
		setMana();
		setKnockBackResistance();
		setAttackSpeed();
		setMovementSpeed();
		setFollowRange();
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
		double regen = Gradient(getSettings().math.attribute_mana_regen_maximum,getSettings().math.attribute_mana_regen_minimum)
				* this.intelligence * getSettings().math.attribute_mana_regen_intelligence
				+ getSettings().math.attribute_mana_regen_minimum;
		this.currentMana += regen;
		if(this.currentMana > mana)this.currentMana = mana;
	}

	/**
	 * start a new mana regeneration task on the server
     */
	private void StartManaRegenTask(){
		if(PlayerListener.plugin != null){
			this.manaRegenTask = PlayerListener.plugin.getServer().getScheduler().runTaskTimer(PlayerListener.plugin, new Runnable() {
				@Override
				public void run() {
					RegenMana();
				}
			}, 0, getSettings().mana_regen_interval * 20);
		}
	}

	/**
	 * stop mana regeneration task on server
     */
	private void StopManaRegenTask(){
		if (PlayerListener.plugin != null && this.manaRegenTask != null){
			PlayerListener.plugin.getServer().getScheduler().cancelTask(this.manaRegenTask.getTaskId());
		}
	}

	private double Gradient(double max, double min){
		double maxstats = getSettings().max_stats <= 0 ? 99 : getSettings().max_stats;
		return (max - min)/ maxstats;
	}

	private Settings getSettings(){
		return PlayerListener.plugin.settings;
	}

	private void SendMessage(String msg){
		this.getPlayer().sendMessage(ChatColor.GREEN + msg);
	}

	//===============================================END OF PRIVATE METHODES HELPER===============================
}

