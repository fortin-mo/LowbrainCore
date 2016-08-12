package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moofy on 14/07/2016.
 */
public class Config {

    public float first_lvl_exp= 75;
    public int max_lvl= 100;
    public int max_stats= 100;
    public boolean can_switch_class= false;
    public boolean can_switch_race = false;
    public boolean allow_deduction_points = false;
    public int points_per_lvl= 2;
    public int starting_points= 3;
    public boolean auto_save= true;
    public int save_interval= 360;
    public boolean allow_stats_reset = false;
    public boolean allow_complete_reset = false;
    public boolean debug = false;
    public List<Integer> lstClassId = new ArrayList<Integer>();
    public int mana_regen_interval = 5;
    public int starting_skill_points = 0;
    public int skill_points_level_interval = 5;
    public int skill_points_per_level = 1;
    public boolean automatic_server_difficulty = true;
    public boolean nearby_players_gain_xp = true;
    public float nearby_players_max_distance = 15;

    public MathSettings math;

    public Config(FileConfiguration config){
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
        lstClassId = config.getIntegerList("Classes.list");
        mana_regen_interval = config.getInt("mana_regen_interval");
        automatic_server_difficulty = config.getBoolean("automatic_server_difficulty");
        nearby_players_gain_xp = config.getBoolean("nearby_players_gain_xp");
        nearby_players_max_distance = (float)config.getDouble("nearby_players_max_distance");
        starting_skill_points = config.getInt("starting_skill_points");
        skill_points_level_interval = config.getInt("skill_points_level_interval");
        skill_points_per_level = config.getInt("skill_points_per_level");
        math = new MathSettings(config);
    }

}
