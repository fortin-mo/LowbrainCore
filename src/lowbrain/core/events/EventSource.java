package lowbrain.core.events;

import lowbrain.core.commun.Helper;
import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.LowbrainPlayer;
import lowbrain.core.rpg.LowbrainSkill;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventSource {
    public LowbrainPlayer damager;
    public LowbrainPlayer damagee;
    public EventSourceEnum source;
    public LowbrainSkill skill;
    public ConfigurationSection staffSection;

    public EventSource(LowbrainPlayer damager, LowbrainPlayer damagee, EventSourceEnum source, LowbrainSkill skill, ConfigurationSection staffSection) {
        this.damager = damager;
        this.damagee = damagee;
        this.source = source;
        this.skill = skill;
        this.staffSection = staffSection;
    }

    public static EventSource getFromAttack(EntityDamageByEntityEvent e) {
        if (e == null)
            return null;

        LowbrainPlayer damager = null;
        LowbrainPlayer damagee = null;
        EventSourceEnum source = null;
        LowbrainSkill skill = null;
        ConfigurationSection staffSection = null;

        /* if (e.getEntity() instanceof Player) {
            damagee = LowbrainCore.getInstance().getPlayerHandler().getList().get(e.getEntity().getUniqueId());
        } */

        if (e.getDamager() instanceof Player) {
            damager = LowbrainCore.getInstance().getPlayerHandler().getList().get(e.getDamager().getUniqueId());
            LowbrainCore.getInstance().debugInfo("              ---- Attacked by another player : " + damager.getPlayer().getName());
            source = EventSourceEnum.NORMAL;
        }

        else if (e.getDamager() instanceof Arrow) {
            Arrow ar = (Arrow) e.getDamager();

            if (ar.getShooter() instanceof Player) {
                damager = LowbrainCore.getInstance().getPlayerHandler().getList().get(((Player)((Arrow) e.getDamager()).getShooter()).getUniqueId());
            }
            LowbrainCore.getInstance().debugInfo("              ---- Attacked by arrow");
            source = EventSourceEnum.ARROW;
        }

        else if (e.getDamager() instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) e.getDamager();
            if (pot.getShooter() instanceof Player) {
                damager = LowbrainCore.getInstance().getPlayerHandler().getList().get(((Player)((ThrownPotion) e.getDamager()).getShooter()).getUniqueId());
            }
            LowbrainCore.getInstance().debugInfo("              ---- Attacked by potion");
            source = EventSourceEnum.MAGIC;
        }

        //CUSTOM PROJECTILE (SKILLS AND STAFFS)
        if(e.getDamager() instanceof Projectile && e.getEntity() instanceof LivingEntity){

            Projectile projectile = (Projectile) e.getDamager();

            if(projectile.getShooter() instanceof Player && damager == null){
                damager = LowbrainCore.getInstance().getPlayerHandler().getList().get(((Player) projectile.getShooter()).getUniqueId());
            }

            if(damager != null && !Helper.StringIsNullOrEmpty(projectile.getCustomName())){

                if(damager.getSkills().containsKey(projectile.getCustomName())){
                    skill = damager.getSkills().get(projectile.getCustomName());

                    LowbrainCore.getInstance().debugInfo("              ---- getting skilled attack effect");
                }

                else if (LowbrainCore.getInstance().useLowbrainItems) {
                    staffSection = lowbrain.items.main.Main.getInstance().getStaffConfig().getConfigurationSection(projectile.getCustomName());
                    if (staffSection != null) {
                        source = EventSourceEnum.MAGIC_PROJECTILE;
                    }
                }

            }
        }

        return new EventSource(damager, damagee, source, skill, staffSection);
    }
}
