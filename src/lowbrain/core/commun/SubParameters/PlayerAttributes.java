package lowbrain.core.commun.SubParameters;

import lowbrain.core.commun.Multiplier;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

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
