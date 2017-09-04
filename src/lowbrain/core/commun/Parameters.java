package lowbrain.core.commun;

import lowbrain.core.config.DefaultParameters;
import lowbrain.core.main.LowbrainCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;

import java.io.File;

/**
 * Created by Ninja on 2017-01-25.
 */
public class Parameters {

    private final static String DEFAULT_PARAMETERS = "default_parameters.yml";

    private float nextLvlMultiplier;
    private float naturalXpGainMultiplier;
    private float killerLevelGainMultiplier;
    private float levelDifferenceMultiplier;
    private float killerBaseExp;
    private boolean playerKillsPlayerExpEnable;
    private FunctionType functionType;

    private Multiplier onPlayerConsumePotion;
    private OnPlayerGetDamaged onPlayerGetDamaged;
    private PlayerAttributes playerAttributes;
    private OnPlayerShootBow onPlayerShootBow;
    private OnPlayerAttackEntity onPlayerAttackEntity;
    private OnPlayerDies onPlayerDies;

    public Parameters(String path) {
        LowbrainCore plugin = LowbrainCore.getInstance();

        FileConfiguration config = null;

        if (path == null || path == DEFAULT_PARAMETERS) {
            plugin.log("using default parameters");
            config = DefaultParameters.getInstance();
        } else {
            File file = new File(plugin.getDataFolder(), path);
            if (!file.exists()) {
                plugin.warn("could not find : " + path);
                plugin.warn("using default parameters");
                config = DefaultParameters.getInstance();
            } else {
                config = new YamlConfiguration();
                try {
                    config.load(file);
                } catch (Exception e) {
                    plugin.warn("error while loading parameters file !!!!");
                    plugin.onDisable();
                    return;
                }
            }
        }

        this.functionType = FunctionType.get(config.getInt("function_type", -1));
        this.nextLvlMultiplier = (float)config.getDouble("next_lvl_multiplier");
        this.naturalXpGainMultiplier = (float)config.getDouble("natural_xp_gain_multiplier");
        this.killerLevelGainMultiplier = (float)config.getDouble("on_player_kills_player.killer_level_gain_multiplier");
        this.levelDifferenceMultiplier = (float)config.getDouble("on_player_kills_player.level_difference_multiplier");
        this.killerBaseExp = (float)config.getDouble("on_player_kills_player.killer_base_exp");
        this.playerKillsPlayerExpEnable = config.getBoolean("on_player_kills_player.enable");

        onPlayerAttackEntity = new OnPlayerAttackEntity(config.getConfigurationSection("on_player_attack_entity"));
        onPlayerConsumePotion = new Multiplier(config.getConfigurationSection("on_player_consume_potion"));
        onPlayerShootBow = new OnPlayerShootBow(config.getConfigurationSection("on_player_shoot_bow"));
        playerAttributes = new PlayerAttributes(config.getConfigurationSection("player_attributes"));
        onPlayerGetDamaged = new OnPlayerGetDamaged(config.getConfigurationSection("on_player_get_damaged"));
        onPlayerDies = new OnPlayerDies(config.getConfigurationSection("on_player_dies"));
    }

    public float getNextLvlMultiplier() {
        return nextLvlMultiplier;
    }

    public float getNaturalXpGainMultiplier() {
        return naturalXpGainMultiplier;
    }

    public float getKillerLevelGainMultiplier() {
        return killerLevelGainMultiplier;
    }

    public float getLevelDifferenceMultiplier() {
        return levelDifferenceMultiplier;
    }

    public float getKillerBaseExp() {
        return killerBaseExp;
    }

    public boolean isPlayerKillsPlayerExpEnable() {
        return playerKillsPlayerExpEnable;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public Multiplier getOnPlayerConsumePotion() {
        return onPlayerConsumePotion;
    }

    public OnPlayerGetDamaged getOnPlayerGetDamaged() {
        return onPlayerGetDamaged;
    }

    public PlayerAttributes getPlayerAttributes() {
        return playerAttributes;
    }

    public OnPlayerShootBow getOnPlayerShootBow() {
        return onPlayerShootBow;
    }

    public OnPlayerAttackEntity getOnPlayerAttackEntity() {
        return onPlayerAttackEntity;
    }

    public OnPlayerDies getOnPlayerDies() {
        return onPlayerDies;
    }

    public class OnPlayerAttackEntity{
        private Multiplier chanceOfCreatingMagicAttack;
        private creatingMagicAttack creatingMagicAttack;
        private attackEntityBy attackEntityBy;
        private criticalHit criticalHit;
        private Multiplier chanceOfMissing;
        private Multiplier backStab;

        @Contract("null -> fail")
        public OnPlayerAttackEntity(ConfigurationSection config){
            if(config == null)
                throw new NullPointerException("ConfigurationSection for onPlayerAttackEntity cannot be null");

            chanceOfCreatingMagicAttack = new Multiplier(config.getConfigurationSection("chance_of_creating_magic_attack"));
            creatingMagicAttack = new creatingMagicAttack(config.getConfigurationSection("creating_magic_attack"));
            attackEntityBy = new attackEntityBy(config);
            criticalHit = new criticalHit(config.getConfigurationSection("critical_hit"));
            chanceOfMissing = new Multiplier(config.getConfigurationSection("chance_of_missing"));
            backStab = new Multiplier(config.getConfigurationSection("backstab"));
        }

        public Multiplier getChanceOfCreatingMagicAttack() {
            return chanceOfCreatingMagicAttack;
        }

        public OnPlayerAttackEntity.creatingMagicAttack getCreatingMagicAttack() {
            return creatingMagicAttack;
        }

        public OnPlayerAttackEntity.attackEntityBy getAttackEntityBy() {
            return attackEntityBy;
        }

        public OnPlayerAttackEntity.criticalHit getCriticalHit() {
            return criticalHit;
        }

        public Multiplier getChanceOfMissing() {
            return chanceOfMissing;
        }

        public Multiplier getBackStab() {
            return backStab;
        }

        public class criticalHit {
            public boolean enable;
            public Multiplier chance;
            public Multiplier damage;

            public criticalHit(ConfigurationSection config){
                enable = config.getBoolean("enable");
                chance = new Multiplier(config.getConfigurationSection("chance"));
                damage = new Multiplier(config.getConfigurationSection("damage_multiplier"));
            }
        }

        public class creatingMagicAttack{
            //=
            public boolean enable;
            public Multiplier duration;
            public Multiplier harm;
            public Multiplier slow;
            public Multiplier poison;
            public Multiplier weakness;
            public Multiplier wither;
            public Multiplier blindness;
            public Multiplier confusion;


            @Contract("null -> fail")
            public creatingMagicAttack(ConfigurationSection config){
                if (config == null)
                    throw new NullPointerException("ConfigurationSection for creatingMagicAttack cannot be null");

                this.enable = config.getBoolean("enable");
                duration = new Multiplier(config.getConfigurationSection("duration"));
                harm = new Multiplier(config.getConfigurationSection("harm"));
                slow = new Multiplier(config.getConfigurationSection("slow"));
                poison = new Multiplier(config.getConfigurationSection("poison"));
                weakness = new Multiplier(config.getConfigurationSection("weakness"));
                wither = new Multiplier(config.getConfigurationSection("wither"));
                blindness = new Multiplier(config.getConfigurationSection("blindness"));
                confusion = new Multiplier(config.getConfigurationSection("confusion"));
            }
        }

        public class attackEntityBy{
            //normal_attack_damage_=
            public Multiplier weapon;

            //magic_attack_damage_=
            public Multiplier magic;

            //arrow_attack_damage_=
            public Multiplier projectile;

            public attackEntityBy(ConfigurationSection config){
                weapon = new Multiplier(config.getConfigurationSection("by_weapon"));
                projectile = new Multiplier(config.getConfigurationSection("by_projectile"));
                magic = new Multiplier(config.getConfigurationSection("by_magic"));
            }
        }
    }

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

    public class PlayerAttributes{
        private Multiplier movementSpeed;
        private Multiplier attackSpeed;
        private Multiplier totalMana;
        private Multiplier totalHealth;
        private Multiplier manaRegen;
        private Multiplier luck;
        private Multiplier knockbackResistance;

        @Contract("null -> fail")
        public PlayerAttributes(ConfigurationSection config){
            if(config == null) throw new NullPointerException("ConfigurationSection for playerAttributes cannot be null");
            movementSpeed = new Multiplier(config.getConfigurationSection("movement_speed"));
            attackSpeed = new Multiplier(config.getConfigurationSection("attack_speed"));
            totalMana = new Multiplier(config.getConfigurationSection("total_mana"));
            totalHealth = new Multiplier(config.getConfigurationSection("total_health"));
            manaRegen = new Multiplier(config.getConfigurationSection("mana_regen"));
            luck = new Multiplier(config.getConfigurationSection("luck"));
            knockbackResistance = new Multiplier(config.getConfigurationSection("knockback_resistance"));
        }

        public Multiplier getMovementSpeed() {
            return movementSpeed;
        }

        public Multiplier getAttackSpeed() {
            return attackSpeed;
        }

        public Multiplier getTotalMana() {
            return totalMana;
        }

        public Multiplier getTotalHealth() {
            return totalHealth;
        }

        public Multiplier getManaRegen() {
            return manaRegen;
        }

        public Multiplier getLuck() {
            return luck;
        }

        public Multiplier getKnockbackResistance() {
            return knockbackResistance;
        }
    }

    public class OnPlayerShootBow{
        private boolean enable;
        private Multiplier precision;
        private Multiplier speed;

        @Contract("null -> fail")
        public OnPlayerShootBow(ConfigurationSection section){
            if(section == null)
                throw new NullPointerException("ConfigurationSection for onPlayerShootBow cannot be null");

            enable = section.getBoolean("enable", true);
            speed = new Multiplier(section.getConfigurationSection("speed"));
            precision = new Multiplier(section.getConfigurationSection("precision"));
        }

        public boolean isEnable() {
            return enable;
        }

        public Multiplier getPrecision() {
            return precision;
        }

        public Multiplier getSpeed() {
            return speed;
        }
    }

    public class OnPlayerDies{
        private float xp_loss;
        private Multiplier items_drops;
        private boolean enable;

        public OnPlayerDies(ConfigurationSection section){
            enable = section.getBoolean("enable");
            xp_loss = (float)section.getDouble("xp_loss");
            items_drops = new Multiplier(section.getConfigurationSection("items_drops"));
        }

        public float getXp_loss() {
            return xp_loss;
        }

        public Multiplier getItems_drops() {
            return items_drops;
        }

        public boolean isEnable() {
            return enable;
        }
    }

    public class Reputation {
        public boolean enabled;
        public int initial;
        public int onMobKill;
        public int onDeath;
        public int onBadPlayerKill;
        public int onGoodPlayerKill;

        Reputation(ConfigurationSection config) {


        }
    }
}
