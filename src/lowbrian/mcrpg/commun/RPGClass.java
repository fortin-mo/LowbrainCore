package lowbrian.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

import lowbrain.mcrpg.main.PlayerListener;

public class RPGClass {
	private int strength = -1;
	private int intelligence = -1;
	private int dexterity = -1;
	private int health = -1;
	private int defence = -1;
	private String name = "";
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
			health = config.getInt("Class.Knight.health");
			strength = config.getInt("Class.Knight.strength");
			defence = config.getInt("Class.Knight.defence");
			dexterity = config.getInt("Class.Knight.dexterity");
			intelligence = config.getInt("Class.Knight.intelligence");
			break;
		case 2:
			name = "Paladin";
			health = config.getInt("Class.Paladin.health");
			strength = config.getInt("Class.Paladin.strength");
			defence = config.getInt("Class.Paladin.defence");
			dexterity = config.getInt("Class.Paladin.dexterity");
			intelligence = config.getInt("Class.Paladin.intelligence");
			break;
		case 3:
			name = "Mage";
			health = config.getInt("Class.Mage.health");
			strength = config.getInt("Class.Mage.strength");
			defence = config.getInt("Class.Mage.defence");
			dexterity = config.getInt("Class.Mage.dexterity");
			intelligence = config.getInt("Class.Mage.intelligence");
			break;
		case 4:
			name = "Archer";
			health = config.getInt("Class.Archer.health");
			strength = config.getInt("Class.Archer.strength");
			defence = config.getInt("Class.Archer.defence");
			dexterity = config.getInt("Class.Archer.dexterity");
			intelligence = config.getInt("Class.Archer.intelligence");
			break;
		}
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
}
