package lowbrain.core.commun;

import lowbrain.core.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

/**
 * Created by Moofy on 14/07/2016.
 */
public class Settings {

    private static Settings instance;

    public boolean op_bypass_permission;
    public float first_lvl_exp;
    public int max_lvl;
    public int max_stats;
    public boolean can_switch_class;
    public boolean can_switch_race;
    public boolean allow_deduction_points;
    public int points_per_lvl;
    public int starting_points;
    public boolean auto_save;
    public int save_interval;
    public boolean allow_stats_reset;
    public boolean allow_complete_reset;
    public boolean debug;
    public int mana_regen_interval;
    public int starting_skill_points;
    public int skill_points_level_interval;
    public int skill_points_per_interval;
    public boolean disable_mob_no_tick_damage;
    public boolean hard_core_enable;
    public int hard_core_max_deaths;

    public boolean asd_enable;
    public int asd_easy_from;
    public int asd_easy_to;
    public int asd_medium_from;
    public int asd_medium_to;
    public int asd_hard_from;
    public int asd_hard_to;

    public boolean group_xp_enable;
    public boolean group_xp_enable_parties;
    public float group_xp_range;
    public float group_xp_main;
    public float group_xp_others;

    public String parameters_file;
    public Parameters parameters;

    private Settings(FileConfiguration config){
        hard_core_enable = config.getBoolean("hard_core.enable");
        hard_core_max_deaths = config.getInt("hard_core.max_deaths");
        disable_mob_no_tick_damage = config.getBoolean("disable_mob_no_tick_damage");
        op_bypass_permission = config.getBoolean("op_bypass_permission");
        allow_deduction_points = config.getBoolean("allow_deduction_points");
        can_switch_class = config.getBoolean("can_switch_class");
        can_switch_race = config.getBoolean("can_switch_race");
        first_lvl_exp = (float)config.getDouble("first_lvl_exp");
        max_lvl = config.getInt("max_lvl");
        max_stats = config.getInt("max_stats");
        points_per_lvl = config.getInt("points_per_lvl");
        starting_points = config.getInt("starting_points");
        auto_save = config.getBoolean("auto_save");
        save_interval = config.getInt("save_interval");
        allow_stats_reset = config.getBoolean("allow_stats_reset");
        allow_complete_reset = config.getBoolean("allow_complete_reset");
        debug = config.getBoolean("debug");
        mana_regen_interval = config.getInt("mana_regen_interval");

        asd_enable = config.getBoolean("automatic_server_difficulty.enable");
        asd_easy_from = config.getInt("automatic_server_difficulty.easy.from");
        asd_easy_to = config.getInt("automatic_server_difficulty.easy.to");
        asd_medium_from = config.getInt("automatic_server_difficulty.medium.from");
        asd_medium_to = config.getInt("automatic_server_difficulty.medium.to");
        asd_hard_from = config.getInt("automatic_server_difficulty.hard.from");
        asd_hard_to = config.getInt("automatic_server_difficulty.hard.to");

        group_xp_enable = config.getBoolean("group_xp.enable");
        group_xp_enable_parties = config.getBoolean("group_ep_enable.enable_parties");
        group_xp_range = (float)config.getDouble("group_xp.range");
        group_xp_main = (float)config.getDouble("group_xp.main");
        group_xp_others = (float)config.getDouble("group_xp.others");

        starting_skill_points = config.getInt("starting_skill_points");
        skill_points_level_interval = config.getInt("skill_points_level_interval");
        skill_points_per_interval = config.getInt("skill_points_per_interval");

        parameters_file = config.getString("parameters_file", "default_parameters.yml");

        parameters = new Parameters(parameters_file);
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

}


