package lowbrain.mcrpg.commun;

import lowbrain.mcrpg.main.PlayerListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;

/**
 * Created by Moofy on 17/07/2016.
 */
public class RPGPowers {
    private double mana;
    private int minLevel;
    private int minIntelligence;
    private String name;
    private double range;

    public RPGPowers(String name){
        this.name = name;
        FileConfiguration config = PlayerListener.plugin.getConfig();
        this.mana = config.getDouble("Powers." + this.name + ".mana");
        this.minLevel = config.getInt("Powers." + this.name + ".min_level");
        this.minIntelligence = config.getInt("Powers." + this.name + ".min_intelligence");
        this.range = config.getDouble("Powers." + this.name + ".range");
    }

    public double getMana() {
        return mana;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMinIntelligence() {
        return minIntelligence;
    }

    public String getName() {
        return name;
    }

    public double getRange() {
        return range;
    }

    public boolean Cast(RPGPlayer from, RPGPlayer to){

        PotionEffect effect;
        int duration = 0;
        int amplifier = 0;

        if(to == null) to = from; //if null cast to self

        switch (this.name){
            case"healing":
                break;
            case"fire_resistance":
                break;
            case"resistance":
                break;
            case"water_breathing":
                break;
            case"invisibility":
                break;
            case"regeneration":
                break;
            case"jump_boost":
                break;
            case"strength":
                break;
            case"haste":
                break;
            case"speed":
                break;
            case"night_vision":
                break;
            case"health_boost":
                break;
            case"absorption":
                break;
            case"saturation":
                break;

        }
        return true;
    }

    private boolean Healing(RPGPlayer from, RPGPlayer to){
        return true;
    }


}
