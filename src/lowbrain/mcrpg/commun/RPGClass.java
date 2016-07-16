package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

import lowbrain.mcrpg.main.PlayerListener;

import java.util.ArrayList;
import java.util.List;

public class RPGClass {

	private int strength = 0;
	private int intelligence = 0;
	private int dexterity = 0;
	private int health = 0;
	private int defence = 0;
	private String name = "";
	private int magicResistance = 0;
	private int id = -1;
	private List<String> bonusAttributes = new ArrayList<String>();
	
	public RPGClass(int id){
		this.id = id;
		Initialize();
	}
	
	public void Initialize(){
		FileConfiguration config = PlayerListener.plugin.getConfig();
		name = config.getString("Class."+ id +".name");
		health = config.getInt("Class."+id+".health");
		strength = config.getInt("Class."+id+".strength");
		defence = config.getInt("Class."+id+".defence");
		dexterity = config.getInt("Class."+id+".dexterity");
		intelligence = config.getInt("Class."+id+".intelligence");
		magicResistance = config.getInt("Class."+id+".magicResistance");
		bonusAttributes = config.getStringList("Class."+id+".bonusAttributes");
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

	public String toString() {
		String s = "Name: " + name + "\n";
		s += "Defence: " + defence + "\n";
		s += "Strength: " + strength + "\n";
		s += "Health: " + health + "\n";
		s += "Dexterity: " + dexterity + "\n";
		s += "Intelligence: " + intelligence + "\n";
		s += "Magic Resistance: " + magicResistance + "\n";
		s += "Bonus Attributes: " + bonusAttributes + "\n";
		s += "Id: " + id + "\n";

		return s;
	}


	public List<String> getBonusAttributes() {
		return bonusAttributes;
	}
}
