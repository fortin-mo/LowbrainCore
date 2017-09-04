package lowbrain.core.rpg;

import lowbrain.core.config.Races;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a single lowbrain race
 */
public class LowbrainRace {
    private int strength = 0;
    private String tag = "";
    private int intelligence = 0;
    private int dexterity = 0;
    private int vitality = 0;
    private int defence = 0;
    private String name = "";
    private int magicResistance = 0;
    private int agility = 0;

    private float maxHealth = 0F;
    private float baseHealth = 0F;
    private float maxMana = 0F;
    private float baseMana = 0F;

    private List<String> bonusAttributes = new ArrayList<String>();
    private List<String> powers = new ArrayList<>();

    /**
     * construct race object using name
     * will retrieve information from races.yml
     * @param name
     */
    public LowbrainRace(String name){
        this.name = name;
        Initialize();
    }

    /**
     * initialise
     * called when constructed
     */
    public void Initialize(){
        FileConfiguration config = Races.getInstance();

        tag = config.getString(name+".tag","");
        vitality = config.getInt(name+".vitality",0);
        strength = config.getInt(name+".strength",0);
        defence = config.getInt(name+".defence",0);
        dexterity = config.getInt(name+".dexterity",0);
        intelligence = config.getInt(name+".intelligence",0);
        magicResistance = config.getInt(name+".magic_resistance",0);
        agility = config.getInt(name+".agility",0);

        maxHealth = (float)config.getDouble(name+".max_health",0);
        baseHealth = (float)config.getDouble(name+".base_health",0);
        maxMana = (float)config.getDouble(name+".max_mana",0);
        baseMana = (float)config.getDouble(name+".base_mana",0);

        bonusAttributes = config.getStringList(name+".bonus_attributes");
        if(bonusAttributes == null)bonusAttributes = new ArrayList<String>();
        SetPowers();
    }

    /**
     * set the list of powers available for a particular class
     */
    private void SetPowers(){
        this.powers = Races.getInstance().getStringList(name+".powers");
        if(this.powers == null)
            this.powers = new ArrayList<String>();
    }

    /**
     * format a string using race information
     * @return formated information
     */
    public String toString() {
        String s = "Name : " + name + "\n";
        s += "Tag : " + tag + "\n";
        s += "Defence : " + defence + "\n";
        s += "Strength : " + strength + "\n";
        s += "Health : " + vitality + "\n";
        s += "Dexterity : " + dexterity + "\n";
        s += "Intelligence : " + intelligence + "\n";
        s += "Magic Resistance : " + magicResistance + "\n";
        s += "Max vitality : " + maxHealth + "\n";
        s += "Base vitality : " + baseHealth + "\n";
        s += "Max mana : " + maxMana + "\n";
        s += "Base mana : " + baseMana + "\n";
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
     * get the strength attribute
     * @return strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * get the intelligence attribute
     * @return intelligence
     */
    public int getIntelligence() {
        return intelligence;
    }

    /**
     * get the dexterity attribute
     * @return dexterity
     */
    public int getDexterity() {
        return dexterity;
    }

    /**
     * get the vitality attribute
     * @return vitality
     */
    public int getVitality() {
        return vitality;
    }

    /**
     * get the defence attribute
     * @return defence
     */
    public int getDefence() {
        return defence;
    }

    /**
     * get the name of the race
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * get the magic resistance attribute
     * @return magic resistance
     */
    public int getMagicResistance() {
        return magicResistance;
    }

    /**
     * get the list of bonus attributes as strings
     * @return bonus attributes
     */
    public List<String> getBonusAttributes() {
        return bonusAttributes;
    }

    /**
     * get agility attribute
     * @return agility
     */
    public int getAgility() {
        return agility;
    }

    /**
     * get the list of availables powers as strings
     * @return powers
     */
    public List<String> getPowers() {
        return powers;
    }

    /**
     * get the race tag
     * @return tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * get the race max vitality
     * @return max health
     */
    public float getMaxHealth() {
        return maxHealth;
    }

    /**
     * get the race base vitality / minimum
     * @return base health
     */
    public float getBaseHealth() {
        return baseHealth;
    }

    /**
     * get the race max mana
     * @return max mana
     */
    public float getMaxMana() {
        return maxMana;
    }

    /**
     * get the race base mana / minimum
     * @return base mana
     */
    public float getBaseMana() {
        return baseMana;
    }
}
