package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

import lowbrain.mcrpg.main.PlayerListener;
import org.bukkit.entity.Player;

import java.util.*;

public class RPGClass {

	private int strength = 0;
	private String tag = "";
	private int intelligence = 0;
	private int dexterity = 0;
	private int health = 0;
	private int defence = 0;
	private String name = "";
	private int magicResistance = 0;
	private int agility = 0;
	private List<String> bonusAttributes = new ArrayList<String>();
	private Map<String, RPGPowers> powers = new HashMap<String, RPGPowers>();
	
	public RPGClass(String name){
		this.name = name;
		Initialize();
	}
	
	public void Initialize(){
		FileConfiguration config = PlayerListener.plugin.classesConfig;
		tag = config.getString(name+".tag");
		health = config.getInt(name+".health");
		strength = config.getInt(name+".strength");
		defence = config.getInt(name+".defence");
		dexterity = config.getInt(name+".dexterity");
		intelligence = config.getInt(name+".intelligence");
		magicResistance = config.getInt(name+".magic_resistance");
		agility = config.getInt(name+".agility");
		bonusAttributes = config.getStringList(name+".bonus_attributes");
		SetPowers();
	}

	public String toString() {
		String s = "Name: " + name + "\n";
		s += "Tag: " + tag + "\n";
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

		s += "Powers: ";
		for (RPGPowers powa :
				powers.values()) {
			s += powa.getName() + ", ";
		}
		s += "\n";

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
     */
	private void SetPowers(){
		List<String> tmp = PlayerListener.plugin.classesConfig.getStringList(name+".powers");
		for (String n :
				tmp) {
			powers.put(name,new RPGPowers(n));
		}
	}


	public String getTag() {
		return tag;
	}
}
