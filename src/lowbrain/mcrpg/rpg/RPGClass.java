package lowbrain.mcrpg.rpg;

import lowbrain.mcrpg.config.Classes;
import org.bukkit.configuration.file.FileConfiguration;

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
	private List<String> powers = new ArrayList<>();
	
	public RPGClass(String name){
		this.name = name;
		Initialize();
	}
	
	public void Initialize(){
		FileConfiguration config = Classes.getInstance();
		tag = config.getString(name+".tag","");
		health = config.getInt(name+".health",0);
		strength = config.getInt(name+".strength",0);
		defence = config.getInt(name+".defence",0);
		dexterity = config.getInt(name+".dexterity",0);
		intelligence = config.getInt(name+".intelligence",0);
		magicResistance = config.getInt(name+".magic_resistance",0);
		agility = config.getInt(name+".agility",0);
		bonusAttributes = config.getStringList(name+".bonus_attributes");
		if(bonusAttributes == null)bonusAttributes = new ArrayList<String>();
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
		for (String powa :
				powers) {
			s += powa + ", ";
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

	public List<String> getPowers() {
		return powers;
	}

	/**
	 * set the list of powers available for a particular class
     */
	private void SetPowers(){
		this.powers = Classes.getInstance().getStringList(name+".powers");
		if(this.powers == null) this.powers = new ArrayList<String>();
	}


	public String getTag() {
		return tag;
	}
}
