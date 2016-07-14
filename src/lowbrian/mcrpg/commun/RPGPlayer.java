package lowbrian.mcrpg.commun;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import lowbrain.mcrpg.main.PlayerListener;

public class RPGPlayer {
	private Player player;
	private int strength = -1;
	private int intelligence = -1;
	private int dexterity = -1;
	private int health = -1;
	private int defence = -1;
	private double nextLvl = -1;
	private int idClass = -1;
	private boolean classIsSet = false;
	private int points = -1;
	private double experience = -1;
	private int lvl = -1;
	
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
        classIsSet = playerData.getBoolean("Class.isSet");
        idClass = playerData.getInt("Class.id");
        experience = playerData.getDouble("Stats.experience");
        points = playerData.getInt("Stats.points");
        lvl = playerData.getInt("Stats.lvl");
        nextLvl = playerData.getDouble("Stats.next_lvl");
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
            
            playerData.set("Stats.health", this.health);
            playerData.set("Stats.lvl", this.lvl);
            playerData.set("Stats.strength", this.strength);
            playerData.set("Stats.intelligence", this.intelligence);
            playerData.set("Stats.dexterity", this.dexterity);
            playerData.set("Stats.defence", this.defence);
            playerData.set("Stats.points", this.points);
            playerData.set("Stats.experience", this.experience);
            
            playerData.save(f);
		} catch (Exception e) {
			PlayerListener.plugin.getLogger().info(e.getMessage());// TODO: handle exception
		}
	}
	
	/**
	 * add experience to current player
	 * @param exp
	 */
	public void AddExp(double exp){
		this.experience += exp;
		if(this.experience >= nextLvl){
			this.LevelUP();
		}
	}
	
	/**
	 * level up add one level... increment player points
	 */
	public void LevelUP(){
		double maxLevel = PlayerListener.plugin.getConfig().getInt("Settings.max_lvl");
		if((maxLevel < 0 || this.lvl < maxLevel)){
			this.lvl += 1;
			points += PlayerListener.plugin.getConfig().getDouble("Settings.points_per_lvl");
			double lvlExponential = PlayerListener.plugin.getConfig().getDouble("Settings.next_lvl_exponential");
			this.nextLvl = Math.pow(this.nextLvl,lvlExponential);
		}
	}
	
	/**
	 * Set class of the current player (add default class attributes)
	 * @param id
	 */
	public void SetClass(int id){
		if(!classIsSet){
			RPGClass rc = new RPGClass(id);
			this.defence = rc.getDefence();
			this.dexterity = rc.getDexterity();
			this.intelligence = rc.getIntelligence();
			this.strength = rc.getStrength();
			this.health = rc.getHealth();
		}
		else if(PlayerListener.plugin.getConfig().getBoolean("Settings.allow_switch_class")){
			RPGClass oldClass = new RPGClass(this.idClass);
			RPGClass newClass = new RPGClass(id);
			
			if(this.idClass == id){
				this.getPlayer().sendMessage("You are already a " + oldClass.getName());
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
			
			this.getPlayer().sendMessage("You are now a " + newClass.getName());
		}
		else{
			this.getPlayer().sendMessage("You cannot switch class !");
		}
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
		int maxStats = PlayerListener.plugin.getConfig().getInt("Settings.max_stats");
		int oldStrength = this.strength;
		if(usePoints && this.points >= nb){
			this.strength += nb;
			if(maxStats >= 0 && this.strength > maxStats){
				this.strength = maxStats;
			}
			
			double dif = Math.abs(oldStrength - this.strength);
			
			this.points -= dif;
			
			this.player.sendMessage("Strength incremented by " + dif);
		}
		else if(!usePoints){
			this.strength += nb;
			if(maxStats >= 0 && this.strength > maxStats){
				this.strength = maxStats;
				this.player.sendMessage("Strength set to " + maxStats);
				return;
			}
			this.player.sendMessage("Strength incremented by " + nb);
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
		int maxStats = PlayerListener.plugin.getConfig().getInt("Settings.max_stats");
		int oldIntelligence = this.intelligence;
		if(usePoints && this.points >= nb){
			this.intelligence += nb;
			if(maxStats >= 0 && this.intelligence > maxStats){
				this.intelligence = maxStats;
			}
			
			double dif = Math.abs(oldIntelligence - this.intelligence);
			
			this.points -= dif;
			
			this.player.sendMessage("Intelligence incremented by " + dif);
		}
		else if(!usePoints){
			this.intelligence += nb;
			if(maxStats >= 0 && this.intelligence > maxStats){
				this.intelligence = maxStats;
				this.player.sendMessage("Intelligence set to " + maxStats);
				return;
			}
			this.player.sendMessage("Intelligence incremented by " + nb);
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
		int maxStats = PlayerListener.plugin.getConfig().getInt("Settings.max_stats");
		int oldDexterity = this.dexterity;
		if(usePoints && this.points >= nb){
			this.dexterity += nb;
			if(maxStats >= 0 && this.dexterity > maxStats){
				this.dexterity = maxStats;
			}
			
			double dif = Math.abs(oldDexterity - this.dexterity);
			
			this.points -= dif;
			
			this.player.sendMessage("Dexterity incremented by " + dif);
		}
		else if(!usePoints){
			this.dexterity += nb;
			if(maxStats >= 0 && this.dexterity > maxStats){
				this.dexterity = maxStats;
				this.player.sendMessage("Dexterity set to " + maxStats);
				return;
			}
			this.player.sendMessage("Dexterity incremented by " + nb);
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
	}
	/**
	 * add health to current player
	 * @param nb
	 * @param usePoints
	 */
	public void addHealth(int nb, boolean usePoints){
		int maxStats = PlayerListener.plugin.getConfig().getInt("Settings.max_stats");
		int oldHealth = this.health;
		if(usePoints && this.points >= nb){
			this.health += nb;
			if(maxStats >= 0 && this.health > maxStats){
				this.health = maxStats;
			}
			
			double dif = Math.abs(oldHealth - this.health);
			
			this.points -= dif;
			
			this.player.sendMessage("Health incremented by " + dif);
		}
		else if(!usePoints){
			this.health += nb;
			if(maxStats >= 0 && this.health > maxStats){
				this.health = maxStats;
				this.player.sendMessage("Health set to " + maxStats);
				return;
			}
			this.player.sendMessage("Health incremented by " + nb);
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
		int maxStats = PlayerListener.plugin.getConfig().getInt("Settings.max_stats");
		int oldDefence = this.defence;
		if(usePoints && this.points >= nb){
			this.defence += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.defence = maxStats;
			}
			
			double dif = Math.abs(oldDefence - this.defence);
			
			this.points -= dif;
			
			this.player.sendMessage("Defence incremented by " + dif);
		}
		else if(!usePoints){
			this.defence += nb;
			if(maxStats >= 0 && this.defence > maxStats){
				this.defence = maxStats;
				this.player.sendMessage("Defence set to " + maxStats);
				return;
			}
			this.player.sendMessage("Defence incremented by " + nb);
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
		int maxLvl = PlayerListener.plugin.getConfig().getInt("Settings.max_lvl");
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
		int maxLvl = PlayerListener.plugin.getConfig().getInt("Settings.max_lvl");
		int nbPointsPerLevel = PlayerListener.plugin.getConfig().getInt("Settings.points_per_lvl");
		
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
		this.player.sendMessage("Not enough points !");
		this.player.sendMessage("You currently have " + this.points + " points");
	}
}

