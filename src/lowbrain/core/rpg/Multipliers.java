package lowbrain.core.rpg;

import lowbrain.core.commun.Settings;

/**
 * class containing the variables
 * to maximize performance, multipliers are computed when the players joins
 * they are update only if the player's attributes change
 */
public class Multipliers {

    LowbrainPlayer p;

    // ON PLAYER ATTACK
    private double BowArrowSpeed;
    private double BowPrecision;
    private double AttackByWeapon;
    private double AttackByProjectile;
    private double AttackByMagic;
    private double CriticalHitChance;
    private double CriticalHitMultiplier;
    private double ChanceOfMissing;
    private double BackStabMultiplier;
    private double ChanceOfMagicEffect;

    // ON PLAYER CONSUME POTION
    private double ConsumedPotionMultiplier;

    //PLAYER ATTRIBUTES
    private double PlayerMaxHealth;
    private double PlayerMaxMana;
    private double PlayerManaRegen;
    private double PlayerAttackSpeed;
    private double PlayerMovementSpeed;
    private double PlayerKnockbackResistance;
    private double PlayerLuck;
    private double PlayerDropPercentage;

    //ON PLAYER GET DAMAGED
    private double ChanceOfRemovingPotionEffect;
    private double ReducingPotionEffect;
    private double DamagedByFire;
    private double DamagedByFireTick;
    private double DamagedByPoison;
    private double DamagedByWither;
    private double DamagedByContact;
    private double DamagedByFlyIntoWall;
    private double DamagedByFall;
    private double DamagedByWeapon;
    private double DamagedByArrow;
    private double DamagedByProjectile;
    private double DamagedByMagic;
    private double DamagedBySuffocation;
    private double DamagedByDrowning;
    private double DamagedByStarvation;
    private double DamagedByLightning;
    private double DamagedByVoid;
    private double DamagedByHotFloor;
    private double DamagedByExplosion;
    private double DamagedByLava;
    private double DamagedByDefault;
    private double ChanceOfDodging;

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

    private void setPlayerMaxHealth() {
        this.PlayerMaxHealth = Settings.getInstance()
                .getParameters()
                .getPlayerAttributes()
                .getTotalHealth()
                .computeWith(p, this.p.getLowbrainRace().getBaseHealth(), this.p.getLowbrainRace().getMaxHealth());
    }
    private void setPlayerMaxMana() {
        this.PlayerMaxMana = Settings.getInstance()
                .getParameters()
                .getPlayerAttributes()
                .getTotalMana()
                .computeWith(p, this.p.getLowbrainRace().getBaseMana(), this.p.getLowbrainRace().getMaxMana());
    }
    private void setPlayerManaRegen() {
        this.PlayerManaRegen = Settings.getInstance().getParameters().getPlayerAttributes().getManaRegen().compute(p);
    }
    private void setPlayerAttackSpeed() {
        this.PlayerAttackSpeed = Settings.getInstance().getParameters().getPlayerAttributes().getAttackSpeed().compute(p);
    }
    private void setPlayerMovementSpeed() {
        this.PlayerMovementSpeed = Settings.getInstance().getParameters().getPlayerAttributes().getMovementSpeed().compute(p);
    }
    private void setPlayerKnockbackResistance() {
        this.PlayerKnockbackResistance = Settings.getInstance().getParameters().getPlayerAttributes().getKnockbackResistance().compute(p);
    }
    private void setPlayerLuck() {
        this.PlayerLuck = Settings.getInstance().getParameters().getPlayerAttributes().getLuck().compute(p);
    }
    private void setPlayerDropPercentage() {
        this.PlayerDropPercentage = Settings.getInstance().getParameters().getOnPlayerDies().getItems_drops().compute(p);
    }

    //ON PLAYER GET DAMAGED

    private void setChanceOfDodging() {
        this.ChanceOfDodging = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfDodging().compute(p);
    }
    private void setChanceOfRemovingPotionEffect() {
        this.ChanceOfRemovingPotionEffect = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfRemovingMagicEffect().compute(p);
    }
    private void setReducingPotionEffect() {
        this.ReducingPotionEffect = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getReducingBadPotionEffect().compute(p);
    }
    private void setDamagedByFire() {
        this.DamagedByFire = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFire().compute(p);
    }
    private void setDamagedByFireTick() {
        this.DamagedByFireTick = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFireTick().compute(p);
    }
    private void setDamagedByPoison() {
        this.DamagedByPoison = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByPoison().compute(p);
    }
    private void setDamagedByWither() {
        this.DamagedByWither = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWither().compute(p);
    }
    private void setDamagedByContact() {
        this.DamagedByContact = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByContact().compute(p);
    }
    private void setDamagedByFlyIntoWall() {
        this.DamagedByFlyIntoWall = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFlyIntoWall().compute(p);
    }
    private void setDamagedByFall() {
        this.DamagedByFall = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFall().compute(p);
    }
    private void setDamagedByWeapon() {
        this.DamagedByWeapon = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWeapon().compute(p);
    }
    private void setDamagedByArrow() {
        this.DamagedByArrow = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByArrow().compute(p);
    }
    private void setDamagedByProjectile() {
        this.DamagedByProjectile = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByProjectile().compute(p);
    }
    private void setDamagedByMagic() {
        this.DamagedByMagic = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByMagic().compute(p);
    }
    private void setDamagedBySuffocation() {
        this.DamagedBySuffocation = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getBySuffocation().compute(p);
    }
    private void setDamagedByDrowning() {
        this.DamagedByDrowning = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByDrowning().compute(p);
    }
    private void setDamagedByStarvation() {
        this.DamagedByStarvation = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByStarvation().compute(p);
    }
    private void setDamagedByLightning() {
        this.DamagedByLightning = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByLightning().compute(p);
    }
    private void setDamagedByVoid() {
        this.DamagedByVoid = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByVoid().compute(p);
    }
    private void setDamagedByHotFloor() {
        this.DamagedByHotFloor = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByHotFloor().compute(p);
    }
    private void setDamagedByExplosion() {
        this.DamagedByExplosion = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByExplosion().compute(p);
    }
    private void setDamagedByLava() {
        this.DamagedByLava = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByLava().compute(p);
    }
    private void setDamagedByDefault() {
        this.DamagedByDefault = Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByDefault().compute(p);
    }

    public double getChanceOfMagicEffect() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getChanceOfCreatingMagicAttack().randomizeFromMultiplier(ChanceOfMagicEffect);
    }
    public double getChanceOfMissing() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getChanceOfMissing().randomizeFromMultiplier(ChanceOfMissing);
    }
    public double getChanceOfDodging() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfDodging().randomizeFromMultiplier(ChanceOfDodging);
    }
    public double getBowArrowSpeed() {
        return Settings.getInstance().getParameters().getOnPlayerShootBow().getSpeed().randomizeFromMultiplier(BowArrowSpeed);
    }
    public double getBowPrecision() {
        return Settings.getInstance().getParameters().getOnPlayerShootBow().getPrecision().randomizeFromMultiplier(BowPrecision);
    }
    public double getAttackByWeapon() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().weapon.randomizeFromMultiplier(AttackByWeapon);
    }
    public double getAttackByProjectile() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().projectile.randomizeFromMultiplier(AttackByProjectile);
    }
    public double getAttackByMagic() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getAttackEntityBy().magic.randomizeFromMultiplier(AttackByMagic);
    }
    public double getCriticalHitChance() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCriticalHit().chance.randomizeFromMultiplier(CriticalHitChance);
    }
    public double getCriticalHitMultiplier() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getCriticalHit().damage.randomizeFromMultiplier(CriticalHitMultiplier);
    }
    public double getBackStabMultiplier() {
        return Settings.getInstance().getParameters().getOnPlayerAttackEntity().getBackStab().randomizeFromMultiplier(BackStabMultiplier);
    }
    public double getConsumedPotionMultiplier() {
        return Settings.getInstance().getParameters().getOnPlayerConsumePotion().randomizeFromMultiplier(ConsumedPotionMultiplier);
    }
    public double getPlayerDropPercentage() {
        return Settings.getInstance().getParameters().getOnPlayerDies().getItems_drops().randomizeFromMultiplier(PlayerDropPercentage);
    }
    public double getChanceOfRemovingPotionEffect() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getChanceOfRemovingMagicEffect().randomizeFromMultiplier(ChanceOfRemovingPotionEffect);
    }
    public double getReducingPotionEffect() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getReducingBadPotionEffect().randomizeFromMultiplier(ReducingPotionEffect);
    }
    public double getDamagedByFire() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFire().randomizeFromMultiplier(DamagedByFire);
    }
    public double getDamagedByFireTick() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFireTick().randomizeFromMultiplier(DamagedByFireTick);
    }
    public double getDamagedByPoison() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByPoison().randomizeFromMultiplier(DamagedByPoison);
    }
    public double getDamagedByWither() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWither().randomizeFromMultiplier(DamagedByWither);
    }
    public double getDamagedByContact() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByContact().randomizeFromMultiplier(DamagedByContact);
    }
    public double getDamagedByFlyIntoWall() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFlyIntoWall().randomizeFromMultiplier(DamagedByFlyIntoWall);
    }
    public double getDamagedByFall() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByFall().randomizeFromMultiplier(DamagedByFall);
    }
    public double getDamagedByWeapon() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByWeapon().randomizeFromMultiplier(DamagedByWeapon);
    }
    public double getDamagedByArrow() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByArrow().randomizeFromMultiplier(DamagedByArrow);
    }
    public double getDamagedByProjectile() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByProjectile().randomizeFromMultiplier(DamagedByProjectile);
    }
    public double getDamagedByMagic() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByMagic().randomizeFromMultiplier(DamagedByMagic);
    }
    public double getDamagedBySuffocation() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getBySuffocation().randomizeFromMultiplier(DamagedBySuffocation);
    }
    public double getDamagedByDrowning() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByDrowning().randomizeFromMultiplier(DamagedByDrowning);
    }
    public double getDamagedByStarvation() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByStarvation().randomizeFromMultiplier(DamagedByStarvation);
    }
    public double getDamagedByLightning() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByLightning().randomizeFromMultiplier(DamagedByLightning);
    }
    public double getDamagedByVoid() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByVoid().randomizeFromMultiplier(DamagedByVoid);
    }
    public double getDamagedByHotFloor() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByHotFloor().randomizeFromMultiplier(DamagedByHotFloor);
    }
    public double getDamagedByExplosion() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByExplosion().randomizeFromMultiplier(DamagedByExplosion);
    }
    public double getDamagedByLava() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByLava().randomizeFromMultiplier(DamagedByLava);
    }
    public double getDamagedByDefault() {
        return Settings.getInstance().getParameters().getOnPlayerGetDamaged().getByDefault().randomizeFromMultiplier(DamagedByDefault);
    }

    // these cannot be randomize as it wouldnt make sense
    public double getPlayerMaxHealth() {
        return PlayerMaxHealth;
    }
    public double getPlayerMaxMana() {
        return PlayerMaxMana;
    }
    public double getPlayerManaRegen() {
        return PlayerManaRegen;
    }
    public double getPlayerAttackSpeed() {
        return PlayerAttackSpeed;
    }
    public double getPlayerMovementSpeed() {
        return PlayerMovementSpeed;
    }
    public double getPlayerKnockbackResistance() {
        return PlayerKnockbackResistance;
    }
    public double getPlayerLuck() {
        return PlayerLuck;
    }
}
