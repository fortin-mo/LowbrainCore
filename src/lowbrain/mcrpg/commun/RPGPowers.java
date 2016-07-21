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
    private float range;

    public RPGPowers(String name){
        this.name = name;
        FileConfiguration config = PlayerListener.plugin.powersConfig;
        this.mana = config.getDouble(this.name + ".mana");
        this.minLevel = config.getInt(this.name + ".min_level");
        this.minIntelligence = config.getInt(this.name + ".min_intelligence");
        this.range = (float)config.getDouble(this.name + ".range");
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

    public float getRange() {
        return range;
    }

    public boolean Cast(RPGPlayer from, RPGPlayer to){
        if(to == null) to = from; //if null cast to self
        boolean succeed = false;
        switch (this.name){
            case"healing":
                succeed = healing(from,to);
                break;
            case"fire_resistance":
                succeed = fire_resistance(from,to);
            case"resistance":
                succeed = resistance(from,to);
                break;
            case"water_breathing":
                succeed = water_breathing(from,to);
                break;
            case"invisibility":
                succeed = invisibility(from,to);
                break;
            case"regeneration":
                succeed = regeneration(from,to);
                break;
            case"jump_boost":
                succeed = jump_boost(from,to);
                break;
            case"strength":
                succeed = strength(from,to);
                break;
            case"haste":
                succeed = haste(from,to);
                break;
            case"speed":
                succeed = speed(from,to);
                break;
            case"night_vision":
                succeed = night_vision(from,to);
                break;
            case"health_boost":
                succeed = health_boost(from,to);
                break;
            case"absorption":
                succeed = absorption(from,to);
                break;
            case"saturation":
                succeed = saturation(from,to);
                break;

        }
        return succeed;
    }

    private boolean healing(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean fire_resistance(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean resistance(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean water_breathing(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean invisibility(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean regeneration(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean jump_boost(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean strength(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean haste(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean speed(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean night_vision(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean health_boost(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean absorption(RPGPlayer from, RPGPlayer to){
        return true;
    }
    private boolean saturation(RPGPlayer from, RPGPlayer to){
        return true;
    }


}
