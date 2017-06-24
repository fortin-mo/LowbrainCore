package lowbrain.core.commun;

import lowbrain.core.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Moofy on 14/07/2016.
 */
public class Settings {

    private static Settings instance;

    private boolean opBypassPermission;
    private float firstLvlExp;
    private int maxLvl;
    private int maxStats;
    private boolean canSwitchClass;
    private boolean canSwitchRace;
    private boolean allowDeductionPoints;
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
    private float reduceBreedingSpawn;
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
        hardCoreEnable = config.getBoolean("hard_core.enable");
        hardCoreMaxDeaths = config.getInt("hard_core.max_deaths");
        disableMobNoTickDamage = config.getBoolean("disable_mob_no_tick_damage");
        reduceBreedingSpawn = (float)config.getDouble("reduce_breeding_spawn", 1);
        opBypassPermission = config.getBoolean("op_bypass_permission");
        allowDeductionPoints = config.getBoolean("allow_deduction_points");
        canSwitchClass = config.getBoolean("can_switch_class");
        canSwitchRace = config.getBoolean("can_switch_race");
        firstLvlExp = (float)config.getDouble("first_lvl_exp");
        maxLvl = config.getInt("max_lvl");
        maxStats = config.getInt("max_stats");
        pointsPerLvl = config.getInt("points_per_lvl");
        startingPoints = config.getInt("startingPoints");
        autoSave = config.getBoolean("auto_save");
        saveInterval = config.getInt("save_interval");
        allowStatsReset = config.getBoolean("allow_stats_reset");
        allowCompleteReset = config.getBoolean("allow_complete_reset");
        debug = config.getBoolean("debug");
        manaRegenInterval = config.getInt("mana_regen_interval");

        asdEnable = config.getBoolean("automatic_server_difficulty.enable");
        asdEasyFrom = config.getInt("automatic_server_difficulty.easy.from");
        asdEasyTo = config.getInt("automatic_server_difficulty.easy.to");
        asdMediumFrom = config.getInt("automatic_server_difficulty.medium.from");
        asdMediumTo = config.getInt("automatic_server_difficulty.medium.to");
        asdHardFrom = config.getInt("automatic_server_difficulty.hard.from");
        asdHardTo = config.getInt("automatic_server_difficulty.hard.to");

        groupXpEnable = config.getBoolean("group_xp.enable");
        groupXpEnableParties = config.getBoolean("group_ep_enable.enable_parties");
        groupXpRange = (float)config.getDouble("group_xp.range");
        groupXpMain = (float)config.getDouble("group_xp.main");
        groupXpOthers = (float)config.getDouble("group_xp.others");

        startingSkillPoints = config.getInt("starting_skill_points");
        skillPointsLevelInterval = config.getInt("skill_points_level_interval");
        skillPointsPerInterval = config.getInt("skill_points_per_interval");

        parametersFile = config.getString("parameters_file", "default_parameters.yml");

        parameters = new Parameters(getParametersFile());
    }

    public static Settings getInstance(){
        if(instance == null){
            instance = new Settings(Config.getInstance());
        }
        return instance;
    }

    public static void reload(){
        instance = null;
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

    public boolean isAllowDeductionPoints() {
        return allowDeductionPoints;
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

    public float getReduceBreedingSpawn() {
        return reduceBreedingSpawn;
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
}


