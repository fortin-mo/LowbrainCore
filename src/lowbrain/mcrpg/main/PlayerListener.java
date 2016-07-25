package lowbrain.mcrpg.main;

import lowbrain.mcrpg.commun.Helper;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import lowbrain.mcrpg.rpg.RPGPlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class PlayerListener implements Listener {

	public static Main plugin;
    private static int max_stats;

    public PlayerListener(Main instance) {
        plugin = instance;
        max_stats = plugin.config.max_stats <= 0 ? 100 : plugin.config.max_stats;
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
            if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) && plugin.config.math.damaged_by_fall_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByFall(damagee);
            }

            //FIRE DAMAGE
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)) && plugin.config.math.damaged_by_fire_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByFire(damagee);
            }

            //EXPLOSION
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))&& plugin.config.math.damaged_by_explosion_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByExplosion(damagee);
            }

            //MAGIC POTION DAMAGE
            else if((e.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.WITHER)
                    || e.getCause().equals(EntityDamageEvent.DamageCause.POISON)) && plugin.config.math.damaged_by_magic_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByMagic(damagee);

                float changeOfRemovingEffect = -1F;

                if(plugin.config.math.chance_of_removing_magic_effect_enable){
                    changeOfRemovingEffect = getChangeOfRemovingPotionEffect(damagee);
                }

                double rdm = Math.random();
                if(rdm < changeOfRemovingEffect){
                    RemoveBadPotionEffect(damagee.getPlayer());
                    plugin.debugMessage("all effect removed");
                }
                else if(plugin.config.math.reducing_bad_potion_effect_enable){
                    ReducingBadPotionEffect(damagee);
                }
            }

            else if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && plugin.config.math.damaged_by_weapon_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByWeapon(damagee);
            }

            //ARROW
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) && plugin.config.math.damaged_by_projectile_enable){
                plugin.debugMessage("Damage caused by : " + e.getCause().name());
                multiplier = getDamagedByProjectile(damagee);
            }

            //CONTACT
            else if(e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT) && plugin.config.math.damaged_by_contact_enable){
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
        Player p = e.getPlayer();
        RPGPlayer rp = plugin.connectedPlayers.get(p.getUniqueId());
        plugin.debugMessage("Player gains " + e.getAmount() * plugin.config.math.natural_xp_gain_multiplier + " xp");
        rp.addExp(e.getAmount() * plugin.config.math.natural_xp_gain_multiplier);
    }

    /**
     * called when a player dies
     * @param e
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        if(e.getEntity() instanceof Player){

            plugin.debugMessage("Player dies !");

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
                        plugin.debugMessage("Killed by arrow!");
                    }
                }/*
                else if(killed.getLastDamageCause().getEntity() instanceof TippedArrow){
                    if(((TippedArrow) killed.getLastDamageCause().getEntity()).getShooter() != null && ((TippedArrow) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get((((Player) ((TippedArrow) killed.getLastDamageCause().getEntity()).getShooter()).getPlayer().getUniqueId()));
                        plugin.debugMessage("Killed by tipped arrow!");
                    }
                }
                else if(killed.getLastDamageCause().getEntity() instanceof SpectralArrow){
                    if(((SpectralArrow) killed.getLastDamageCause().getEntity()).getShooter() != null && ((SpectralArrow) killed.getLastDamageCause().getEntity()).getShooter() instanceof Player){
                        rpKiller = plugin.connectedPlayers.get((((Player) ((SpectralArrow) killed.getLastDamageCause().getEntity()).getShooter()).getPlayer().getUniqueId()));
                        plugin.debugMessage("Killed by spectral arrow!");
                    }
                }*/
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

            rpKilled.addExp(-(plugin.config.exp_loss_on_death / 100 * rpKilled.getExperience()));
            rpKilled.addDeaths(1);
        }
    }

    @EventHandler
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

        boolean magicAttack = false;
        boolean arrowAttact = false;
        boolean normalAttack = false;

        double oldDamage = e.getDamage();

        //DEFINING CAUSE OF DAMAGE
        if (e.getDamager() instanceof Player) {
            damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            plugin.debugMessage("Attacked by another player");
            normalAttack  = true;
        } else if (e.getDamager() instanceof Arrow) {
            Arrow ar = (Arrow) e.getDamager();
            if (ar.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            }
            plugin.debugMessage("Attacked by arrow");
            arrowAttact = true;
        } else if (e.getDamager() instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) e.getDamager();
            if (pot.getShooter() instanceof Player) {
                damager = plugin.connectedPlayers.get(e.getDamager().getUniqueId());
            }
            plugin.debugMessage("Attacked by potion");
            magicAttack = true;
        }
        else{
            normalAttack  = true;
        }


        //APLLYING MAGIC EFFECT BY ATTACKER
        if(damager != null && !magicAttack && plugin.config.math.creating_magic_attack_enable){
            plugin.debugMessage("From " + damager.getPlayer().getName());
            double chanceOfMagicEffect = Gradient(plugin.config.math.chance_of_creating_magic_attack_maximum,plugin.config.math.chance_of_removing_magic_effect_minimum)
                    * (damager.getIntelligence() * plugin.config.math.chance_of_creating_magic_attack_intelligence
                    + damager.getDexterity() * plugin.config.math.chance_of_creating_magic_attack_dexterity)
                    + plugin.config.math.chance_of_creating_magic_attack_minimum;

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
        if(arrowAttact && damager != null && plugin.config.math.attack_by_projectile_enable){
            e.setDamage(getAttackByProjectile(damager,e.getDamage()));
        }
        else if(normalAttack && damager != null && plugin.config.math.attack_by_weapon_enable){
            e.setDamage(getAttackByWeapon(damager,e.getDamage()));
        }
        else if(magicAttack && damager != null && plugin.config.math.attack_by_potion_enable){
            e.setDamage(getAttackByPotion(damager,e.getDamage()));
        }

        if(damager != null) {
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
        if(e.getItem() != null && e.getItem().getType().equals(Material.POTION) && plugin.config.math.on_player_consume_potion_enable){
            RPGPlayer rp = plugin.connectedPlayers.get(e.getPlayer().getUniqueId());
            if(rp != null) {

                float result = getConsumedPotionMultiplier(rp);

                PotionEffect po = (PotionEffect) rp.getPlayer().getActivePotionEffects().toArray()[rp.getPlayer().getActivePotionEffects().size() -1];

                int newDuration = (int) (po.getDuration() * result);
                PotionEffect tmp = new PotionEffect(po.getType(), newDuration, po.getAmplifier());
                rp.getPlayer().removePotionEffect(po.getType());
                rp.getPlayer().addPotionEffect(tmp);

                /*
                for (PotionEffect pe : rp.getPlayer().getActivePotionEffects()) {
                    int newDuration = (int) (pe.getDuration() * rdm);
                    PotionEffect tmp = new PotionEffect(pe.getType(), newDuration, pe.getAmplifier());
                    rp.getPlayer().removePotionEffect(pe.getType());
                    rp.getPlayer().addPotionEffect(tmp);
                }
                */
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
        if(e.getEntity() instanceof Player && plugin.config.math.on_player_shoot_bow_enable){
            //set new force
            Arrow ar = (Arrow) e.getProjectile();
            RPGPlayer rpPlayer = plugin.connectedPlayers.get(e.getEntity().getUniqueId());

            float speed = getBowArrowSpeed(rpPlayer);
            plugin.debugMessage("Arrow speed multiplier : " + speed);
            ar.setVelocity(ar.getVelocity().multiply(speed));
            float prec = getBowPrecision(rpPlayer);
            plugin.debugMessage("Arrow precision multiplier : " + prec);
            ar.setVelocity(new Vector(ar.getVelocity().getX() * prec, ar.getVelocity().getY() * prec, ar.getVelocity().getZ() * prec));
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
        int rdm = 1 + (int)(Math.random() * 7);
        int duration = 0;
        int amplifier = 0;
        float max = plugin.config.math.creating_magic_attack_maximum_duration;
        float min = plugin.config.math.creating_magic_attack_minimum_duration;

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
                max =  plugin.config.math.creating_magic_attack_minimum_duration;
                min = plugin.config.math.creating_magic_attack_maximum_duration;
                amplifier = (int)(Gradient(plugin.config.math.creating_magic_attack_maximum_harm_amplifier,plugin.config.math.creating_magic_attack_minimum_harm_amplifier)
                        * p.getIntelligence() * plugin.config.math.creating_magic_attack_intelligence_on_harm_amplifier);
                break;
            case 4:
                type = PotionEffectType.POISON;
                amplifier = (int)(Gradient(plugin.config.math.creating_magic_attack_maximum_poison_amplifier,plugin.config.math.creating_magic_attack_minimum_poison_amplifier)
                        * p.getIntelligence() * plugin.config.math.creating_magic_attack_intelligence_on_poison_amplifier);
                break;
            case 5:
                type = PotionEffectType.SLOW;
                amplifier = (int)(Gradient(plugin.config.math.creating_magic_attack_maximum_slow_amplifier,plugin.config.math.creating_magic_attack_minimum_slow_amplifier)
                        * p.getIntelligence() * plugin.config.math.creating_magic_attack_intelligence_on_slow_amplifier);
                break;
            case 6:
                type = PotionEffectType.WEAKNESS;
                amplifier = (int)(Gradient(plugin.config.math.creating_magic_attack_maximum_weakness_amplifier,plugin.config.math.creating_magic_attack_minimum_weakness_amplifier)
                        * p.getIntelligence() * plugin.config.math.creating_magic_attack_intelligence_on_weakness_amplifier);
                break;
            case 7:
                type = PotionEffectType.WITHER;
                amplifier = (int)(Gradient(plugin.config.math.creating_magic_attack_maximum_wither_amplifier,plugin.config.math.creating_magic_attack_minimum_wither_amplifier)
                        * p.getIntelligence() * plugin.config.math.creating_magic_attack_intelligence_on_wither_amplifier);
                break;
        }

        duration = (int)(Gradient(max,min)
                * (p.getIntelligence() * plugin.config.math.creating_magic_attack_intelligence_on_duration
                + p.getDexterity() * plugin.config.math.creating_magic_attack_dexterity_on_duration)
                + min);

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

    public static float Gradient(float max, float min){
        return (max - min)/max_stats;
    }

    //ON PLAYER GET DAMAGED

    public float getDamagedByFire(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.damaged_by_fire_function)){
            float max = plugin.config.math.damaged_by_fire_maximum;
            float min = plugin.config.math.damaged_by_fire_minimum;
            double x = damagee.getMagicResistance() * plugin.config.math.damaged_by_fire_magic_resistance
                    + damagee.getDefence() * plugin.config.math.damaged_by_fire_defence
                    + damagee.getIntelligence() * plugin.config.math.damaged_by_fire_intelligence;

            return (float)(Gradient(max,min) * x + min);
        }
        else{
            String[] st = plugin.config.math.damaged_by_fire_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public float getDamagedByContact(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.damaged_by_contact_function)){
            float max = plugin.config.math.damaged_by_contact_maximum;
            float min = plugin.config.math.damaged_by_contact_minimum;
            double x = damagee.getDefence() * plugin.config.math.damaged_by_contact_defence;

            return (float)(Gradient(max,min) * x + min);
        }
        else{
            String[] st = plugin.config.math.damaged_by_contact_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public float getDamagedByWeapon(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.damaged_by_weapon_function)){
            float max = plugin.config.math.damaged_by_weapon_maximum;
            float min = plugin.config.math.damaged_by_weapon_minimum;
            double x = damagee.getDefence() * plugin.config.math.damaged_by_weapon_defence;

            return (float)(Gradient(max,min) * x + min);
        }
        else{
            String[] st = plugin.config.math.damaged_by_weapon_function.split(",");
            if(st.length > 1){
                return Helper.eval( Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByMagic(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.damaged_by_magic_function)){
            float max = plugin.config.math.damaged_by_magic_maximum;
            float min = plugin.config.math.damaged_by_magic_minimum;
            double x = (damagee.getMagicResistance() * plugin.config.math.damaged_by_magic_magic_resistance
                    + damagee.getIntelligence() * plugin.config.math.damaged_by_magic_intelligence);

            return (float)(Gradient(max,min) * x + min);
        }
        else{
            String[] st = plugin.config.math.damaged_by_magic_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByFall(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.damaged_by_fall_function)){
            float max = plugin.config.math.damaged_by_fall_maximum;
            float min = plugin.config.math.damaged_by_fall_minimum;
            double x = damagee.getAgility() * plugin.config.math.damaged_by_fall_agility
                    + damagee.getDefence() * plugin.config.math.damaged_by_fall_defence;

            return (float)(Gradient(max,min) * x + min);
        }
        else{
            String[] st = plugin.config.math.damaged_by_fall_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByExplosion(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.damaged_by_explosion_function)){
            float max = plugin.config.math.damaged_by_explosion_maximum;
            float min = plugin.config.math.damaged_by_explosion_minimum;
            double x = damagee.getDefence() * plugin.config.math.damaged_by_explosion_defence
                    + damagee.getStrength() * plugin.config.math.damaged_by_explosion_strength;

            return (float)(Gradient(max,min) * x + min);
        }
        else{
            String[] st = plugin.config.math.damaged_by_explosion_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getDamagedByProjectile(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.damaged_by_projectile_function)){
            float max = plugin.config.math.damaged_by_projectile_maximum;
            float min = plugin.config.math.damaged_by_projectile_minimum;
            double x = damagee.getDefence() * plugin.config.math.damaged_by_projectile_defence;

            return (float)(Gradient(max,min) * x + min);
        }
        else{
            String[] st = plugin.config.math.damaged_by_projectile_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getChangeOfRemovingPotionEffect(RPGPlayer damagee){

        if(Helper.StringIsNullOrEmpty(plugin.config.math.reducing_bad_potion_effect_function)){
            float minChance = plugin.config.math.chance_of_removing_magic_effect_minimum;
            float maxChance = plugin.config.math.chance_of_removing_magic_effect_maximum;

            return (float)(Gradient(maxChance,minChance)
                    * (damagee.getMagicResistance()* plugin.config.math.chance_of_removing_magic_effect_magic_resistance
                    + damagee.getIntelligence() * plugin.config.math.chance_of_removing_magic_effect_intelligence
                    + damagee.getDexterity() * plugin.config.math.chance_of_removing_magic_effect_dexterity)
                    +minChance);
        }
        else{
            String[] st = plugin.config.math.chance_of_removing_magic_effect_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public  float getReducingPotionEffect(RPGPlayer damagee){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.reducing_bad_potion_effect_function)){
            float min = plugin.config.math.reducing_bad_potion_effect_minimum;
            float max = plugin.config.math.reducing_bad_potion_effect_maximum;
            float range = plugin.config.math.reducing_bad_potion_effect_range;

            float reduction = (float)Gradient(max,min)
                    * (damagee.getMagicResistance()* plugin.config.math.reducing_bad_potion_effect_magic_resistance
                    + damagee.getIntelligence()) * plugin.config.math.reducing_bad_potion_effect_intelligence
                    + min;

            float minReduction = reduction < (min - range) ? reduction + range : min;

            return (float)(reduction + (Math.random() * minReduction));
        }
        else{
            String[] st = plugin.config.math.reducing_bad_potion_effect_function.split(",");
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
        if(Helper.StringIsNullOrEmpty(plugin.config.math.on_player_shoot_bow_speed_function)) {
            result = Gradient(plugin.config.math.on_player_shoot_bow_speed_maximum,plugin.config.math.on_player_shoot_bow_speed_minimum)
                    * (p.getDexterity() * plugin.config.math.on_player_shoot_bow_speed_dexterity
                    + p.getStrength() * plugin.config.math.on_player_shoot_bow_speed_strength)
                    + plugin.config.math.on_player_shoot_bow_speed_minimum;
        }
        else{
            String[] st = plugin.config.math.on_player_shoot_bow_speed_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(plugin.config.math.on_player_shoot_bow_speed_range > 0){
            result = (float)((result - plugin.config.math.on_player_shoot_bow_speed_range) + Math.random() * (result + plugin.config.math.on_player_shoot_bow_speed_range));
            if(result < plugin.config.math.on_player_shoot_bow_speed_minimum)result = plugin.config.math.on_player_shoot_bow_speed_minimum;
            else if(result > plugin.config.math.on_player_shoot_bow_speed_maximum) result = plugin.config.math.on_player_shoot_bow_speed_maximum;
        }

        return result;
    }

    public  float getBowPrecision(RPGPlayer p){
        float result = 1F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.on_player_shoot_bow_precision_function)) {
            if(p.getDexterity() < plugin.config.math.on_player_shoot_bow_precision_min_dexterity) {
                result = p.getDexterity() / plugin.config.math.on_player_shoot_bow_precision_min_dexterity;
            }
        }
        else{
            String[] st = plugin.config.math.on_player_shoot_bow_precision_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(plugin.config.math.on_player_shoot_bow_precision_range > 0){
            result = (float)((result - plugin.config.math.on_player_shoot_bow_precision_range) + Math.random() * (result + plugin.config.math.on_player_shoot_bow_precision_range));
            if(result < 0)result = 0;
            else if(result > 1) result = 1;
        }

        return result;
    }

    public  float getAttackByWeapon(RPGPlayer damager,double damage){

        float max = plugin.config.math.attack_by_weapon_maximum;
        float min = plugin.config.math.attack_by_weapon_minimum;
        float range = plugin.config.math.attack_by_weapon_range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attack_by_weapon_function)) {
            result = (float)damage * Gradient(max,min) *
                    (damager.getDexterity() * plugin.config.math.attack_by_weapon_dexterity
                            +  damager.getStrength() * plugin.config.math.attack_by_weapon_strength)
                    + min;
        }
        else{
            String[] st = plugin.config.math.attack_by_weapon_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,damager));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(range > 0){
            result = (float)((result - range) + (Math.random() * (result + range)));
            if(result < 0)result = 0;
        }
        return result;
    }

    public  float getAttackByProjectile(RPGPlayer damager, double damage){
        float max = plugin.config.math.attack_by_projectile_maximum;
        float min = plugin.config.math.attack_by_projectile_minimum;
        float range = plugin.config.math.attack_by_projectile_range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attack_by_projectile_function)) {
            result = (float)damage * (float)Gradient(max,min)
                    * (damager.getDexterity() * plugin.config.math.attack_by_projectile_dexterity
                    + damager.getStrength() * plugin.config.math.attack_by_projectile_strength) + min;
        }
        else{
            String[] st = plugin.config.math.attack_by_projectile_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,damager));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(range > 0){
            result = (float)((result - range) + (Math.random() * (result + range)));
            if(result < 0)result = 0;
        }
        return result;
    }

    public  float getAttackByPotion(RPGPlayer damager, double damage){
        float max = plugin.config.math.attack_by_potion_maximum;
        float min = plugin.config.math.attack_by_potion_minimum;
        float range = plugin.config.math.attack_by_potion_range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attack_by_potion_function)) {
            result = (float)damage * Gradient(max,min)
                    * (damager.getDexterity() * plugin.config.math.attack_by_potion_dexterity
                    + damager.getIntelligence() * plugin.config.math.attack_by_potion_intelligence) + min;
        }
        else{
            String[] st = plugin.config.math.attack_by_potion_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,damager));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(range > 0){
            result = (float)((result - range) + (Math.random() * (result + range)));
            if(result < 0)result = 0;
        }
        return result;
    }

    // ON PLAYER CONSUME POTION

    public  float getConsumedPotionMultiplier(RPGPlayer p){

        float max = plugin.config.math.on_player_consume_potion_maximum;
        float min = plugin.config.math.on_player_consume_potion_minimum;
        float range = plugin.config.math.on_player_consume_potion_range;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(plugin.config.math.on_player_consume_potion_function)) {
            result = (float)Gradient(max,min)
                    * (p.getIntelligence() * plugin.config.math.on_player_consume_potion_intelligence)
                    + min;
        }
        else{
            String[] st = plugin.config.math.on_player_consume_potion_function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        if(range > 0){
            result = (float)((result - range) + (Math.random() * (result + range)));
            if(result < min)result = min;
            else if(result > max) result = max;
        }

        return result;
    }

    //PLAYER ATTRIBUTES

    public static float getPlayerMaxHealth(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attribute_total_health_function)) {
            return (float)Gradient(p.getRpgRace().getMax_health(), p.getRpgRace().getBase_health())
                    * p.getHealth() * plugin.config.math.attribute_total_health_health
                    + p.getRpgRace().getBase_health();
        }
        else{
            String[] st = plugin.config.math.attribute_total_health_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerMaxMana(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attribute_total_mana_function)) {
            return (float) Gradient(p.getRpgRace().getMax_mana(), p.getRpgRace().getBase_mana())
                    * p.getIntelligence() * plugin.config.math.attribute_total_mana_intelligence
                    + p.getRpgRace().getBase_mana();
        }
        else{
            String[] st = plugin.config.math.attribute_total_mana_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerManaRegen(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attribute_mana_regen_function)) {
            return (float)Gradient(plugin.config.math.attribute_mana_regen_maximum,plugin.config.math.attribute_mana_regen_minimum)
                    * p.getIntelligence() * plugin.config.math.attribute_mana_regen_intelligence
                    + plugin.config.math.attribute_mana_regen_minimum;
        }
        else{
            String[] st = plugin.config.math.attribute_mana_regen_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerAttackSpeed(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attribute_attack_speed_function)) {
            return (float)(Gradient(plugin.config.math.attribute_attack_speed_maximum,plugin.config.math.attribute_attack_speed_minimum)
                    * (p.getAgility() * plugin.config.math.attribute_attack_speed_agility
                    + p.getDexterity() * plugin.config.math.attribute_attack_speed_dexterity
                    + p.getStrength() * plugin.config.math.attribute_attack_speed_strength)
                    + plugin.config.math.attribute_attack_speed_minimum);
        }
        else{
            String[] st = plugin.config.math.attribute_attack_speed_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerMovementSpeed(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attribute_movement_speed_function)) {
            return Gradient(plugin.config.math.attribute_movement_speed_maximum,plugin.config.math.attribute_movement_speed_minimum)
                    * (p.getAgility() * plugin.config.math.attribute_movement_speed_agility
                    + p.getDexterity() * plugin.config.math.attribute_movement_speed_dexterity)
                    + plugin.config.math.attribute_movement_speed_minimum;
        }
        else{
            String[] st = plugin.config.math.attribute_movement_speed_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerKnockbackResistance(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attribute_knockback_resistance_function)) {
            return (float)Gradient(plugin.config.math.attribute_knockback_resistance_maximum,plugin.config.math.attribute_knockback_resistance_minimum)
                    * (p.getStrength() * plugin.config.math.attribute_knockback_resistance_strength
                    + p.getDefence() * plugin.config.math.attribute_knockback_resistance_defence
                    + p.getDexterity() * plugin.config.math.attribute_knockback_resistance_dexterity
                    + p.getAgility() * plugin.config.math.attribute_knockback_resistance_agility);
        }
        else{
            String[] st = plugin.config.math.attribute_knockback_resistance_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }

    public static float getPlayerLuck(RPGPlayer p){
        if(Helper.StringIsNullOrEmpty(plugin.config.math.attribute_luck_function)) {
            return (float)Gradient(plugin.config.math.attribute_luck_maximum,plugin.config.math.attribute_luck_minimum)
                    * (p.getAgility() * plugin.config.math.attribute_luck_agility
                    + p.getIntelligence() * plugin.config.math.attribute_luck_intelligence
                    + p.getDexterity() * plugin.config.math.attribute_luck_dexterity);
        }
        else{
            String[] st = plugin.config.math.attribute_luck_function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }
}
