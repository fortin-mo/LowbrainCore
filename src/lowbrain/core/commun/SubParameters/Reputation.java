package lowbrain.core.commun.SubParameters;

import lowbrain.core.commun.Helper;
import lowbrain.core.main.LowbrainCore;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;

/**
 * represents the configurable part of reputation
 */
public class Reputation {
    private boolean enabled;
    private int initial;
    private int onDeath;
    private HashMap<String, Integer> onMobKills;
    private HashMap<String, ReputationStatus> repStatus;

    /**
     * constructor for reputation parameter
     * @param config configuration section
     */
    public Reputation(ConfigurationSection config) {
        if (config == null) {
            this.enabled = false;
            LowbrainCore.getInstance().warn("could not load reputation configuration. Reason : config is null");
            return;
        }

        enabled = config.getBoolean("enabled", true);
        initial = config.getInt("initial", 0);
        onDeath = config.getInt("on_death", -1);


        ConfigurationSection mobKillsSection = config.getConfigurationSection("on_mob_kills");
        onMobKills = new HashMap<>();
        if(mobKillsSection != null)
            for (String k : mobKillsSection.getKeys(false))
                onMobKills.put(k, mobKillsSection.getInt(k,1));


        ConfigurationSection statusSection = config.getConfigurationSection("status");
        repStatus = new HashMap<>();
        if (statusSection != null)
            for (String k : statusSection.getKeys(false))
                repStatus.put(k, new ReputationStatus(statusSection.getConfigurationSection(k), k));

    }

    /**
     * get corresponding reputation status based on value
     * @param value value
     * @return reputation status
     */
    public ReputationStatus getRepFrom(int value) {
        ReputationStatus found = null;

        for (ReputationStatus status : repStatus.values()) {
            if (Helper.isBetween(value, status.getFrom(), status.getTo())) {
                found = status;
                break;
            }
        }

        if (found == null)
            LowbrainCore.getInstance().warn("reputation status may not be well configured, nothing was found for value : " + value);

        return found;
    }

    public HashMap<String, Integer> getOnMobKills() {
        return onMobKills;
    }

    public HashMap<String, ReputationStatus> getRepStatus() {
        return repStatus;
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
}

