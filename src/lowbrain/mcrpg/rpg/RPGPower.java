package lowbrain.mcrpg.rpg;

import lowbrain.mcrpg.commun.Helper;
import lowbrain.mcrpg.config.Powers;
import lowbrain.mcrpg.events.PlayerListener;
import net.minecraft.server.v1_10_R1.PlayerList;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sun.util.resources.cldr.aa.CalendarData_aa_DJ;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by moo on 2016-07-21.
 */
public class RPGPower {
    private float mana;
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
    private Calendar lastCast;
    private int minimum_cooldown;
    private int maximum_cooldown;
    private HashMap<String,Float> cooldown_influence;
    private HashMap<String,Integer> requirements;


    public RPGPower(String name){
        this.name = name;
        this.requirements = new HashMap<>();
        this.cooldown_influence = new HashMap<>();
        FileConfiguration config = Powers.getInstance();
        this.mana = (float)config.getDouble(this.name + ".mana",0);
        this.cast_range = (float)config.getDouble(this.name + ".cast_range",0);

        this.maximum_amplifier = (float)config.getDouble(this.name + ".cast.amplifier.maximum",0);
        this.minimum_amplifier = (float)config.getDouble(this.name + ".cast.amplifier.minimum",0);
        this.amplifier_range = (float)config.getDouble(this.name + ".cast.amplifier.range",0);
        this.amplifier_intelligence = (float)config.getDouble(this.name + ".cast.amplifier.intelligence",0);
        this.amplifier_dexterity = (float)config.getDouble(this.name + ".cast.amplifier.dexterity",0);
        this.amplifier_function = config.getString(this.name + ".cast.amplifier.function","");

        this.maximum_duration = (float)config.getDouble(this.name + ".cast.duration.maximum",0);
        this.minimum_duration = (float)config.getDouble(this.name + ".cast.duration.minimum",0);
        this.duration_range = (float)config.getDouble(this.name + ".cast.duration.range",0);
        this.duration_intelligence = (float)config.getDouble(this.name + ".cast.duration.intelligence",0);
        this.duration_dexterity = (float)config.getDouble(this.name + ".cast.duration.dexterity",0);
        this.duration_function = config.getString(this.name + ".cast.duration.function","");
        this.lastCast = Calendar.getInstance();

        ConfigurationSection requirementsSection = config.getConfigurationSection(this.name + ".requirements");
        if(requirementsSection != null){
            for (String key :
                    requirementsSection.getKeys(false)) {
                this.requirements.put(key,requirementsSection.getInt(key));
            }
        }

        ConfigurationSection cooldownSection = config.getConfigurationSection(this.name + ".cast.cooldown");
        if(cooldownSection != null){
            maximum_cooldown = cooldownSection.getInt("maximum");
            minimum_cooldown = cooldownSection.getInt("minimum");
            ConfigurationSection influ = cooldownSection.getConfigurationSection("influence");
            if(influ != null){
                for (String key :
                        influ.getKeys(false)) {
                    this.cooldown_influence.put(key,(float)influ.getDouble(key));
                }
            }
        }

        this.setPotionEffectType();
    }

    public float getMana() {
        return mana;
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
            result =  Helper.ValueFromFunction(maximum_duration,minimum_duration,
                    (from.getIntelligence() * duration_intelligence
                    + from.getDexterity() * duration_dexterity));
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
            result = Helper.randomFloat(result - duration_range,result + duration_range);
            if(result < minimum_duration)result = minimum_duration;
            else if(result > maximum_duration) result = maximum_duration;
        }
        
        return (int)(result * 20);
    }

    private int getCastAmplifier(RPGPlayer from){
        float result = 0F;
        if(Helper.StringIsNullOrEmpty(amplifier_function)) {
            result = Helper.ValueFromFunction(maximum_amplifier,minimum_amplifier,
                    (from.getIntelligence() * amplifier_intelligence
                    + from.getDexterity() * amplifier_dexterity));
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
            result = Helper.randomFloat(result-amplifier_range,result+amplifier_range);
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

    private int getCooldown(RPGPlayer p){
        return (int)Helper.ValueFromFunction(maximum_cooldown,minimum_cooldown,cooldown_influence,p);
    }

    public boolean Cast(RPGPlayer from, RPGPlayer to){
        try{
            if(from == null || to == null)return false;

            Calendar cooldowntime = Calendar.getInstance();
            cooldowntime.add(Calendar.SECOND,-getCooldown(from));

            if(this.lastCast.after(cooldowntime)){
                int rest = (int)((lastCast.getTimeInMillis() - cooldowntime.getTimeInMillis()) / 1000);
                from.SendMessage("Spell in cooldown ! " + rest + " seconds left !",ChatColor.RED);
                return false;
            }

            if(to != null && this.getCast_range() == 0){
                from.SendMessage("You cannot cast this spell on others !",ChatColor.RED);
                return false;
            }
            if(to != null && this.getCast_range() > 0){
                double x = from.getPlayer().getLocation().getX() - to.getPlayer().getLocation().getX();
                double y = from.getPlayer().getLocation().getY() - to.getPlayer().getLocation().getY();
                double z = from.getPlayer().getLocation().getZ() - to.getPlayer().getLocation().getZ();

                double distance = Math.sqrt(x*x + y*y + z*z);

                if(this.getCast_range() < distance){
                    from.SendMessage("The player is to far away ! Range : " + this.getCast_range() + "/" + distance,ChatColor.RED);
                    return false;
                }
            }

            String msg = from.meetRequirementsString(this.requirements);

            if(!Helper.StringIsNullOrEmpty(msg)){
                from.SendMessage("You cannot cast this spell yet ! Requirements ===>" + msg,ChatColor.RED);
                return false;
            }

            if(from.getCurrentMana() < this.getMana()){
                from.SendMessage("Insufficient mana ! " + this.getMana() + "/" + from.getCurrentMana(),ChatColor.RED);
                return false;
            }

            PotionEffect p =  new PotionEffect(this.potionEffectType,this.getCastDuration(from),this.getCastAmplifier(from),true,true);
            p.apply(to.getPlayer());

            from.setCurrentMana(from.getCurrentMana() - this.getMana());
            this.lastCast = Calendar.getInstance();
            PlayerListener.plugin.debugMessage(from.getPlayer().getName() + " cast " + this.getName());
            from.SendMessage("Cast succeeded !");
            return true;
        }catch (Exception e){
            from.SendMessage("Failed to cast " + this.name, ChatColor.RED);
        }
        return false;
    }

}
