package lowbrain.mcrpg.commun;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import lowbrain.mcrpg.main.PlayerListener;

import java.io.File;

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
		setPlayerMaxHealth();
	}

	/**
	 * return experience needed for next level
	 * @return
	 */
	public double getNextLvl(){
		return this.nextLvl;
	}
	
	/**
	 * return current bukkit.Player
	 * @return
	 */
	public Player getPlayer(){
		return this.player;
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
            
            playerData.save(f);
		} catch (Exception e) {
			PlayerListener.plugin.getLogger().info(e.getMessage());// TODO: handle exception
		}
	}
	
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
	 * level up add one level... increment player points
	 */
	public void levelUP(){
		double maxLevel = PlayerListener.plugin.settings.getMax_lvl();
		if((maxLevel < 0 || this.lvl < maxLevel)){
			this.lvl += 1;

			RPGClass rc = new RPGClass(PlayerListener.plugin.settings.getLstClassId().get(this.idClass + 1));

			for (String attribute :
					rc.getBonusAttributes()) {
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
					case "all":
						addHealth(1,false);
						addDefence(1,false);
						addMagicResistance(1,false);
						addDexterity(1,false);
						addIntelligence(1,false);
						addHealth(1,false);
						addStrength(1,false);
						break;
				}
			}


			points += PlayerListener.plugin.settings.getPoints_per_lvl();
			double lvlExponential = PlayerListener.plugin.settings.getNext_lvl_multiplier();
			this.nextLvl += this.nextLvl * lvlExponential;
		}
		player.setHealth(player.getMaxHealth());
		SendMessage("LEVEL UP !!!! You are now lvl " + this.lvl);
	}

	public void reset(int idClass){
		if(PlayerListener.plugin.settings.isAllow_stats_reset()){
			SetClass(idClass,true);
		}
	}

	/**
	 * Set class of the current player (add default class attributes)
	 * @param id
	 */
	public void SetClass(int id, boolean override){
		if(!classIsSet || override){
			RPGClass rc = new RPGClass(id);
			this.defence = rc.getDefence();
			this.dexterity = rc.getDexterity();
			this.intelligence = rc.getIntelligence();
			this.strength = rc.getStrength();
			this.health = rc.getHealth();
			this.magicResistance = rc.getMagicResistance();
			this.idClass = id;
			this.experience = 0;
			this.nextLvl = PlayerListener.plugin.settings.getFirst_lvl_exp();
			this.lvl = 1;
			SendMessage("You are now a " + rc.getName());
		}
		else if(PlayerListener.plugin.settings.isCan_switch_class()){
			RPGClass oldClass = new RPGClass(this.idClass);
			RPGClass newClass = new RPGClass(id);
			
			if(this.idClass == id){
				SendMessage("You are already a " + oldClass.getName());
				return;
			}
			
			this.defence -= oldClass.getDefence();
			this.dexterity -= oldClass.getDexterity();
			this.intelligence -= oldClass.getIntelligence();
			this.strength -= oldClass.getStrength();
			this.health -= oldClass.getHealth();
			
			this.defence += newClass.getDefence();
			this.dexterity += newClass.getDexterity();
			this.intelligence += newClass.getIntelligence();
			this.strength += newClass.getStrength();
			this.health += newClass.getHealth();
			SendMessage("You are now a " + newClass.getName());
		}
		else{
			SendMessage("You cannot switch class !");
		}
		this.classIsSet = true;
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
	}
	
	/**
	 * add strength to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addStrength(int nb, boolean usePoints){
		int maxStats = PlayerListener.plugin.settings.getMax_stats();
		int oldStrength = this.strength;
		if(usePoints && this.points >= nb){
			this.strength += nb;
			if(maxStats >= 0 && this.strength > maxStats){
				this.strength = maxStats;
			}
			
			double dif = Math.abs(oldStrength - this.strength);
			
			this.points -= dif;
			
			SendMessage("Strength incremented by " + dif);
		}
		else if(!usePoints){
			this.strength += nb;
			if(maxStats >= 0 && this.strength > maxStats){
				this.strength = maxStats;
				SendMessage("Strength set to " + maxStats);
				return;
			}
			SendMessage("Strength incremented by " + nb);
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
	}
	
	/**
	 * add intelligence to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addIntelligence(int nb, boolean usePoints){
		int maxStats = PlayerListener.plugin.settings.getMax_stats();
		int oldIntelligence = this.intelligence;
		if(usePoints && this.points >= nb){
			this.intelligence += nb;
			if(maxStats >= 0 && this.intelligence > maxStats){
				this.intelligence = maxStats;
			}
			
			double dif = Math.abs(oldIntelligence - this.intelligence);
			
			this.points -= dif;
			
			SendMessage("Intelligence incremented by " + dif);
		}
		else if(!usePoints){
			this.intelligence += nb;
			if(maxStats >= 0 && this.intelligence > maxStats){
				this.intelligence = maxStats;
				SendMessage("Intelligence set to " + maxStats);
				return;
			}
			SendMessage("Intelligence incremented by " + nb);
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
	}
	
	/**
	 * add dexterity to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addDexterity(int nb, boolean usePoints){
		int maxStats = PlayerListener.plugin.settings.getMax_stats();
		int oldDexterity = this.dexterity;
		if(usePoints && this.points >= nb){
			this.dexterity += nb;
			if(maxStats >= 0 && this.dexterity > maxStats){
				this.dexterity = maxStats;
			}
			
			double dif = Math.abs(oldDexterity - this.dexterity);
			
			this.points -= dif;
			
			SendMessage("Dexterity incremented by " + dif);
		}
		else if(!usePoints){
			this.dexterity += nb;
			if(maxStats >= 0 && this.dexterity > maxStats){
				this.dexterity = maxStats;
				SendMessage("Dexterity set to " + maxStats);
				return;
			}
			SendMessage("Dexterity incremented by " + nb);
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
		setPlayerMaxHealth();
	}
	/**
	 * add health to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addHealth(int nb, boolean usePoints){
		int maxStats = PlayerListener.plugin.settings.getMax_stats();
		int oldHealth = this.health;
		if(usePoints && this.points >= nb){
			this.health += nb;
			if(maxStats >= 0 && this.health > maxStats){
				this.health = maxStats;
			}
			
			double dif = Math.abs(oldHealth - this.health);
			
			this.points -= dif;
			setPlayerMaxHealth();
			SendMessage("Health incremented by " + dif);
		}
		else if(!usePoints){
			this.health += nb;
			if(maxStats >= 0 && this.health > maxStats){
				this.health = maxStats;
				setPlayerMaxHealth();
				SendMessage("Health set to " + maxStats);
				return;
			}
			setPlayerMaxHealth();
			SendMessage("Health incremented by " + nb);
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
	}

	/**
	 * add defence to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addDefence(int nb, boolean usePoints){
		int maxStats = PlayerListener.plugin.settings.getMax_stats();
		int oldDefence = this.defence;
		if(usePoints && this.points >= nb){
			this.defence += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.defence = maxStats;
			}
			
			double dif = Math.abs(oldDefence - this.defence);
			
			this.points -= dif;
			
			SendMessage("Defence incremented by " + dif);
		}
		else if(!usePoints){
			this.defence += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.defence = maxStats;
				SendMessage("Defence set to " + maxStats);
				return;
			}
			SendMessage("Defence incremented by " + nb);
		}
		else{
			this.ErrorMessageNotEnoughPoints();
			return;
		}
	}

	/**
	 * add magic resistance to current player
	 * @param nb
	 * @param usePoints
     */
	public void addMagicResistance(int nb, boolean usePoints){
		int maxStats = PlayerListener.plugin.settings.getMax_stats();
		int oldMagicResistance = this.defence;
		if(usePoints && this.points >= nb){
			this.magicResistance += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.magicResistance = maxStats;
			}

			double dif = Math.abs(oldMagicResistance - this.defence);

			this.points -= dif;

			SendMessage("Magic Resistance incremented by " + dif);
		}
		else if(!usePoints){
			this.magicResistance += nb;
			if(maxStats >= 0 && this.magicResistance > maxStats){
				this.magicResistance = maxStats;
				SendMessage("Magic Resistance set to " + maxStats);
				return;
			}
			SendMessage("Magic Resistance incremented by " + nb);
		}
		else{
			this.ErrorMessageNotEnoughPoints();
			return;
		}
	}
	
	/**
	 * define if player class has been set
	 * @param classIsSet
	 */
	public void setClassIsSet(boolean classIsSet) {
		this.classIsSet = classIsSet;
	}
	
	/**
	 * set number of points of current player
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
	/**
	 * add points to current player
	 * @param nbPoints
	 */
	public void addPoints(int nbPoints){
		this.points += nbPoints;
	}
	
	public boolean hasEnoughPoints(int nb){
		return this.points >= nb;
	}

	/**
	 * set player level. will not add points
	 * @param lvl
	 */
	public void setLvl(int lvl) {
		int maxLvl = PlayerListener.plugin.settings.getMax_lvl();
		this.lvl = lvl;
		if(maxLvl >= 0 && this.lvl > maxLvl){
			this.lvl = maxLvl;
		}
	}
	
	/**
	 * add lvl to current player. will add points as well
	 * @param nbLvl
	 */
	public void addLevel(int nbLvl){
		int oldLvl = this.lvl;
		this.lvl += nbLvl;
		int maxLvl = PlayerListener.plugin.settings.getMax_lvl();
		int nbPointsPerLevel = PlayerListener.plugin.settings.getPoints_per_lvl();
		
		if(maxLvl >= 0 && this.lvl > maxLvl){
			this.lvl = maxLvl;
			int dif = Math.abs(oldLvl - this.lvl);
			this.points += (dif * nbPointsPerLevel);
		}
	}
	
	/**
	 * send error message to current player
	 */
	private void ErrorMessageNotEnoughPoints(){
		SendMessage("Not enough points !");
		SendMessage("You currently have " + this.points + " points");
	}

	public int getKills() {
		return kills;
	}

	public void addKills(int kills) {
		this.kills += kills;
	}

	public int getDeaths() {
		return this.deaths;
	}

	public void addDeaths(int deaths) {
		this.deaths += deaths;
	}

	public int getMagicResistance() {
		return magicResistance;
	}

	public void setMagicResistance(int magicResistance) {
		this.magicResistance = magicResistance;
	}

	/**
	 * set player maximum health based on rpg player health points
     */
	public void setPlayerMaxHealth(){
		player.setMaxHealth(0.40404 * this.health + 10 );
	}

	private void SendMessage(String msg){
		this.getPlayer().sendMessage(ChatColor.GREEN + msg);
	}

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
}

