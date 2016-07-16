package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

import lowbrain.mcrpg.main.PlayerListener;

public class RPGClass {
	private int strength = 0;
	private int intelligence = 0;
	private int dexterity = 0;
	private int health = 0;
	private int defence = 0;
	private String name = "";
	private int magicResistance = 0;
	private int id = -1;
	
	public RPGClass(int id){
		this.id = id;
		Initialize();
	}
	
	public void Initialize(){
		FileConfiguration config = PlayerListener.plugin.getConfig();
		switch (this.id) {
            case 1:
                name = "Knight";
                break;
            case 2:
                name = "Paladin";
                break;
            case 3:
                name = "Mage";
                break;
            case 4:
                name = "Archer";
                break;
            default:
                return;
		}


		health = config.getInt("Class."+name+".health");
		strength = config.getInt("Class."+name+".strength");
		defence = config.getInt("Class."+name+".defence");
		dexterity = config.getInt("Class."+name+".dexterity");
		intelligence = config.getInt("Class."+name+".intelligence");
		magicResistance = config.getInt("Class."+name+".magicResistance");
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
		s += "Id: " + id + "\n";

		return s;
	}
}
