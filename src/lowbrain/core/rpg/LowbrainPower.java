package lowbrain.core.rpg;

import lowbrain.core.commun.Helper;
import lowbrain.core.config.Internationalization;
import lowbrain.core.config.Powers;
import lowbrain.core.events.CoreListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by moo on 2016-07-21.
 */
public class LowbrainPower {
    private float mana;
    private String name;
    private float castRange;
    private float maximumDuration;
    private float minimumDuration;
    private float maximumAmplifier;
    private float minimumAmplifier;
    private float durationRange;
    private float amplifierRange;
    private String amplifierFunction;
    private String durationFunction;
    private PotionEffectType potionEffectType;
    private Calendar lastCast;
    private int minimumCooldown;
    private int maximumCooldown;
    private HashMap<String,Float> cooldownVariables;
    private HashMap<String,Float> amplifierVariables;
    private HashMap<String,Float> durationVariables;
    private HashMap<String,Integer> requirements;


    public LowbrainPower(String name){
        this.name = name;
        this.requirements = new HashMap<>();
        this.cooldownVariables = new HashMap<>();
        this.amplifierVariables = new HashMap<>();
        this.durationVariables = new HashMap<>();
        FileConfiguration config = Powers.getInstance();
        this.mana = (float)config.getDouble(this.name + ".mana",0);
        this.castRange = (float)config.getDouble(this.name + ".cast_range",0);
        this.amplifierRange = (float)config.getDouble(this.name + ".cast.amplifier.range",0);
        this.amplifierFunction = config.getString(this.name + ".cast.amplifier.function","");
        this.lastCast = Calendar.getInstance();

        ConfigurationSection requirementsSection = config.getConfigurationSection(this.name + ".requirements");
        if(requirementsSection != null){
            for (String key :
                    requirementsSection.getKeys(false)) {
                this.requirements.put(key,requirementsSection.getInt(key));
            }
        }

        this.maximumDuration = (float)config.getDouble(this.name + ".cast.duration.maximum",0);
        this.minimumDuration = (float)config.getDouble(this.name + ".cast.duration.minimum",0);
        this.durationRange = (float)config.getDouble(this.name + ".cast.duration.range",0);
        this.durationFunction = config.getString(this.name + ".cast.duration.function","");
        ConfigurationSection durationVar = config.getConfigurationSection(this.name + ".cast.duration.variables");
        if(durationVar != null){
            for (String key :
                    durationVar.getKeys(false)) {
                this.durationVariables.put(key,(float)durationVar.getDouble(key));
            }
        }

        this.maximumAmplifier = (float)config.getDouble(this.name + ".cast.amplifier.maximum",0);
        this.minimumAmplifier = (float)config.getDouble(this.name + ".cast.amplifier.minimum",0);
        ConfigurationSection var = config.getConfigurationSection(this.name + ".cast.amplifier.variables");
        if(var != null){
            for (String key :
                    var.getKeys(false)) {
                this.amplifierVariables.put(key,(float)var.getDouble(key));
            }
        }

        maximumCooldown = config.getInt(this.name + ".cast.cooldown.maximum");
        minimumCooldown = config.getInt(this.name + ".cast.cooldown.minimum");
        ConfigurationSection influ = config.getConfigurationSection(this.name + ".cast.cooldown.variables");
        if(influ != null){
            for (String key :
                    influ.getKeys(false)) {
                this.cooldownVariables.put(key,(float)influ.getDouble(key));
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

    public float getCastRange() {
        return castRange;
    }
    
    private int getCastDuration(LowbrainPlayer from){
        float result = 0F;
        if(Helper.StringIsNullOrEmpty(durationFunction)) {
            result =  Helper.ValueFromFunction(maximumDuration, minimumDuration, durationVariables,from);
        }
        else{
            String[] st = durationFunction.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,from));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }
        
        if(durationRange > 0){
            result = Helper.randomFloat(result - durationRange,result + durationRange);
            if(result < minimumDuration)result = minimumDuration;
            else if(result > maximumDuration) result = maximumDuration;
        }
        
        return (int)(result * 20);
    }

    private int getCastAmplifier(LowbrainPlayer from){
        float result = 0F;
        if(Helper.StringIsNullOrEmpty(amplifierFunction)) {
            result = Helper.ValueFromFunction(maximumAmplifier, minimumAmplifier, amplifierVariables,from);
        }
        else{
            String[] st = amplifierFunction.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,from));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(amplifierRange > 0){
            result = Helper.randomFloat(result- amplifierRange,result+ amplifierRange);
            result = result > maximumAmplifier ? maximumAmplifier : result < minimumAmplifier ? minimumAmplifier : result;
        }
        return (int)(result);
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

    private int getCooldown(LowbrainPlayer p){
        return (int)Helper.ValueFromFunction(maximumCooldown, minimumCooldown, cooldownVariables,p);
    }

    public boolean Cast(LowbrainPlayer from, LowbrainPlayer to){
        try{
            if(from == null || to == null)return false;

            Calendar cooldowntime = Calendar.getInstance();
            cooldowntime.add(Calendar.SECOND,-getCooldown(from));

            if(this.lastCast.after(cooldowntime)){
                int rest = (int)((lastCast.getTimeInMillis() - cooldowntime.getTimeInMillis()) / 1000);
                from.sendMessage(Internationalization.getInstance().getString("spell_in_cooldown") + ""
                        + rest + " "
                        + Internationalization.getInstance().getString("seconds_left"),ChatColor.RED);
                return false;
            }

            if(to != null && this.getCastRange() == 0){
                from.sendMessage(Internationalization.getInstance().getString("cant_cast_this_spell_on_others"),ChatColor.RED);
                return false;
            }
            if(to != null && this.getCastRange() > 0){
                double x = from.getPlayer().getLocation().getX() - to.getPlayer().getLocation().getX();
                double y = from.getPlayer().getLocation().getY() - to.getPlayer().getLocation().getY();
                double z = from.getPlayer().getLocation().getZ() - to.getPlayer().getLocation().getZ();

                double distance = Math.sqrt(x*x + y*y + z*z);

                if(this.getCastRange() < distance){
                    from.sendMessage(Internationalization.getInstance().getString("player_out_of_range") + " " + this.getCastRange() + "/" + distance,ChatColor.RED);
                    return false;
                }
            }

            String msg = from.meetRequirementsString(this.requirements);

            if(!Helper.StringIsNullOrEmpty(msg)){
                from.sendMessage(Internationalization.getInstance().getString("spell_requirements_to_high") + " " + msg,ChatColor.RED);
                return false;
            }

            if(from.getCurrentMana() < this.getMana()){
                from.sendMessage(Internationalization.getInstance().getString("insufficient_mana") + " " + this.getMana() + "/" + from.getCurrentMana(),ChatColor.RED);
                return false;
            }

            PotionEffect p =  new PotionEffect(this.potionEffectType,this.getCastDuration(from),this.getCastAmplifier(from),true,true);
            p.apply(to.getPlayer());

            from.setCurrentMana(from.getCurrentMana() - this.getMana());
            this.lastCast = Calendar.getInstance();
            CoreListener.plugin.debugInfo(from.getPlayer().getName() + " cast " + this.getName());
            from.sendMessage(Internationalization.getInstance().getString("cast_succesfull"));
            return true;
        }catch (Exception e){
            from.sendMessage(Internationalization.getInstance().getString("cast_failed") + " " + this.name, ChatColor.RED);
        }
        return false;
    }

}
