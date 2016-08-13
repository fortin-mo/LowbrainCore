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
                rp.SendMessage("You can't equip or use this item !" + requirements, ChatColor.RED);
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
            rp.SendMessage("You can't equip or use this item !" + requirements, ChatColor.RED);
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
                            rp.SendMessage("Item destroyed !",ChatColor.GRAY);
                        }
                        else{
                            if(durability <= 10)rp.SendMessage("Only 10 cast left !");

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
     * Called when a player get damaged
     * @param e
     */
    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            RPGPlayer damagee = plugin.connectedPlayers.get(e.getEntity().getUniqueId());
            // multiplier = a * x + b : where a = max-min/max_stats, and b = min
            float multiplier = 1;

            plugin.debugMessage("Initial damage : " +e.getDamage());

            //FALL DAMAGE
            if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && Settings.getInstance().math.onPlayerGetDamaged.fall_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByFall(damagee);
            }

            //FIRE DAMAGE
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)) && Settings.getInstance().math.onPlayerGetDamaged.fire_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByFire(damagee);
            }

            //EXPLOSION
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))&& Settings.getInstance().math.onPlayerGetDamaged.explosion_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByExplosion(damagee);
            }

            //MAGIC POTION DAMAGE
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.WITHER)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.POISON)) && Settings.getInstance().math.onPlayerGetDamaged.magic_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByMagic(damagee);

                float changeOfRemovingEffect = -1F;

                if(Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.enable){
                    changeOfRemovingEffect = getChangeOfRemovingPotionEffect(damagee);
                }

                double rdm = Math.random();
                if(rdm < changeOfRemovingEffect){
                    RemoveBadPotionEffect(damagee.getPlayer());
                    plugin.debugMessage("all effect removed");
                }
                else if(Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.enable){
                    ReducingBadPotionEffect(damagee);
                }
            }

            //ARROW
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) && Settings.getInstance().math.onPlayerGetDamaged.projectile_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByProjectile(damagee);
            }

            //CONTACT
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT) && Settings.getInstance().math.onPlayerGetDamaged.contact_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByContact(damagee);
            }

            else if(Settings.getInstance().math.onPlayerGetDamaged.weapon_enable){
                if(e.getCause() != null) {
                    plugin.debugMessage("Damage caused by : " + e.getCause().name());
                }
                multiplier = getDamagedByWeapon(damagee);
            }

            plugin.debugMessage("Deffencive damage multiplier : " + multiplier);
            e.setDamage(e.getDamage() * multiplier);
            plugin.debugMessage("Damage after deffencive multiplier : " + e.getDamage());
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
            plugin.debugMessage("Player gains " + e.getAmount() * Settings.getInstance().math.natural_xp_gain_multiplier + " xp");
            rp.addExp(e.getAmount() * Settings.getInstance().math.natural_xp_gain_multiplier);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDies(PlayerDeathEvent e){
        e.setKeepInventory(true);
    }

    /***
     * called when either a player or a mob dies
     * @param e
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        //PLAYER DIES
        if(e.getEntity() instanceof Player){

            plugin.debugMessage(e.getEntity().getName() + " died !");

            RPGPlayer rpKiller = null;
            Player killed = (Player) e.getEntity();
            RPGPlayer rpKilled = plugin.connectedPlayers.get(killed.getUniqueId());
            if(killed.getKiller() != null) {
                if(killed.getKiller() instanceof Player) {
                    Player killer = killed.getKiller();
                    rpKiller = plugin.connectedPlayers.get(killer.getUniqueId());
                }
            }
            else if(killed.getLastDamageCause() != null){
                if(killed.getLastDamageCause().getEntity() instanceof Arrow){
                    if(((Arrow) killed.getLastDamageCause().getEntity()).getShooter() != null && ((Arrow) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get((((Player) ((Arrow) killed.getLastDamageCause().getEntity()).getShooter()).getPlayer().getUniqueId()));
                        plugin.debugMessage("Killed by arrow!");
                    }
                }
                else if(killed.getLastDamageCause().getEntity() instanceof ThrownPotion){
                    if(((ThrownPotion) killed.getLastDamageCause().getEntity()).getShooter() != null && ((ThrownPotion) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get((((Player) ((ThrownPotion) killed.getLastDamageCause().getEntity()).getShooter()).getPlayer().getUniqueId()));
                        plugin.debugMessage("Killed by potion!");
                    }
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
                plugin.debugMessage("Killer gains "+ xpGained+" xp!");
            }

            if(Settings.getInstance().math.onPlayerDies.enable){
                rpKilled.addExp(-(Settings.getInstance().math.onPlayerDies.xp_loss / 100 * rpKilled.getExperience()));

                double dropPercentage = getPlayerDropPercentage(rpKilled);

                plugin.debugMessage("Percentage of dropped items : " + dropPercentage);

                int count = (int)(rpKilled.getPlayer().getInventory().getSize() * dropPercentage);

                for (int i = 0; i < count; i++) {
                    int rdm = Helper.randomInt(0,rpKilled.getPlayer().getInventory().getSize() - 1);

                    ItemStack item = rpKilled.getPlayer().getInventory().getItem(rdm);

                    if(item != null){
                        rpKilled.getPlayer().getWorld().dropItemNaturally(rpKilled.getPlayer().getLocation(),item);
                        rpKilled.getPlayer().getInventory().remove(item);
                    }
                }

                plugin.debugMessage("Items dropped : " + count);
            }
            rpKilled.addDeaths(1);

        }
        //MOB DIES
        else {
            RPGPlayer rpKiller = null;
            if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player){
                rpKiller = plugin.connectedPlayers.get(e.getEntity().getKiller().getUniqueId());
            }
            else if(e.getEntity().getLastDamageCause() != null){
                if(e.getEntity().getLastDamageCause().getEntity() instanceof Arrow){
                    if(((Arrow) e.getEntity().getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get(((Player) ((Arrow) e.getEntity().getLastDamageCause().getEntity()).getShooter()).getUniqueId());
                    }
                }
                else if(e.getEntity().getLastDamageCause().getEntity() instanceof ThrownPotion){
                    if(((ThrownPotion) e.getEntity().getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get(((Player) ((ThrownPotion) e.getEntity().getLastDamageCause().getEntity()).getShooter()).getUniqueId());
                    }
                }
            }

            if(rpKiller != null){

                String mobName = e.getEntity().getName().toLowerCase();

                rpKiller.getMobKills().put(mobName,rpKiller.getMobKills().getOrDefault(mobName,0) + 1);

                int killsCount = rpKiller.getMobKills().get(mobName);
                ConfigurationSection section = MobsXP.getInstance().getConfigurationSection(mobName);
                if(section != null){
                    int interval = section.getInt("xp_bonus_interval");
                    double xp = section.getDouble("base_xp");
                    if(killsCount % interval == 0){
                        rpKiller.SendMessage("You've just killed your " + killsCount + " " + mobName);
                        xp = killsCount / interval * section.getDouble("xp_bonus_multiplier");
                    }

                    if(Settings.getInstance().nearby_players_gain_xp){
                        List<RPGPlayer> others = plugin.getNearbyPlayers(rpKiller,Settings.getInstance().nearby_players_max_distance);
                        rpKiller.addExp(xp * 0.66666);
                        plugin.debugMessage(rpKiller.getPlayer().getName() + " gains "+ xp * 0.66666 +" xp!");

                        if(others.size() > 0) {
                            double othersXP = xp * 0.33333 / others.size();
                            for (RPGPlayer other : others) {
                                other.addExp(othersXP);
                                plugin.debugMessage(other.getPlayer().getName() + " gains " + othersXP + " xp!");
                            }
                        }
                    }
                    else{
                        rpKiller.addExp(xp);
                        plugin.debugMessage(rpKiller.getPlayer().getName() + " gains "+ xp+" xp!");
                    }

                }


                /*
                if(e.getEntity() instanceof Bat){

                }
                else if(e.getEntity() instanceof Blaze){

                }
                else if(e.getEntity() instanceof CaveSpider){

                }
                else if(e.getEntity() instanceof Chicken){

                }
                else if(e.getEntity() instanceof Cow){

                }
                else if(e.getEntity() instanceof Creeper){

                }
                else if(e.getEntity() instanceof EnderDragon){

                }
                else if(e.getEntity() instanceof Enderman){

                }
                else if(e.getEntity() instanceof Endermite){

                }
                else if(e.getEntity() instanceof Ghast){

                }
                else if(e.getEntity() instanceof Giant){

                }
                else if(e.getEntity() instanceof Guardian){

                }
                else if(e.getEntity() instanceof Horse){

                }
                else if(e.getEntity() instanceof IronGolem){

                }
                else if(e.getEntity() instanceof MushroomCow){

                }
                else if(e.getEntity() instanceof Pig){

                }
                else if(e.getEntity() instanceof PigZombie){

                }
                else if(e.getEntity() instanceof PolarBear){

                }
                else if(e.getEntity() instanceof Rabbit){

                }
                else if(e.getEntity() instanceof Sheep){

                }
                else if(e.getEntity() instanceof Shulker){

                }
                else if(e.getEntity() instanceof Skeleton){

                }
                else if(e.getEntity() instanceof Slime){

                }
                else if(e.getEntity() instanceof Snowman){

                }
                else if(e.getEntity() instanceof Spider){

                }
                else if(e.getEntity() instanceof Squid){

                }
                else if(e.getEntity() instanceof Villager){

                }
                else if(e.getEntity() instanceof WaterMob){

                }
                else if(e.getEntity() instanceof Witch){

                }
                else if(e.getEntity() instanceof Wither){

                }
                else if(e.getEntity() instanceof Wolf){

                }
                else if(e.getEntity() instanceof Zombie){

                }
                */
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
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
        RPGPlayer damager = null;

        boolean magicAttack = false;
        boolean arrowAttack = false;
        boolean normalAttack = false;

        double oldDamage = e.getDamage();

        //DEFINING CAUSE OF DAMAGE
        if (e.getDamager() instanceof Player) {
            damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            plugin.debugMessage("Attacked by another player : " + damager.getPlayer().getName());
            normalAttack  = true;
            magicAttack = false;
            arrowAttack = false;
        } else if (e.getDamager() instanceof Arrow) {
            Arrow ar = (Arrow) e.getDamager();

            if (ar.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(((Player)((Arrow) e.getDamager()).getShooter()).getUniqueId());
            }
            plugin.debugMessage("Attacked by arrow");
            arrowAttack = true;
            normalAttack = false;
            magicAttack = false;
        } else if (e.getDamager() instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) e.getDamager();
            if (pot.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(((Player)((ThrownPotion) e.getDamager()).getShooter()).getUniqueId());
            }
            plugin.debugMessage("Attacked by potion");
            magicAttack = true;
            normalAttack = false;
            arrowAttack = false;
        }

        //CUSTOM PROJECTILE (SKILLS AND STAFFS)
        if(e.getDamager() instanceof  Projectile){

            Projectile projectile = (Projectile) e.getDamager();

            if(projectile.getShooter() instanceof Player && damager == null){
                damager = plugin.connectedPlayers.get(((Player) projectile.getShooter()).getUniqueId());
            }

            if(damager != null && !Helper.StringIsNullOrEmpty(projectile.getCustomName())){

                if(damager.getSkills().containsKey(projectile.getCustomName())){
                    RPGSkill skill = damager.getSkills().get(projectile.getCustomName());
                    switch (skill.getName()){
                        case "frozen_arrow":
                            PotionEffect po = new PotionEffect(PotionEffectType.SLOW,skill.getCurrentLevel() * 2 *20,skill.getCurrentLevel(),true,true);
                            if(e.getEntity() instanceof  LivingEntity)po.apply((LivingEntity)e.getEntity());
                            break;
                    }
                }
                else{
                    ConfigurationSection staffSection = Staffs.getInstance().getConfigurationSection(projectile.getCustomName());
                    if(staffSection != null){

                        double baseDamage = staffSection.getDouble("base_damage",-1);
                        String effect = staffSection.getString("effect","");
                        int effectDuration = staffSection.getInt("effect_duration",3);
                        plugin.debugMessage("Attacked by magic projectile");

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
                                if(e.getEntity() instanceof LivingEntity) po.apply((LivingEntity)e.getEntity());
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
                return;
            }
        }

        //APLLYING MAGIC EFFECT BY ATTACKER
        if(damager != null && !magicAttack && Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.enable){
            plugin.debugMessage("From " + damager.getPlayer().getName());
            double chanceOfMagicEffect = Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.maximum,Settings.getInstance().math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.minimum,
                    (damager.getIntelligence() * Settings.getInstance().math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.intelligence
                    + damager.getDexterity() * Settings.getInstance().math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.dexterity));

            double rdm = Math.random();
            if(rdm < chanceOfMagicEffect){
                PotionEffect effect = CreateMagicAttack(damager);
                if(e.getEntity() instanceof LivingEntity){
                    ((LivingEntity) e.getEntity()).addPotionEffect(effect);
                    plugin.debugMessage("magic effect added : " + effect.getType().getName() + ", " + effect.getDuration()/20 + ", " + effect.getAmplifier());
                }
            }
        }

        //APPLYING DAMAGE CHANGE DEPENDING ON OFFENCIVE ATTRIBUTES
        if(arrowAttack && damager != null && Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_enable){
            e.setDamage(getAttackByProjectile(damager,e.getDamage()));
        }
        else if(normalAttack && damager != null && Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_enable){
            e.setDamage(getAttackByWeapon(damager,e.getDamage()));
        }
        else if(magicAttack && damager != null && Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.potion_enable){
            e.setDamage(getAttackByPotion(damager,e.getDamage()));
        }

        //applying skilled attack if necessary
        if(normalAttack && damager != null && damager.getCurrentSkill() != null && e.getEntity() instanceof LivingEntity && damager.getCurrentSkill().executeWeaponAttackSkill(damager,(LivingEntity) e.getEntity(),e.getFinalDamage())){

        }

        if(damager != null) {

            //CREATING DAMAGE HOLOGRAM
            if(plugin.useHolographicDisplays){
                NumberFormat formatter = new DecimalFormat("#0.00");
                Location loc = e.getEntity().getLocation().add(0,2,0);
                Hologram holoDamage = HologramsAPI.createHologram(plugin,loc);
                holoDamage.appendTextLine(ChatColor.WHITE + formatter.format(e.getDamage()));

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

            plugin.debugMessage("inital damage : " + oldDamage);
            double damageMultiplier = e.getDamage() / oldDamage;
            plugin.debugMessage("Offencive damage multiplier : " + damageMultiplier);
            plugin.debugMessage("Damage after offencive multiplier : " + e.getDamage());
        }

    }

    /**
     * When a player consume a potion
     * @param e
     */
    @EventHandler
    public void onPlayerConsumePotion(PlayerItemConsumeEvent e){
        if(e.getItem() != null && e.getItem().getType().equals(Material.POTION) && Settings.getInstance().math.onPlayerConsumePotion.enable){
            RPGPlayer rp = plugin.connectedPlayers.get(e.getPlayer().getUniqueId());
            if(rp != null) {

                float result = getConsumedPotionMultiplier(rp);

                PotionEffect po = (PotionEffect) rp.getPlayer().getActivePotionEffects().toArray()[rp.getPlayer().getActivePotionEffects().size() -1];

                int newDuration = (int) (po.getDuration() * result);
                PotionEffect tmp = new PotionEffect(po.getType(), newDuration, po.getAmplifier());
                rp.getPlayer().removePotionEffect(po.getType());
                rp.getPlayer().addPotionEffect(tmp);

                plugin.debugMessage("effect duration multiply by " + result);
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

            float speed = getBowArrowSpeed(rpPlayer);
            plugin.debugMessage("Arrow speed multiplier : " + speed);
            ar.setVelocity(ar.getVelocity().multiply(speed));

            float precX = getBowPrecision(rpPlayer);
            int direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precX = 1 + (1-precX)*direction;

            float precY = getBowPrecision(rpPlayer);
            direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precY = 1 + (1-precY)*direction;

            float precZ = getBowPrecision(rpPlayer);
            direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precZ = 1 + (1-precZ)*direction;

            plugin.debugMessage("Arrow precision multiplier : " + precX);
            ar.setVelocity(new Vector(ar.getVelocity().getX() * precX, ar.getVelocity().getY() * precY, ar.getVelocity().getZ() * precZ));

            if(rpPlayer.getCurrentSkill() != null && rpPlayer.getCurrentSkill().executeBowSkill(rpPlayer,ar,speed) ){

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
        plugin.connectedPlayers.get(e.getPlayer().getUniqueId()).Disconnect();
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
        switch (rdm){
            case 1:
                type = PotionEffectType.BLINDNESS;
                amplifier = 0;
                break;
            case 2:
                type = PotionEffectType.CONFUSION;
                amplifier = 0;
                break;
            case 3:
                type = PotionEffectType.HARM;
                max =  Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.minimum_duration;
                min = Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.maximum_duration;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.maximum_harm_amplifier,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.minimum_harm_amplifier,
                        p.getIntelligence() * Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_harm_amplifier));
                break;
            case 4:
                type = PotionEffectType.POISON;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.maximum_poison_amplifier,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.minimum_poison_amplifier,
                        p.getIntelligence() * Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_poison_amplifier));
                break;
            case 5:
                type = PotionEffectType.SLOW;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.maximum_slow_amplifier,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.minimum_slow_amplifier,
                        p.getIntelligence() * Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_slow_amplifier));
                break;
            case 6:
                type = PotionEffectType.WEAKNESS;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.maximum_weakness_amplifier,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.minimum_weakness_amplifier,
                        p.getIntelligence() * Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_weakness_amplifier));
                break;
            case 7:
                type = PotionEffectType.WITHER;
                amplifier = (int)(Helper.ValueFromFunction(Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.maximum_wither_amplifier,Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.minimum_wither_amplifier,
                        p.getIntelligence() * Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_wither_amplifier));
                break;
        }

        duration = (int)(Helper.ValueFromFunction(max,min,
                (p.getIntelligence() * Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_duration
                + p.getDexterity() * Settings.getInstance().math.onPlayerAttackEntity.creatingMagicAttack.dexterity_on_duration)));

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

        float rdm = getReducingPotionEffect(rp);

        for (PotionEffect pe : rp.getPlayer().getActivePotionEffects()) {
            int newDuration = (int)(pe.getDuration() * rdm);
            int newAmplifier = (int)(pe.getAmplifier() * rdm);
            PotionEffect tmp = new PotionEffect(pe.getType(),newDuration,newAmplifier);
            rp.getPlayer().removePotionEffect(pe.getType());
            rp.getPlayer().addPotionEffect(tmp);
        }
        plugin.debugMessage("all effect reduced by " + rdm);
    }

    /**
     * Set the server difficulty depending on average connectedplayer level
     */
    private void SetServerDifficulty(){

        Difficulty diff;
        Double averageLevel = 0.0;
        for (RPGPlayer rp :
                this.plugin.connectedPlayers.values()) {
            averageLevel += rp.getLvl();
        }
        averageLevel = averageLevel / this.plugin.connectedPlayers.size();

        if(averageLevel <= 25){
            diff = Difficulty.EASY;
        }
        else if(averageLevel <= 60){
            diff = Difficulty.NORMAL;
        }
        else {
            diff = Difficulty.HARD;
        }

        for (World world :
                this.plugin.getServer().getWorlds()) {
            world.setDifficulty(diff);
        }

        this.plugin.debugMessage("Server difficulty set to " + diff.name());
    }
    
    //ON PLAYER GET DAMAGED

    public float getDamagedByFire(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.fire_function)){
            float max = Settings.getInstance().math.onPlayerGetDamaged.fire_maximum;
            float min = Settings.getInstance().math.onPlayerGetDamaged.fire_minimum;
            float x = damagee.getMagicResistance() * Settings.getInstance().math.onPlayerGetDamaged.fire_magic_resistance
                    + damagee.getDefence() * Settings.getInstance().math.onPlayerGetDamaged.fire_defence
                    + damagee.getIntelligence() * Settings.getInstance().math.onPlayerGetDamaged.fire_intelligence;

            return Helper.ValueFromFunction(max,min,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.fire_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public float getDamagedByContact(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.contact_function)){
            float max = Settings.getInstance().math.onPlayerGetDamaged.contact_maximum;
            float min = Settings.getInstance().math.onPlayerGetDamaged.contact_minimum;
            float x = damagee.getDefence() * Settings.getInstance().math.onPlayerGetDamaged.contact_defence;

            return Helper.ValueFromFunction(max,min,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.contact_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public float getDamagedByWeapon(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.weapon_function)){
            float max = Settings.getInstance().math.onPlayerGetDamaged.weapon_maximum;
            float min = Settings.getInstance().math.onPlayerGetDamaged.weapon_minimum;
            float  x = damagee.getDefence() * Settings.getInstance().math.onPlayerGetDamaged.weapon_defence;

            return Helper.ValueFromFunction(max,min,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.weapon_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByMagic(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.magic_function)){
            float max = Settings.getInstance().math.onPlayerGetDamaged.magic_maximum;
            float min = Settings.getInstance().math.onPlayerGetDamaged.magic_minimum;
            float x = (damagee.getMagicResistance() * Settings.getInstance().math.onPlayerGetDamaged.magic_magic_resistance
                    + damagee.getIntelligence() * Settings.getInstance().math.onPlayerGetDamaged.magic_intelligence);

            return Helper.ValueFromFunction(max,min,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.magic_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByFall(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.fall_function)){
            float max = Settings.getInstance().math.onPlayerGetDamaged.fall_maximum;
            float min = Settings.getInstance().math.onPlayerGetDamaged.fall_minimum;
            float x = damagee.getAgility() * Settings.getInstance().math.onPlayerGetDamaged.fall_agility
                    + damagee.getDefence() * Settings.getInstance().math.onPlayerGetDamaged.fall_defence;

            return Helper.ValueFromFunction(max,min,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.fall_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByExplosion(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.explosion_function)){
            float max = Settings.getInstance().math.onPlayerGetDamaged.explosion_maximum;
            float min = Settings.getInstance().math.onPlayerGetDamaged.explosion_minimum;
            float x = damagee.getDefence() * Settings.getInstance().math.onPlayerGetDamaged.explosion_defence
                    + damagee.getStrength() * Settings.getInstance().math.onPlayerGetDamaged.explosion_strength;

            return Helper.ValueFromFunction(max,min,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.explosion_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByProjectile(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.projectile_function)){
            float max = Settings.getInstance().math.onPlayerGetDamaged.projectile_maximum;
            float min = Settings.getInstance().math.onPlayerGetDamaged.projectile_minimum;
            float x = damagee.getDefence() * Settings.getInstance().math.onPlayerGetDamaged.projectile_defence;

            return Helper.ValueFromFunction(max,min,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.projectile_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getChangeOfRemovingPotionEffect(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.function)){
            float minChance = Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.minimum;
            float maxChance = Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.maximum;

            float x = damagee.getMagicResistance()* Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.magic_resistance
                    + damagee.getIntelligence() * Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.intelligence
                    + damagee.getDexterity() * Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.dexterity;
            return Helper.ValueFromFunction(maxChance,minChance,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getReducingPotionEffect(RPGPlayer damagee){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.function)){
            float min = Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.minimum;
            float max = Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.maximum;
            float range = Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.range;

            float x = (damagee.getMagicResistance()* Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.magic_resistance
                    + damagee.getIntelligence()) * Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.intelligence;

            float reduction = Helper.ValueFromFunction(max,min,x);

            float minReduction = reduction < (min - range) ? reduction + range : min;

            return Helper.randomFloat(reduction,minReduction);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerGetDamaged.reducingBadPotionEffect.function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    // ON PLAYER ATTACK

    public  float getBowArrowSpeed(RPGPlayer p){
        float result = 0F;
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerShootBow.speed_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().math.onPlayerShootBow.speed_maximum,Settings.getInstance().math.onPlayerShootBow.speed_minimum,
                    (p.getDexterity() * Settings.getInstance().math.onPlayerShootBow.speed_dexterity
                    + p.getStrength() * Settings.getInstance().math.onPlayerShootBow.speed_strength));
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerShootBow.speed_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(Settings.getInstance().math.onPlayerShootBow.speed_range > 0){
            result = Helper.randomFloat((result - Settings.getInstance().math.onPlayerShootBow.speed_range),(result + Settings.getInstance().math.onPlayerShootBow.speed_range));
            if(result < Settings.getInstance().math.onPlayerShootBow.speed_minimum)result = Settings.getInstance().math.onPlayerShootBow.speed_minimum;
            else if(result > Settings.getInstance().math.onPlayerShootBow.speed_maximum) result = Settings.getInstance().math.onPlayerShootBow.speed_maximum;
        }

        return result;
    }

    public  float getBowPrecision(RPGPlayer p){
        float result = 1F;
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerShootBow.precision_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().math.onPlayerShootBow.precision_maximum,Settings.getInstance().math.onPlayerShootBow.precision_minimum,
                    p.getDexterity() * Settings.getInstance().math.onPlayerShootBow.precision_dexterity);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerShootBow.precision_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(Settings.getInstance().math.onPlayerShootBow.precision_range > 0){
            result = Helper.randomFloat(result - Settings.getInstance().math.onPlayerShootBow.precision_range,result + Settings.getInstance().math.onPlayerShootBow.precision_range);
            if(result < 0)result = 0;
            else if(result > 1) result = 1;
        }

        return result;
    }

    public  float getAttackByWeapon(RPGPlayer damager,double damage){

        float max = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_maximum;
        float min = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_minimum;
        float range = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_range;
        float x = damager.getDexterity() * Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_dexterity
                +  damager.getStrength() * Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_strength;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_function)) {
            result = (float)damage * Helper.ValueFromFunction(max,min,x);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.weapon_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,damager));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }
        if(range > 0){
            result = Helper.randomFloat(result-range,result+range);
            if(result < 0)result = 0;
        }
        return result;
    }

    public  float getAttackByProjectile(RPGPlayer damager, double damage){
        float max = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_maximum;
        float min = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_minimum;
        float range = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_function)) {
            result = (float)damage * Helper.ValueFromFunction(max,min,
                    damager.getDexterity() * Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_dexterity
                    + damager.getStrength() * Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_strength);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.projectile_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,damager));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(range > 0){
            result = Helper.randomFloat(result - range,result + range);
            if(result < 0)result = 0;
        }

        return result;
    }

    public  float getAttackByPotion(RPGPlayer damager, double damage){
        float max = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.potion_maximum;
        float min = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.potion_minimum;
        float range = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.potion_range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.potion_function)) {
            result = (float)damage * Helper.ValueFromFunction(max,min,
                    damager.getDexterity() * Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.potion_dexterity
                    + damager.getIntelligence() * Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.potion_intelligence);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerAttackEntity.attackEntityBy.potion_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,damager));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(range > 0){
            result = Helper.randomFloat(result - range,result + range);
            if(result < 0)result = 0;
        }
        return result;
    }

    // ON PLAYER CONSUME POTION

    public  float getConsumedPotionMultiplier(RPGPlayer p){

        float max = Settings.getInstance().math.onPlayerConsumePotion.maximum;
        float min = Settings.getInstance().math.onPlayerConsumePotion.minimum;
        float range = Settings.getInstance().math.onPlayerConsumePotion.range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerConsumePotion.function)) {
            result = Helper.ValueFromFunction(max,min,p.getIntelligence() * Settings.getInstance().math.onPlayerConsumePotion.intelligence);
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerConsumePotion.function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(range > 0){
            result = Helper.randomFloat(result - range,result + range);
            if(result < min)result = min;
            else if(result > max) result = max;
        }

        return result;
    }

    //PLAYER ATTRIBUTES

    public static float getPlayerMaxHealth(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.playerAttributes.total_health_function)) {
            return Helper.ValueFromFunction(p.getRpgRace().getMax_health(), p.getRpgRace().getBase_health(),p.getHealth() * Settings.getInstance().math.playerAttributes.total_health_health);
        }
        else{
            String[] st = Settings.getInstance().math.playerAttributes.total_health_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerMaxMana(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.playerAttributes.total_mana_function)) {
            return Helper.ValueFromFunction(p.getRpgRace().getMax_mana(), p.getRpgRace().getBase_mana(),
                    p.getIntelligence() * Settings.getInstance().math.playerAttributes.total_mana_intelligence);
        }
        else{
            String[] st = Settings.getInstance().math.playerAttributes.total_mana_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerManaRegen(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.playerAttributes.mana_regen_function)) {
            return Helper.ValueFromFunction(Settings.getInstance().math.playerAttributes.mana_regen_maximum,Settings.getInstance().math.playerAttributes.mana_regen_minimum,
                    p.getIntelligence() * Settings.getInstance().math.playerAttributes.mana_regen_intelligence
            );
        }
        else{
            String[] st = Settings.getInstance().math.playerAttributes.mana_regen_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerAttackSpeed(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.playerAttributes.attack_speed_function)) {
            return Helper.ValueFromFunction(Settings.getInstance().math.playerAttributes.attack_speed_maximum,Settings.getInstance().math.playerAttributes.attack_speed_minimum,
                    (p.getAgility() * Settings.getInstance().math.playerAttributes.attack_speed_agility
                    + p.getDexterity() * Settings.getInstance().math.playerAttributes.attack_speed_dexterity
                    + p.getStrength() * Settings.getInstance().math.playerAttributes.attack_speed_strength));
        }
        else{
            String[] st = Settings.getInstance().math.playerAttributes.attack_speed_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerMovementSpeed(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.playerAttributes.movement_speed_function)) {
            return Helper.ValueFromFunction(Settings.getInstance().math.playerAttributes.movement_speed_maximum,Settings.getInstance().math.playerAttributes.movement_speed_minimum,
                    (p.getAgility() * Settings.getInstance().math.playerAttributes.movement_speed_agility
                    + p.getDexterity() * Settings.getInstance().math.playerAttributes.movement_speed_dexterity));
        }
        else{
            String[] st = Settings.getInstance().math.playerAttributes.movement_speed_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerKnockbackResistance(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.playerAttributes.knockback_resistance_function)) {
            return Helper.ValueFromFunction(Settings.getInstance().math.playerAttributes.knockback_resistance_maximum,Settings.getInstance().math.playerAttributes.knockback_resistance_minimum,
                    (p.getStrength() * Settings.getInstance().math.playerAttributes.knockback_resistance_strength
                    + p.getDefence() * Settings.getInstance().math.playerAttributes.knockback_resistance_defence
                    + p.getDexterity() * Settings.getInstance().math.playerAttributes.knockback_resistance_dexterity
                    + p.getAgility() * Settings.getInstance().math.playerAttributes.knockback_resistance_agility));
        }
        else{
            String[] st = Settings.getInstance().math.playerAttributes.knockback_resistance_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerLuck(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.playerAttributes.luck_function)) {
            return Helper.ValueFromFunction(Settings.getInstance().math.playerAttributes.luck_maximum,Settings.getInstance().math.playerAttributes.luck_minimum,
                    (p.getAgility() * Settings.getInstance().math.playerAttributes.luck_agility
                    + p.getIntelligence() * Settings.getInstance().math.playerAttributes.luck_intelligence
                    + p.getDexterity() * Settings.getInstance().math.playerAttributes.luck_dexterity));
        }
        else{
            String[] st = Settings.getInstance().math.playerAttributes.luck_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerDropPercentage(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().math.onPlayerDies.function)) {
            return Helper.ValueFromFunction(Settings.getInstance().math.onPlayerDies.items_drops_maximum,Settings.getInstance().math.onPlayerDies.items_drops_minimum,
                    (p.getAgility() * Settings.getInstance().math.onPlayerDies.agility
                        + p.getIntelligence() * Settings.getInstance().math.onPlayerDies.intelligence));
        }
        else{
            String[] st = Settings.getInstance().math.onPlayerDies.function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }
}
