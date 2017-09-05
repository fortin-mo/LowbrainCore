package lowbrain.core.commun.SubParameters;

import lowbrain.core.commun.Multiplier;
import org.bukkit.configuration.ConfigurationSection;

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
