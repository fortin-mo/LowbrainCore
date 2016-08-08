package lowbrain.mcrpg.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lowbrain.mcrpg.commun.Helper;
import lowbrain.mcrpg.main.Main;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class PlayerListener implements Listener {

	public static Main plugin;
    private static int max_stats;


    public PlayerListener(Main instance) {
        plugin = instance;
        max_stats = plugin.config.max_stats <= 0 ? 100 : plugin.config.max_stats;
    }

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

        /*if(e.getItem().getType().equals(Material.WOOD_AXE) && e.getAction().equals(Action.LEFT_CLICK_AIR)){
            plugin.debugMessage(e.getItem().getDurability());
            //e.getItem().setDurability((short)(e.getItem().getDurability() + 1));
            Fireball ball = e.getPlayer().launchProjectile(Fireball.class,e.getPlayer().getLocation().getDirection());
            ball.setGravity(true);
            //ball.setFallDistance();
            plugin.debugMessage(e.getItem().getDurability());
        }*/
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
            if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && plugin.config.math.onPlayerGetDamaged.fall_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByFall(damagee);
            }

            //FIRE DAMAGE
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)) && plugin.config.math.onPlayerGetDamaged.fire_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByFire(damagee);
            }

            //EXPLOSION
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))&& plugin.config.math.onPlayerGetDamaged.explosion_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByExplosion(damagee);
            }

            //MAGIC POTION DAMAGE
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.WITHER)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.POISON)) && plugin.config.math.onPlayerGetDamaged.magic_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByMagic(damagee);

                float changeOfRemovingEffect = -1F;

                if(plugin.config.math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.enable){
                    changeOfRemovingEffect = getChangeOfRemovingPotionEffect(damagee);
                }

                double rdm = Math.random();
                if(rdm < changeOfRemovingEffect){
                    RemoveBadPotionEffect(damagee.getPlayer());
                    plugin.debugMessage("all effect removed");
                }
                else if(plugin.config.math.onPlayerGetDamaged.reducingBadPotionEffect.enable){
                    ReducingBadPotionEffect(damagee);
                }
            }

            else if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && plugin.config.math.onPlayerGetDamaged.weapon_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByWeapon(damagee);
            }

            //ARROW
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) && plugin.config.math.onPlayerGetDamaged.projectile_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByProjectile(damagee);
            }

            //CONTACT
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT) && plugin.config.math.onPlayerGetDamaged.contact_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByContact(damagee);
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
            plugin.debugMessage("Player gains " + e.getAmount() * plugin.config.math.natural_xp_gain_multiplier + " xp");
            rp.addExp(e.getAmount() * plugin.config.math.natural_xp_gain_multiplier);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDies(PlayerDeathEvent e){
        e.setKeepInventory(true);
    }

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

            if(rpKiller != null && plugin.config.math.player_kills_player_exp_enable){
                int diffLvl = Math.abs(rpKilled.getLvl() - rpKiller.getLvl());
                rpKiller.addKills(1);
                float xpGained = 0.0F;
                if(diffLvl == 0){
                    xpGained = plugin.config.math.killer_base_exp * rpKiller.getLvl() * plugin.config.math.killer_level_gain_multiplier;
                }else if(rpKilled.getLvl() < rpKiller.getLvl()){
                    xpGained = plugin.config.math.killer_base_exp / (diffLvl * plugin.config.math.level_difference_multiplier) * rpKiller.getLvl() * plugin.config.math.killer_level_gain_multiplier;
                }else{
                    xpGained = plugin.config.math.killer_base_exp * (diffLvl * plugin.config.math.level_difference_multiplier) * rpKiller.getLvl() * plugin.config.math.killer_level_gain_multiplier;
                }
                rpKiller.addExp(xpGained);
                plugin.debugMessage("Killer gains "+ xpGained+" xp!");
            }

            if(plugin.config.math.onPlayerDies.enable){
                rpKilled.addExp(-(plugin.config.math.onPlayerDies.xp_loss / 100 * rpKilled.getExperience()));

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
                ConfigurationSection section = plugin.mobsxpConfig.getConfigurationSection(mobName);
                if(section != null){
                    int interval = section.getInt("xp_bonus_interval");
                    double xp = section.getDouble("base_xp");
                    if(killsCount % interval == 0){
                        rpKiller.SendMessage("You've just killed your " + killsCount + " " + mobName);
                        xp = killsCount / interval * section.getDouble("xp_bonus_multiplier");
                    }

                    if(plugin.config.nearby_players_gain_xp){
                        List<RPGPlayer> others = plugin.getNearbyPlayers(rpKiller,plugin.config.nearby_players_max_distance);
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
        } else if (e.getDamager() instanceof Arrow) {
            Arrow ar = (Arrow) e.getDamager();
            if (ar.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(((Player)((Arrow) e.getDamager()).getShooter()).getUniqueId());
            }
            plugin.debugMessage("Attacked by arrow");
            arrowAttack = true;
        } else if (e.getDamager() instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) e.getDamager();
            if (pot.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(((Player)((ThrownPotion) e.getDamager()).getShooter()).getUniqueId());
            }
            plugin.debugMessage("Attacked by potion");
            magicAttack = true;
        }
        else{
            normalAttack  = true;
        }

        //CHECK IF PLAYER CAN USE THE ITEM IN HAND
        if(damager != null && damager.getPlayer().getInventory().getItemInMainHand() != null){
            if(!damager.canEquipItem(damager.getPlayer().getInventory().getItemInMainHand())){
                e.setCancelled(true);
                return;
            }
        }

        //APLLYING MAGIC EFFECT BY ATTACKER
        if(damager != null && !magicAttack && plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.enable){
            plugin.debugMessage("From " + damager.getPlayer().getName());
            double chanceOfMagicEffect = ValueFromFunction(plugin.config.math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.maximum,plugin.config.math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.minimum,
                    (damager.getIntelligence() * plugin.config.math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.intelligence
                    + damager.getDexterity() * plugin.config.math.onPlayerAttackEntity.chanceOfCreatingMagicAttack.dexterity));

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
        if(arrowAttack && damager != null && plugin.config.math.onPlayerAttackEntity.attackEntityBy.projectile_enable){
            e.setDamage(getAttackByProjectile(damager,oldDamage));
        }
        else if(normalAttack && damager != null && plugin.config.math.onPlayerAttackEntity.attackEntityBy.weapon_enable){
            e.setDamage(getAttackByWeapon(damager,oldDamage));
        }
        else if(magicAttack && damager != null && plugin.config.math.onPlayerAttackEntity.attackEntityBy.potion_enable){
            e.setDamage(getAttackByPotion(damager,oldDamage));
        }

        if(damager != null) {

            //CREATING DAMAGE HOLOGRAM
            if(plugin.useHolographicDisplays){
                NumberFormat formatter = new DecimalFormat("#0.00");
                Location loc = e.getEntity().getLocation().add(0,1,0);
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
                        if (ticksRun > 100) { // 100 ticks = 5 seconds
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
        if(e.getItem() != null && e.getItem().getType().equals(Material.POTION) && plugin.config.math.onPlayerConsumePotion.enable){
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
        if(e.getEntity() instanceof Player && plugin.config.math.onPlayerShootBow.enable){
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
        float max = plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.maximum_duration;
        float min = plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.minimum_duration;

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
                max =  plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.minimum_duration;
                min = plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.maximum_duration;
                amplifier = (int)(ValueFromFunction(plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.maximum_harm_amplifier,plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.minimum_harm_amplifier,
                        p.getIntelligence() * plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_harm_amplifier));
                break;
            case 4:
                type = PotionEffectType.POISON;
                amplifier = (int)(ValueFromFunction(plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.maximum_poison_amplifier,plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.minimum_poison_amplifier,
                        p.getIntelligence() * plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_poison_amplifier));
                break;
            case 5:
                type = PotionEffectType.SLOW;
                amplifier = (int)(ValueFromFunction(plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.maximum_slow_amplifier,plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.minimum_slow_amplifier,
                        p.getIntelligence() * plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_slow_amplifier));
                break;
            case 6:
                type = PotionEffectType.WEAKNESS;
                amplifier = (int)(ValueFromFunction(plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.maximum_weakness_amplifier,plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.minimum_weakness_amplifier,
                        p.getIntelligence() * plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_weakness_amplifier));
                break;
            case 7:
                type = PotionEffectType.WITHER;
                amplifier = (int)(ValueFromFunction(plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.maximum_wither_amplifier,plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.minimum_wither_amplifier,
                        p.getIntelligence() * plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_wither_amplifier));
                break;
        }

        duration = (int)(ValueFromFunction(max,min,
                (p.getIntelligence() * plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.intelligence_on_duration
                + p.getDexterity() * plugin.config.math.onPlayerAttackEntity.creatingMagicAttack.dexterity_on_duration)));

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

    /**
     * evaluate slope
     * @param max max
     * @param min min
     * @return
     */
    public static float Slope(float max, float min){
        float slope = 0;
        switch (plugin.config.math.function_type){
            case 1:
                slope = (max - min)/(float)Math.pow(max_stats,2);
                break;
            case 2:
                slope = (max - min)/(float)Math.pow(max_stats,0.5);
                break;
            default:
                slope = (max - min)/max_stats;
                break;
        }
        //plugin.debugMessage("slope: " + slope);
        return slope;
    }

    /**
     * evaluate value
     * @param max max
     * @param min min
     * @param x value
     * @return
     */
    public static float ValueFromFunction(float max, float min, float x){
        //plugin.debugMessage("function_type: " + plugin.config.math.function_type);
        //plugin.debugMessage("max: " + max);
        //plugin.debugMessage("min: " + min);
        //plugin.debugMessage("x: " + x);
        float result = 0;
        switch (plugin.config.math.function_type){
            case 1:
                result = Slope(max,min) * (float)Math.pow(x,2) + min;
                break;
            case 2:
                result = Slope(max,min) * (float)Math.pow(x,0.5) + min;
                break;
            default:
                result = Slope(max,min) * x + min;
                break;
        }
        //plugin.debugMessage("result: " + result);
        return result;
    }
    
    //ON PLAYER GET DAMAGED

    public float getDamagedByFire(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.fire_function)){
            float max = plugin.config.math.onPlayerGetDamaged.fire_maximum;
            float min = plugin.config.math.onPlayerGetDamaged.fire_minimum;
            float x = damagee.getMagicResistance() * plugin.config.math.onPlayerGetDamaged.fire_magic_resistance
                    + damagee.getDefence() * plugin.config.math.onPlayerGetDamaged.fire_defence
                    + damagee.getIntelligence() * plugin.config.math.onPlayerGetDamaged.fire_intelligence;

            return ValueFromFunction(max,min,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.fire_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public float getDamagedByContact(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.contact_function)){
            float max = plugin.config.math.onPlayerGetDamaged.contact_maximum;
            float min = plugin.config.math.onPlayerGetDamaged.contact_minimum;
            float x = damagee.getDefence() * plugin.config.math.onPlayerGetDamaged.contact_defence;

            return ValueFromFunction(max,min,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.contact_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public float getDamagedByWeapon(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.weapon_function)){
            float max = plugin.config.math.onPlayerGetDamaged.weapon_maximum;
            float min = plugin.config.math.onPlayerGetDamaged.weapon_minimum;
            float  x = damagee.getDefence() * plugin.config.math.onPlayerGetDamaged.weapon_defence;

            return ValueFromFunction(max,min,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.weapon_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByMagic(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.magic_function)){
            float max = plugin.config.math.onPlayerGetDamaged.magic_maximum;
            float min = plugin.config.math.onPlayerGetDamaged.magic_minimum;
            float x = (damagee.getMagicResistance() * plugin.config.math.onPlayerGetDamaged.magic_magic_resistance
                    + damagee.getIntelligence() * plugin.config.math.onPlayerGetDamaged.magic_intelligence);

            return ValueFromFunction(max,min,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.magic_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByFall(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.fall_function)){
            float max = plugin.config.math.onPlayerGetDamaged.fall_maximum;
            float min = plugin.config.math.onPlayerGetDamaged.fall_minimum;
            float x = damagee.getAgility() * plugin.config.math.onPlayerGetDamaged.fall_agility
                    + damagee.getDefence() * plugin.config.math.onPlayerGetDamaged.fall_defence;

            return ValueFromFunction(max,min,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.fall_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByExplosion(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.explosion_function)){
            float max = plugin.config.math.onPlayerGetDamaged.explosion_maximum;
            float min = plugin.config.math.onPlayerGetDamaged.explosion_minimum;
            float x = damagee.getDefence() * plugin.config.math.onPlayerGetDamaged.explosion_defence
                    + damagee.getStrength() * plugin.config.math.onPlayerGetDamaged.explosion_strength;

            return ValueFromFunction(max,min,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.explosion_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByProjectile(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.projectile_function)){
            float max = plugin.config.math.onPlayerGetDamaged.projectile_maximum;
            float min = plugin.config.math.onPlayerGetDamaged.projectile_minimum;
            float x = damagee.getDefence() * plugin.config.math.onPlayerGetDamaged.projectile_defence;

            return ValueFromFunction(max,min,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.projectile_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getChangeOfRemovingPotionEffect(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.function)){
            float minChance = plugin.config.math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.minimum;
            float maxChance = plugin.config.math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.maximum;

            float x = damagee.getMagicResistance()* plugin.config.math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.magic_resistance
                    + damagee.getIntelligence() * plugin.config.math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.intelligence
                    + damagee.getDexterity() * plugin.config.math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.dexterity;
            return ValueFromFunction(maxChance,minChance,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.chanceOfRemovingMagicEffect.function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getReducingPotionEffect(RPGPlayer damagee){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerGetDamaged.reducingBadPotionEffect.function)){
            float min = plugin.config.math.onPlayerGetDamaged.reducingBadPotionEffect.minimum;
            float max = plugin.config.math.onPlayerGetDamaged.reducingBadPotionEffect.maximum;
            float range = plugin.config.math.onPlayerGetDamaged.reducingBadPotionEffect.range;

            float x = (damagee.getMagicResistance()* plugin.config.math.onPlayerGetDamaged.reducingBadPotionEffect.magic_resistance
                    + damagee.getIntelligence()) * plugin.config.math.onPlayerGetDamaged.reducingBadPotionEffect.intelligence;

            float reduction = ValueFromFunction(max,min,x);

            float minReduction = reduction < (min - range) ? reduction + range : min;

            return Helper.randomFloat(reduction,minReduction);
        }
        else{
            String[] st = plugin.config.math.onPlayerGetDamaged.reducingBadPotionEffect.function.split(",");
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
        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerShootBow.speed_function)) {
            result = ValueFromFunction(plugin.config.math.onPlayerShootBow.speed_maximum,plugin.config.math.onPlayerShootBow.speed_minimum,
                    (p.getDexterity() * plugin.config.math.onPlayerShootBow.speed_dexterity
                    + p.getStrength() * plugin.config.math.onPlayerShootBow.speed_strength));
        }
        else{
            String[] st = plugin.config.math.onPlayerShootBow.speed_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(plugin.config.math.onPlayerShootBow.speed_range > 0){
            result = Helper.randomFloat((result - plugin.config.math.onPlayerShootBow.speed_range),(result + plugin.config.math.onPlayerShootBow.speed_range));
            if(result < plugin.config.math.onPlayerShootBow.speed_minimum)result = plugin.config.math.onPlayerShootBow.speed_minimum;
            else if(result > plugin.config.math.onPlayerShootBow.speed_maximum) result = plugin.config.math.onPlayerShootBow.speed_maximum;
        }

        return result;
    }

    public  float getBowPrecision(RPGPlayer p){
        float result = 1F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerShootBow.precision_function)) {
            result = ValueFromFunction(plugin.config.math.onPlayerShootBow.precision_maximum,plugin.config.math.onPlayerShootBow.precision_minimum,
                    p.getDexterity() * plugin.config.math.onPlayerShootBow.precision_dexterity);
        }
        else{
            String[] st = plugin.config.math.onPlayerShootBow.precision_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(plugin.config.math.onPlayerShootBow.precision_range > 0){
            result = Helper.randomFloat(result - plugin.config.math.onPlayerShootBow.precision_range,result + plugin.config.math.onPlayerShootBow.precision_range);
            if(result < 0)result = 0;
            else if(result > 1) result = 1;
        }

        return result;
    }

    public  float getAttackByWeapon(RPGPlayer damager,double damage){

        float max = plugin.config.math.onPlayerAttackEntity.attackEntityBy.weapon_maximum;
        float min = plugin.config.math.onPlayerAttackEntity.attackEntityBy.weapon_minimum;
        float range = plugin.config.math.onPlayerAttackEntity.attackEntityBy.weapon_range;
        float x = damager.getDexterity() * plugin.config.math.onPlayerAttackEntity.attackEntityBy.weapon_dexterity
                +  damager.getStrength() * plugin.config.math.onPlayerAttackEntity.attackEntityBy.weapon_strength;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerAttackEntity.attackEntityBy.weapon_function)) {
            result = (float)damage * ValueFromFunction(max,min,x);
        }
        else{
            String[] st = plugin.config.math.onPlayerAttackEntity.attackEntityBy.weapon_function.split(",");
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
        float max = plugin.config.math.onPlayerAttackEntity.attackEntityBy.projectile_maximum;
        float min = plugin.config.math.onPlayerAttackEntity.attackEntityBy.projectile_minimum;
        float range = plugin.config.math.onPlayerAttackEntity.attackEntityBy.projectile_range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerAttackEntity.attackEntityBy.projectile_function)) {
            result = (float)damage * ValueFromFunction(max,min,
                    damager.getDexterity() * plugin.config.math.onPlayerAttackEntity.attackEntityBy.projectile_dexterity
                    + damager.getStrength() * plugin.config.math.onPlayerAttackEntity.attackEntityBy.projectile_strength);
        }
        else{
            String[] st = plugin.config.math.onPlayerAttackEntity.attackEntityBy.projectile_function.split(",");
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
        float max = plugin.config.math.onPlayerAttackEntity.attackEntityBy.potion_maximum;
        float min = plugin.config.math.onPlayerAttackEntity.attackEntityBy.potion_minimum;
        float range = plugin.config.math.onPlayerAttackEntity.attackEntityBy.potion_range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerAttackEntity.attackEntityBy.potion_function)) {
            result = (float)damage * ValueFromFunction(max,min,
                    damager.getDexterity() * plugin.config.math.onPlayerAttackEntity.attackEntityBy.potion_dexterity
                    + damager.getIntelligence() * plugin.config.math.onPlayerAttackEntity.attackEntityBy.potion_intelligence);
        }
        else{
            String[] st = plugin.config.math.onPlayerAttackEntity.attackEntityBy.potion_function.split(",");
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

        float max = plugin.config.math.onPlayerConsumePotion.maximum;
        float min = plugin.config.math.onPlayerConsumePotion.minimum;
        float range = plugin.config.math.onPlayerConsumePotion.range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerConsumePotion.function)) {
            result = ValueFromFunction(max,min,p.getIntelligence() * plugin.config.math.onPlayerConsumePotion.intelligence);
        }
        else{
            String[] st = plugin.config.math.onPlayerConsumePotion.function.split(",");
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
        if(Helper.StringIsNullOrEmpty(plugin.config.math.playerAttributes.total_health_function)) {
            return ValueFromFunction(p.getRpgRace().getMax_health(), p.getRpgRace().getBase_health(),p.getHealth() * plugin.config.math.playerAttributes.total_health_health);
        }
        else{
            String[] st = plugin.config.math.playerAttributes.total_health_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerMaxMana(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.playerAttributes.total_mana_function)) {
            return ValueFromFunction(p.getRpgRace().getMax_mana(), p.getRpgRace().getBase_mana(),
                    p.getIntelligence() * plugin.config.math.playerAttributes.total_mana_intelligence);
        }
        else{
            String[] st = plugin.config.math.playerAttributes.total_mana_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerManaRegen(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.playerAttributes.mana_regen_function)) {
            return ValueFromFunction(plugin.config.math.playerAttributes.mana_regen_maximum,plugin.config.math.playerAttributes.mana_regen_minimum,
                    p.getIntelligence() * plugin.config.math.playerAttributes.mana_regen_intelligence
            );
        }
        else{
            String[] st = plugin.config.math.playerAttributes.mana_regen_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerAttackSpeed(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.playerAttributes.attack_speed_function)) {
            return ValueFromFunction(plugin.config.math.playerAttributes.attack_speed_maximum,plugin.config.math.playerAttributes.attack_speed_minimum,
                    (p.getAgility() * plugin.config.math.playerAttributes.attack_speed_agility
                    + p.getDexterity() * plugin.config.math.playerAttributes.attack_speed_dexterity
                    + p.getStrength() * plugin.config.math.playerAttributes.attack_speed_strength));
        }
        else{
            String[] st = plugin.config.math.playerAttributes.attack_speed_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerMovementSpeed(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.playerAttributes.movement_speed_function)) {
            return ValueFromFunction(plugin.config.math.playerAttributes.movement_speed_maximum,plugin.config.math.playerAttributes.movement_speed_minimum,
                    (p.getAgility() * plugin.config.math.playerAttributes.movement_speed_agility
                    + p.getDexterity() * plugin.config.math.playerAttributes.movement_speed_dexterity));
        }
        else{
            String[] st = plugin.config.math.playerAttributes.movement_speed_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerKnockbackResistance(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.playerAttributes.knockback_resistance_function)) {
            return ValueFromFunction(plugin.config.math.playerAttributes.knockback_resistance_maximum,plugin.config.math.playerAttributes.knockback_resistance_minimum,
                    (p.getStrength() * plugin.config.math.playerAttributes.knockback_resistance_strength
                    + p.getDefence() * plugin.config.math.playerAttributes.knockback_resistance_defence
                    + p.getDexterity() * plugin.config.math.playerAttributes.knockback_resistance_dexterity
                    + p.getAgility() * plugin.config.math.playerAttributes.knockback_resistance_agility));
        }
        else{
            String[] st = plugin.config.math.playerAttributes.knockback_resistance_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerLuck(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.playerAttributes.luck_function)) {
            return ValueFromFunction(plugin.config.math.playerAttributes.luck_maximum,plugin.config.math.playerAttributes.luck_minimum,
                    (p.getAgility() * plugin.config.math.playerAttributes.luck_agility
                    + p.getIntelligence() * plugin.config.math.playerAttributes.luck_intelligence
                    + p.getDexterity() * plugin.config.math.playerAttributes.luck_dexterity));
        }
        else{
            String[] st = plugin.config.math.playerAttributes.luck_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerDropPercentage(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.onPlayerDies.function)) {
            return ValueFromFunction(plugin.config.math.onPlayerDies.items_drops_maximum,plugin.config.math.onPlayerDies.items_drops_minimum,
                    (p.getAgility() * plugin.config.math.onPlayerDies.agility
                        + p.getIntelligence() * plugin.config.math.onPlayerDies.intelligence));
        }
        else{
            String[] st = plugin.config.math.onPlayerDies.function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }
}
