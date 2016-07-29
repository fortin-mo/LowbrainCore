package lowbrain.mcrpg.rpg;

import lowbrain.mcrpg.commun.Helper;
import lowbrain.mcrpg.main.PlayerListener;
import lowbrain.mcrpg.rpg.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by moo on 2016-07-21.
 */
public class RPGPower {
    private double mana;
    private int minLevel;
    private int minIntelligence;
    private String name;
    private float cast_range;

    private float maximum_duration;
    private float minimum_duration;
    private float maximum_amplifier;
    private float minimum_amplifier;
    private float duration_range;
    private float amplifier_range;
    private float amplifier_intelligence;
    private float amplifier_dexterity;
    private float duration_intelligence;
    private float duration_dexterity;
    private String amplifier_function;
    private String duration_function;
    private PotionEffectType potionEffectType;



    public RPGPower(String name){
        this.name = name;
        FileConfiguration config = PlayerListener.plugin.powersConfig;
        this.mana = config.getDouble(this.name + ".mana");
        this.minLevel = config.getInt(this.name + ".min_level");
        this.minIntelligence = config.getInt(this.name + ".min_intelligence");
        this.cast_range = (float)config.getDouble(this.name + ".cast_range");

        this.maximum_amplifier = (float)config.getDouble(this.name + ".cast.amplifier.maximum");
        this.minimum_amplifier = (float)config.getDouble(this.name + ".cast.amplifier.minimum");
        this.amplifier_range = (float)config.getDouble(this.name + ".cast.amplifier.range");
        this.amplifier_intelligence = (float)config.getDouble(this.name + ".cast.amplifier.intelligence");
        this.amplifier_dexterity = (float)config.getDouble(this.name + ".cast.amplifier.dexterity");
        this.amplifier_function = config.getString(this.name + ".cast.amplifier.function");

        this.maximum_duration = (float)config.getDouble(this.name + ".cast.duration.maximum");
        this.minimum_duration = (float)config.getDouble(this.name + ".cast.duration.minimum");
        this.duration_range = (float)config.getDouble(this.name + ".cast.duration.range");
        this.duration_intelligence = (float)config.getDouble(this.name + ".cast.duration.intelligence");
        this.duration_dexterity = (float)config.getDouble(this.name + ".cast.duration.dexterity");
        this.duration_function = config.getString(this.name + ".cast.duration.function");

        this.setPotionEffectType();
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

    public float getCast_range() {
        return cast_range;
    }
    
    private int getCastDuration(RPGPlayer from){
        float result = 0F;
        if(Helper.StringIsNullOrEmpty(duration_function)) {
            result =  (float)(PlayerListener.Gradient(maximum_duration,minimum_duration)
                    * (from.getIntelligence() * duration_intelligence
                    + from.getDexterity() * duration_dexterity)
                    + minimum_duration);
        }
        else{
            String[] st = duration_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,from));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }
        
        if(duration_range > 0){
            result = (float)((result - duration_range) + (Math.random() * (result + duration_range)));
            if(result < minimum_duration)result = minimum_duration;
            else if(result > maximum_duration) result = maximum_duration;
        }
        
        return (int)(result * 20);
    }

    private int getCastAmplifier(RPGPlayer from){
        float result = 0F;
        if(Helper.StringIsNullOrEmpty(amplifier_function)) {
            result =  (float)(PlayerListener.Gradient(maximum_amplifier,minimum_amplifier)
                    * (from.getIntelligence() * amplifier_intelligence
                    + from.getDexterity() * amplifier_dexterity)
                    + minimum_amplifier);
        }
        else{
            String[] st = amplifier_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,from));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(amplifier_range > 0){
            result = (float)((result - amplifier_range) + (Math.random() * (result + amplifier_range)));
            if(result < minimum_amplifier)result = minimum_amplifier;
            else if(result > maximum_amplifier) result = maximum_amplifier;
        }

        return (int)(result * 20);
    }

    private void setPotionEffectType(){
        switch (this.name){
            case"healing":
                this.potionEffectType = PotionEffectType.HEAL;
                break;
            case"fire_resistance":
                this.potionEffectType = PotionEffectType.FIRE_RESISTANCE;
                break;
            case"resistance":
                this.potionEffectType = PotionEffectType.DAMAGE_RESISTANCE;
                break;
            case"water_breathing":
                this.potionEffectType = PotionEffectType.WATER_BREATHING;
                break;
            case"invisibility":
                this.potionEffectType = PotionEffectType.INVISIBILITY;
                break;
            case"regeneration":
                this.potionEffectType = PotionEffectType.REGENERATION;
                break;
            case"jump_boost":
                this.potionEffectType = PotionEffectType.JUMP;
                break;
            case"strength":
                this.potionEffectType = PotionEffectType.INCREASE_DAMAGE;
                break;
            case"haste":
                this.potionEffectType = PotionEffectType.FAST_DIGGING;
                break;
            case"speed":
                this.potionEffectType = PotionEffectType.SPEED;
                break;
            case"night_vision":
                this.potionEffectType = PotionEffectType.NIGHT_VISION;
                break;
            case"health_boost":
                this.potionEffectType = PotionEffectType.HEALTH_BOOST;
                break;
            case"absorption":
                this.potionEffectType = PotionEffectType.ABSORPTION;
                break;
            case"saturation":
                this.potionEffectType = PotionEffectType.SATURATION;
                break;
        }
    }

    public boolean Cast(RPGPlayer from, RPGPlayer to){
        try{
            PotionEffect p =  new PotionEffect(this.potionEffectType,this.getCastDuration(from),this.getCastAmplifier(from),true,true);
            p.apply(to.getPlayer());
            return true;
        }catch (Exception e){
            from.SendMessage("Failed to cast " + this.name, ChatColor.RED);
        }
        return false;
    }

}
