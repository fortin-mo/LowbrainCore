package lowbrain.core.rpg;

import lowbrain.core.commun.Helper;
import lowbrain.core.commun.Settings;

import java.util.HashMap;

/**
 * class containing the variables
 * to maximize performance, multipliers are computed when the players joins
 * they are update only if the player's attributes change
 */
public class Multipliers{

    LowbrainPlayer p;

    public Multipliers(LowbrainPlayer p){
        this.p = p;
        this.update();
    }

    public boolean update(){

        // ON PLAYER ATTACK

        setBowArrowSpeed();
        setBowPrecision();
        setAttackByWeapon();
        setAttackByProjectile();
        setAttackByMagic();
        setCriticalHitChance();
        setCriticalHitMultiplier();
        setChanceOfMissing();

        // ON PLAYER CONSUME POTION

        setConsumedPotionMultiplier();

        //PLAYER ATTRIBUTES

        setPlayerMaxHealth();
        setPlayerMaxMana();
        setPlayerManaRegen();
        setPlayerAttackSpeed();
        setPlayerMovementSpeed();
        setPlayerKnockbackResistance();
        setPlayerLuck();
        setPlayerDropPercentage();

        //ON PLAYER GET DAMAGED

        setChanceOfDodging();
        setChanceOfRemovingPotionEffect();
        setReducingPotionEffect();
        setDamagedByFire();
        setDamagedByFireTick();
        setDamagedByPoison();
        setDamagedByWither();
        setDamagedByContact();
        setDamagedByFlyIntoWall();
        setDamagedByFall();
        setDamagedByWeapon();
        setDamagedByArrow();
        setDamagedByProjectile();
        setDamagedByMagic();
        setDamagedBySuffocation();
        setDamagedByDrowning();
        setDamagedByStarvation();
        setDamagedByLightning();
        setDamagedByVoid();
        setDamagedByHotFloor();
        setDamagedByExplosion();
        setDamagedByLava();
        setDamagedByDefault();

        return true;
    }

    // ON PLAYER ATTACK

    private float BowArrowSpeed;
    private float BowPrecision;
    private float AttackByWeapon;
    private float AttackByProjectile;
    private float AttackByMagic;
    private float CriticalHitChance;
    private float CriticalHitMultiplier;
    private float ChanceOfMissing;

    // ON PLAYER CONSUME POTION

    private float ConsumedPotionMultiplier;

    //PLAYER ATTRIBUTES

    private float PlayerMaxHealth;
    private float PlayerMaxMana;
    private float PlayerManaRegen;
    private float PlayerAttackSpeed;
    private float PlayerMovementSpeed;
    private float PlayerKnockbackResistance;
    private float PlayerLuck;
    private float PlayerDropPercentage;

    //ON PLAYER GET DAMAGED

    private float ChanceOfRemovingPotionEffect;
    private float ReducingPotionEffect;
    private float DamagedByFire;
    private float DamagedByFireTick;
    private float DamagedByPoison;
    private float DamagedByWither;
    private float DamagedByContact;
    private float DamagedByFlyIntoWall;
    private float DamagedByFall;
    private float DamagedByWeapon;
    private float DamagedByArrow;
    private float DamagedByProjectile;
    private float DamagedByMagic;
    private float DamagedBySuffocation;
    private float DamagedByDrowning;
    private float DamagedByStarvation;
    private float DamagedByLightning;
    private float DamagedByVoid;
    private float DamagedByHotFloor;
    private float DamagedByExplosion;
    private float DamagedByLava;
    private float DamagedByDefault;
    private float ChanceOfDodging;

    private float getValue(Float max, Float min, HashMap<String,Float> variables, Float range, String function){
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(function)) {
            result = Helper.ValueFromFunction(max, min,variables, p);
        } else {
            String[] st = function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        return result;
    }

    private float getValue(float max, float min, HashMap<String,Float> variables, float range){
        return getValue(max,min,variables,range, null);
    }

    private float getValue(float max, float min, HashMap<String,Float> variables, String function){
        return getValue(max,min,variables,null,function);
    }


    // ON PLAYER ATTACK

    public void setChanceOfMissing() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerAttackEntity.chanceOfMissing.function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerAttackEntity.chanceOfMissing.maximum, Settings.getInstance().maths.onPlayerAttackEntity.chanceOfMissing.minimum,
                    Settings.getInstance().maths.onPlayerAttackEntity.chanceOfMissing.variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerAttackEntity.chanceOfMissing.function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.ChanceOfMissing = result;
    }
    public void setBowArrowSpeed() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerShootBow.speed_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerShootBow.speed_maximum, Settings.getInstance().maths.onPlayerShootBow.speed_minimum,
                    Settings.getInstance().maths.onPlayerShootBow.speed_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerShootBow.speed_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.BowArrowSpeed = result;
    }
    public void setBowPrecision() {
        float result = 1F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerShootBow.precision_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerShootBow.precision_maximum, Settings.getInstance().maths.onPlayerShootBow.precision_minimum,
                    Settings.getInstance().maths.onPlayerShootBow.precision_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerShootBow.precision_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.BowPrecision = result;
    }
    public void setAttackByWeapon() {

        float max = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_maximum;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_minimum;

        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_function)) {
            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.AttackByWeapon = result;
    }
    public void setAttackByProjectile() {
        float max = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_maximum;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_minimum;

        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_function)) {
            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.AttackByProjectile = result;
    }
    public void setAttackByMagic() {
        float max = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_maximum;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_minimum;

        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_function)) {
            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.AttackByMagic = result;
    }
    public void setCriticalHitChance() {
        float max = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.maximumChance;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.minimumChance;

        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.chanceFunction)) {
            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.chanceVariables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.chanceFunction.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.CriticalHitChance = result;
    }
    public void setCriticalHitMultiplier() {
        float max = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.maximumDamageMultiplier;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.minimumDamageMultiplier;

        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.damageMultiplierFunction)) {
            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.damageMultiplierVariables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.damageMultiplierFunction.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.CriticalHitMultiplier = result;
    }


    // ON PLAYER CONSUME POTION

    public void setConsumedPotionMultiplier(){

        float max = Settings.getInstance().maths.onPlayerConsumePotion.maximum;
        float min = Settings.getInstance().maths.onPlayerConsumePotion.minimum;

        float result = 0F;
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerConsumePotion.function)) {
            result = Helper.ValueFromFunction(max,min,Settings.getInstance().maths.onPlayerConsumePotion.variables,p);
        }
        else{
            String[] st = Settings.getInstance().maths.onPlayerConsumePotion.function.split(",");
            if(st.length > 1){
                result = Helper.eval(Helper.FormatStringWithValues(st,p));
            }
            else{
                result = Helper.eval(st[0]);
            }
        }

        this.ConsumedPotionMultiplier = result;
    }

    //PLAYER ATTRIBUTES

    public void setPlayerMaxHealth() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.playerAttributes.total_health_function)) {
            result = Helper.ValueFromFunction(p.getLowbrainRace().getMax_health(), p.getLowbrainRace().getBase_health(), Settings.getInstance().maths.playerAttributes.total_health_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.playerAttributes.total_health_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.PlayerMaxHealth = result;
    }
    public void setPlayerMaxMana() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.playerAttributes.total_mana_function)) {
            result = Helper.ValueFromFunction(p.getLowbrainRace().getMax_mana(), p.getLowbrainRace().getBase_mana(), Settings.getInstance().maths.playerAttributes.total_mana_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.playerAttributes.total_mana_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.PlayerMaxMana = result;
    }
    public void setPlayerManaRegen() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.playerAttributes.mana_regen_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.playerAttributes.mana_regen_maximum,
                    Settings.getInstance().maths.playerAttributes.mana_regen_minimum,
                    Settings.getInstance().maths.playerAttributes.mana_regen_variables, p
            );
        } else {
            String[] st = Settings.getInstance().maths.playerAttributes.mana_regen_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.PlayerManaRegen = result;
    }
    public void setPlayerAttackSpeed() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.playerAttributes.attack_speed_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.playerAttributes.attack_speed_maximum,
                    Settings.getInstance().maths.playerAttributes.attack_speed_minimum,
                    Settings.getInstance().maths.playerAttributes.attack_speed_variables, p
            );
        } else {
            String[] st = Settings.getInstance().maths.playerAttributes.attack_speed_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.PlayerAttackSpeed = result;
    }
    public void setPlayerMovementSpeed() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.playerAttributes.movement_speed_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.playerAttributes.movement_speed_maximum, Settings.getInstance().maths.playerAttributes.movement_speed_minimum,
                    Settings.getInstance().maths.playerAttributes.movement_speed_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.playerAttributes.movement_speed_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.PlayerMovementSpeed = result;
    }
    public void setPlayerKnockbackResistance() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.playerAttributes.knockback_resistance_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.playerAttributes.knockback_resistance_maximum, Settings.getInstance().maths.playerAttributes.knockback_resistance_minimum,
                    Settings.getInstance().maths.playerAttributes.knockback_resistance_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.playerAttributes.knockback_resistance_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.PlayerKnockbackResistance = result;
    }
    public void setPlayerLuck() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.playerAttributes.luck_function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.playerAttributes.luck_maximum, Settings.getInstance().maths.playerAttributes.luck_minimum,
                    Settings.getInstance().maths.playerAttributes.luck_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.playerAttributes.luck_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.PlayerLuck = result;
    }
    public void setPlayerDropPercentage() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerDies.function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerDies.items_drops_maximum, Settings.getInstance().maths.onPlayerDies.items_drops_minimum,
                    Settings.getInstance().maths.onPlayerDies.variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerDies.function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.PlayerDropPercentage = result;
    }

    //ON PLAYER GET DAMAGED

    public void setChanceOfDodging() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.chanceOfDodging.function)) {
            result = Helper.ValueFromFunction(Settings.getInstance().maths.onPlayerGetDamaged.chanceOfDodging.maximum, Settings.getInstance().maths.onPlayerGetDamaged.chanceOfDodging.minimum,
                    Settings.getInstance().maths.onPlayerGetDamaged.chanceOfDodging.variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.chanceOfDodging.function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }

        this.ChanceOfDodging = result;
    }
    public void setChanceOfRemovingPotionEffect() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.chanceOfRemovingMagicEffect.function)) {
            float minChance = Settings.getInstance().maths.onPlayerGetDamaged.chanceOfRemovingMagicEffect.minimum;
            float maxChance = Settings.getInstance().maths.onPlayerGetDamaged.chanceOfRemovingMagicEffect.maximum;

            result = Helper.ValueFromFunction(maxChance, minChance, Settings.getInstance().maths.onPlayerGetDamaged.chanceOfRemovingMagicEffect.variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.chanceOfRemovingMagicEffect.function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.ChanceOfRemovingPotionEffect = result;
    }
    public void setReducingPotionEffect() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.function)) {
            float min = Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.minimum;
            float max = Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.maximum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.ReducingPotionEffect = result;
    }
    public void setDamagedByFire() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_fire_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_fire_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_fire_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_fire_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_fire_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByFire = result;
    }
    public void setDamagedByFireTick() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_fire_tick_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_fire_tick_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_fire_tick_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_fire_tick_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_fire_tick_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByFireTick = result;
    }
    public void setDamagedByPoison() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_poison_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_poison_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_poison_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_poison_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_poison_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByPoison = result;
    }
    public void setDamagedByWither() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_wither_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_wither_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_wither_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_wither_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_wither_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByWither = result;
    }
    public void setDamagedByContact() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_contact_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_contact_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_contact_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_contact_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_contact_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByContact = result;
    }
    public void setDamagedByFlyIntoWall() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_fly_into_wall_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_fly_into_wall_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_fly_into_wall_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_fly_into_wall_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_fly_into_wall_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByFlyIntoWall = result;
    }
    public void setDamagedByFall() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_fall_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_fall_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_fall_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_fall_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_fall_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByFall = result;
    }
    public void setDamagedByWeapon() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_weapon_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_weapon_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_weapon_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_weapon_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_weapon_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByWeapon = result;
    }
    public void setDamagedByArrow() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_arrow_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_arrow_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_arrow_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_arrow_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_arrow_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByArrow = result;
    }
    public void setDamagedByProjectile() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_projectile_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_projectile_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_projectile_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_projectile_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_projectile_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByProjectile = result;
    }
    public void setDamagedByMagic() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_magic_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_magic_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_magic_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_magic_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_magic_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByMagic = result;
    }
    public void setDamagedBySuffocation() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_suffocation_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_suffocation_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_suffocation_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_suffocation_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_suffocation_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedBySuffocation = result;
    }
    public void setDamagedByDrowning() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_drowning_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_drowning_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_drowning_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_drowning_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_drowning_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByDrowning = result;
    }
    public void setDamagedByStarvation() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_starvation_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_starvation_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_starvation_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_starvation_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_starvation_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByStarvation = result;
    }
    public void setDamagedByLightning() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_lightning_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_lightning_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_lightning_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_lightning_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_lightning_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByLightning = result;
    }
    public void setDamagedByVoid() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_void_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_void_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_void_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_void_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_void_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByVoid = result;
    }
    public void setDamagedByHotFloor() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_hot_floor_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_hot_floor_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_hot_floor_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_hot_floor_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_hot_floor_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByHotFloor = result;
    }
    public void setDamagedByExplosion() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_explosion_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_explosion_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_explosion_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_explosion_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_explosion_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByExplosion = result;
    }
    public void setDamagedByLava() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_lava_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_lava_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_lava_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_lava_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_lava_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByLava = result;
    }
    public void setDamagedByDefault() {
        float result = 0F;
        if (Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.by_default_function)) {
            float max = Settings.getInstance().maths.onPlayerGetDamaged.by_default_maximum;
            float min = Settings.getInstance().maths.onPlayerGetDamaged.by_default_minimum;

            result = Helper.ValueFromFunction(max, min, Settings.getInstance().maths.onPlayerGetDamaged.by_default_variables, p);
        } else {
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.by_default_function.split(",");
            if (st.length > 1) {
                result = Helper.eval(Helper.FormatStringWithValues(st, p));
            } else {
                result = Helper.eval(st[0]);
            }
        }
        this.DamagedByDefault = result;
    }

    public float getChanceOfMissing() {
        return ChanceOfMissing;
    }
    public float getChanceOfDodging() {
        return ChanceOfDodging;
    }
    public float getBowArrowSpeed() {
        return BowArrowSpeed;
    }
    public float getBowPrecision() {
        return BowPrecision;
    }
    public float getAttackByWeapon() {
        return AttackByWeapon;
    }
    public float getAttackByProjectile() {
        return AttackByProjectile;
    }
    public float getAttackByMagic() {
        return AttackByMagic;
    }
    public float getCriticalHitChance() {
        return CriticalHitChance;
    }
    public float getCriticalHitMultiplier() {
        return CriticalHitMultiplier;
    }
    public float getConsumedPotionMultiplier() {
        return ConsumedPotionMultiplier;
    }
    public float getPlayerMaxHealth() {
        return PlayerMaxHealth;
    }
    public float getPlayerMaxMana() {
        return PlayerMaxMana;
    }
    public float getPlayerManaRegen() {
        return PlayerManaRegen;
    }
    public float getPlayerAttackSpeed() {
        return PlayerAttackSpeed;
    }
    public float getPlayerMovementSpeed() {
        return PlayerMovementSpeed;
    }
    public float getPlayerKnockbackResistance() {
        return PlayerKnockbackResistance;
    }
    public float getPlayerLuck() {
        return PlayerLuck;
    }
    public float getPlayerDropPercentage() {
        return PlayerDropPercentage;
    }
    public float getChanceOfRemovingPotionEffect() {
        return ChanceOfRemovingPotionEffect;
    }
    public float getReducingPotionEffect() {
        return ReducingPotionEffect;
    }
    public float getDamagedByFire() {
        return DamagedByFire;
    }
    public float getDamagedByFireTick() {
        return DamagedByFireTick;
    }
    public float getDamagedByPoison() {
        return DamagedByPoison;
    }
    public float getDamagedByWither() {
        return DamagedByWither;
    }
    public float getDamagedByContact() {
        return DamagedByContact;
    }
    public float getDamagedByFlyIntoWall() {
        return DamagedByFlyIntoWall;
    }
    public float getDamagedByFall() {
        return DamagedByFall;
    }
    public float getDamagedByWeapon() {
        return DamagedByWeapon;
    }
    public float getDamagedByArrow() {
        return DamagedByArrow;
    }
    public float getDamagedByProjectile() {
        return DamagedByProjectile;
    }
    public float getDamagedByMagic() {
        return DamagedByMagic;
    }
    public float getDamagedBySuffocation() {
        return DamagedBySuffocation;
    }
    public float getDamagedByDrowning() {
        return DamagedByDrowning;
    }
    public float getDamagedByStarvation() {
        return DamagedByStarvation;
    }
    public float getDamagedByLightning() {
        return DamagedByLightning;
    }
    public float getDamagedByVoid() {
        return DamagedByVoid;
    }
    public float getDamagedByHotFloor() {
        return DamagedByHotFloor;
    }
    public float getDamagedByExplosion() {
        return DamagedByExplosion;
    }
    public float getDamagedByLava() {
        return DamagedByLava;
    }
    public float getDamagedByDefault() {
        return DamagedByDefault;
    }
}
