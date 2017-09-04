package lowbrain.core.commun;

import lowbrain.core.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * represents all settings from config.yml
 */
public class Settings {

    private static Settings instance;

    private boolean opBypassPermission;
    private float firstLvlExp;
    private int maxLvl;
    private int maxStats;
    private boolean canSwitchClass;
    private boolean canSwitchRace;
    private boolean allowPointDeduction;
    private int pointsPerLvl;
    private int startingPoints;
    private boolean autoSave;
    private int saveInterval;
    private boolean allowStatsReset;
    private boolean allowCompleteReset;
    private boolean debug;
    private int manaRegenInterval;
    private int startingSkillPoints;
    private int skillPointsLevelInterval;
    private int skillPointsPerInterval;
    private boolean disableMobNoTickDamage;
    private float reduceSpawnFromBreeding;
    private boolean hardCoreEnable;
    private int hardCoreMaxDeaths;

    private boolean asdEnable;
    private int asdEasyFrom;
    private int asdEasyTo;
    private int asdMediumFrom;
    private int asdMediumTo;
    private int asdHardFrom;
    private int asdHardTo;

    private boolean groupXpEnable;
    private boolean groupXpEnableParties;
    private float groupXpRange;
    private float groupXpMain;
    private float groupXpOthers;

    private String parametersFile;
    private Parameters parameters;

    private Settings(FileConfiguration config){
        hardCoreEnable = config.getBoolean("hard_core.enable", false);
        hardCoreMaxDeaths = config.getInt("hard_core.max_deaths", 1);
        disableMobNoTickDamage = config.getBoolean("disable_mob_no_tick_damage", true);
        reduceSpawnFromBreeding = (float)config.getDouble("reduce_spawn_from_breeding", 0.5);
        opBypassPermission = config.getBoolean("op_bypass_permission", true);
        allowPointDeduction = config.getBoolean("allow_point_deduction", false);
        canSwitchClass = config.getBoolean("can_switch_class", false);
        canSwitchRace = config.getBoolean("can_switch_race", false);
        firstLvlExp = (float)config.getDouble("first_lvl_exp", 75);
        maxLvl = config.getInt("max_lvl", 100);
        maxStats = config.getInt("max_stats", 100);
        pointsPerLvl = config.getInt("points_per_lvl", 2);
        startingPoints = config.getInt("startingPoints", 3);
        autoSave = config.getBoolean("auto_save", true);
        saveInterval = config.getInt("save_interval", 360);
        allowStatsReset = config.getBoolean("allow_stats_reset", false);
        allowCompleteReset = config.getBoolean("allow_complete_reset", true);
        debug = config.getBoolean("debug", false);
        manaRegenInterval = config.getInt("mana_regen_interval", 5);

        asdEnable = config.getBoolean("automatic_server_difficulty.enable", true);
        asdEasyFrom = config.getInt("automatic_server_difficulty.easy.from", 0);
        asdEasyTo = config.getInt("automatic_server_difficulty.easy.to", 25);
        asdMediumFrom = config.getInt("automatic_server_difficulty.medium.from",26);
        asdMediumTo = config.getInt("automatic_server_difficulty.medium.to", 55);
        asdHardFrom = config.getInt("automatic_server_difficulty.hard.from", 56);
        asdHardTo = config.getInt("automatic_server_difficulty.hard.to", -1);

        groupXpEnable = config.getBoolean("group_xp.enable", true);
        groupXpEnableParties = config.getBoolean("group_ep_enable.enable_parties", true);
        groupXpRange = (float)config.getDouble("group_xp.range",15);
        groupXpMain = (float)config.getDouble("group_xp.main", 0.667);
        groupXpOthers = (float)config.getDouble("group_xp.others", 0.333);

        startingSkillPoints = config.getInt("starting_skill_points", 0);
        skillPointsLevelInterval = config.getInt("skill_points_level_interval", 5);
        skillPointsPerInterval = config.getInt("skill_points_per_interval", 1);

        parametersFile = config.getString("parameters_file", "default_parameters.yml");

        parameters = new Parameters(getParametersFile());
    }

    public static Settings getInstance(){
        if(instance == null)
            instance = new Settings(Config.getInstance());

        return instance;
    }

    public static void reload(){
        instance = null;
        getInstance();
    }

    public Parameters getParameters() {
        return parameters;
    }

    public boolean isOpBypassPermission() {
        return opBypassPermission;
    }

    public float getFirstLvlExp() {
        return firstLvlExp;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public int getMaxStats() {
        return maxStats;
    }

    public boolean isCanSwitchClass() {
        return canSwitchClass;
    }

    public boolean isCanSwitchRace() {
        return canSwitchRace;
    }

    public boolean isAllowPointDeduction() {
        return allowPointDeduction;
    }

    public int getPointsPerLvl() {
        return pointsPerLvl;
    }

    public int getStartingPoints() {
        return startingPoints;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public int getSaveInterval() {
        return saveInterval;
    }

    public boolean isAllowStatsReset() {
        return allowStatsReset;
    }

    public boolean isAllowCompleteReset() {
        return allowCompleteReset;
    }

    public boolean isDebug() {
        return debug;
    }

    public int getManaRegenInterval() {
        return manaRegenInterval;
    }

    public int getStartingSkillPoints() {
        return startingSkillPoints;
    }

    public int getSkillPointsLevelInterval() {
        return skillPointsLevelInterval;
    }

    public int getSkillPointsPerInterval() {
        return skillPointsPerInterval;
    }

    public boolean isDisableMobNoTickDamage() {
        return disableMobNoTickDamage;
    }

    public boolean isHardCoreEnable() {
        return hardCoreEnable;
    }

    public int getHardCoreMaxDeaths() {
        return hardCoreMaxDeaths;
    }

    public boolean isAsdEnable() {
        return asdEnable;
    }

    public int getAsdEasyFrom() {
        return asdEasyFrom;
    }

    public int getAsdEasyTo() {
        return asdEasyTo;
    }

    public int getAsdMediumFrom() {
        return asdMediumFrom;
    }

    public int getAsdMediumTo() {
        return asdMediumTo;
    }

    public int getAsdHardFrom() {
        return asdHardFrom;
    }

    public int getAsdHardTo() {
        return asdHardTo;
    }

    public boolean isGroupXpEnable() {
        return groupXpEnable;
    }

    public boolean isGroupXpEnableParties() {
        return groupXpEnableParties;
    }

    public float getGroupXpRange() {
        return groupXpRange;
    }

    public float getGroupXpMain() {
        return groupXpMain;
    }

    public float getGroupXpOthers() {
        return groupXpOthers;
    }

    public String getParametersFile() {
        return parametersFile;
    }

    public float getReduceSpawnFromBreeding() {
        return reduceSpawnFromBreeding;
    }
}


