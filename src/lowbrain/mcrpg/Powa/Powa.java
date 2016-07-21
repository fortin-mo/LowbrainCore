package lowbrain.mcrpg.Powa;

import lowbrain.mcrpg.commun.Helper;
import lowbrain.mcrpg.commun.RPGPlayer;
import lowbrain.mcrpg.main.PlayerListener;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by moo on 2016-07-21.
 */
public abstract class Powa {
    private double mana;
    private int minLevel;
    private int minIntelligence;
    private String name;
    private float cast_range;

    protected float maximum_duration;
    protected float minimum_duration;
    protected float maximum_amplifier;
    protected float minimum_amplifier;
    protected float duration_range;
    protected float amplifier_range;
    protected float amplifier_intelligence;
    protected float amplifier_dexterity;
    protected float duration_intelligence;
    protected float duration_dexterity;
    protected String amplifier_function;
    protected String duration_function;

    public Powa(String name){
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
    
    protected int getCastDuration(RPGPlayer from){
        float result = 0F;
        if(Helper.StringIsNullOrEmplty(duration_function)) {
            result =  (float)(Helper.Gradient(maximum_duration,minimum_duration,PlayerListener.plugin.config)
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
    
    protected int getCastAmplifier(RPGPlayer from){
        float result = 0F;
        if(Helper.StringIsNullOrEmplty(amplifier_function)) {
            result =  (float)(Helper.Gradient(maximum_amplifier,minimum_amplifier,PlayerListener.plugin.config)
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

    public abstract boolean Cast(RPGPlayer from, RPGPlayer to);

}
