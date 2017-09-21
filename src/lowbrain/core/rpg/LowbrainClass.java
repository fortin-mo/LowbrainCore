package lowbrain.core.rpg;

import lowbrain.core.main.LowbrainCore;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * represents a single lowbrain class
 */
public class LowbrainClass {

	private int strength = 0;
	private String tag = "";
	private int intelligence = 0;
	private int dexterity = 0;
	private int vitality = 0;
	private int defence = 0;
	private String name = "";
	private int magicResistance = 0;
	private int agility = 0;
	private List<String> bonusAttributes = new ArrayList<String>();
	private List<String> powers = new ArrayList<>();

	/**
	 * Create class object using the name
	 * @param name name of the class
	 */
	public LowbrainClass(String name){
		this.name = name;
		Initialize();
	}

	/**
	 * initialize
	 */
	public void Initialize(){
		FileConfiguration config = LowbrainCore.getInstance().getConfigHandler().classes();
		tag = config.getString(name+".tag","");
		vitality = config.getInt(name+".vitality",0);
		strength = config.getInt(name+".strength",0);
		defence = config.getInt(name+".defence",0);
		dexterity = config.getInt(name+".dexterity",0);
		intelligence = config.getInt(name+".intelligence",0);
		magicResistance = config.getInt(name+".magic_resistance",0);
		agility = config.getInt(name+".agility",0);
		bonusAttributes = config.getStringList(name+".bonus_attributes");

		if(bonusAttributes == null)
		    bonusAttributes = new ArrayList<String>();

		initPowers();
	}

	/**
	 * Create a formatted string with all the informations of the specific class
	 * @return class's information
	 */
	public String toString() {
		String s = "Name : " + name + "\n";
		s += "Tag : " + tag + "\n";
		s += "Defence : " + defence + "\n";
		s += "Strength : " + strength + "\n";
		s += "Vitality : " + vitality + "\n";
		s += "Dexterity : " + dexterity + "\n";
		s += "Intelligence : " + intelligence + "\n";
		s += "Magic Resistance : " + magicResistance + "\n";
		s += "Bonus Attributes : ";
		for (String attributes :
				bonusAttributes) {
			s += bonusAttributes + ", ";
		}
		s += "\n";

		s += "Powers : ";
		for (String powa :
				powers) {
			s += powa + ", ";
		}
		s += "\n";

		return s;
	}

	/**
	 * return strength attribute
	 * @return strength
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * return intelligence attribute
	 * @return intelligence
	 */
	public int getIntelligence() {
		return intelligence;
	}

	/**
	 * return dexterity attribute
	 * @return dexterity
	 */
	public int getDexterity() {
		return dexterity;
	}

	/**
	 * return vitality attribute
	 * @return vitality
	 */
	public int getVitality() {
		return vitality;
	}

	/**
	 * return defence attribute
	 * @return defence
	 */
	public int getDefence() {
		return defence;
	}

	/**
	 * return the name of the class
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * return the magic resitance attribute
	 * @return magicResistance
	 */
	public int getMagicResistance() {
		return magicResistance;
	}

	/**
	 * return a list of bonus attributes as string
	 * @return bonusAttributes
	 */
	public List<String> getBonusAttributes() {
		return bonusAttributes;
	}

	/**
	 * return agility attribute
	 * @return agility
	 */
	public int getAgility() {
		return agility;
	}

	/**
	 * return a list of available powers as string
	 * @return powers
	 */
	public List<String> getPowers() {
		return powers;
	}

	/**
	 * set the list of powers available for a particular class
     */
	private void initPowers(){
		this.powers = LowbrainCore.getInstance().getConfigHandler().classes().getStringList(name+".powers");

		if(this.powers == null)
		    this.powers = new ArrayList<String>();
	}

	/**
	 * return the class tag
	 * @return tag
	 */
	public String getTag() {
		return tag;
	}
}
