package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

import lowbrain.mcrpg.main.PlayerListener;

import java.util.*;

public class RPGClass {

	private int strength = 0;
	private int intelligence = 0;
	private int dexterity = 0;
	private int health = 0;
	private int defence = 0;
	private String name = "";
	private int magicResistance = 0;
	private int agility = 0;
	private int id = -1;
	private List<String> bonusAttributes = new ArrayList<String>();
	private Map<String, RPGPowers> powers = new HashMap<String, RPGPowers>();
	
	public RPGClass(int id){
		this.id = id;
		Initialize();
	}
	
	public void Initialize(){
		FileConfiguration config = PlayerListener.plugin.getConfig();
		name = config.getString("Classes."+ id +".name");
		health = config.getInt("Classes."+id+".health");
		strength = config.getInt("Classes."+id+".strength");
		defence = config.getInt("Classes."+id+".defence");
		dexterity = config.getInt("Classes."+id+".dexterity");
		intelligence = config.getInt("Classes."+id+".intelligence");
		magicResistance = config.getInt("Classes."+id+".magicResistance");
		agility = config.getInt("Classes."+id+".agility");
		bonusAttributes = config.getStringList("Classes."+id+".bonusAttributes");
		SetPowers(config);
	}

	public String toString() {
		String s = "Name: " + name + "\n";
		s += "Defence: " + defence + "\n";
		s += "Strength: " + strength + "\n";
		s += "Health: " + health + "\n";
		s += "Dexterity: " + dexterity + "\n";
		s += "Intelligence: " + intelligence + "\n";
		s += "Magic Resistance: " + magicResistance + "\n";
		s += "Bonus Attributes: ";
		for (String attributes :
				bonusAttributes) {
			s += bonusAttributes + ", ";
		}
		s += "\n";
		s += "Id: " + id + "\n";

		return s;
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

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getMagicResistance() {
		return magicResistance;
	}

	public List<String> getBonusAttributes() {
		return bonusAttributes;
	}

	public int getAgility() {
		return agility;
	}

	public Map<String, RPGPowers> getPowers() {
		return powers;
	}

	/**
	 * set the list of powers available for a particular class
	 * @param config
     */
	private void SetPowers(FileConfiguration config){
		List<String> tmp = config.getStringList("Classes."+id+".powers");
		for (String name :
				tmp) {
			powers.put(name,new RPGPowers(name));
		}
	}


}
