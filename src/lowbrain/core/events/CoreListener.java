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
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class CoreListener implements Listener {

	public static LowbrainCore plugin;

    public CoreListener(LowbrainCore instance) {
        plugin = instance;
    }

    /**
     * called when create spawn in the world
     * we only set no tick damage if needed
     * @param e
     */
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e){
        if(Settings.getInstance().isDisableMobNoTickDamage())
            e.getEntity().setNoDamageTicks(0);

        // animal spawned from breeding
        if (e.getEntity() instanceof Animals && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {
            float limit = 0.5F;

            if (limit < Math.random()) {
                // cancel spawning
                e.setCancelled(true);
            }
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
            rp.sendMessage(Internationalization.format("cannot_equit_armor_or_item", requirements), ChatColor.RED);
            e.setCancelled(true);
            return;
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) /*|| e.getAction().equals(Action.RIGHT_CLICK_AIR.RIGHT_CLICK_BLOCK)*/){

            if(e.getItem().getItemMeta() != null && !Helper.StringIsNullOrEmpty(e.getItem().getItemMeta().getDisplayName())){
                ItemMeta iMeta = e.getItem().getItemMeta();
                String n = iMeta.getDisplayName().substring(2);
                if(n.startsWith("staff")){

                    ConfigurationSection staffSection = Staffs.getInstance().getConfigurationSection(n);

                    if (staffSection == null)
                        return;

                    onPlayerUseStaff(rp, staffSection, e.getItem());
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

            if(rp == null)
                return;

            plugin.debugInfo("************* On Player Exp Change ( naturally ) **************");
            plugin.debugInfo("              Player gains : " + e.getAmount() * Settings.getInstance().getParameters().getNaturalXpGainMultiplier() + " xp");
            rp.addExp(e.getAmount() * Settings.getInstance().getParameters().getNaturalXpGainMultiplier());
            plugin.debugInfo("************* ------------------------ **************");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDies(PlayerDeathEvent e){
        if(Settings.getInstance().getParameters().getOnPlayerDies().getItems_drops().isEnabled())
            e.setKeepInventory(true);
        else
            e.setKeepInventory(false);

    }

    /***
     * called when either a player or a mob dies
     * @param e
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        //PLAYER DIES
        if(e.getEntity() instanceof Player){
            plugin.debugInfo("************* On Player died **************");
            plugin.debugInfo("              name : " + e.getEntity().getName());

            LowbrainPlayer rpKiller = null;
            Player killed = (Player) e.getEntity();
            LowbrainPlayer rpKilled = plugin.getPlayerHandler().getList().get(killed.getUniqueId());
            if(killed.getKiller() != null) {
                if(killed.getKiller() instanceof Player) {
                    Player killer = killed.getKiller();
                    rpKiller = plugin.getPlayerHandler().getList().get(killer.getUniqueId());
                    plugin.debugInfo("              Killed by player : " + killed.getKiller().getName());
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

            if(rpKiller != null && Settings.getInstance().getParameters().isPlayerKillsPlayerExpEnable()){
                int diffLvl = Math.abs(rpKilled.getLvl() - rpKiller.getLvl());
                rpKiller.addKills(1);
                float xpGained = 0.0F;
                if(diffLvl == 0){
                    xpGained = Settings.getInstance().getParameters().getKillerBaseExp() * rpKiller.getLvl() * Settings.getInstance().getParameters().getLevelDifferenceMultiplier();
                }else if(rpKilled.getLvl() < rpKiller.getLvl()){
                    xpGained = Settings.getInstance().getParameters().getKillerBaseExp() / (diffLvl * Settings.getInstance().getParameters().getLevelDifferenceMultiplier()) * rpKiller.getLvl() * Settings.getInstance().getParameters().getKillerLevelGainMultiplier();
                }else{
                    xpGained = Settings.getInstance().getParameters().getKillerBaseExp() * (diffLvl * Settings.getInstance().getParameters().getLevelDifferenceMultiplier()) * rpKiller.getLvl() * Settings.getInstance().getParameters().getKillerLevelGainMultiplier();
                }
                rpKiller.addExp(xpGained);
                plugin.debugInfo("              Killer gained : "+ xpGained+" xp!");
            }

            if(Settings.getInstance().getParameters().getOnPlayerDies().isEnable()){
                rpKilled.addExp(-(Settings.getInstance().getParameters().getOnPlayerDies().getXp_loss() / 100 * rpKilled.getExperience()));

                double dropPercentage = rpKilled.getMultipliers().getPlayerDropPercentage();

                plugin.debugInfo("              Percentage of dropped items : " + dropPercentage);

                int count = (int)(rpKilled.getPlayer().getInventory().getSize() * dropPercentage);

                for (int i = 0; i < count; i++) {
                    int rdm = Helper.randomInt(0,rpKilled.getPlayer().getInventory().getSize() - 1);

                    ItemStack item = rpKilled.getPlayer().getInventory().getItem(rdm);

                    if(item != null){
                        rpKilled.getPlayer().getWorld().dropItemNaturally(rpKilled.getPlayer().getLocation(),item);
                        rpKilled.getPlayer().getInventory().remove(item);
                    }
                }

                plugin.debugInfo("              Items dropped : " + count);
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

                plugin.debugInfo("************* On Mob get killed **************");
                plugin.debugInfo("              Killed by : " + rpKiller.getPlayer().getName());
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

                    if(Settings.getInstance().isGroupXpEnable()){

                        List<LowbrainPlayer> others = null;

                        if(plugin.useParties){
                            others = new ArrayList<>();
                            Party party = Parties.getInstance().getPlayerHandler().getPartyFromPlayer(rpKiller.getPlayer());
                            if(party != null){
                                for (Player p :
                                        party.getOnlinePlayers()) {
                                    if (!p.equals(rpKiller.getPlayer())){
                                        if(Settings.getInstance().getGroupXpRange() == -1
                                                || Helper.getDistanceBetweenTwoPlayers(rpKiller.getPlayer(),p) <= Settings.getInstance().getGroupXpRange()){
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
                            others = Helper.getNearbyPlayers(rpKiller, Settings.getInstance().getGroupXpRange());
                        }
                        double mainXP = others != null && others.size() > 0 ? Settings.getInstance().getGroupXpMain() : 1;
                        rpKiller.addExp(xp * mainXP);
                        plugin.debugInfo("              Killer gained : " + ( xp * mainXP ) +" xp!");

                        if(others != null && others.size() > 0) {
                            double othersXP = xp * Settings.getInstance().getGroupXpOthers() / others.size();
                            plugin.debugInfo("              - In party");
                            for (LowbrainPlayer other : others) {
                                other.addExp(othersXP);
                                plugin.debugInfo("                      " + other.getPlayer().getName() + " gained " + othersXP + " xp!");
                            }
                        }
                    }
                    else{
                        rpKiller.addExp(xp);
                        plugin.debugInfo("              Killer gained : " + ( xp ) +" xp!");;
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
        plugin.debugInfo("************* On Player Attack (EntityDamageByEntityEvent) **************");
        MutableBoolean isCritical = new MutableBoolean(false);
        MutableFloat missChance = new MutableFloat(0F);
        boolean isMissed = false;
        boolean off = applyOffensiveAttack(e,isCritical, missChance);
        boolean deff = applyDefensive(e, missChance);

        double rdm = Math.random();

        plugin.debugInfo("              Missing chance : " + missChance.floatValue());
        if(rdm < missChance.floatValue()){
            plugin.debugInfo("              Attack was missed !");
            e.setDamage(0);
        }

        if(off){
            //CREATING DAMAGE HOLOGRAM
            if(plugin.useHolographicDisplays){
                NumberFormat formatter = new DecimalFormat("#0.00");
                Location loc = e.getEntity().getLocation().add(0,2,0);
                Hologram holoDamage = HologramsAPI.createHologram(plugin,loc);
                ChatColor color = isCritical.booleanValue() ? ChatColor.DARK_RED : ChatColor.RED;

                if(isMissed)
                    holoDamage.appendTextLine(color + "miss");
                else
                    holoDamage.appendTextLine(color + formatter.format(e.getFinalDamage()));


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
        plugin.debugInfo("************* ------------------------ **************");
    }

    /**
     * Called when a player get damaged
     * @param e
     */
    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent e){

        if(!(e.getEntity() instanceof Player))
            return;

        if(e.getDamage() <= 0 )
            return;

        LowbrainPlayer damagee = plugin.getPlayerHandler().get(e.getEntity().getUniqueId());

        if(damagee == null)
            return;

        float multiplier = 1;

        plugin.debugInfo("************* On Player get Damaged **************");
        plugin.debugInfo("              Damage caused by : " +e.getCause().name());
        plugin.debugInfo("              Initial damage : " +e.getDamage());

        switch (e.getCause()){
            case BLOCK_EXPLOSION:
                multiplier = damagee.getMultipliers().getDamagedByExplosion();
                break;
            case CONTACT:
                multiplier = damagee.getMultipliers().getDamagedByContact();
                break;
            case DRAGON_BREATH:
                multiplier = damagee.getMultipliers().getDamagedByFire();
                break;
            case DROWNING:
                multiplier = damagee.getMultipliers().getDamagedByDrowning();
                break;
            case ENTITY_EXPLOSION:
                multiplier = damagee.getMultipliers().getDamagedByExplosion();
                break;
            case FALL:
                multiplier = damagee.getMultipliers().getDamagedByFall();
                break;
            case FALLING_BLOCK:
                multiplier = damagee.getMultipliers().getDamagedByWeapon();
                break;
            case FIRE:
                multiplier = damagee.getMultipliers().getDamagedByFire();
                break;
            case FIRE_TICK:
                multiplier = damagee.getMultipliers().getDamagedByFireTick();
                break;
            case FLY_INTO_WALL:
                multiplier = damagee.getMultipliers().getDamagedByFlyIntoWall();
                break;
            case HOT_FLOOR:
                multiplier = damagee.getMultipliers().getDamagedByHotFloor();
                break;
            case LAVA:
                multiplier = damagee.getMultipliers().getDamagedByLava();
                break;
            case LIGHTNING:
                multiplier = damagee.getMultipliers().getDamagedByLightning();
                break;
            case MAGIC:
                multiplier = damagee.getMultipliers().getDamagedByMagic();
                break;
            case MELTING:
                multiplier = damagee.getMultipliers().getDamagedByMagic();
                break;
            case POISON:
                multiplier = damagee.getMultipliers().getDamagedByPoison();
                break;
            case STARVATION:
                multiplier = damagee.getMultipliers().getDamagedByStarvation();
                break;
            case SUFFOCATION:
                multiplier = damagee.getMultipliers().getDamagedBySuffocation();
                break;
            case THORNS:
                multiplier = damagee.getMultipliers().getDamagedByWeapon();
                break;
            case VOID:
                multiplier = damagee.getMultipliers().getDamagedByVoid();
                break;
            case WITHER:
                multiplier = damagee.getMultipliers().getDamagedByWither();
                break;
            default:
                multiplier = damagee.getMultipliers().getDamagedByDefault();
                break;
        }

        plugin.debugInfo("              Defensive damage multiplier : " + multiplier);
        e.setDamage(e.getDamage() * multiplier);
        plugin.debugInfo("              Damage after multiplier : " + e.getDamage());
        plugin.debugInfo("************* ------------------------ **************");
    }

    /**
     * When a player consume a potion
     * @param e
     */
    @EventHandler
    public void onPlayerConsumePotion(PlayerItemConsumeEvent e){
        if(e.getItem() != null && e.getItem().getType().equals(Material.POTION) && Settings.getInstance().getParameters().getOnPlayerConsumePotion().isEnabled()){
            LowbrainPlayer rp = plugin.getPlayerHandler().get(e.getPlayer().getUniqueId());
            if(rp != null && !rp.getPlayer().getActivePotionEffects().isEmpty()) {

                float result = rp.getMultipliers().getConsumedPotionMultiplier();

                PotionEffect po = (PotionEffect) rp.getPlayer().getActivePotionEffects().toArray()[rp.getPlayer().getActivePotionEffects().size() -1];

                int newDuration = (int) (po.getDuration() * result);
                PotionEffect tmp = new PotionEffect(po.getType(), newDuration, po.getAmplifier());
                rp.getPlayer().removePotionEffect(po.getType());
                rp.getPlayer().addPotionEffect(tmp);

                plugin.debugInfo("************* On Player Consume Potion **************");
                plugin.debugInfo("              Duration multiplied by : " + result);
                plugin.debugInfo("************* ------------------------ **************");
            }
        }
    }

    /**
     * called when a player shoots with a  bow
     * @param e
     */
    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent e){
        if(e.getEntity() instanceof Player && Settings.getInstance().getParameters().getOnPlayerShootBow().isEnable()){
            //set new force
            Arrow ar = (Arrow) e.getProjectile();
            LowbrainPlayer rpPlayer = plugin.getPlayerHandler().get(e.getEntity().getUniqueId());

            plugin.debugInfo("************* On Player Consume Potion **************");

            float speed = rpPlayer.getMultipliers().getBowArrowSpeed();

            plugin.debugInfo("              Arrow speed multiplier : " + speed);
            ar.setVelocity(ar.getVelocity().multiply(speed));

            float precX = rpPlayer.getMultipliers().getBowPrecision();

            int direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precX = 1 + (1-precX)*direction;

            float precY = rpPlayer.getMultipliers().getBowPrecision();

            direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precY = 1 + (1-precY)*direction;

            float precZ = rpPlayer.getMultipliers().getBowPrecision();

            direction = Helper.randomInt(0,1) == 0 ? -1 : 1;
            precZ = 1 + (1-precZ)*direction;

            plugin.debugInfo("              Arrow precision multiplier : " + precX);
            ar.setVelocity(new Vector(ar.getVelocity().getX() * precX, ar.getVelocity().getY() * precY, ar.getVelocity().getZ() * precZ));

            if(rpPlayer.getCurrentSkill() != null
                    && (rpPlayer.getCurrentSkill().getEventType().equals("bow_shoot") || rpPlayer.getCurrentSkill().getEventType().equals("both"))
                    && rpPlayer.getCurrentSkill().executeBowSkill(rpPlayer,ar,speed) ){

            }
            plugin.debugInfo("************* ------------------------ **************");
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

    // PRIVATES

    /**
     * create damaging effect depending on player attributes
     * @param p
     * @return potion effect
     */
    @Nullable
    private PotionEffect createMagicAttack(LowbrainPlayer p){
        int rdm = Helper.randomInt(1,7);
        int duration = 0;
        int amplifier = 0;

        float max = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().duration.getMax();
        float min = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().duration.getMin();

        PotionEffect effect;
        PotionEffectType type = PotionEffectType.POISON;
        boolean valid = true;
        switch (rdm){
            case 1:
                type = PotionEffectType.BLINDNESS;
                amplifier = 0;
                valid = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().blindness.isEnabled();
                break;
            case 2:
                type = PotionEffectType.CONFUSION;
                amplifier = 0;
                valid = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().confusion.isEnabled();
                break;
            case 3:
                type = PotionEffectType.HARM;
                max =  Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().duration.getMin();
                min = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().duration.getMax();
                amplifier = (int) Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().harm.compute(p);
                valid = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().harm.isEnabled();
                break;
            case 4:
                type = PotionEffectType.POISON;
                amplifier = (int) Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().poison.compute(p);
                valid = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().poison.isEnabled();
                break;
            case 5:
                type = PotionEffectType.SLOW;
                amplifier = (int) Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().slow.compute(p);
                valid = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().slow.isEnabled();
                break;
            case 6:
                type = PotionEffectType.WEAKNESS;
                amplifier = (int) Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().weakness.compute(p);
                valid = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().weakness.isEnabled();
                break;
            case 7:
                type = PotionEffectType.WITHER;
                amplifier = (int) Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().wither.compute(p);
                valid = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().wither.isEnabled();
                break;
        }

        if(!valid) return null;

        // with harm we need to change those values before computing
        Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().duration.setMax(max);
        Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().duration.setMin(min);

        duration = (int)Math.ceil(Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().duration.computeWith(p, min, max));

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

        float rdm = rp.getMultipliers().getReducingPotionEffect();
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
        plugin.debugInfo("------------- Reducing Bad Potion Effect --------------");
        plugin.debugInfo("              Effects reduced by : " + rdm);
        plugin.debugInfo("-------------------------------------------------------");
    }

    /**
     * Set the server difficulty depending on average connectedplayer level
     */
    private void setServerDifficulty(){

        if(!Settings.getInstance().isAsdEnable())return;

        Difficulty diff = Difficulty.NORMAL;
        int averageLevel = plugin.getPlayerHandler().getAverageLevel();

        if(averageLevel >= Settings.getInstance().getAsdEasyFrom() && averageLevel <= (Settings.getInstance().getAsdEasyTo() != -1 ? Settings.getInstance().getAsdEasyTo() : 9999999)){
            diff = Difficulty.EASY;
        }
        else if(averageLevel >= Settings.getInstance().getAsdMediumFrom() && averageLevel <= (Settings.getInstance().getAsdMediumTo() != -1 ? Settings.getInstance().getAsdMediumTo() : 9999999)){
            diff = Difficulty.NORMAL;
        }
        else if(averageLevel >= Settings.getInstance().getAsdHardFrom() && averageLevel <= (Settings.getInstance().getAsdHardTo() != -1 ? Settings.getInstance().getAsdHardTo() : 9999999)){
            diff = Difficulty.HARD;
        }


        for (World world :
                this.plugin.getServer().getWorlds()) {
            world.setDifficulty(diff);
        }
        plugin.debugInfo("************* Server Difficulty Reset **************");
        this.plugin.debugInfo("         Set to : " + diff.name());
        plugin.debugInfo("************* ------------------------ **************");
    }

    /**
     * apply deffensive multipler to damage from event
     * @param e EntityDamageByEntityEvent
     * @param missChance mutable float, value of missing chance
     * @return true if multiplier was applied
     */
    private boolean applyDefensive(EntityDamageByEntityEvent e, MutableFloat missChance){
        if( !(e.getEntity() instanceof Player))
            return false;

        LowbrainPlayer damagee = plugin.getPlayerHandler().getList().get(e.getEntity().getUniqueId());

        if(damagee == null)
            return false;

        plugin.debugInfo("------------- Applying Defense ---------------");

        double multiplier = 1;
        boolean damageSet = false;

        if(e.getDamager() instanceof Arrow){
            multiplier = damagee.getMultipliers().getDamagedByArrow();
            damageSet = true;
        }

        else if(e.getDamager() instanceof ThrownPotion){
            if(Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByMagic().isEnabled()){
                multiplier = damagee.getMultipliers().getDamagedByMagic();
                damageSet = true;
            }
            plugin.debugInfo("              Damage from magic projectile reduced by : " + multiplier);
            float chanceOfRemovingEffect = -1F;

            if(Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfRemovingMagicEffect().isEnabled()){
                chanceOfRemovingEffect = damagee.getMultipliers().getChanceOfRemovingPotionEffect();
                plugin.debugInfo("              Chance of removing effect : " + chanceOfRemovingEffect);
            }
            double rdm = Math.random();
            if(rdm < chanceOfRemovingEffect){
                removeBadPotionEffect(damagee.getPlayer());
                plugin.debugInfo("              All effect removed");
            }
            else if(Settings.getInstance().getParameters().getOnPlayerGetDamaged().getReducingBadPotionEffect().isEnabled()){
                reducingBadPotionEffect(damagee);
            }
        }

        else if(e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof Creeper)){
            if(Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWeapon().isEnabled()){
                multiplier = damagee.getMultipliers().getDamagedByWeapon();
            }
        }

        else if(e.getDamager() instanceof Projectile){
            Projectile projectile = (Projectile) e.getDamager();

            if(projectile.getShooter() instanceof Player && Helper.StringIsNullOrEmpty(projectile.getCustomName())){
                if(Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByMagic().isEnabled()){
                    multiplier = damagee.getMultipliers().getDamagedByMagic();
                    damageSet = true;
                }
                plugin.debugInfo("              Damage from magic projectile reduced by : " + multiplier);
                float chanceOfRemovingEffect = -1F;

                if(Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfRemovingMagicEffect().isEnabled()){
                    chanceOfRemovingEffect = damagee.getMultipliers().getChanceOfRemovingPotionEffect();
                    plugin.debugInfo("              Chance of removing effects : " + chanceOfRemovingEffect);
                }

                double rdm = Math.random();
                if(rdm < chanceOfRemovingEffect){
                    removeBadPotionEffect(damagee.getPlayer());
                    plugin.debugInfo("              All effects removed");
                }
                else if(Settings.getInstance().getParameters().getOnPlayerGetDamaged().getReducingBadPotionEffect().isEnabled()){
                    reducingBadPotionEffect(damagee);
                }

                e.setDamage(e.getDamage() * multiplier);
            }
            else if(Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByProjectile().isEnabled()){
                multiplier = damagee.getMultipliers().getDamagedByProjectile();
                damageSet = true;
            }

        }

        if(damagee != null && Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfDodging().isEnabled()){
            float oldChance = missChance.floatValue();
            float newChance = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getChanceOfMissing().isEnabled() ?
                    oldChance * damagee.getMultipliers().getChanceOfDodging() : damagee.getMultipliers().getChanceOfDodging();
            missChance.setValue(newChance);
        }

        if(!damageSet)
            return damageSet;



        plugin.debugInfo("              Initial damage : " + e.getDamage());
        e.setDamage(e.getDamage() * multiplier);
        plugin.debugInfo("              Defensive damage multiplier : " + multiplier);
        plugin.debugInfo("              Damage after defensive multiplier : " + e.getDamage());
        plugin.debugInfo("-------------------------------------------------");
        return damageSet;
    }

    /**
     * apply offensive multiplier to damage from event
     * @param e EntityDamageByEntityEvent
     * @param isCritical set to true if the hit was critical
     * @param missChance mutable float, value of missing chance
     * @return true if multiplier was applied
     */
    private boolean applyOffensiveAttack(EntityDamageByEntityEvent e, MutableBoolean isCritical, MutableFloat missChance){

        plugin.debugInfo("------------- Applying Offensive Attack ------------------");
        EventSource eventSource = EventSource.getFromAttack(e);

        float absorbDamage = 0F;

        double oldDamage = e.getDamage();

        if (eventSource == null || eventSource.damager == null)
            return false;

        if (eventSource.skill != null) {
            for (Map.Entry<String, String> effect :
                    eventSource.skill.getEffects().entrySet()) {

                PotionEffect po = null;

                switch (effect.getKey()){
                    case "poison":
                        po = new PotionEffect(PotionEffectType.WITHER,(int)(eventSource.skill.getEffectValue(effect.getValue()) *20),eventSource.skill.getCurrentLevel(),true,true);
                        break;
                    case "fire_tick":
                        e.getEntity().setFireTicks((int)(eventSource.skill.getEffectValue(effect.getValue()) * 20));
                        break;
                    case "freeze":
                        po = new PotionEffect(PotionEffectType.SLOW,(int)(eventSource.skill.getEffectValue(effect.getValue()) *20),eventSource.skill.getCurrentLevel(),true,true);
                        break;
                    case "absorb":
                        absorbDamage = eventSource.skill.getEffectValue(effect.getValue());
                        break;
                    case "knockback":
                        e.getEntity().setVelocity(((LivingEntity) e.getEntity()).getEyeLocation().getDirection().multiply(-1 * eventSource.skill.getEffectValue(effect.getValue())));
                        break;
                    case "lightning":
                        e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                        break;
                    case "damage":
                        ((LivingEntity) e.getEntity()).damage(eventSource.skill.getEffectValue(effect.getValue()));
                        break;
                }
                if(po != null){
                    po.apply((LivingEntity)e.getEntity());
                }

                plugin.debugInfo("              skilled attack effect : " + effect.getKey());
            }
        }

        if (eventSource.source == EventSourceEnum.MAGIC_PROJECTILE && eventSource.staffSection != null) {
            double baseDamage = eventSource.staffSection.getDouble("base_damage",-1);
            String effect = eventSource.staffSection.getString("effect","");
            int effectDuration = eventSource.staffSection.getInt("effect_duration",3);
            plugin.debugInfo("              ---- Attacked by magic projectile : " + effect);

            if(baseDamage >= 0) {
                e.setDamage(baseDamage);
                oldDamage = baseDamage;
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

        //CHECK IF PLAYER CAN USE THE ITEM IN HAND
        if(eventSource.damager.getPlayer().getInventory().getItemInMainHand() != null){
            if(!eventSource.damager.canEquipItem(eventSource.damager.getPlayer().getInventory().getItemInMainHand())){
                e.setCancelled(true);
                plugin.debugInfo("              ---- Player can't use this item !");
                return false;
            }
        }

        //APPLYING MAGIC EFFECT BY ATTACKER
        if(eventSource.source != EventSourceEnum.MAGIC_PROJECTILE
                && eventSource.source != EventSourceEnum.MAGIC
                && Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCreatingMagicAttack().enable){

            plugin.debugInfo("              From : " + eventSource.damager.getPlayer().getName());
            double chanceOfMagicEffect = eventSource.damager.getMultipliers().getChanceOfMagicEffect();
            plugin.debugInfo("              Chance of performing magic attack : " + chanceOfMagicEffect);
            double rdm = Math.random();
            if(rdm < chanceOfMagicEffect){
                PotionEffect effect = createMagicAttack(eventSource.damager);
                if(e.getEntity() instanceof LivingEntity && effect != null){
                    ((LivingEntity) e.getEntity()).addPotionEffect(effect);
                    plugin.debugInfo("              Magic effect added : " + effect.getType().getName() + ", " + effect.getDuration()/20 + ", " + effect.getAmplifier());
                }
            }
        }

        //APPLYING DAMAGE CHANGE DEPENDING ON OFFENCIVE ATTRIBUTES
        if(eventSource.source == EventSourceEnum.ARROW && Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().projectile.isEnabled()){
            e.setDamage(
                    Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().projectile.randomizeFromValue(
                            eventSource.damager.getMultipliers().getAttackByProjectile() * (float)e.getDamage()
                    )
            );
        }

        else if(eventSource.source == EventSourceEnum.NORMAL && Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().weapon.isEnabled()){
            e.setDamage(
                    Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().weapon.randomizeFromValue(
                            eventSource.damager.getMultipliers().getAttackByWeapon() * (float)e.getDamage()
                    )
            );
        }

        else if((eventSource.source == EventSourceEnum.MAGIC
                || eventSource.source == EventSourceEnum.MAGIC_PROJECTILE)
                && Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().magic.isEnabled()){
            e.setDamage(
                    Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().magic.randomizeFromValue(
                            eventSource.damager.getMultipliers().getAttackByMagic() * (float)e.getDamage()
                    )
            );
        }

        //applying skilled attack if necessary
        if(eventSource.source == EventSourceEnum.NORMAL && eventSource.damager.getCurrentSkill() != null
                && (eventSource.damager.getCurrentSkill().getEventType().equals("attack_entity") || eventSource.damager.getCurrentSkill().getEventType().equals("both"))
                && e.getEntity() instanceof LivingEntity
                && eventSource.damager.getCurrentSkill().executeWeaponAttackSkill(eventSource.damager,(LivingEntity) e.getEntity(),e.getFinalDamage())){

        }

        if(Settings.getInstance().getParameters().getOnPlayerAttackEntity().getChanceOfMissing().isEnabled()) {
            missChance.setValue(eventSource.damager.getMultipliers().getChanceOfMissing());
        }

        if(Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCriticalHit().enable){
            double rdm = Math.random();
            float chance = eventSource.damager.getMultipliers().getCriticalHitChance();
            plugin.debugInfo("              Chance of performing a critical hit : " + chance);
            if(rdm < chance){
                isCritical.setValue(true);
                float criticalHitMultiplier = eventSource.damager.getMultipliers().getCriticalHitMultiplier();
                plugin.debugInfo("              Critical hit multiplier : " + criticalHitMultiplier);
                e.setDamage(e.getDamage() * criticalHitMultiplier);
            }
        }

        if(Settings.getInstance().getParameters().getOnPlayerAttackEntity().getBackStab().isEnabled()){
            Vector attackerDirection = e.getDamager().getLocation().getDirection();
            // Vector attackerDirection = damager.getPlayer().getLocation().getDirection();
            Vector victimDirection = e.getEntity().getLocation().getDirection();
            //determine if the dot product between the vectors is greater than 0
            if (attackerDirection.dot(victimDirection) > 0) {
                //player was backstabbed.}
                float bs = eventSource.damager.getMultipliers().getBackStabMultiplier();
                e.setDamage(e.getDamage() * bs);
                plugin.debugInfo("              Backstap multiplier : " + bs);
            }
        }

        if(absorbDamage > 0){
            eventSource.damager.getPlayer().setHealth(eventSource.damager.getPlayer().getHealth() + absorbDamage);
        }

        plugin.debugInfo("              Initial damage : " + oldDamage);
        double damageMultiplier = e.getDamage() / oldDamage;
        plugin.debugInfo("              Offencive damage multiplier : " + damageMultiplier);
        plugin.debugInfo("              Damage after offensive multiplier : " + e.getDamage());
        plugin.debugInfo("-------------------------------------------------------");
        return true;
    }

    private void onPlayerUseStaff(LowbrainPlayer rp, ConfigurationSection staffSection, ItemStack item) {
        if (item == null || item.getItemMeta() == null)
            return;

        ItemMeta iMeta = item.getItemMeta();
        Boolean gravity = staffSection.getBoolean("gravity");
        double speed = staffSection.getDouble("speed");
        int maxDurability = staffSection.getInt("durability");
        int durability = maxDurability;
        int cooldown = staffSection.getInt("cooldown",0);
        String sDurability = "";
        String sLastUsed = "";
        Calendar lastUsed = Calendar.getInstance();
        lastUsed.add(Calendar.SECOND,-cooldown - 1);
        String n = iMeta.getDisplayName().substring(2);

        if(iMeta.hasLore()){

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
            rp.getPlayer().getInventory().remove(item);
            rp.getPlayer().updateInventory();
            rp.sendMessage(Internationalization.format("item_destroyed"),ChatColor.GRAY);
        }
        else{
            if(durability <= 10)rp.sendMessage(Internationalization.format("only_10_cast_left"));

            List<String> lores = iMeta.getLore();
            lores.remove(lores.size() - 1);
            lores.remove(lores.size() - 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            lores.add("last_used : "  + sdf.format(Calendar.getInstance().getTime()));
            lores.add("durability : " + durability);
            iMeta.setLore(lores);
            item.setItemMeta(iMeta);
        }
    }
}
