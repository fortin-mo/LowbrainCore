package lowbrain.core.commun;

import lowbrain.core.commun.SubParameters.*;
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

    private Courage courage;
    private Reputation reputation;

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

        courage = new Courage(config.getConfigurationSection("courage"));
        reputation = new Reputation(config.getConfigurationSection("reputation"));
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

    public Courage getCourage() {
        return courage;
    }

    public Reputation getReputation() {
        return reputation;
    }
}
