package lowbrain.mcrpg.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lowbrain.mcrpg.commun.Helper;
import lowbrain.mcrpg.commun.Settings;
import lowbrain.mcrpg.config.MobsXP;
import lowbrain.mcrpg.config.Staffs;
import lowbrain.mcrpg.main.Main;
import lowbrain.mcrpg.rpg.RPGSkill;
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

import lowbrain.mcrpg.rpg.RPGPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PlayerListener implements Listener {

	public static Main plugin;

    public PlayerListener(Main instance) {
        plugin = instance;
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

    /***
     * called when player equip or unequip a armor (foot, chest, shield, helmet, legs)
     * @param e
     */
    @EventHandler
    public void onPlayerEquipArmor(ArmorEquipEvent e){
        RPGPlayer rp = plugin.connectedPlayers.get(e.getPlayer().getUniqueId());
        if(rp == null)return;

        if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR){

            String requirements =  rp.canEquipItemString(e.getNewArmorPiece());
            if(!Helper.StringIsNullOrEmpty(requirements)) {
                rp.sendMessage("You can't equip or use this item !" + requirements, ChatColor.RED);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        RPGPlayer rp = plugin.connectedPlayers.get(e.getPlayer().getUniqueId());
        if(rp == null)return;

        if(e.getItem() == null) return;

        String requirements =  rp.canEquipItemString(e.getItem());
        if(!Helper.StringIsNullOrEmpty(requirements)) {
            e.setUseItemInHand(Event.Result.DENY);
            rp.sendMessage("You can't equip or use this item !" + requirements, ChatColor.RED);
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
                            sLastUsed = iMeta.getLore().get(e.getItem().getItemMeta().getLore().size() - 2); //before last lore
                            sDurability = iMeta.getLore().get(e.getItem().getItemMeta().getLore().size() - 1); //last lore
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
                            rp.sendMessage("Item destroyed !",ChatColor.GRAY);
                        }
                        else{
                            if(durability <= 10)rp.sendMessage("Only 10 cast left !");

                            List<String> lores = iMeta.getLore();
                            lores.remove(lores.size() - 1);
                            lores.remove(lores.size() - 1);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            lores.add("last used : "  + sdf.format(Calendar.getInstance().getTime()));
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
            RPGPlayer rp = plugin.connectedPlayers.get(p.getUniqueId());
            plugin.debugInfo("Player gains " + e.getAmount() * Settings.getInstance().math.natural_xp_gain_multiplier + " xp");
            rp.addExp(e.getAmount() * Settings.getInstance().math.natural_xp_gain_multiplier);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDies(PlayerDeathEvent e){
        if(Settings.getInstance().math.onPlayerDies.items_drops_enable){
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

            RPGPlayer rpKiller = null;
            Player killed = (Player) e.getEntity();
            RPGPlayer rpKilled = plugin.connectedPlayers.get(killed.getUniqueId());
            if(killed.getKiller() != null) {
                if(killed.getKiller() instanceof Player) {
                    Player killer = killed.getKiller();
                    rpKiller = plugin.connectedPlayers.get(killer.getUniqueId());
                    plugin.debugInfo("Killed by player : " + killed.getKiller().getName());
                }
            }
            else if(killed.getLastDamageCause() != null && killed.getLastDamageCause().getEntity() != null){
                if(killed.getLastDamageCause().getEntity() instanceof Player){
                    rpKiller = plugin.connectedPlayers.get(killed.getLastDamageCause().getEntity().getUniqueId());
                }
                else if(killed.getLastDamageCause().getEntity() instanceof Projectile && ((Projectile) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                    rpKiller = plugin.connectedPlayers.get(((Player) ((Projectile) killed.getLastDamageCause().getEntity()).getShooter()).getUniqueId());
                }
            }

            if(rpKiller != null && Settings.getInstance().math.player_kills_player_exp_enable){
                int diffLvl = Math.abs(rpKilled.getLvl() - rpKiller.getLvl());
                rpKiller.addKills(1);
                float xpGained = 0.0F;
                if(diffLvl == 0){
                    xpGained = Settings.getInstance().math.killer_base_exp * rpKiller.getLvl() * Settings.getInstance().math.killer_level_gain_multiplier;
                }else if(rpKilled.getLvl() < rpKiller.getLvl()){
                    xpGained = Settings.getInstance().math.killer_base_exp / (diffLvl * Settings.getInstance().math.level_difference_multiplier) * rpKiller.getLvl() * Settings.getInstance().math.killer_level_gain_multiplier;
                }else{
                    xpGained = Settings.getInstance().math.killer_base_exp * (diffLvl * Settings.getInstance().math.level_difference_multiplier) * rpKiller.getLvl() * Settings.getInstance().math.killer_level_gain_multiplier;
                }
                rpKiller.addExp(xpGained);
                plugin.debugInfo("Killer gains "+ xpGained+" xp!");
            }

            if(Settings.getInstance().math.onPlayerDies.enable){
                rpKilled.addExp(-(Settings.getInstance().math.onPlayerDies.xp_loss / 100 * rpKilled.getExperience()));

                double dropPercentage = Helper.getPlayerDropPercentage(rpKilled);

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
            RPGPlayer rpKiller = null;
            if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
                rpKiller = plugin.connectedPlayers.get(e.getEntity().getKiller().getUniqueId());
            }
            else if(e.getEntity().getLastDamageCause() != null
                    && e.getEntity().getLastDamageCause().getEntity() instanceof Projectile
                    && ((Projectile) e.getEntity().getLastDamageCause().getEntity()).getShooter() instanceof Player){
                rpKiller = plugin.connectedPlayers.get(((Player) ((Projectile) e.getEntity().getLastDamageCause().getEntity()).getShooter()).getUniqueId());
            }
            else if(e.getEntity().getLastDamageCause() != null
                    && e.getEntity().getLastDamageCause().getEntity() instanceof Player){
                rpKiller = plugin.connectedPlayers.get(e.getEntity().getLastDamageCause().getEntity().getUniqueId());
            }

            if(rpKiller != null){

                String mobName = e.getEntity().getName().toLowerCase();

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
                        List<RPGPlayer> others = plugin.getNearbyPlayers(rpKiller,Settings.getInstance().group_xp_range);
                        rpKiller.addExp(xp * 0.66666);
                        plugin.debugInfo(rpKiller.getPlayer().getName() + " gains "+ xp * Settings.getInstance().group_xp_main +" xp!");

                        if(others.size() > 0) {
                            double othersXP = xp * Settings.getInstance().group_xp_others / others.size();
                            for (RPGPlayer other : others) {
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
        RPGPlayer rp = plugin.connectedPlayers.get(p.getUniqueId());

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

        boolean o = applyOffensiveAttack(e);
        boolean d = applyDefensive(e);
        if(o){
            //CREATING DAMAGE HOLOGRAM
            if(plugin.useHolographicDisplays){
                NumberFormat formatter = new DecimalFormat("#0.00");
                Location loc = e.getEntity().getLocation().add(0,2,0);
                Hologram holoDamage = HologramsAPI.createHologram(plugin,loc);
                holoDamage.appendTextLine(ChatColor.WHITE + formatter.format(e.getFinalDamage()));

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

    private boolean applyDefensive(EntityDamageByEntityEvent e){
        if( !(e.getEntity() instanceof Player)) return false;
        RPGPlayer damagee = plugin.connectedPlayers.get(e.getEntity().getUniqueId());
        if(damagee == null) return false;

        double multiplier = 1;
        boolean damageSet = false;

        if(e.getDamager() instanceof Arrow){
            multiplier = Helper.getDamagedByArrow(damagee);
            damageSet = true;
        }

        else if(e.getDamager() instanceof ThrownPotion){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_magic_enable){
                multiplier = Helper.getDamagedByMagic(damagee);
                damageSet = true;
            }
            plugin.debugInfo("Damage from magic projectile reduced by : " + multiplier);
            float changeOfRemovingEffect = -1F;

            if(Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.enable){
                changeOfRemovingEffect = Helper.getChangeOfRemovingPotionEffect(damagee);
            }

            double rdm = Math.random();
            if(rdm < changeOfRemovingEffect){
                RemoveBadPotionEffect(damagee.getPlayer());
                plugin.debugInfo("all effect removed");
            }
            else if(Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.enable){
                ReducingBadPotionEffect(damagee);
            }
        }

        else if(e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof Creeper)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_weapon_enable){
                multiplier = Helper.getDamagedByWeapon(damagee);
            }
        }

        else if(e.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) e.getDamager();

            if(projectile.getShooter() instanceof Player && Helper.StringIsNullOrEmpty(projectile.getCustomName())){
                if(Settings.getInstance().math.onPlayerGetDamaged.by_magic_enable){
                    multiplier = Helper.getDamagedByMagic(damagee);
                    damageSet = true;
                }
                plugin.debugInfo("Damage from magic projectile reduced by : " + multiplier);
                float changeOfRemovingEffect = -1F;

                if(Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.enable){
                    changeOfRemovingEffect = Helper.getChangeOfRemovingPotionEffect(damagee);
                }

                double rdm = Math.random();
                if(rdm < changeOfRemovingEffect){
                    RemoveBadPotionEffect(damagee.getPlayer());
                    plugin.debugInfo("all effect removed");
                }
                else if(Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.enable){
                    ReducingBadPotionEffect(damagee);
                }

                e.setDamage(e.getDamage() * multiplier);
            }
            else if(Settings.getInstance().math.onPlayerGetDamaged.by_projectile_enable){
                multiplier = Helper.getDamagedByProjectile(damagee);
                damageSet = true;
            }

        }

        if(!damageSet) return damageSet;

        plugin.debugInfo("Initial damage : " + e.getDamage());
        e.setDamage(e.getDamage() * multiplier);
        plugin.debugInfo("Defensive damage multiplier : " + multiplier);
        plugin.debugInfo("Damage after defensive multiplier : " + e.getDamage());

        return damageSet;
    }

    private boolean applyOffensiveAttack(EntityDamageByEntityEvent e){
        RPGPlayer damager = null;

        boolean magicAttack = false;
        boolean arrowAttack = false;
        boolean normalAttack = false;
        float absorbDamage = 0F;

        double oldDamage = e.getDamage();

        //DEFINING CAUSE OF DAMAGE
        if (e.getDamager() instanceof Player) {
            damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            plugin.debugInfo("Attacked by another player : " + damager.getPlayer().getName());
            normalAttack  = true;
            magicAttack = false;
            arrowAttack = false;
        } else if (e.getDamager() instanceof Arrow) {
            Arrow ar = (Arrow) e.getDamager();

            if (ar.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(((Player)((Arrow) e.getDamager()).getShooter()).getUniqueId());
            }
            plugin.debugInfo("Attacked by arrow");
            arrowAttack = true;
            normalAttack = false;
            magicAttack = false;
        } else if (e.getDamager() instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) e.getDamager();
            if (pot.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(((Player)((ThrownPotion) e.getDamager()).getShooter()).getUniqueId());
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
                damager = plugin.connectedPlayers.get(((Player) projectile.getShooter()).getUniqueId());
            }

            if(damager != null && !Helper.StringIsNullOrEmpty(projectile.getCustomName())){

                if(damager.getSkills().containsKey(projectile.getCustomName())){
                    RPGSkill skill = damager.getSkills().get(projectile.getCustomName());
                    PotionEffect po = null;
                    for (Map.Entry<String, String> effect :
                            skill.getEffects().entrySet()) {
                        switch (effect.getKey()){
                            case "poison":
                                po = new PotionEffect(PotionEffectType.POISON,(int)(skill.getEffectValue(effect.getValue()) *20),skill.getCurrentLevel(),true,true);
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
                            po = null; //reset po
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
        if(damager != null && !magicAttack && Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.enable){
            plugin.debugInfo("From " + damager.getPlayer().getName());
            double chanceOfMagicEffect = Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.maximum,
                    Settings.getInstance().math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.minimum,Settings.getInstance().math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.variables,damager);

            double rdm = Math.random();
            if(rdm < chanceOfMagicEffect){
                PotionEffect effect = CreateMagicAttack(damager);
                if(e.getEntity() instanceof LivingEntity && effect != null){
                    ((LivingEntity) e.getEntity()).addPotionEffect(effect);
                    plugin.debugInfo("magic effect added : " + effect.getType().getName() + ", " + effect.getDuration()/20 + ", " + effect.getAmplifier());
                }
            }
        }

        //APPLYING DAMAGE CHANGE DEPENDING ON OFFENCIVE ATTRIBUTES
        if(arrowAttack && damager != null && Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_enable){
            e.setDamage(Helper.getAttackByProjectile(damager,e.getDamage()));
        }
        else if(normalAttack && damager != null && Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_enable){
            e.setDamage(Helper.getAttackByWeapon(damager,e.getDamage()));
        }
        else if(magicAttack && damager != null && Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.magic_enable){
            e.setDamage(Helper.getAttackByMagic(damager,e.getDamage()));
        }

        //applying skilled attack if necessary
        if(normalAttack && damager != null && damager.getCurrentSkill() != null
                && (damager.getCurrentSkill().getEventType().equals("attack_entity") || damager.getCurrentSkill().getEventType().equals("both"))
                && e.getEntity() instanceof LivingEntity
                && damager.getCurrentSkill().executeWeaponAttackSkill(damager,(LivingEntity) e.getEntity(),e.getFinalDamage())){

        }

        if(damager == null) return false;

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
        RPGPlayer damagee = plugin.connectedPlayers.get(e.getEntity().getUniqueId());

        if(damagee == null) return;
        float multiplier = 1;

        plugin.debugInfo("Damage caused by : " +e.getCause().name());
        plugin.debugInfo("Initial damage : " +e.getDamage());

        if(e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_explosion_enable){
                multiplier = Helper.getDamagedByExplosion(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_contact_enable){
                multiplier = Helper.getDamagedByContact(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_fire_enable){
                multiplier = Helper.getDamagedByFire(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_drowning_enable){
                multiplier = Helper.getDamagedByDrowning(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_explosion_enable){
                multiplier = Helper.getDamagedByExplosion(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_fall_enable){
                multiplier = Helper.getDamagedByFall(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_weapon_enable){
                multiplier = Helper.getDamagedByWeapon(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_fire_enable){
                multiplier = Helper.getDamagedByFire(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_fire_tick_enable){
                multiplier = Helper.getDamagedByFireTick(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_fly_into_wall_enable){
                multiplier = Helper.getDamagedByFlyIntoWall(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.HOT_FLOOR)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_hot_floor_enable){
                multiplier = Helper.getDamagedByHotFloor(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.LAVA)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_lava_enable){
                multiplier = Helper.getDamagedByLava(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_lightning_enable){
                multiplier = Helper.getDamagedByLightning(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_magic_enable){
                multiplier = Helper.getDamagedByMagic(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.MELTING)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_magic_enable){
                multiplier = Helper.getDamagedByMagic(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.POISON)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_poison_enable){
                multiplier = Helper.getDamagedByPoison(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.STARVATION)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_starvation_enable){
                multiplier = Helper.getDamagedByStarvation(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_suffocation_enable){
                multiplier = Helper.getDamagedBySuffocation(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.THORNS)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_weapon_enable){
                multiplier = Helper.getDamagedByWeapon(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_void_enable){
                multiplier = Helper.getDamagedByVoid(damagee);
            }
        }
        else if(e.getCause().equals(EntityDamageEvent.DamageCause.WITHER)){
            if(Settings.getInstance().math.onPlayerGetDamaged.by_wither_enable){
                multiplier = Helper.getDamagedByWither(damagee);
            }
        }
        else{
            if(Settings.getInstance().math.onPlayerGetDamaged.by_default_enable){
                multiplier = Helper.getDamagedByDefault(damagee);
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
        if(e.getItem() != null && e.getItem().getType().equals(Material.POTION) && Settings.getInstance().math.onPlayerConsumePotion.enable){
            RPGPlayer rp = plugin.connectedPlayers.get(e.getPlayer().getUniqueId());
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
        if(e.getEntity() instanceof Player && Settings.getInstance().math.onPlayerShootBow.enable){
            //set new force
            Arrow ar = (Arrow) e.getProjectile();
            RPGPlayer rpPlayer = plugin.connectedPlayers.get(e.getEntity().getUniqueId());

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
    	Player p = e.getPlayer();
        plugin.connectedPlayers.put(p.getUniqueId(),new RPGPlayer(p));
        SetServerDifficulty();
    }

    /**
     * called when a player quit the server
     * @param e
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        plugin.connectedPlayers.get(e.getPlayer().getUniqueId()).disconnect();
        plugin.connectedPlayers.remove(e.getPlayer().getUniqueId());
        SetServerDifficulty();
    }

    /**
     * create damaging effect depending on player attributes
     * @param p
     * @return
     */
    private PotionEffect CreateMagicAttack(RPGPlayer p){
        int rdm = Helper.randomInt(1,7);
        int duration = 0;
        int amplifier = 0;
        float max = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.maximum_duration;
        float min = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.minimum_duration;

        PotionEffect effect;
        PotionEffectType type = PotionEffectType.POISON;
        boolean valid = true;
        switch (rdm){
            case 1:
                type = PotionEffectType.BLINDNESS;
                amplifier = 0;
                valid = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.blindness_enable;
                break;
            case 2:
                type = PotionEffectType.CONFUSION;
                amplifier = 0;
                valid = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.confusion_enable;
                break;
            case 3:
                type = PotionEffectType.HARM;
                max =  Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.minimum_duration;
                min = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.maximum_duration;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.harm_amplifier_maximum
                        ,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.harm_amplifier_minimum,
                        Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.harm_amplifier_variables,p
                        ));
                valid = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.harm_enable;
                break;
            case 4:
                type = PotionEffectType.POISON;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.poison_amplifier_maximum
                        ,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.poison_amplifier_minimum,
                        Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.poison_amplifier_variables,p));
                valid = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.poison_enable;
                break;
            case 5:
                type = PotionEffectType.SLOW;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.slow_amplifier_maximum
                        ,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.slow_amplifier_minimum,
                        Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.slow_amplifier_variables,p));
                valid = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.slow_enable;
                break;
            case 6:
                type = PotionEffectType.WEAKNESS;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.weakness_amplifier_maximum
                        ,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.weakness_amplifier_minimum,
                        Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.weakness_amplifier_variables,p));
                valid = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.weakness_enable;
                break;
            case 7:
                type = PotionEffectType.WITHER;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.wither_amplifier_maximum
                        ,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.wither_amplifier_minimum,
                        Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.wither_amplifier_variables,p));
                valid = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.wither_enable;
                break;
        }

        if(!valid) return null;

        duration = (int)Math.ceil(Helper.ValueFromFunction(max,min,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.duration_variables,p));

        effect = new PotionEffect(type, duration * 20, amplifier, true,true);
        return effect;
    }

    /**
     * remove bad potion effect from player
     * @param p
     */
    private void RemoveBadPotionEffect(Player p){
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
    private void ReducingBadPotionEffect(RPGPlayer rp){

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
    private void SetServerDifficulty(){

        if(!Settings.getInstance().asd_enable)return;

        Difficulty diff = Difficulty.NORMAL;
        Double averageLevel = 0.0;
        for (RPGPlayer rp :
                this.plugin.connectedPlayers.values()) {
            averageLevel += rp.getLvl();
        }
        averageLevel = averageLevel / this.plugin.connectedPlayers.size();

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
