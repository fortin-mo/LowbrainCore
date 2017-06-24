package lowbrain.core.rpg;

import lowbrain.core.commun.Settings;

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

        setChanceOfMagicEffect();
        setBowArrowSpeed();
        setBowPrecision();
        setAttackByWeapon();
        setAttackByProjectile();
        setAttackByMagic();
        setCriticalHitChance();
        setCriticalHitMultiplier();
        setChanceOfMissing();
        setBackStabMultiplier();

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
    private float BackStabMultiplier;
    private float ChanceOfMagicEffect;

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

    // ON PLAYER ATTACK

    public void setChanceOfMagicEffect() {
        this.ChanceOfMagicEffect = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getChanceOfCreatingMagicAttack().compute(p);
    }
    public void setChanceOfMissing() {
        this.ChanceOfMissing = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getChanceOfMissing().compute(p);
    }
    public void setBowArrowSpeed() {
        this.BowArrowSpeed = Settings.getInstance().getParameters().getOnPlayerShootBow().getSpeed().compute(p);
    }
    public void setBowPrecision() {
        this.BowPrecision = Settings.getInstance().getParameters().getOnPlayerShootBow().getPrecision().compute(p);
    }
    public void setAttackByWeapon() {
        this.AttackByWeapon = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().weapon.compute(p);
    }
    public void setAttackByProjectile() {
        this.AttackByProjectile = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().projectile.compute(p);
    }
    public void setAttackByMagic() {
        this.AttackByMagic = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().magic.compute(p);
    }
    public void setCriticalHitChance() {
        this.CriticalHitChance = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCriticalHit().chance.compute(p);
    }
    public void setCriticalHitMultiplier() {
        this.CriticalHitMultiplier = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCriticalHit().damage.compute(p);
    }
    public void setBackStabMultiplier() {
        this.BackStabMultiplier = Settings.getInstance().getParameters().getOnPlayerAttackEntity().getBackStab().compute(p);
    }


    // ON PLAYER CONSUME POTION

    public void setConsumedPotionMultiplier(){
        this.ConsumedPotionMultiplier = Settings.getInstance().getParameters().getOnPlayerConsumePotion().compute(p);
    }

    //PLAYER ATTRIBUTES

    public void setPlayerMaxHealth() {
        float max = this.p.getLowbrainRace().getMax_health();
        float min = this.p.getLowbrainRace().getBase_health();
        this.PlayerMaxHealth = Settings.getInstance().getParameters().getPlayerAttributes().getTotalHealth().computeWith(p, max, min);
    }
    public void setPlayerMaxMana() {
        float max = this.p.getLowbrainRace().getMax_mana();
        float min = this.p.getLowbrainRace().getBase_mana();
        this.PlayerMaxMana = Settings.getInstance().getParameters().getPlayerAttributes().getTotalMana().computeWith(p, max, min);
    }
    public void setPlayerManaRegen() {
        this.PlayerManaRegen = Settings.getInstance().getParameters().getPlayerAttributes().getManaRegen().compute(p);
    }
    public void setPlayerAttackSpeed() {
        this.PlayerAttackSpeed = Settings.getInstance().getParameters().getPlayerAttributes().getAttackSpeed().compute(p);
    }
    public void setPlayerMovementSpeed() {
        this.PlayerMovementSpeed = Settings.getInstance().getParameters().getPlayerAttributes().getMovementSpeed().compute(p);
    }
    public void setPlayerKnockbackResistance() {
        this.PlayerKnockbackResistance = Settings.getInstance().getParameters().getPlayerAttributes().getKnockbackResistance().compute(p);
    }
    public void setPlayerLuck() {
        this.PlayerLuck = Settings.getInstance().getParameters().getPlayerAttributes().getLuck().compute(p);
    }
    public void setPlayerDropPercentage() {
        this.PlayerDropPercentage = Settings.getInstance().getParameters().getOnPlayerDies().getItems_drops().compute(p);
    }

    //ON PLAYER GET DAMAGED

    public void setChanceOfDodging() {
        this.ChanceOfDodging = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfDodging().compute(p);
    }
    public void setChanceOfRemovingPotionEffect() {
        this.ChanceOfRemovingPotionEffect = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfRemovingMagicEffect().compute(p);
    }
    public void setReducingPotionEffect() {
        this.ReducingPotionEffect = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getReducingBadPotionEffect().compute(p);
    }
    public void setDamagedByFire() {
        this.DamagedByFire = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFire().compute(p);
    }
    public void setDamagedByFireTick() {
        this.DamagedByFireTick = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFireTick().compute(p);
    }
    public void setDamagedByPoison() {
        this.DamagedByPoison = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByPoison().compute(p);
    }
    public void setDamagedByWither() {
        this.DamagedByWither = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWither().compute(p);
    }
    public void setDamagedByContact() {
        this.DamagedByContact = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByContact().compute(p);
    }
    public void setDamagedByFlyIntoWall() {
        this.DamagedByFlyIntoWall = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFlyIntoWall().compute(p);
    }
    public void setDamagedByFall() {
        this.DamagedByFall = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFall().compute(p);
    }
    public void setDamagedByWeapon() {
        this.DamagedByWeapon = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWeapon().compute(p);
    }
    public void setDamagedByArrow() {
        this.DamagedByArrow = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByArrow().compute(p);
    }
    public void setDamagedByProjectile() {
        this.DamagedByProjectile = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByProjectile().compute(p);
    }
    public void setDamagedByMagic() {
        this.DamagedByMagic = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByMagic().compute(p);
    }
    public void setDamagedBySuffocation() {
        this.DamagedBySuffocation = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getBySuffocation().compute(p);
    }
    public void setDamagedByDrowning() {
        this.DamagedByDrowning = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByDrowning().compute(p);
    }
    public void setDamagedByStarvation() {
        this.DamagedByStarvation = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByStarvation().compute(p);
    }
    public void setDamagedByLightning() {
        this.DamagedByLightning = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByLightning().compute(p);
    }
    public void setDamagedByVoid() {
        this.DamagedByVoid = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByVoid().compute(p);
    }
    public void setDamagedByHotFloor() {
        this.DamagedByHotFloor = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByHotFloor().compute(p);
    }
    public void setDamagedByExplosion() {
        this.DamagedByExplosion = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByExplosion().compute(p);
    }
    public void setDamagedByLava() {
        this.DamagedByLava = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByLava().compute(p);
    }
    public void setDamagedByDefault() {
        this.DamagedByDefault = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByDefault().compute(p);
    }

    public float getChanceOfMagicEffect() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getChanceOfCreatingMagicAttack().randomizeFromMultiplier(ChanceOfMagicEffect);
    }
    public float getChanceOfMissing() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getChanceOfMissing().randomizeFromMultiplier(ChanceOfMissing);
    }
    public float getChanceOfDodging() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfDodging().randomizeFromMultiplier(ChanceOfDodging);
    }
    public float getBowArrowSpeed() {
        return Settings.getInstance().getParameters().getOnPlayerShootBow().getSpeed().randomizeFromMultiplier(BowArrowSpeed);
    }
    public float getBowPrecision() {
        return Settings.getInstance().getParameters().getOnPlayerShootBow().getPrecision().randomizeFromMultiplier(BowPrecision);
    }
    public float getAttackByWeapon() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().weapon.randomizeFromMultiplier(AttackByWeapon);
    }
    public float getAttackByProjectile() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().projectile.randomizeFromMultiplier(AttackByProjectile);
    }
    public float getAttackByMagic() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().magic.randomizeFromMultiplier(AttackByMagic);
    }
    public float getCriticalHitChance() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCriticalHit().chance.randomizeFromMultiplier(CriticalHitChance);
    }
    public float getCriticalHitMultiplier() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCriticalHit().damage.randomizeFromMultiplier(CriticalHitMultiplier);
    }
    public float getBackStabMultiplier() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getBackStab().randomizeFromMultiplier(BackStabMultiplier);
    }
    public float getConsumedPotionMultiplier() {
        return Settings.getInstance().getParameters().getOnPlayerConsumePotion().randomizeFromMultiplier(ConsumedPotionMultiplier);
    }
    public float getPlayerDropPercentage() {
        return Settings.getInstance().getParameters().getOnPlayerDies().getItems_drops().randomizeFromMultiplier(PlayerDropPercentage);
    }
    public float getChanceOfRemovingPotionEffect() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfRemovingMagicEffect().randomizeFromMultiplier(ChanceOfRemovingPotionEffect);
    }
    public float getReducingPotionEffect() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getReducingBadPotionEffect().randomizeFromMultiplier(ReducingPotionEffect);
    }
    public float getDamagedByFire() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFire().randomizeFromMultiplier(DamagedByFire);
    }
    public float getDamagedByFireTick() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFireTick().randomizeFromMultiplier(DamagedByFireTick);
    }
    public float getDamagedByPoison() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByPoison().randomizeFromMultiplier(DamagedByPoison);
    }
    public float getDamagedByWither() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWither().randomizeFromMultiplier(DamagedByWither);
    }
    public float getDamagedByContact() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByContact().randomizeFromMultiplier(DamagedByContact);
    }
    public float getDamagedByFlyIntoWall() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFlyIntoWall().randomizeFromMultiplier(DamagedByFlyIntoWall);
    }
    public float getDamagedByFall() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFall().randomizeFromMultiplier(DamagedByFall);
    }
    public float getDamagedByWeapon() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWeapon().randomizeFromMultiplier(DamagedByWeapon);
    }
    public float getDamagedByArrow() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByArrow().randomizeFromMultiplier(DamagedByArrow);
    }
    public float getDamagedByProjectile() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByProjectile().randomizeFromMultiplier(DamagedByProjectile);
    }
    public float getDamagedByMagic() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByMagic().randomizeFromMultiplier(DamagedByMagic);
    }
    public float getDamagedBySuffocation() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getBySuffocation().randomizeFromMultiplier(DamagedBySuffocation);
    }
    public float getDamagedByDrowning() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByDrowning().randomizeFromMultiplier(DamagedByDrowning);
    }
    public float getDamagedByStarvation() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByStarvation().randomizeFromMultiplier(DamagedByStarvation);
    }
    public float getDamagedByLightning() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByLightning().randomizeFromMultiplier(DamagedByLightning);
    }
    public float getDamagedByVoid() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByVoid().randomizeFromMultiplier(DamagedByVoid);
    }
    public float getDamagedByHotFloor() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByHotFloor().randomizeFromMultiplier(DamagedByHotFloor);
    }
    public float getDamagedByExplosion() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByExplosion().randomizeFromMultiplier(DamagedByExplosion);
    }
    public float getDamagedByLava() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByLava().randomizeFromMultiplier(DamagedByLava);
    }
    public float getDamagedByDefault() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByDefault().randomizeFromMultiplier(DamagedByDefault);
    }

    // these cannot be randomize as it wouldnt make sense
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
}
