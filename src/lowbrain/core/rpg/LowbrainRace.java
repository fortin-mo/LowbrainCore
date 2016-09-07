package lowbrain.core.rpg;

import lowbrain.core.config.Races;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moofy on 19/07/2016.
 */
public class LowbrainRace {
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
    private List<String> powers = new ArrayList<>();

    public LowbrainRace(String name){
        this.name = name;
        Initialize();
    }

    public void Initialize(){
        FileConfiguration config = Races.getInstance();
        tag = config.getString(name+".tag","");
        health = config.getInt(name+".health",0);
        strength = config.getInt(name+".strength",0);
        defence = config.getInt(name+".defence",0);
        dexterity = config.getInt(name+".dexterity",0);
        intelligence = config.getInt(name+".intelligence",0);
        magicResistance = config.getInt(name+".magic_resistance",0);
        agility = config.getInt(name+".agility",0);

        max_health = (float)config.getDouble(name+".max_health",0);
        base_health = (float)config.getDouble(name+".base_health",0);
        max_mana = (float)config.getDouble(name+".max_mana",0);
        base_mana = (float)config.getDouble(name+".base_mana",0);

        bonusAttributes = config.getStringList(name+".bonus_attributes");
        if(bonusAttributes == null)bonusAttributes = new ArrayList<String>();
        SetPowers();
    }

    /**
     * set the list of powers available for a particular class
     */
    private void SetPowers(){
        this.powers = Races.getInstance().getStringList(name+".powers");
        if(this.powers == null) this.powers = new ArrayList<String>();
    }

    public String toString() {
        String s = "Name : " + name + "\n";
        s += "Tag : " + tag + "\n";
        s += "Defence : " + defence + "\n";
        s += "Strength : " + strength + "\n";
        s += "Health : " + health + "\n";
        s += "Dexterity : " + dexterity + "\n";
        s += "Intelligence : " + intelligence + "\n";
        s += "Magic Resistance : " + magicResistance + "\n";
        s += "Max health : " + max_health + "\n";
        s += "Base health : " + base_health + "\n";
        s += "Max mana : " + max_mana + "\n";
        s += "Base mana : " + base_mana + "\n";
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