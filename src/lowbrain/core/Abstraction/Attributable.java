package lowbrain.core.Abstraction;

import lowbrain.core.commun.Helper;
import lowbrain.core.commun.Settings;
import lowbrain.core.config.Internationalization;
import org.bukkit.ChatColor;

/**
 * Created by Mooffy on 2017-07-20.
 */
public abstract class Attributable {
    protected int reputation = 0;
    protected int strength = 0;
    protected int intelligence = 0;
    protected int dexterity = 0;
    protected int vitality = 0;
    protected int defence = 0;
    protected int agility = 0;
    protected int magicResistance = 0;
    protected int points = 0;
    protected int skillPoints = 0;
    protected float experience = 0;
    protected int lvl = 0;
    protected int kills = 0;
    protected int deaths = 0;

    public int getReputation(){return reputation;}
    public int getStrength(){return strength;}
    public int getIntelligence(){return intelligence;}
    public int getDexterity(){return dexterity;}
    public int getVitality(){return vitality;}
    public int getDefence(){return defence;}
    public int getAgility(){return agility;}
    public int getPoints() {return points;}
    public int getLvl() {return lvl;}
    public int getKills() {return kills;}
    public int getDeaths() {return deaths;}
    public float getExperience() {return experience;}
    public int getSkillPoints() {return skillPoints;}
    public int getMagicResistance() {return magicResistance;}

    public void setReputation(int n){reputation = n < 0 ? 0 : n; onAttributeChange();}
    public void setPoints(int n) {this.points = n < 0 ? 0 : n; onAttributeChange();}
    public void setSkillPoints(int n) {this.skillPoints = n < 0 ? 0 : n; onAttributeChange();}
    public void setExperience(float n) {this.experience = n < 0 ? 0 : n; onAttributeChange();}
    public void setLvl(int n) {this.lvl = n < 0 ? 0 : n; onAttributeChange();}
    public void setKills(int n) {this.kills = n < 0 ? 0 : n; onAttributeChange();}
    public void setDeaths(int n) {this.deaths = n < 0 ? 0 : n; onAttributeChange();}

    public void addPoints(int n) {points += n; points = points < 0 ? 0 : points; onAttributeChange();}
    public void addLvl(int n) {lvl += n; lvl = lvl < 0 ? 0 : lvl; onAttributeChange();}
    public void addKills(int n) {kills += n; kills = kills < 0 ? 0 : kills; onAttributeChange();}
    public void addDeaths(int n) {deaths += n; deaths = deaths < 0 ? 0 : deaths; onAttributeChange();}
    public void addExperience(float n) {experience += n; experience = experience < 0 ? 0 : experience; onAttributeChange();}
    public void addSkillPoints(int n) {skillPoints += n; skillPoints = skillPoints < 0 ? 0 : skillPoints; onAttributeChange();}
    public void addMagicResistance(int n) {magicResistance += n; magicResistance = magicResistance < 0 ? 0 : magicResistance; onAttributeChange();}

    /**
     * set dexterity of current player
     * @param val
     */
    public void setDexterity(int val) {
        this.dexterity = val;
        if(Settings.getInstance().getMaxStats() >=0
                && this.dexterity > Settings.getInstance().getMaxStats())
                this.dexterity = Settings.getInstance().getMaxStats();

        else if(this.dexterity < 0)
            this.dexterity = 0;

        onAttributeChange();
    }

    /**
     * set strength of current player
     * @param val
     */
    public void setStrength(int val) {
        this.strength = val;
        if(Settings.getInstance().getMaxStats() >=0
                && this.strength > Settings.getInstance().getMaxStats())
            this.strength = Settings.getInstance().getMaxStats();

        else if(this.strength < 0)
            this.strength = 0;

        onAttributeChange();
    }

    /**
     * set intelligence of current player
     * @param val
     */
    public void setIntelligence(int val) {
        this.intelligence = val;
        if(Settings.getInstance().getMaxStats() >=0
                && this.intelligence > Settings.getInstance().getMaxStats())
            this.intelligence = Settings.getInstance().getMaxStats();

        else if(this.intelligence < 0)
            this.intelligence = 0;

        onAttributeChange();
    }

    /**
     * set vitality of current player
     * @param val
     */
    public void setVitality(int val) {
        this.vitality = val;
        if(Settings.getInstance().getMaxStats() >=0
                && this.vitality > Settings.getInstance().getMaxStats())
            this.vitality = Settings.getInstance().getMaxStats();

        else if(this.vitality < 0)
            this.vitality = 0;

        onAttributeChange();
    }

    /**
     * set defence of current player
     * @param val
     */
    public void setDefence(int val) {
        this.defence = val;
        if(Settings.getInstance().getMaxStats() >=0
                && this.defence > Settings.getInstance().getMaxStats())
            this.defence = Settings.getInstance().getMaxStats();

        else if(this.defence < 0)
            this.defence = 0;

        onAttributeChange();
    }

    /**
     * set agility of current player
     * @param val
     */
    public void setAgility(int val) {
        this.agility = val;
        if(Settings.getInstance().getMaxStats() >=0
                && this.agility > Settings.getInstance().getMaxStats())
            this.agility = Settings.getInstance().getMaxStats();

        else if(this.agility < 0)
            this.agility = 0;

        onAttributeChange();
    }

    /**
     * set magic resistance of current player
     * @param val
     */
    public void setMagicResistance(int val) {
        this.dexterity = val;
        if(Settings.getInstance().getMaxStats() >=0
                && this.dexterity > Settings.getInstance().getMaxStats())
            this.dexterity = Settings.getInstance().getMaxStats();

        else if(this.dexterity < 0)
            this.dexterity = 0;

        onAttributeChange();
    }

    protected abstract void onAttributeChange();

    /**
     * get the value of an attribute by name
     * @param n the name of the attribute
     * @return getAttribute with 0 as default value
     */
    public int getAttribute(String n){
        return getAttribute(n, 0);
    }

    /**
     * get the value of an attribute by name
     * @param n the name of the attribute
     * @param d default value in cause of not found
     * @return value
     */
    public int getAttribute(String n, int d){
        if(Helper.StringIsNullOrEmpty(n))return d;

        switch (n.toLowerCase()){
            case "level":
            case "lvl":
                return this.getLvl();

            case "intelligence":
            case "intel":
                return this.getIntelligence();

            case "agility":
            case "agi":
                return this.getAgility();

            case "vitality":
            case "vit":
                return this.getVitality();

            case "strength":
            case "str":
                return this.getStrength();

            case "dexterity":
            case "dext":
                return this.getDexterity();

            case "defence":
            case "def":
                return this.getDefence();

            case "magic_resistance":
            case "magicresistance":
            case "mr":
                return this.getMagicResistance();
            case "kills":
            case "kill":
                return this.getKills();

            case "deaths":
            case "death":
                return this.getDeaths();

            case "reputation" :
            case "rep" :
                return this.getReputation();
        }
        return d;
    }

}
