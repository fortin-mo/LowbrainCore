package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moofy on 14/07/2016.
 */
public class Settings {

    private double first_lvl_exp= 100;
    private int max_lvl= 99;
    private int max_stats= 99;
    private double next_lvl_multiplier= 1.25;
    private boolean can_switch_class= false;
    private int points_per_lvl= 1;
    private int starting_points= 3;
    private boolean auto_save= true;
    private int save_interval= 360;
    private double exp_loss_on_death = 25;
    private double exp_on_player_kill = 10;
    private boolean allow_stats_reset = true;
    private boolean debug = false;
    private List<Integer> lstClassId = new ArrayList<Integer>();

    public Settings(FileConfiguration config){
        setCan_switch_class(config.getBoolean("Settings.can_switch_class"));
        setFirst_lvl_exp(config.getDouble("Settings.first_lvl_exp"));
        setMax_lvl(config.getInt("Settings.max_lvl"));
        setMax_stats(config.getInt("Settings.max_stats"));
        setNext_lvl_multiplier(config.getDouble("Settings.next_lvl_multiplier"));
        setPoints_per_lvl(config.getInt("Settings.points_per_lvl"));
        setStarting_points(config.getInt("Settings.starting_points"));
        setAuto_save(config.getBoolean("Settings.auto_save"));
        setSave_interval(config.getInt("Settings.save_interval"));
        setExp_loss_on_death(config.getDouble("Settings.exp_loss_on_death"));
        setExp_on_player_kill(config.getDouble("Settings.exp_on_player_kill"));
        setAllow_stats_reset(config.getBoolean("Settings.allow_stats_reset"));
        setDebug(config.getBoolean("Settings.debug"));
        setLstClassId(config.getIntegerList("Class"));
    }

    public double getFirst_lvl_exp() {
        return first_lvl_exp;
    }

    public void setFirst_lvl_exp(double first_lvl_exp) {
        this.first_lvl_exp = first_lvl_exp;
    }

    public int getMax_lvl() {
        return max_lvl;
    }

    public void setMax_lvl(int max_lvl) {
        this.max_lvl = max_lvl;
    }

    public int getMax_stats() {
        return max_stats;
    }

    public void setMax_stats(int max_stats) {
        this.max_stats = max_stats;
    }

    public double getNext_lvl_multiplier() {
        return next_lvl_multiplier;
    }

    public void setNext_lvl_multiplier(double next_lvl_multiplier) {
        this.next_lvl_multiplier = next_lvl_multiplier;
    }

    public boolean isCan_switch_class() {
        return can_switch_class;
    }

    public void setCan_switch_class(boolean can_switch_class) {
        this.can_switch_class = can_switch_class;
    }

    public int getPoints_per_lvl() {
        return points_per_lvl;
    }

    public void setPoints_per_lvl(int points_per_lvl) {
        this.points_per_lvl = points_per_lvl;
    }

    public int getStarting_points() {
        return starting_points;
    }

    public void setStarting_points(int starting_points) {
        this.starting_points = starting_points;
    }

    public boolean isAuto_save() {
        return auto_save;
    }

    public void setAuto_save(boolean auto_save) {
        this.auto_save = auto_save;
    }

    public int getSave_interval() {
        return save_interval;
    }

    public void setSave_interval(int save_interval) {
        this.save_interval = save_interval;
    }

    public double getExp_loss_on_death() {
        return exp_loss_on_death;
    }

    public void setExp_loss_on_death(double exp_loss_on_death) {
        this.exp_loss_on_death = exp_loss_on_death;
    }

    public double getExp_on_player_kill() {
        return exp_on_player_kill;
    }

    public void setExp_on_player_kill(double exp_on_player_kill) {
        this.exp_on_player_kill = exp_on_player_kill;
    }

    public boolean isAllow_stats_reset() {
        return allow_stats_reset;
    }

    public void setAllow_stats_reset(boolean allow_stats_reset) {
        this.allow_stats_reset = allow_stats_reset;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public List<Integer> getLstClassId() {
        return lstClassId;
    }

    public void setLstClassId(List<Integer> lstClassId) {
        this.lstClassId = lstClassId;
    }
}
