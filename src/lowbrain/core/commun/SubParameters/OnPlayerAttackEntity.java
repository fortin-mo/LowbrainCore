package lowbrain.core.commun.SubParameters;

import lowbrain.core.commun.Multiplier;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

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
