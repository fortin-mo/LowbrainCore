package lowbrain.core.commun.SubParameters;

import lowbrain.core.commun.Multiplier;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

public class OnPlayerGetDamaged{

    private Multiplier byMagic;
    private Multiplier byPoison;
    private Multiplier byWither;
    private Multiplier byVoid;
    private Multiplier byFire;
    private Multiplier byLava;
    private Multiplier byLightning;
    private Multiplier byHotFloor;
    private Multiplier byFireTick;
    private Multiplier byExplosion;
    private Multiplier byFall;
    private Multiplier byFlyIntoWall;
    private Multiplier byContact;
    private Multiplier byArrow;
    private Multiplier byProjectile;
    private Multiplier byWeapon;
    private Multiplier bySuffocation;
    private Multiplier byStarvation;
    private Multiplier byDrowning;
    private Multiplier byDefault;


    private Multiplier chanceOfRemovingMagicEffect;
    private Multiplier reducingBadPotionEffect;
    private Multiplier chanceOfDodging;

    @Contract("null -> fail")
    public OnPlayerGetDamaged(ConfigurationSection config){
        if(config == null) throw new NullPointerException("ConfigurationSection for OnPLayerGetDamaged cannot be null");

        chanceOfRemovingMagicEffect = new Multiplier(config.getConfigurationSection("chance_of_removing_magic_effect"));
        reducingBadPotionEffect = new Multiplier(config.getConfigurationSection("reducing_bad_potion_effect"));
        chanceOfDodging = new Multiplier(config.getConfigurationSection("chance_of_dodging"));

        byMagic = new Multiplier(config.getConfigurationSection("by_magic"));
        byPoison = new Multiplier(config.getConfigurationSection("by_poison"));
        byWither = new Multiplier(config.getConfigurationSection("by_wither"));
        byVoid = new Multiplier(config.getConfigurationSection("by_void"));
        byFire = new Multiplier(config.getConfigurationSection("by_fire"));
        byLava = new Multiplier(config.getConfigurationSection("by_lava"));
        byLightning = new Multiplier(config.getConfigurationSection("by_lightning"));
        byHotFloor = new Multiplier(config.getConfigurationSection("by_hot_floor"));
        byFireTick = new Multiplier(config.getConfigurationSection("by_fire_tick"));
        byExplosion = new Multiplier(config.getConfigurationSection("by_explosion"));
        byFall = new Multiplier(config.getConfigurationSection("by_fall"));
        byFlyIntoWall = new Multiplier(config.getConfigurationSection("by_fly_into_wall"));
        byContact = new Multiplier(config.getConfigurationSection("by_contact"));
        byArrow = new Multiplier(config.getConfigurationSection("by_arrow"));
        byProjectile = new Multiplier(config.getConfigurationSection("by_projectile"));
        byWeapon = new Multiplier(config.getConfigurationSection("by_weapon"));
        bySuffocation = new Multiplier(config.getConfigurationSection("by_suffocation"));
        byStarvation = new Multiplier(config.getConfigurationSection("by_starvation"));
        byDrowning = new Multiplier(config.getConfigurationSection("by_drowning"));
        byDefault = new Multiplier(config.getConfigurationSection("by_default"));
    }

    public Multiplier getByMagic() {
        return byMagic;
    }

    public Multiplier getByPoison() {
        return byPoison;
    }

    public Multiplier getByWither() {
        return byWither;
    }

    public Multiplier getByVoid() {
        return byVoid;
    }

    public Multiplier getByFire() {
        return byFire;
    }

    public Multiplier getByLava() {
        return byLava;
    }

    public Multiplier getByLightning() {
        return byLightning;
    }

    public Multiplier getByHotFloor() {
        return byHotFloor;
    }

    public Multiplier getByFireTick() {
        return byFireTick;
    }

    public Multiplier getByExplosion() {
        return byExplosion;
    }

    public Multiplier getByFall() {
        return byFall;
    }

    public Multiplier getByFlyIntoWall() {
        return byFlyIntoWall;
    }

    public Multiplier getByContact() {
        return byContact;
    }

    public Multiplier getByArrow() {
        return byArrow;
    }

    public Multiplier getByProjectile() {
        return byProjectile;
    }

    public Multiplier getByWeapon() {
        return byWeapon;
    }

    public Multiplier getBySuffocation() {
        return bySuffocation;
    }

    public Multiplier getByStarvation() {
        return byStarvation;
    }

    public Multiplier getByDrowning() {
        return byDrowning;
    }

    public Multiplier getByDefault() {
        return byDefault;
    }

    public Multiplier getChanceOfRemovingMagicEffect() {
        return chanceOfRemovingMagicEffect;
    }

    public Multiplier getReducingBadPotionEffect() {
        return reducingBadPotionEffect;
    }

    public Multiplier getChanceOfDodging() {
        return chanceOfDodging;
    }
}