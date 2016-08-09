package lowbrain.mcrpg.rpg;

import lowbrain.mcrpg.commun.Helper;
import lowbrain.mcrpg.events.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Moofy on 08/08/2016.
 */
public class RPGSkill {

    private String name;
    private int maxLevel;
    private int coolDown;
    private float manaCost;
    private int skillPointsCost;
    private HashMap<String,Integer> requirements;
    private boolean enable;
    private Calendar lastExecuted;
    private int currentLevel;

    public RPGSkill(String n, int lvl,FileConfiguration config){
        this.name = n;
        this.lastExecuted = Calendar.getInstance();
        initialize(config);
        this.currentLevel = lvl > this.maxLevel ? this.maxLevel : lvl;
    }

    public RPGSkill(String n , FileConfiguration config){
        this.name = n;
        this.lastExecuted = Calendar.getInstance();
        initialize(config);
        this.currentLevel = 0;
    }

    private void initialize(FileConfiguration config){
        ConfigurationSection sec = config.getConfigurationSection(this.name);
        if(sec == null) {
            enable = false;
            return;
        }
        enable = true;

        this.maxLevel = sec.getInt("max_level");
        this.coolDown = sec.getInt("cooldown");
        this.manaCost = (float)sec.getDouble("mana_cost");
        this.skillPointsCost = sec.getInt("skill_points_cost");
        this.requirements = new HashMap<String,Integer>();

        ConfigurationSection requirementsSection = sec.getConfigurationSection("requirements_per_level");
        if(requirementsSection != null){
            for (String key :
                    requirementsSection.getKeys(false)) {
                this.requirements.put(key,requirementsSection.getInt(key));
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public float getManaCost() {
        return manaCost;
    }

    public int getSkillPointsCost() {
        return skillPointsCost;
    }

    public HashMap<String, Integer> getRequirements() {
        return requirements;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean executeBowSkill(RPGPlayer p,Vector vector, Arrow ar,float speed) {
        try {
            boolean succeed = false;

            if(p == null || vector == null || ar == null ) return succeed;

            if(!canExecute(p))return succeed;

            PlayerListener.plugin.debugMessage(this.name);

            switch (this.name) {
                case "spread_of_arrow":
                    int[] angles = new int[currentLevel * 2];
                    for (int i = 0; i < angles.length / 2; i++) {
                        angles[i] = (i + 1) * 2;
                        angles[i + 2] = (i + 1) * -2;
                    }

                    for (int angle : angles) {
                        Vector vec;
                        vec = Helper.rotateYAxis(p.getPlayer().getLocation().getDirection().normalize(), angle);
                        p.getPlayer().getLocation().getWorld().spawnArrow(p.getPlayer().getLocation().clone().add(0, 1.5, 0), vec.clone(), 6F, 0).setShooter(p.getPlayer());
                    }
                    succeed = true;
                    break;
                case "barrage_of_arrow":
                    new BukkitRunnable() {
                        int counts;
                        @Override
                        public void run() {
                            counts++;
                            p.getPlayer().getLocation().getWorld().spawnArrow(p.getPlayer().getLocation().clone().add(0, 1.5, 0), p.getPlayer().getLocation().getDirection().clone().normalize(), 6F, 0).setShooter(p.getPlayer());

                            p.getPlayer().getWorld().playEffect(p.getPlayer().getLocation(), Effect.BOW_FIRE, 1, 0);
                            if (counts > currentLevel) {
                                cancel();
                            }
                        }
                    }.runTaskTimer(PlayerListener.plugin, 5L, 5L);
                    succeed = true;
                    break;
                case "flaming_arrow":
                    ar.setFireTicks(currentLevel * 2);
                    succeed = true;
                    break;
                case "frozen_arrow":
                    ar.setCustomName(this.name+","+ currentLevel);
                    succeed = true;
                    break;
                case "straight_arrow":
                    vector.normalize();
                    Vector vec = vector.clone();
                    ar.setVelocity(vec.clone().multiply(speed));
                    ar.setGravity(false);
                    succeed = true;
                    break;
            }

            if(succeed){
                this.setLastExecuted(Calendar.getInstance());
                p.setCurrentMana(p.getCurrentMana() - getManaCost());
            }

            return succeed;
        }catch (Exception e){
            return false;
        }
    }

    public boolean executeWeaponAttackSkill(RPGPlayer p, LivingEntity to, double damage){

        try {
            boolean succeed = false;

            if(p == null || to == null) return succeed;

            if(!canExecute(p))return succeed;

            PlayerListener.plugin.debugMessage(this.name);

            switch (this.name){
                case "fire_slash":
                    to.setFireTicks(currentLevel * 2);
                    succeed = true;
                    break;
                case "frozen_slash":
                    PotionEffect po = new PotionEffect(PotionEffectType.SLOW,currentLevel * 2 *20,currentLevel,true,true);
                    po.apply(to);
                    succeed = true;
                    break;
                case "power_hit":
                    to.setVelocity(to.getLocation().getDirection().multiply(-1 * currentLevel));
                    to.damage(currentLevel);
                    succeed = true;
                    break;
                case "absorb":
                    double pc = 1 / this.maxLevel * currentLevel;
                    double absorb = damage * pc;
                    double newHealth = p.getPlayer().getHealth() + absorb;
                    newHealth = newHealth > p.getPlayer().getMaxHealth() ? p.getPlayer().getMaxHealth() : newHealth;
                    p.getPlayer().setHealth(newHealth);
                    succeed = true;
                    break;
            }

            if(succeed){
                this.setLastExecuted(Calendar.getInstance());
                p.setCurrentMana(p.getCurrentMana() - getManaCost());
            }

            return succeed;
        }catch (Exception e){
            return false;
        }
    }

    public boolean canExecute(RPGPlayer p){
        if(p.getPlayer().isSneaking() && p.getCurrentSkill() != null){

            if(currentLevel == 0)return false;

            Calendar cooldownTime = Calendar.getInstance();
            cooldownTime.add(Calendar.SECOND,-getCoolDown());

            if(lastExecuted.before(cooldownTime)){
                if(p.getCurrentMana() < p.getCurrentSkill().getManaCost()){
                    p.SendMessage("Not enough mana !", ChatColor.RED);
                    return false;
                }
                else {
                    return true;
                }
            }
            else{
                p.SendMessage("Cooldown !",ChatColor.RED);
                return false;
            }
        }
        else{
            return false;
        }
    }

    public Calendar getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(Calendar lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int lvl){
        this.currentLevel = lvl > this.maxLevel ? this.maxLevel : lvl;
    }

    public void addLevel(int lvl) {
        this.currentLevel += lvl;
    }
}
