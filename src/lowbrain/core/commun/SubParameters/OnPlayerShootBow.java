package lowbrain.core.commun.SubParameters;

import lowbrain.core.commun.Multiplier;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

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