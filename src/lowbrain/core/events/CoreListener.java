package lowbrain.core.events;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.objects.Party;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lowbrain.core.commun.Helper;
import lowbrain.core.commun.Settings;
import lowbrain.core.config.Internationalization;
import lowbrain.core.config.MobsXP;
import lowbrain.core.config.Staffs;
import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.LowbrainPlayer;
import lowbrain.core.rpg.LowbrainSkill;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.commons.lang.mutable.MutableFloat;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class CoreListener implements Listener {

	public static LowbrainCore plugin;

    public CoreListener(LowbrainCore instance) {
        plugin = instance;
    }

    public void onCreatureSpawn(CreatureSpawnEvent e){
        if(Settings.getInstance().disable_mob_no_tick_damage){
            e.getEntity().setNoDamageTicks(0);
        }
    }

    /***
     * Called when player teleport
     * remove damage when teleporting with enderpearl
     * @param e
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)){
            e.setCancelled(true);
            e.getPlayer().teleport(e.getTo());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        LowbrainPlayer rp = plugin.getPlayerHandler().getList().get(e.getPlayer().getUniqueId());
        if(rp == null)return;

        if(e.getItem() == null) return;

        String requirements =  rp.canEquipItemString(e.getItem());
        if(!Helper.StringIsNullOrEmpty(requirements)) {
            e.setUseItemInHand(Event.Result.DENY);
            rp.sendMessage(Internationalization.getInstance().getString("cannot_equit_armor_or_item") + requirements, ChatColor.RED);
            e.setCancelled(true);
            return;
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) /*|| e.getAction().equals(Action.RIGHT_CLICK_AIR.RIGHT_CLICK_BLOCK)*/){

            if(e.getItem().getItemMeta() != null && !Helper.StringIsNullOrEmpty(e.getItem().getItemMeta().getDisplayName())){
                ItemMeta iMeta = e.getItem().getItemMeta();
                String n = iMeta.getDisplayName().substring(2);
                if(n.startsWith("staff")){

                    ConfigurationSection staffSection = Staffs.getInstance().getConfigurationSection(n);
                    if(staffSection != null){
                        Boolean gravity = staffSection.getBoolean("gravity");
                        double speed = staffSection.getDouble("speed");
                        int maxDurability = staffSection.getInt("durability");
                        int durability = maxDurability;
                        int cooldown = staffSection.getInt("cooldown",0);
                        String sDurability = "";
                        String sLastUsed = "";
                        Calendar lastUsed = Calendar.getInstance();
                        lastUsed.add(Calendar.SECOND,-cooldown - 1);

                        if(e.getItem().getItemMeta().hasLore()){
                            
                            int lastUsedIntex = iMeta.getLore().indexOf("last used : ");
                            int durabilityIndex = iMeta.getLore().indexOf("durability : ");
                            //sLastUsed = iMeta.getLore().get(e.getItem().getItemMeta().getLore().size() - 2); //before last lore
                            //sDurability = iMeta.getLore().get(e.getItem().getItemMeta().getLore().size() - 1); //last lore
                            sLastUsed = lastUsedIntex >= 0 ? iMeta.getLore().get(lastUsedIntex) : "";
                            sDurability = durabilityIndex >= 0 ? iMeta.getLore().get(durabilityIndex) : "";
                        }

                        //if has durability lore, get the durability
                        if(!Helper.StringIsNullOrEmpty(sDurability)){
                            String[] tmp = sDurability.split(" : ");
                            durability = tmp.length > 1 ? Helper.intTryParse(tmp[1],durability) : durability;
                        }
                        //if has lastUsed lore, get the last used date
                        if(!Helper.StringIsNullOrEmpty(sLastUsed)){
                            String[] tmp = sLastUsed.split(" : ");
                            lastUsed = tmp.length > 1 ? Helper.dateTryParse(tmp[1],lastUsed) : lastUsed;
                        }

                        lastUsed.add(Calendar.SECOND,cooldown);
                        if(lastUsed.after(Calendar.getInstance())) return;

                        durability -= 1;

                        String effect = staffSection.getString("effect");
                        switch (effect){
                            case "fire_tick":
                                Snowball fireTick = rp.getPlayer().launchProjectile(Snowball.class,rp.getPlayer().getLocation().getDirection().clone().multiply(speed));
                                fireTick.setGravity(gravity);
                                fireTick.setCustomName(n);
                                fireTick.setShooter(rp.getPlayer());
                                fireTick.setFireTicks(staffSection.getInt("effect_duration") * 20);
                                rp.getPlayer().getWorld().playEffect(rp.getPlayer().getLocation(),Effect.BOW_FIRE,1,0);
                                break;
                            case "fire_ball":
                                Fireball fireBall = rp.getPlayer().launchProjectile(Fireball.class,rp.getPlayer().getLocation().getDirection().clone().multiply(speed));
                                fireBall.setCustomName(n);
                                fireBall.setGravity(gravity);
                                fireBall.setShooter(rp.getPlayer());
                                rp.getPlayer().getWorld().playEffect(rp.getPlayer().getLocation(),Effect.BOW_FIRE,1,0);
                                break;
                            case "freezing_ball":
                                Snowball freezingBall = rp.getPlayer().launchProjectile(Snowball.class,rp.getPlayer().getLocation().getDirection().clone().multiply(speed));
                                freezingBall.setGravity(gravity);
                                freezingBall.setCustomName(n);
                                freezingBall.setShooter(rp.getPlayer());
                                rp.getPlayer().getWorld().playEffect(rp.getPlayer().getLocation(),Effect.BOW_FIRE,1,0);
                                break;
                            case "teleport":
                                EnderPearl enderPearl = rp.getPlayer().launchProjectile(EnderPearl.class,rp.getPlayer().getLocation().getDirection().clone().multiply(speed));
                                enderPearl.setGravity(gravity);
                                enderPearl.setCustomName(n);
                                enderPearl.setShooter(rp.getPlayer());
                                rp.getPlayer().getWorld().playEffect(rp.getPlayer().getLocation(),Effect.BOW_FIRE,1,0);
                                break;
                        }
                        if(durability <= 0){
                            rp.getPlayer().getInventory().remove(e.getItem());
                            rp.getPlayer().updateInventory();
                            rp.sendMessage(Internationalization.getInstance().getString("item_destroyed"),ChatColor.GRAY);
                        }
                        else{
                            if(durability <= 10)rp.sendMessage(Internationalization.getInstance().getString("ony_10_cast_left"));

                            List<String> lores = iMeta.getLore();
                            lores.remove(lores.size() - 1);
                            lores.remove(lores.size() - 1);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            lores.add("last_used : "  + sdf.format(Calendar.getInstance().getTime()));
                            lores.add("durability : " + durability);
                            iMeta.setLore(lores);
                            e.getItem().setItemMeta(iMeta);
                        }
                    }
                }
            }
        }
    }

    /**
     * called when a player experience changes naturally
     * @param e
     */
    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent e){
        if(e.getAmount() > 0) {
            Player p = e.getPlayer();
            LowbrainPlayer rp = plugin.getPlayerHandler().getList().get(p.getUniqueId());
            if(rp == null)return;
            plugin.debugInfo("Player gains " + e.getAmount() * Settings.getInstance().maths.natural_xp_gain_multiplier + " xp");
            rp.addExp(e.getAmount() * Settings.getInstance().maths.natural_xp_gain_multiplier);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDies(PlayerDeathEvent e){
        if(Settings.getInstance().maths.onPlayerDies.items_drops_enable){
            e.setKeepInventory(true);
        }
        else{
            e.setKeepInventory(false);
        }
    }

    /***
     * called when either a player or a mob dies
     * @param e
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        //PLAYER DIES
        if(e.getEntity() instanceof Player){

            plugin.debugInfo(e.getEntity().getName() + " died !");

            LowbrainPlayer rpKiller = null;
            Player killed = (Player) e.getEntity();
            LowbrainPlayer rpKilled = plugin.getPlayerHandler().getList().get(killed.getUniqueId());
            if(killed.getKiller() != null) {
                if(killed.getKiller() instanceof Player) {
                    Player killer = killed.getKiller();
                    rpKiller = plugin.getPlayerHandler().getList().get(killer.getUniqueId());
                    plugin.debugInfo("Killed by player : " + killed.getKiller().getName());
                }
            }
            else if(killed.getLastDamageCause() != null && killed.getLastDamageCause().getEntity() != null){
                if(killed.getLastDamageCause().getEntity() instanceof Player){
                    rpKiller = plugin.getPlayerHandler().getList().get(killed.getLastDamageCause().getEntity().getUniqueId());
                }
                else if(killed.getLastDamageCause().getEntity() instanceof Projectile && ((Projectile) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                    rpKiller = plugin.getPlayerHandler().getList().get(((Player) ((Projectile) killed.getLastDamageCause().getEntity()).getShooter()).getUniqueId());
                }
            }

            if(rpKiller != null && Settings.getInstance().maths.player_kills_player_exp_enable){
                int diffLvl = Math.abs(rpKilled.getLvl() - rpKiller.getLvl());
                rpKiller.addKills(1);
                float xpGained = 0.0F;
                if(diffLvl == 0){
                    xpGained = Settings.getInstance().maths.killer_base_exp * rpKiller.getLvl() * Settings.getInstance().maths.killer_level_gain_multiplier;
                }else if(rpKilled.getLvl() < rpKiller.getLvl()){
                    xpGained = Settings.getInstance().maths.killer_base_exp / (diffLvl * Settings.getInstance().maths.level_difference_multiplier) * rpKiller.getLvl() * Settings.getInstance().maths.killer_level_gain_multiplier;
                }else{
                    xpGained = Settings.getInstance().maths.killer_base_exp * (diffLvl * Settings.getInstance().maths.level_difference_multiplier) * rpKiller.getLvl() * Settings.getInstance().maths.killer_level_gain_multiplier;
                }
                rpKiller.addExp(xpGained);
                plugin.debugInfo("Killer gains "+ xpGained+" xp!");
            }

            if(Settings.getInstance().maths.onPlayerDies.enable){
                rpKilled.addExp(-(Settings.getInstance().maths.onPlayerDies.xp_loss / 100 * rpKilled.getExperience()));

                double dropPercentage = rpKilled.getMultipliers().getPlayerDropPercentage();

                plugin.debugInfo("Percentage of dropped items : " + dropPercentage);

                int count = (int)(rpKilled.getPlayer().getInventory().getSize() * dropPercentage);

                for (int i = 0; i < count; i++) {
                    int rdm = Helper.randomInt(0,rpKilled.getPlayer().getInventory().getSize() - 1);

                    ItemStack item = rpKilled.getPlayer().getInventory().getItem(rdm);

                    if(item != null){
                        rpKilled.getPlayer().getWorld().dropItemNaturally(rpKilled.getPlayer().getLocation(),item);
                        rpKilled.getPlayer().getInventory().remove(item);
                    }
                }

                plugin.debugInfo("Items dropped : " + count);
            }
            rpKilled.addDeaths(1);

        }
        //MOB DIES
        else {
            LowbrainPlayer rpKiller = null;
            if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
                rpKiller = plugin.getPlayerHandler().getList().get(e.getEntity().getKiller().getUniqueId());
            }
            else if(e.getEntity().getLastDamageCause() != null
                    && e.getEntity().getLastDamageCause().getEntity() instanceof Projectile
                    && ((Projectile) e.getEntity().getLastDamageCause().getEntity()).getShooter() instanceof Player){
                rpKiller = plugin.getPlayerHandler().getList().get(((Player) ((Projectile) e.getEntity().getLastDamageCause().getEntity()).getShooter()).getUniqueId());
            }
            else if(e.getEntity().getLastDamageCause() != null
                    && e.getEntity().getLastDamageCause().getEntity() instanceof Player){
                rpKiller = plugin.getPlayerHandler().getList().get(e.getEntity().getLastDamageCause().getEntity().getUniqueId());
            }

            if(rpKiller != null){

                String mobName = e.getEntity().getType().name().toLowerCase();

                rpKiller.getMobKills().put(mobName,rpKiller.getMobKills().getOrDefault(mobName,0) + 1);

                int killsCount = rpKiller.getMobKills().get(mobName);
                ConfigurationSection section = MobsXP.getInstance().getConfigurationSection(mobName);
                if(section == null)section = MobsXP.getInstance().getConfigurationSection("default");

                if(section != null){
                    int interval = section.getInt("xp_bonus_interval");
                    double xp = section.getDouble("base_xp");
                    if(killsCount % interval == 0){
                        rpKiller.sendMessage("You've just killed your " + killsCount + " " + mobName);
                        xp = killsCount / interval * section.getDouble("xp_bonus_multiplier");
                    }

                    if(Settings.getInstance().group_xp_enable){

                        List<LowbrainPlayer> others = null;

                        if(plugin.useParties){
                            others = new ArrayList<>();
                            Party party = Parties.getInstance().getPlayerHandler().getPartyFromPlayer(rpKiller.getPlayer());
                            if(party != null){
                                for (Player p :
                                        party.getOnlinePlayers()) {
                                    if (!p.equals(rpKiller.getPlayer())){
                                        if(Settings.getInstance().group_xp_range == -1
                                                || Helper.getDistanceBetweenTwoPlayers(rpKiller.getPlayer(),p) <= Settings.getInstance().group_xp_range){
                                            LowbrainPlayer p2 = plugin.getPlayerHandler().getList().get(p.getUniqueId());
                                            if(p2 != null){
                                                others.add(p2);
                                            }
                                        }
                                    }
                                }
                            }
                        } 
                        else {
                            others = Helper.getNearbyPlayers(rpKiller,Settings.getInstance().group_xp_range);
                        }
                        double mainXP = others != null && others.size() > 0 ? Settings.getInstance().group_xp_main : 1;
                        rpKiller.addExp(xp * mainXP);
                        plugin.debugInfo(rpKiller.getPlayer().getName() + " gains "+ xp * mainXP +" xp!");

                        if(others != null && others.size() > 0) {
                            double othersXP = xp * Settings.getInstance().group_xp_others / others.size();
                            for (LowbrainPlayer other : others) {
                                other.addExp(othersXP);
                                plugin.debugInfo(other.getPlayer().getName() + " gains " + othersXP + " xp!");
                            }
                        }
                    }
                    else{
                        rpKiller.addExp(xp);
                        plugin.debugInfo(rpKiller.getPlayer().getName() + " gains "+ xp+" xp!");
                    }

                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent e){
        /*Player p = e.getPlayer();
        LowbrainPlayer rp = plugin.getPlayerHandler().getList().get(p.getUniqueId());

        ItemStack item = e.getPlayer().getItemInHand();

        if(item != null){

        }*/
        //TODO
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e){
        //TODO
    }

    /**
     * call when a player attacks
     * @param e
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
        MutableBoolean isCritical = new MutableBoolean(false);
        MutableFloat missChance = new MutableFloat(0F);
        boolean isMissed = false;
        boolean o = applyOffensiveAttack(e,isCritical, missChance);
        boolean d = applyDefensive(e, missChance);

        double rdm = Math.random();
        plugin.debugInfo("Missing chance : " + missChance.floatValue());
        if(rdm < missChance.floatValue()){
            plugin.debugInfo("Attack was missed !");
            e.setDamage(0);
        }

        if(o){
            //CREATING DAMAGE HOLOGRAM
            if(plugin.useHolographicDisplays){
                NumberFormat formatter = new DecimalFormat("#0.00");
                Location loc = e.getEntity().getLocation().add(0,2,0);
                Hologram holoDamage = HologramsAPI.createHologram(plugin,loc);
                ChatColor color = isCritical.booleanValue() ? ChatColor.DARK_RED : ChatColor.RED;
                if(isMissed) {
                    holoDamage.appendTextLine(color + "miss");
                } else {
                    holoDamage.appendTextLine(color + formatter.format(e.getFinalDamage()));
                }
                int directionX = Helper.randomInt(0,1) == 0 ? -1 : 1;
                int directionZ = Helper.randomInt(0,1) == 0 ? -1 : 1;

                new BukkitRunnable() {
                    int ticksRun;
                    @Override
                    public void run() {
                        ticksRun++;
                        int directionY = ticksRun < 10 ? 1 : -1;
                        holoDamage.teleport(holoDamage.getLocation().add(0.025 * directionX, 0.125*directionY, 0.025 * directionZ));
                        if (ticksRun > 60) { // 100 ticks = 5 seconds
                            holoDamage.delete();
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 1L, 1L);
            }
        }
    }

    private boolean applyDefensive(EntityDamageByEntityEvent e, MutableFloat missChance){
        if( !(e.getEntity() instanceof Player)) return false;
        LowbrainPlayer damagee = plugin.getPlayerHandler().getList().get(e.getEntity().getUniqueId());
        if(damagee == null) return false;

        double multiplier = 1;
        boolean damageSet = false;

        if(e.getDamager() instanceof Arrow){
            multiplier = damagee.getMultipliers().getDamagedByArrow();
            damageSet = true;
        }

        else if(e.getDamager() instanceof ThrownPotion){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_magic_enable){
                multiplier = damagee.getMultipliers().getDamagedByMagic();
                damageSet = true;
            }
            plugin.debugInfo("Damage from magic projectile reduced by : " + multiplier);
            float changeOfRemovingEffect = -1F;

            if(Settings.getInstance().maths.onPlayerGetDamaged.chanceOfRemovingMagicEffect.enable){
                changeOfRemovingEffect = damagee.getMultipliers().getChanceOfRemovingPotionEffect();
            }

            double rdm = Math.random();
            if(rdm < changeOfRemovingEffect){
                removeBadPotionEffect(damagee.getPlayer());
                plugin.debugInfo("all effect removed");
            }
            else if(Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.enable){
                reducingBadPotionEffect(damagee);
            }
        }

        else if(e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof Creeper)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_weapon_enable){
                multiplier = damagee.getMultipliers().getDamagedByWeapon();
            }
        }

        else if(e.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) e.getDamager();

            if(projectile.getShooter() instanceof Player && Helper.StringIsNullOrEmpty(projectile.getCustomName())){
                if(Settings.getInstance().maths.onPlayerGetDamaged.by_magic_enable){
                    multiplier = damagee.getMultipliers().getDamagedByMagic();
                    damageSet = true;
                }
                plugin.debugInfo("Damage from magic projectile reduced by : " + multiplier);
                float changeOfRemovingEffect = -1F;
                
                if(Settings.getInstance().maths.onPlayerGetDamaged.chanceOfRemovingMagicEffect.enable){
                    changeOfRemovingEffect = damagee.getMultipliers().getChanceOfRemovingPotionEffect();
                }

                double rdm = Math.random();
                if(rdm < changeOfRemovingEffect){
                    removeBadPotionEffect(damagee.getPlayer());
                    plugin.debugInfo("all effect removed");
                }
                else if(Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.enable){
                    reducingBadPotionEffect(damagee);
                }

                e.setDamage(e.getDamage() * multiplier);
            }
            else if(Settings.getInstance().maths.onPlayerGetDamaged.by_projectile_enable){
                multiplier = damagee.getMultipliers().getDamagedByProjectile();
                damageSet = true;
            }

        }

        if(damagee != null && Settings.getInstance().maths.onPlayerGetDamaged.chanceOfDodging.enable){
            float oldChance = missChance.floatValue();
            float newChance = Settings.getInstance().maths.onPlayerAttackEntity.chanceOfMissing.enable ?
                        oldChance * damagee.getMultipliers().getChanceOfDodging() : damagee.getMultipliers().getChanceOfDodging();
            missChance.setValue(newChance);
        }

        if(!damageSet) return damageSet;



        plugin.debugInfo("Initial damage : " + e.getDamage());
        e.setDamage(e.getDamage() * multiplier);
        plugin.debugInfo("Defensive damage multiplier : " + multiplier);
        plugin.debugInfo("Damage after defensive multiplier : " + e.getDamage());

        return damageSet;
    }

    private boolean applyOffensiveAttack(EntityDamageByEntityEvent e, MutableBoolean isCritical, MutableFloat missChance){
        LowbrainPlayer damager = null;

        boolean magicAttack = false;
        boolean arrowAttack = false;
        boolean normalAttack = false;
        float absorbDamage = 0F;

        double oldDamage = e.getDamage();

        //DEFINING CAUSE OF DAMAGE
        if (e.getDamager() instanceof Player) {
            damager = plugin.getPlayerHandler().getList().get(e.getDamager().getUniqueId());
            plugin.debugInfo("Attacked by another player : " + damager.getPlayer().getName());
            normalAttack  = true;
            magicAttack = false;
            arrowAttack = false;
        } else if (e.getDamager() instanceof Arrow) {
            Arrow ar = (Arrow) e.getDamager();

            if (ar.getShooter() instanceof Player) {
                damager = plugin.getPlayerHandler().getList().get(((Player)((Arrow) e.getDamager()).getShooter()).getUniqueId());
            }
            plugin.debugInfo("Attacked by arrow");
            arrowAttack = true;
            normalAttack = false;
            magicAttack = false;
        } else if (e.getDamager() instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) e.getDamager();
            if (pot.getShooter() instanceof Player) {
                damager = plugin.getPlayerHandler().getList().get(((Player)((ThrownPotion) e.getDamager()).getShooter()).getUniqueId());
            }
            plugin.debugInfo("Attacked by potion");
            magicAttack = true;
            normalAttack = false;
            arrowAttack = false;
        }

        //CUSTOM PROJECTILE (SKILLS AND STAFFS)
        if(e.getDamager() instanceof  Projectile && e.getEntity() instanceof LivingEntity){

            Projectile projectile = (Projectile) e.getDamager();

            if(projectile.getShooter() instanceof Player && damager == null){
                damager = plugin.getPlayerHandler().getList().get(((Player) projectile.getShooter()).getUniqueId());
            }

            if(damager != null && !Helper.StringIsNullOrEmpty(projectile.getCustomName())){

                if(damager.getSkills().containsKey(projectile.getCustomName())){
                    LowbrainSkill skill = damager.getSkills().get(projectile.getCustomName());

                    for (Map.Entry<String, String> effect :
                            skill.getEffects().entrySet()) {

                        PotionEffect po = null;

                        switch (effect.getKey()){
                            case "poison":
                                po = new PotionEffect(PotionEffectType.WITHER,(int)(skill.getEffectValue(effect.getValue()) *20),skill.getCurrentLevel(),true,true);
                                break;
                            case "fire_tick":
                                e.getEntity().setFireTicks((int)(skill.getEffectValue(effect.getValue()) * 20));
                                break;
                            case "freeze":
                                po = new PotionEffect(PotionEffectType.SLOW,(int)(skill.getEffectValue(effect.getValue()) *20),skill.getCurrentLevel(),true,true);
                                break;
                            case "absorb":
                                absorbDamage = skill.getEffectValue(effect.getValue());
                                break;
                            case "knockback":
                                e.getEntity().setVelocity(((LivingEntity) e.getEntity()).getEyeLocation().getDirection().multiply(-1 * skill.getEffectValue(effect.getValue())));
                                break;
                            case "lightning":
                                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                                break;
                            case "damage":
                                ((LivingEntity) e.getEntity()).damage(skill.getEffectValue(effect.getValue()));
                                break;
                        }
                        if(po != null){
                            po.apply((LivingEntity)e.getEntity());
                        }
                    }
                }
                else{
                    ConfigurationSection staffSection = Staffs.getInstance().getConfigurationSection(projectile.getCustomName());
                    if(staffSection != null){

                        double baseDamage = staffSection.getDouble("base_damage",-1);
                        String effect = staffSection.getString("effect","");
                        int effectDuration = staffSection.getInt("effect_duration",3);
                        plugin.debugInfo("Attacked by magic projectile");

                        if(baseDamage >= 0) {
                            e.setDamage(baseDamage);
                            oldDamage = baseDamage;
                            magicAttack = true;
                            arrowAttack = false;
                            normalAttack = false;
                        }

                        switch (effect){
                            case "freezing_ball":
                                PotionEffect po = new PotionEffect(PotionEffectType.SLOW,effectDuration*20,3,true,true);
                                po.apply((LivingEntity)e.getEntity());
                                break;
                            case "fire_tick":
                                e.getEntity().setFireTicks(effectDuration * 20);
                                break;
                        }
                    }
                }

            }
        }

        //CHECK IF PLAYER CAN USE THE ITEM IN HAND
        if(damager != null && damager.getPlayer().getInventory().getItemInMainHand() != null){
            if(!damager.canEquipItem(damager.getPlayer().getInventory().getItemInMainHand())){
                e.setCancelled(true);
                return false;
            }
        }

        //APPLYING MAGIC EFFECT BY ATTACKER
        if(damager != null && !magicAttack && Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.enable){
            plugin.debugInfo("From " + damager.getPlayer().getName());
            double chanceOfMagicEffect = Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerAttackEntity.chanceOfCreatingMagicAttack.maximum,
                    Settings.getInstance().maths.onPlayerAttackEntity.chanceOfCreatingMagicAttack.minimum,Settings.getInstance().maths.onPlayerAttackEntity.chanceOfCreatingMagicAttack.variables,damager);

            double rdm = Math.random();
            if(rdm < chanceOfMagicEffect){
                PotionEffect effect = createMagicAttack(damager);
                if(e.getEntity() instanceof LivingEntity && effect != null){
                    ((LivingEntity) e.getEntity()).addPotionEffect(effect);
                    plugin.debugInfo("magic effect added : " + effect.getType().getName() + ", " + effect.getDuration()/20 + ", " + effect.getAmplifier());
                }
            }
        }

        //APPLYING DAMAGE CHANGE DEPENDING ON OFFENCIVE ATTRIBUTES
        if(arrowAttack && damager != null && Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_enable){
            e.setDamage(Helper.getAttackByProjectile(damager,e.getDamage()));
        }
        else if(normalAttack && damager != null && Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_enable){
            e.setDamage(Helper.getAttackByWeapon(damager,e.getDamage()));
        }
        else if(magicAttack && damager != null && Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_enable){
            e.setDamage(Helper.getAttackByMagic(damager,e.getDamage()));
        }

        //applying skilled attack if necessary
        if(normalAttack && damager != null && damager.getCurrentSkill() != null
                && (damager.getCurrentSkill().getEventType().equals("attack_entity") || damager.getCurrentSkill().getEventType().equals("both"))
                && e.getEntity() instanceof LivingEntity
                && damager.getCurrentSkill().executeWeaponAttackSkill(damager,(LivingEntity) e.getEntity(),e.getFinalDamage())){

        }

        if(damager == null) return false;

        if(Settings.getInstance().maths.onPlayerAttackEntity.chanceOfMissing.enable) {
            missChance.setValue(damager.getMultipliers().getChanceOfMissing());
        }

        if(Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.enable){
            double rdm = Math.random();
            double chance = Helper.getCriticalHitChance(damager);
            if(rdm < chance){
                isCritical.setValue(true);
                float criticalHitMultiplier = Helper.getCriticalHitMultiplier(damager);
                plugin.debugInfo("Critical hit multiplier : " + criticalHitMultiplier);
                e.setDamage(e.getDamage() * criticalHitMultiplier);
            }
        }

        if(Settings.getInstance().maths.onPlayerAttackEntity.backStab.enable){
            Vector attackerDirection = damager.getPlayer().getLocation().getDirection();
            Vector victimDirection = e.getEntity().getLocation().getDirection();
            //determine if the dot product between the vectors is greater than 0
            if (attackerDirection.dot(victimDirection) > 0) {
                //player was backstabbed.}
                float bs =  Helper.getBackstabMultiplier(damager);
                e.setDamage(e.getDamage() * bs);
                plugin.debugInfo("Backstap multiplier : " + bs);
            }
        }

        if(absorbDamage > 0){
            damager.getPlayer().setHealth(damager.getPlayer().getHealth() + absorbDamage);
        }

        plugin.debugInfo("Initial damage : " + oldDamage);
        double damageMultiplier = e.getDamage() / oldDamage;
        plugin.debugInfo("Offencive damage multiplier : " + damageMultiplier);
        plugin.debugInfo("Damage after offencive multiplier : " + e.getDamage());
        return true;
    }

    /**
     * Called when a player get damaged
     * @param e
     */
    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e){

        if(!(e.getEntity() instanceof Player))return;
        if(e.getDamage() <= 0 ) return;
        LowbrainPlayer damagee = plugin.getPlayerHandler().getList().get(e.getEntity().getUniqueId());

        if(damagee == null) return;
        float multiplier = 1;

        plugin.debugInfo("Damage caused by : " +e.getCause().name());
        plugin.debugInfo("Initial damage : " +e.getDamage());

        if(e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_explosion_enable){
                multiplier = damagee.getMultipliers().getDamagedByExplosion();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_contact_enable){
                multiplier = damagee.getMultipliers().getDamagedByContact();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_fire_enable){
                multiplier = damagee.getMultipliers().getDamagedByFire();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_drowning_enable){
                multiplier = damagee.getMultipliers().getDamagedByDrowning();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_explosion_enable){
                multiplier = damagee.getMultipliers().getDamagedByExplosion();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_fall_enable){
                multiplier = damagee.getMultipliers().getDamagedByFall();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_weapon_enable){
                multiplier = damagee.getMultipliers().getDamagedByWeapon();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_fire_enable){
                multiplier = damagee.getMultipliers().getDamagedByFire();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_fire_tick_enable){
                multiplier = damagee.getMultipliers().getDamagedByFireTick();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_fly_into_wall_enable){
                multiplier = damagee.getMultipliers().getDamagedByFlyIntoWall();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.HOT_FLOOR)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_hot_floor_enable){
                multiplier = damagee.getMultipliers().getDamagedByHotFloor();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_lava_enable){
                multiplier = damagee.getMultipliers().getDamagedByLava();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_lightning_enable){
                multiplier = damagee.getMultipliers().getDamagedByLightning();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_magic_enable){
                multiplier = damagee.getMultipliers().getDamagedByMagic();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.MELTING)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_magic_enable){
                multiplier = damagee.getMultipliers().getDamagedByMagic();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.POISON)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_poison_enable){
                multiplier = damagee.getMultipliers().getDamagedByPoison();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.STARVATION)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_starvation_enable){
                multiplier = damagee.getMultipliers().getDamagedByStarvation();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_suffocation_enable){
                multiplier = damagee.getMultipliers().getDamagedBySuffocation();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.THORNS)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_weapon_enable){
                multiplier = damagee.getMultipliers().getDamagedByWeapon();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_void_enable){
                multiplier = damagee.getMultipliers().getDamagedByVoid();
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.WITHER)){
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_wither_enable){
                multiplier = damagee.getMultipliers().getDamagedByWither();
            }
        }
        else{
            if(Settings.getInstance().maths.onPlayerGetDamaged.by_default_enable){
                multiplier = damagee.getMultipliers().getDamagedByDefault();
            }
        }

        plugin.debugInfo("Defensive damage multiplier : " + multiplier);
        e.setDamage(e.getDamage() * multiplier);
        plugin.debugInfo("Damage after Defensive multiplier : " + e.getDamage());
    }

    /**
     * When a player consume a potion
     * @param e
     */
    @EventHandler
    public void onPlayerConsumePotion(PlayerItemConsumeEvent e){
        if(e.getItem() != null && e.getItem().getType().equals(Material.POTION) && Settings.getInstance().maths.onPlayerConsumePotion.enable){
            LowbrainPlayer rp = plugin.getPlayerHandler().getList().get(e.getPlayer().getUniqueId());
            if(rp != null && !rp.getPlayer().getActivePotionEffects().isEmpty()) {

                float result = Helper.getConsumedPotionMultiplier(rp);

                PotionEffect po = (PotionEffect) rp.getPlayer().getActivePotionEffects().toArray()[rp.getPlayer().getActivePotionEffects().size() -1];

                int newDuration = (int) (po.getDuration() * result);
                PotionEffect tmp = new PotionEffect(po.getType(), newDuration, po.getAmplifier());
                rp.getPlayer().removePotionEffect(po.getType());
                rp.getPlayer().addPotionEffect(tmp);

                plugin.debugInfo("effect duration multiply by " + result);
            }
        }
    }

    /**
     * called when a player shoots with a  bow
     * @param e
     */
    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent e){
        if(e.getEntity() instanceof Player && Settings.getInstance().maths.onPlayerShootBow.enable){
            //set new force
            Arrow ar = (Arrow) e.getProjectile();
            LowbrainPlayer rpPlayer = plugin.getPlayerHandler().getList().get(e.getEntity().getUniqueId());

            float speed = Helper.getBowArrowSpeed(rpPlayer);
            plugin.debugInfo("Arrow speed multiplier : " + speed);
            ar.setVelocity(ar.getVelocity().multiply(speed));

            float precX = Helper.getBowPrecision(rpPlayer);
            int direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precX = 1 + (1-precX)*direction;

            float precY = Helper.getBowPrecision(rpPlayer);
            direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precY = 1 + (1-precY)*direction;

            float precZ = Helper.getBowPrecision(rpPlayer);
            direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precZ = 1 + (1-precZ)*direction;

            plugin.debugInfo("Arrow precision multiplier : " + precX);
            ar.setVelocity(new Vector(ar.getVelocity().getX() * precX, ar.getVelocity().getY() * precY, ar.getVelocity().getZ() * precZ));

            if(rpPlayer.getCurrentSkill() != null
                    && (rpPlayer.getCurrentSkill().getEventType().equals("bow_shoot") || rpPlayer.getCurrentSkill().getEventType().equals("both"))
                    && rpPlayer.getCurrentSkill().executeBowSkill(rpPlayer,ar,speed) ){

            }
        }
    }

    /**
     * called when a player join the server
     * @param e
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        plugin.getPlayerHandler().add(e.getPlayer());
        setServerDifficulty();
    }

    /**
     * called when a player quit the server
     * @param e
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        plugin.getPlayerHandler().remove(e.getPlayer());
        setServerDifficulty();
    }

    /**
     * create damaging effect depending on player attributes
     * @param p
     * @return
     */
    private PotionEffect createMagicAttack(LowbrainPlayer p){
        int rdm = Helper.randomInt(1,7);
        int duration = 0;
        int amplifier = 0;
        float max = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.maximum_duration;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.minimum_duration;

        PotionEffect effect;
        PotionEffectType type = PotionEffectType.POISON;
        boolean valid = true;
        switch (rdm){
            case 1:
                type = PotionEffectType.BLINDNESS;
                amplifier = 0;
                valid = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.blindness_enable;
                break;
            case 2:
                type = PotionEffectType.CONFUSION;
                amplifier = 0;
                valid = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.confusion_enable;
                break;
            case 3:
                type = PotionEffectType.HARM;
                max =  Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.minimum_duration;
                min = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.maximum_duration;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.harm_amplifier_maximum
                        ,Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.harm_amplifier_minimum,
                        Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.harm_amplifier_variables,p
                        ));
                valid = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.harm_enable;
                break;
            case 4:
                type = PotionEffectType.POISON;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.poison_amplifier_maximum
                        ,Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.poison_amplifier_minimum,
                        Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.poison_amplifier_variables,p));
                valid = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.poison_enable;
                break;
            case 5:
                type = PotionEffectType.SLOW;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.slow_amplifier_maximum
                        ,Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.slow_amplifier_minimum,
                        Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.slow_amplifier_variables,p));
                valid = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.slow_enable;
                break;
            case 6:
                type = PotionEffectType.WEAKNESS;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.weakness_amplifier_maximum
                        ,Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.weakness_amplifier_minimum,
                        Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.weakness_amplifier_variables,p));
                valid = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.weakness_enable;
                break;
            case 7:
                type = PotionEffectType.WITHER;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.wither_amplifier_maximum
                        ,Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.wither_amplifier_minimum,
                        Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.wither_amplifier_variables,p));
                valid = Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.wither_enable;
                break;
        }

        if(!valid) return null;

        duration = (int)Math.ceil(Helper.ValueFromFunction(max,min,Settings.getInstance().maths.onPlayerAttackEntity.creatingMagicAttack.duration_variables,p));

        effect = new PotionEffect(type, duration * 20, amplifier, true,true);
        return effect;
    }

    /**
     * remove bad potion effect from player
     * @param p
     */
    private void removeBadPotionEffect(Player p){
        if(p.hasPotionEffect(PotionEffectType.BLINDNESS)){
            p.removePotionEffect(PotionEffectType.BLINDNESS);
        }
        if(p.hasPotionEffect(PotionEffectType.CONFUSION)){
            p.removePotionEffect(PotionEffectType.CONFUSION);
        }
        if(p.hasPotionEffect(PotionEffectType.HARM)){
            p.removePotionEffect(PotionEffectType.HARM);
        }
        if(p.hasPotionEffect(PotionEffectType.POISON)){
            p.removePotionEffect(PotionEffectType.POISON);
        }
        if(p.hasPotionEffect(PotionEffectType.SLOW)){
            p.removePotionEffect(PotionEffectType.SLOW);
        }
        if(p.hasPotionEffect(PotionEffectType.WEAKNESS)){
            p.removePotionEffect(PotionEffectType.WEAKNESS);
        }
        if(p.hasPotionEffect(PotionEffectType.WITHER)){
            p.removePotionEffect(PotionEffectType.WITHER);
        }
    }

    /**
     * reducing player potion effect depending on attributes
     * @param rp
     */
    private void reducingBadPotionEffect(LowbrainPlayer rp){

        if(rp.getPlayer().getActivePotionEffects().isEmpty()) return;

        float rdm = Helper.getReducingPotionEffect(rp);

        PotionEffect po = (PotionEffect) rp.getPlayer().getActivePotionEffects().toArray()[rp.getPlayer().getActivePotionEffects().size() -1];

        int newDuration = (int)Math.ceil(po.getDuration() * rdm);
        int newAmplifier = (int)(po.getAmplifier() * rdm);
        PotionEffect tmp = new PotionEffect(po.getType(),newDuration,newAmplifier);
        rp.getPlayer().removePotionEffect(po.getType());
        rp.getPlayer().addPotionEffect(tmp);

        /*
        for (PotionEffect pe : rp.getPlayer().getActivePotionEffects()) {
            int newDuration = (int)Math.ceil(pe.getDuration() * rdm);
            int newAmplifier = (int)(pe.getAmplifier() * rdm);
            PotionEffect tmp = new PotionEffect(pe.getType(),newDuration,newAmplifier);
            rp.getPlayer().removePotionEffect(pe.getType());
            rp.getPlayer().addPotionEffect(tmp);
        }
        */
        plugin.debugInfo("all effect reduced by " + rdm);
    }

    /**
     * Set the server difficulty depending on average connectedplayer level
     */
    private void setServerDifficulty(){

        if(!Settings.getInstance().asd_enable)return;

        Difficulty diff = Difficulty.NORMAL;
        int averageLevel = plugin.getPlayerHandler().getAverageLevel();

        if(averageLevel >= Settings.getInstance().asd_easy_from && averageLevel <= (Settings.getInstance().asd_easy_to != -1 ? Settings.getInstance().asd_easy_to : 9999999)){
            diff = Difficulty.EASY;
        }
        else if(averageLevel >= Settings.getInstance().asd_medium_from && averageLevel <= (Settings.getInstance().asd_medium_to != -1 ? Settings.getInstance().asd_medium_to : 9999999)){
            diff = Difficulty.NORMAL;
        }
        else if(averageLevel >= Settings.getInstance().asd_hard_from && averageLevel <= (Settings.getInstance().asd_hard_to != -1 ? Settings.getInstance().asd_hard_to : 9999999)){
            diff = Difficulty.HARD;
        }


        for (World world :
                this.plugin.getServer().getWorlds()) {
            world.setDifficulty(diff);
        }

        this.plugin.debugInfo("Server difficulty set to " + diff.name());
    }

}
