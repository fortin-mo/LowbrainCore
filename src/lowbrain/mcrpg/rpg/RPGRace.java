package lowbrain.mcrpg.rpg;

import lowbrain.mcrpg.events.PlayerListener;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Moofy on 19/07/2016.
 */
public class RPGRace {
    private int strength = 0;
    private String tag = "";
    private int intelligence = 0;
    private int dexterity = 0;
    private int health = 0;
    private int defence = 0;
    private String name = "";
    private int magicResistance = 0;
    private int agility = 0;

    private float max_health = 0F;
    private float base_health= 0F;
    private float max_mana = 0F;
    private float base_mana = 0F;

    private List<String> bonusAttributes = new ArrayList<String>();
    private Map<String, RPGPower> powers = new HashMap<String, RPGPower>();

    public RPGRace(String name){
        this.name = name;
        Initialize();
    }

    public void Initialize(){
        FileConfiguration config = PlayerListener.plugin.racesConfig;
        tag = config.getString(name+".tag");
        health = config.getInt(name+".health");
        strength = config.getInt(name+".strength");
        defence = config.getInt(name+".defence");
        dexterity = config.getInt(name+".dexterity");
        intelligence = config.getInt(name+".intelligence");
        magicResistance = config.getInt(name+".magic_resistance");
        agility = config.getInt(name+".agility");

        max_health = (float)config.getDouble(name+".max_health");
        base_health = (float)config.getDouble(name+".base_health");
        max_mana = (float)config.getDouble(name+".max_mana");
        base_mana = (float)config.getDouble(name+".base_mana");

        bonusAttributes = config.getStringList(name+".bonus_attributes");
        SetPowers();
    }

    /**
     * set the list of powers available for a particular class
     */
    private void SetPowers(){
        List<String> tmp = PlayerListener.plugin.racesConfig.getStringList(name+".powers");
        for (String n :
                tmp) {
            powers.put(n, new RPGPower(n));
        }
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
        s += "Max health: " + max_health + "\n";
        s += "Base health: " + base_health + "\n";
        s += "Max mana: " + max_mana + "\n";
        s += "Base mana: " + base_mana + "\n";
        s += "Bonus Attributes: ";
        for (String attributes :
                bonusAttributes) {
            s += bonusAttributes + ", ";
        }
        s += "\n";

        s += "Powers: ";
        for (RPGPower powa :
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

    public Map<String, RPGPower> getPowers() {
        return powers;
    }

    public String getTag() {
        return tag;
    }

    public float getMax_health() {
        return max_health;
    }

    public float getBase_health() {
        return base_health;
    }

    public float getMax_mana() {
        return max_mana;
    }

    public float getBase_mana() {
        return base_mana;
    }
}
