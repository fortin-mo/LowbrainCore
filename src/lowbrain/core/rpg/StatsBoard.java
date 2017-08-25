package lowbrain.core.rpg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class StatsBoard {

    //CONSTANTS
    final static String CURRENT_MANA = ChatColor.GREEN + "CURRENT MANA: ";
    final static String MANA_PERCENTAGE = ChatColor.GREEN + "MANA %: ";
    final static String LEVEL = ChatColor.GREEN + "LEVEL: ";
    final static String XP = ChatColor.GREEN + "XP: ";
    final static String NEXT_LEVEL_IN = ChatColor.GREEN + "NEXT LEVEL IN: ";
    final static String POINTS = ChatColor.GREEN + "POINTS: ";
    final static String SKILL_POINTS = ChatColor.GREEN + "SKILL POINTS: ";
    final static String KILLS = ChatColor.GREEN + "KILLS: ";
    final static String DEATHS = ChatColor.GREEN + "DEATHS: ";

    private LowbrainPlayer p;
    private Scoreboard scoreboard;

    /**
     * constructor for stats board
     * @param p LowbrainPlayer
     */
    public StatsBoard(LowbrainPlayer p) {
        this.p = p;
        this.init();
    }

    /**
     * initialize individual player scoreboard for stats
     */
    private void init(){
        if(scoreboard != null)
            return; // already initialize

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objectiveInfo = scoreboard.registerNewObjective("Info", "dummy");
        objectiveInfo.setDisplayName("Info");

        if(p.isShowStats())
            objectiveInfo.setDisplaySlot(DisplaySlot.SIDEBAR);
        else
            objectiveInfo.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        this.p.getPlayer().setScoreboard(scoreboard);
    }

    /**
     * toggle stats display (hide or show)
     */
    public void toggle() {
        this.toggle(null);
    }

    /**
     * show stats scoreboard
     */
    public void show() {
        this.toggle(true);
    }

    /**
     * hide stats scoreboard
     */
    public void hide() {
        this.toggle(false);
    }

    /**
     * hide or show scoreboard stats
     * @param show force hide or show
     */
    public void toggle(Boolean show){
        if(scoreboard == null)
            return;

        if (show == null)
            show = !p.isShowStats();

        p.setShowStats(show);
        Objective info = scoreboard.getObjective("Info");

        if(p.isShowStats())
            info.setDisplaySlot(DisplaySlot.SIDEBAR);
        else
            info.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    /**
     * refresh individual player scoreboard stats
     */
    public void refresh(){
        if (scoreboard == null)
            return;

        Objective info = scoreboard.getObjective("Info");
        info.getScore(CURRENT_MANA).setScore((int)p.getCurrentMana());
        info.getScore(MANA_PERCENTAGE).setScore((int)(p.getCurrentMana() / p.getMaxMana() * 100));
        info.getScore(LEVEL).setScore(p.getLvl());
        info.getScore(XP).setScore((int)p.getExperience());
        info.getScore(NEXT_LEVEL_IN).setScore((int)(p.getNextLvl() - p.getExperience()));
        info.getScore(POINTS).setScore(p.getPoints());
        info.getScore(SKILL_POINTS).setScore(p.getSkillPoints());
        info.getScore(KILLS).setScore(p.getKills());
        info.getScore(DEATHS).setScore(p.getDeaths());

        Objective level = scoreboard.getObjective("Level");
        //level.getScore(this.getPlayer()).setScore(this.lvl);
    }
}
