package lowbrain.mcrpg.rpg;

import lowbrain.mcrpg.commun.Helper;
import lowbrain.mcrpg.config.Skills;
import lowbrain.mcrpg.events.PlayerListener;
import net.minecraft.server.v1_10_R1.EntityArrow;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moofy on 08/08/2016.
 */
public class RPGSkill {

    private String name;
    private int maxLevel;
    private int baseCooldown;
    private float baseManaCost;
    private int baseSkillpointsCost;
    private int skillpointsOperation;
    private int manaCostOperation;
    private int cooldownOperation;
    private int requirementsOperation;
    private float skillpointsOperationValue;
    private float manaCostOperationValue;
    private float cooldownOperationValue;
    private float requirementsOperationValue;
    private HashMap<String,Integer> baseRequirements;
    private boolean enable;
    private Calendar lastExecuted;
    private int currentLevel;

    public String info(){
        String s = "";
        s += "-------------------------" + "\n";
        s += "Name : " + name + "\n";
        s += "Skillpoints cost : " + getSkillpointsCost() + "\n";
        s += "Mana cost : " + getManaCost() + "\n";
        s += "Cooldown : " + getCoolDown() + "\n";
        s += "Max level : " + maxLevel + "\n";
        s += "Current level : " + currentLevel + "\n";
        s += "Next upgrade requirements : ";

        for (Map.Entry<String, Integer> r :
                baseRequirements.entrySet()) {
            s += r.getKey() + " ";
            s += getRequirement(r.getKey());
            s += " ";
        }
        s += "-------------------------" + "\n";
        return s;
    }

    public String toString(){
        String s = "";
        s += "-------------------------" + "\n";
        s += "Name : " + name + "\n";
        s += "Skillpoints cost : " + baseSkillpointsCost + "\n";
        s += "Mana cost : " + baseManaCost + "\n";
        s += "Cooldown : " + baseCooldown + "\n";
        s += "Max level : " + maxLevel + "\n";

        s += "Requirements : ";

        for (Map.Entry<String, Integer> r :
                baseRequirements.entrySet()) {
            s += r.getKey() + " ";
            s += r.getValue();
            s += " ";
        }
        s += "-------------------------" + "\n";
        return s;
    }

    public RPGSkill(String n, int lvl){
        this.name = n;
        this.lastExecuted = Calendar.getInstance();
        initialize();
        this.currentLevel = lvl > this.maxLevel ? this.maxLevel : lvl;
    }

    public RPGSkill(String n){
        this.name = n;
        this.lastExecuted = Calendar.getInstance();
        initialize();
        this.currentLevel = 0;
    }

    private void initialize(){
        ConfigurationSection sec = Skills.getInstance().getConfigurationSection(this.name);
        if(sec == null) {
            enable = false;
            return;
        }
        enable = true;
        this.enable = sec.getBoolean("enable");
        this.maxLevel = sec.getInt("max_level");
        this.baseCooldown = sec.getInt("base_cooldown");
        this.baseManaCost = (float)sec.getDouble("base_mana_cost");
        this.baseSkillpointsCost = sec.getInt("base_skillpoints_cost");

        this.skillpointsOperation = sec.getInt("base_skillpoints_operation");
        this.manaCostOperation = sec.getInt("mana_cost_operation");
        this.cooldownOperation = sec.getInt("cooldown_operation");
        this.requirementsOperation = sec.getInt("requirements_operation");
        this.skillpointsOperationValue = sec.getInt("base_skillpoints_operation_value");
        this.manaCostOperationValue = sec.getInt("mana_cost_operation_value");
        this.cooldownOperationValue = sec.getInt("cooldown_operation_value");
        this.requirementsOperationValue = sec.getInt("requirements_operation_value");

        this.baseRequirements = new HashMap<String,Integer>();

        ConfigurationSection requirementsSection = sec.getConfigurationSection("base_requirements");
        if(requirementsSection != null){
            for (String key :
                    requirementsSection.getKeys(false)) {
                this.baseRequirements.put(key,requirementsSection.getInt(key));
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getBaseCooldown() {
        return baseCooldown;
    }

    public int getCoolDown(){
        return operation(this.baseCooldown,this.cooldownOperationValue,this.cooldownOperation);
    }

    public float getManaCost(){
        return operation(this.baseManaCost,this.manaCostOperationValue,this.manaCostOperation);
    }

    public int getSkillpointsCost(){
        return operation(this.baseSkillpointsCost,this.skillpointsOperationValue,this.skillpointsOperation);
    }

    public float getBaseManaCost() {
        return baseManaCost;
    }

    public int getBaseSkillpointsCost() {
        return baseSkillpointsCost;
    }

    public HashMap<String, Integer> getBaseRequirements() {
        return baseRequirements;
    }

    public int getRequirement(String n){
        return operation(this.baseRequirements.get(n),this.requirementsOperationValue,this.requirementsOperation);
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean executeBowSkill(RPGPlayer p, Arrow ar,float speed) {
        try {
            boolean succeed = false;

            if(p == null || ar == null ) return succeed;

            if(!canExecute(p))return succeed;

            switch (this.name) {
                case "spread_of_arrow":
                    int[] angles = new int[currentLevel * 2];
                    for (int i = 0; i < angles.length / 2; i++) {
                        angles[i] = (i + 1) * 3;
                        angles[i + (angles.length / 2)] = (i + 1) * -3;
                    }

                    for (int angle : angles) {
                        Vector vec;
                        vec = Helper.rotateYAxis(p.getPlayer().getLocation().getDirection().clone().normalize(), angle);
                        Arrow marrow = p.getPlayer().launchProjectile(Arrow.class,vec.clone().multiply(ar.getVelocity().length()));
                        marrow.setShooter(p.getPlayer());
                        marrow.setBounce(false);
                        ((CraftArrow)marrow).getHandle().fromPlayer = EntityArrow.PickupStatus.DISALLOWED;
                    }
                    succeed = true;
                    break;
                case "barrage_of_arrow":
                    new BukkitRunnable() {
                        int counts = 1;
                        @Override
                        public void run() {
                            if (counts > currentLevel) {
                                cancel();
                            }
                            Arrow arrow = p.getPlayer().launchProjectile(Arrow.class,p.getPlayer().getLocation().getDirection().clone().normalize().multiply(ar.getVelocity().length()));
                            arrow.setShooter(p.getPlayer());
                            arrow.setBounce(false);
                            ((CraftArrow)arrow).getHandle().fromPlayer = EntityArrow.PickupStatus.DISALLOWED;
                            p.getPlayer().getWorld().playEffect(p.getPlayer().getLocation(), Effect.BOW_FIRE, 1, 0);
                            counts++;
                        }
                    }.runTaskTimer(PlayerListener.plugin, 4L, 4L);
                    succeed = true;
                    break;
                case "flaming_arrow":
                    ar.setFireTicks(currentLevel * 2 * 10);
                    ar.setGlowing(true);
                    succeed = true;
                    break;
                case "frozen_arrow":
                    ar.setCustomName(this.name+","+ currentLevel);
                    ar.setGlowing(true);
                    succeed = true;
                    break;
                case "straight_arrow":
                    ar.setVelocity(ar.getVelocity().normalize().multiply(speed * currentLevel));
                    ar.setGravity(false);
                    ar.setGlowing(true);
                    ar.setKnockbackStrength(currentLevel);
                    succeed = true;
                    break;
            }

            if(succeed){
                this.setLastExecuted(Calendar.getInstance());
                p.setCurrentMana(p.getCurrentMana() - getBaseManaCost());
                PlayerListener.plugin.debugMessage(this.name);
                p.SendMessage("Skilled attack succeeded !");
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

            switch (this.name){
                case "fire_slash":
                    to.setFireTicks(currentLevel * 20);
                    succeed = true;
                    break;
                case "frozen_slash":
                    PotionEffect po = new PotionEffect(PotionEffectType.SLOW,currentLevel * 2 *20,currentLevel,true,true);
                    po.apply(to);
                    succeed = true;
                    break;
                case "power_hit":
                    to.setVelocity(to.getLocation().getDirection().multiply(-1 * currentLevel / 2));
                    to.damage(currentLevel);
                    succeed = true;
                    break;
                case "absorb":
                    double pc = 1 * currentLevel / this.maxLevel ;
                    double absorb = damage * pc;
                    double newHealth = p.getPlayer().getHealth() + absorb;
                    newHealth = newHealth > p.getPlayer().getMaxHealth() ? p.getPlayer().getMaxHealth() : newHealth;
                    p.getPlayer().setHealth(newHealth);
                    p.SendMessage(absorb + "HP absorbed !");
                    succeed = true;
                    break;
                case "lightning_strike":
                    to.setFireTicks(currentLevel * 10);
                    to.damage(currentLevel * 1.5);
                    to.getWorld().strikeLightningEffect(to.getLocation());
                    break;
            }

            if(succeed){
                this.setLastExecuted(Calendar.getInstance());
                p.setCurrentMana(p.getCurrentMana() - getBaseManaCost());
                PlayerListener.plugin.debugMessage(this.name);
                p.SendMessage("Skilled attack succeeded !");
            }

            return succeed;
        }catch (Exception e){
            return false;
        }
    }

    public boolean canExecute(RPGPlayer p){
        if(p.getPlayer().isSneaking()){

            if(currentLevel == 0)return false;

            Calendar cooldownTime = Calendar.getInstance();
            cooldownTime.add(Calendar.SECOND,-getCoolDown());

            if(this.getLastExecuted().before(cooldownTime)){
                if(p.getCurrentMana() < this.getManaCost()){
                    p.SendMessage("Not enough mana !", ChatColor.RED);
                    return false;
                }
                else {
                    return true;
                }
            }
            else{
                int rest = (int)((getLastExecuted().getTimeInMillis() - cooldownTime.getTimeInMillis()) / 1000);
                p.SendMessage("Cooldown ! " + rest + " seconds left !",ChatColor.RED);
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

    public void setLastExecuted(Calendar cal) {
        this.lastExecuted = cal;
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

    public int operation(int baseValue, float opValue,int operation){
        int returnValue = baseValue;
        switch (operation){
            case 1:
                returnValue = (int)(baseValue * Math.pow(baseValue,getCurrentLevel() - 1));
                break;
            case 2:
                returnValue = (int)(baseValue + ( (baseValue * opValue) * ( getCurrentLevel()-1 )));
                break;
            default:
                returnValue = (int)(baseValue + opValue * (getCurrentLevel() - 1));
                break;
        }
        return returnValue;
    }

    public float operation(float baseValue, float opValue,int operation){
        double returnValue = baseValue;
        switch (operation){
            case 1:
                returnValue = baseValue * Math.pow(baseValue,getCurrentLevel() - 1);
                break;
            case 2:
                returnValue = baseValue + ( (baseValue * baseValue) * ( getCurrentLevel()-1 ));
                break;
            default:
                returnValue = baseValue + opValue * ( getCurrentLevel() - 1 );
                break;
        }
        return (float)returnValue;
    }

    public int getSkillpointsOperation() {
        return skillpointsOperation;
    }

    public int getManaCostOperation() {
        return manaCostOperation;
    }

    public int getCooldownOperation() {
        return cooldownOperation;
    }

    public int getRequirementsOperation() {
        return requirementsOperation;
    }

    public float getSkillpointsOperationValue() {
        return skillpointsOperationValue;
    }

    public float getManaCostOperationValue() {
        return manaCostOperationValue;
    }

    public float getCooldownOperationValue() {
        return cooldownOperationValue;
    }

    public float getRequirementsOperationValue() {
        return requirementsOperationValue;
    }
}
