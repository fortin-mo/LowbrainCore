package lowbrain.mcrpg.main;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import lowbrain.mcrpg.commun.RPGPlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class PlayerListener implements Listener {

	public static Main plugin;

    public PlayerListener(Main instance) {
        plugin = instance;
    }

    /**
     * called when a player experience changes naturally
     * @param e
     */
    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent e){
        Player p = e.getPlayer();
        RPGPlayer rp = plugin.connectedPlayers.get(p.getUniqueId());
        rp.addExp(e.getAmount());
    }


    /**
     * called when a player dies
     * @param e
     */

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(e.getEntity() instanceof Player){
            RPGPlayer rpKiller = null;
            Player killed = e.getEntity();
            RPGPlayer rpKilled = plugin.connectedPlayers.get(killed.getUniqueId());
            if(killed.getKiller() != null) {
                Player killer = killed.getKiller();
                rpKiller = plugin.connectedPlayers.get(killer.getUniqueId());
            }
            else if(killed.getLastDamageCause() != null){
                if(killed.getLastDamageCause().getEntity() instanceof Arrow){
                    if(((Arrow) killed.getLastDamageCause().getEntity()).getShooter() != null && ((Arrow) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get((((Player) ((Arrow) killed.getLastDamageCause().getEntity()).getShooter()).getPlayer().getUniqueId()));
                    }
                }
                else if(killed.getLastDamageCause().getEntity() instanceof TippedArrow){
                    if(((TippedArrow) killed.getLastDamageCause().getEntity()).getShooter() != null && ((TippedArrow) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get((((Player) ((TippedArrow) killed.getLastDamageCause().getEntity()).getShooter()).getPlayer().getUniqueId()));
                    }
                }
                else if(killed.getLastDamageCause().getEntity() instanceof SpectralArrow){
                    if(((SpectralArrow) killed.getLastDamageCause().getEntity()).getShooter() != null && ((SpectralArrow) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get((((Player) ((SpectralArrow) killed.getLastDamageCause().getEntity()).getShooter()).getPlayer().getUniqueId()));
                    }
                }
                else if(killed.getLastDamageCause().getEntity() instanceof ThrownPotion){
                    if(((ThrownPotion) killed.getLastDamageCause().getEntity()).getShooter() != null && ((ThrownPotion) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get((((Player) ((ThrownPotion) killed.getLastDamageCause().getEntity()).getShooter()).getPlayer().getUniqueId()));
                    }
                }
            }

            if(rpKiller != null){
                double diffLvl = Math.abs(rpKilled.getLvl() - rpKiller.getLvl());
                rpKiller.addKills(1);
                double xpGained = 0.0;
                if(diffLvl == 0){
                    xpGained = plugin.settings.getExp_on_player_kill() * rpKiller.getLvl() * 0.5;
                }else if(rpKilled.getLvl() < rpKiller.getLvl()){
                    xpGained = plugin.settings.getExp_on_player_kill() / (diffLvl * 0.5) * rpKiller.getLvl() * 0.5;
                }else{
                    xpGained = plugin.settings.getExp_on_player_kill() * (diffLvl * 0.5) * rpKiller.getLvl() * 0.5;
                }
                rpKiller.addExp(xpGained);

            }

            rpKilled.addExp(-(plugin.settings.getExp_loss_on_death() / 100 * rpKilled.getExperience()));
            rpKilled.addDeaths(1);
        }
    }

    public void onPlayerItemHeld(PlayerItemHeldEvent e){
        Player p = e.getPlayer();
        RPGPlayer rp = plugin.connectedPlayers.get(p.getUniqueId());
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
        RPGPlayer damagee = null;

        boolean magicAttack = false;
        boolean arrowAttact = false;
        boolean normalAttack = false;

        double range = 2;

        plugin.debugMessage("inital damage : " + e.getDamage());

        //DEFINING CAUSE OF DAMAGE
        if (e.getDamager() instanceof Player) {
            damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            normalAttack  = true;
        } else if (e.getDamager() instanceof Arrow) {
            Arrow ar = (Arrow) e.getDamager();
            if (ar.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            }
            arrowAttact = true;
        } else if (e.getDamager() instanceof TippedArrow) {
            TippedArrow ar = (TippedArrow) e.getDamager();
            if (ar.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            }
            arrowAttact = true;
        } else if (e.getDamager() instanceof SpectralArrow) {
            SpectralArrow ar = (SpectralArrow) e.getDamager();
            if (ar.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            }
            arrowAttact = true;
        } else if (e.getDamager() instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) e.getDamager();
            if (pot.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            }
            magicAttack = true;
        }
        else{
            normalAttack  = true;
        }


        //APLLYING MAGIC EFFECT BY ATTACKER
        if(damager != null && !magicAttack){
            double chanceOfMagicEffect = damager.getIntelligence() * 0.5 /100;
            double rdm = Math.random();
            if(rdm < chanceOfMagicEffect){
                PotionEffect effect = CreateMagicAttack(damager);
                if(e.getEntity() instanceof LivingEntity){
                    ((LivingEntity) e.getEntity()).addPotionEffect(effect);
                    plugin.debugMessage("magic effect added");
                }
            }
        }


        //APPLYING DAMAGE CHANGE DEPENDING ON OFFENCIVE ATTRIBUTES
        if(arrowAttact && damager != null){
            double baseDamage = e.getDamage() * (damager.getDexterity() + (damager.getStrength() * 0.5)) * 0.025;
            double rdm = (baseDamage - range) + (Math.random() * (baseDamage + range));
            e.setDamage(rdm);
        }
        else if(normalAttack && damager != null){
            double baseDamage = e.getDamage() * damager.getStrength() * 0.025;
            double rdm = (baseDamage - range) + (Math.random() * (baseDamage + range));
            e.setDamage(rdm);
        }
        else if(magicAttack && damager != null){
            double baseDamage = e.getDamage() * (damager.getIntelligence() + damager.getDexterity() * 0.25) * 0.025;
            double rdm = (baseDamage - range) + (Math.random() * (baseDamage + range));
            e.setDamage(rdm);
        }

        //APPYING DAMAGE CHANGE DEPENDING ON DEFENCIVE ATTRIBUTES
        if(e.getEntity() instanceof Player){
            damagee = plugin.connectedPlayers.get(e.getEntity().getUniqueId());
            if(arrowAttact || normalAttack){
                e.setDamage(e.getDamage() / (0.75 + (damagee.getDefence())/50));
            }
            else if(magicAttack){
                e.setDamage(e.getDamage() / (0.75 + ((damagee.getMagicResistance() + (damagee.getIntelligence() * 0.5)))/50));
            }

            double changeOfRemovingEffect = (damagee.getMagicResistance()*0.333 +damagee.getIntelligence() * 0.125) /200;
            double rdm = Math.random();
            if(rdm < changeOfRemovingEffect){
                RemoveBadPotionEffect(damagee.getPlayer());
                plugin.debugMessage("all effect removed");
            }
            else{
                ReducingBadPotionEffect(damagee);
            }
        }
        plugin.debugMessage("final damage : " + e.getDamage());
    }

    /**
     * called when a player shoots with a  bow
     * @param e
     */
    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent e){
        if(e.getEntity() instanceof Player){
            //set new force
            Arrow ar = (Arrow) e.getProjectile();
            RPGPlayer rpPlayer = plugin.connectedPlayers.get(e.getEntity().getUniqueId());
            //plugin.debugMessage("inital fall distance : " + ar.getFallDistance());
            //ar.setFallDistance( (float)(ar.getFallDistance() * rpPlayer.getStrength() / 100));
            //plugin.debugMessage("inital fall distance : " + ar.getFallDistance());

            if(rpPlayer.getDexterity() < 50) {
                Double precision = rpPlayer.getDexterity() / 50;
                //ex: with 10dext : rdm between 0.2 and 1.8
                //ex: with 50dext : rdm between 1 and 1 = 100%
                double rdm = precision + (Math.random() * (2 - precision));
                ar.setVelocity(new Vector(ar.getVelocity().getX() * rdm, ar.getVelocity().getY() * rdm, ar.getVelocity().getZ() * rdm));
            }
        }
    }


    /**
     * called when a player join the server
     * @param e
     * @throws IOException
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws IOException{
    	Player p = e.getPlayer();
        File userdata = new File(plugin.getDataFolder(), File.separator + "PlayerDB");
        File f = new File(userdata, File.separator + p.getUniqueId() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

        //When the player file is created for the first time...
        if (!f.exists()) { 
            playerData.createSection("Class");
            playerData.set("Class.isSet", false);
            playerData.set("Class.id", "");
            
            playerData.createSection("Stats");
            playerData.set("Stats.health", 0);
            playerData.set("Stats.lvl", 1);
            playerData.set("Stats.strength", 0);
            playerData.set("Stats.intelligence", 0);
            playerData.set("Stats.dexterity", 0);
            playerData.set("Stats.defence", 0);
            playerData.set("Stats.magicResistance", 0);
            playerData.set("Stats.points", plugin.settings.getStarting_points());
            playerData.set("Stats.experience", 0);
            playerData.set("Stats.nextLvl",plugin.settings.getFirst_lvl_exp());
            playerData.set("Stats.kills",0);
            playerData.set("Stats.deaths",0);
            
            playerData.save(f);
        }
        plugin.connectedPlayers.put(p.getUniqueId(),new RPGPlayer(p));
    }

    /**
     * called when a player quit the server
     * @param e
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
    	Player p = e.getPlayer();
        plugin.connectedPlayers.get(p.getUniqueId()).SaveData();
        plugin.connectedPlayers.remove(p.getUniqueId());
    }

    /**
     * create damaging effect depending on player attributes
     * @param p
     * @return
     */
    private PotionEffect CreateMagicAttack(RPGPlayer p){
        int rdm = 1 + (int)(Math.random() * 7);
        int duration = (int)(0.2 * (p.getIntelligence() * 0.75 + p.getDexterity() * 0.25) + 1);
        int amplifier = (int)(0.2 * p.getIntelligence() + 1);
        PotionEffect effect;
        PotionEffectType type = PotionEffectType.POISON;
        switch (rdm){
            case 1:
                type = PotionEffectType.BLINDNESS;
                break;
            case 2:
                type = PotionEffectType.CONFUSION;
                break;
            case 3:
                type = PotionEffectType.HARM;
                break;
            case 4:
                type = PotionEffectType.POISON;
                break;
            case 5:
                type = PotionEffectType.SLOW;
                break;
            case 6:
                type = PotionEffectType.WEAKNESS;
                break;
            case 7:
                type = PotionEffectType.WITHER;
                break;
        }

        effect = new PotionEffect(type, duration * 20, amplifier, true,true, Color.SILVER);
        return effect;
    }

    private PotionEffect CreateMagicDefence(RPGPlayer p){
        return null;
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
        double reduction = -0.01 * (rp.getMagicResistance()*0.75 + rp.getIntelligence() * 0.25) + 1;
        double minReduction = reduction < 0.84 ? reduction + 0.15 : 0.99;
        double rdm = reduction + (Math.random() * minReduction);//top lvl will have reduction of 99%

        for (PotionEffect pe : rp.getPlayer().getActivePotionEffects()) {
            int newDuration = (int)(pe.getDuration() * rdm);
            int newAmplifier = (int)(pe.getAmplifier() * rdm);
            PotionEffect tmp = new PotionEffect(pe.getType(),newDuration,newAmplifier);
            rp.getPlayer().removePotionEffect(pe.getType());
            rp.getPlayer().addPotionEffect(tmp);
        }

    }
}
