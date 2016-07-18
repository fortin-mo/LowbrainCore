package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moofy on 14/07/2016.
 */
public class Settings {

    public double first_lvl_exp= 100;
    public int max_lvl= 99;
    public int max_stats= 99;
    public boolean can_switch_class= false;
    public int points_per_lvl= 1;
    public int starting_points= 3;
    public boolean auto_save= true;
    public int save_interval= 360;
    public double exp_loss_on_death = 25;
    public double exp_on_player_kill = 10;
    public boolean allow_stats_reset = true;
    public boolean debug = false;
    public List<Integer> lstClassId = new ArrayList<Integer>();
    public int mana_regen_interval = 5;
    public boolean automatic_server_difficulty = true;
    public MathSettings math;

    public Settings(FileConfiguration config){
        can_switch_class = config.getBoolean("Settings.can_switch_class");
        first_lvl_exp = config.getDouble("Settings.first_lvl_exp");
        max_lvl = config.getInt("Settings.max_lvl");
        max_stats = config.getInt("Settings.max_stats");
        points_per_lvl = config.getInt("Settings.points_per_lvl");
        starting_points = config.getInt("Settings.starting_points");
        auto_save = config.getBoolean("Settings.auto_save");
        save_interval = config.getInt("Settings.save_interval");
        exp_loss_on_death = config.getDouble("Settings.exp_loss_on_death");
        exp_on_player_kill = config.getDouble("Settings.exp_on_player_kill");
        allow_stats_reset = config.getBoolean("Settings.allow_stats_reset");
        debug = config.getBoolean("Settings.debug");
        lstClassId = config.getIntegerList("Classes.list");
        mana_regen_interval = config.getInt("Settings.mana_regen_interval");
        automatic_server_difficulty = config.getBoolean("Settings.automatic_server_difficulty");
        math = new MathSettings(config);
    }
}
