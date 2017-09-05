package lowbrain.core.commun.SubParameters;

import lowbrain.core.main.LowbrainCore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

/**
 * represents the configurable part of courage
 */
public class Courage {
    private boolean enabled;
    private int initial;
    private int onDeath;
    private HashMap<String, Integer> onMobKills;

    /**
     * constructor
     * @param config configuration section
     */
   public Courage(ConfigurationSection config) {
        if (config == null) {
            this.enabled = false;
            LowbrainCore.getInstance().warn("could not load courage configuration. Reason : config is null");
            return;
        }

        enabled = config.getBoolean("enabled", true);
        initial = config.getInt("initial", 0);
        onDeath = config.getInt("on_death", -2);

        ConfigurationSection mobKillsSection = config.getConfigurationSection("on_mob_kills");
        onMobKills = new HashMap<>();
        if(mobKillsSection != null)
            for (String k : mobKillsSection.getKeys(false))
                onMobKills.put(k, mobKillsSection.getInt(k,1));
    }

    public int getInitial() {
        return initial;
    }

    public int getOnDeath() {
        return onDeath;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public HashMap<String, Integer> getOnMobKills() {
        return onMobKills;
    }
}
