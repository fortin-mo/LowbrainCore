package lowbrain.core.rpg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class StatsBoard {

    //CONSTANTS
    final static String SPACER = "  ";
    final static String INFO_OBJECTIVE = "LOWBRAIN INFO";
    final static String MANA = "MANA";
    final static String NEXT_LEVEL_IN = "NEXT LEVEL IN";
    final static String POINTS = "POINTS";
    final static String SKILL_POINTS = "SKILL POINTS";
    final static String KD = "KILLS";

    final static String DEXTERITY = "Dexterity";
    final static String AGILITY = "Agility";
    final static String INTELLIGENCE = "Intelligence";
    final static String DEFENCE = "Defence";
    final static String MAGIC_RESISTANCE = "Magic resistance";
    final static String STRENGTH = "Strength";
    final static String VITALITY = "Vitality";
    final static String COURAGE = "Courage";
    final static String REPUTATION = "Reputation";

    final static String MANA_PREFIX = ChatColor.GREEN + "MANA: " + ChatColor.WHITE;
    final static String NEXT_LEVEL_IN_PREFIX = ChatColor.GREEN + "LEVEL UP IN: " + ChatColor.WHITE;
    final static String POINTS_PREFIX = ChatColor.GREEN + "POINTS: " + ChatColor.WHITE;
    final static String SKILL_POINTS_PREFIX = ChatColor.GREEN + "SKILL POINTS: " + ChatColor.WHITE;
    final static String KD_PREFIX = ChatColor.GREEN + "K / D: " + ChatColor.WHITE;

    final static String DEXTERITY_PREFIX = ChatColor.GREEN + "Dexterity: " + ChatColor.WHITE;
    final static String AGILITY_PREFIX = ChatColor.GREEN + "Agility: " + ChatColor.WHITE;
    final static String INTELLIGENCE_PREFIX = ChatColor.GREEN + "Intelligence: " + ChatColor.WHITE;
    final static String DEFENCE_PREFIX = ChatColor.GREEN + "Defence: " + ChatColor.WHITE;
    final static String MAGIC_RESISTANCE_PREFIX = ChatColor.GREEN + "Magic resistance: " + ChatColor.WHITE;
    final static String STRENGTH_PREFIX = ChatColor.GREEN + "Strength: " + ChatColor.WHITE;
    final static String VITALITY_PREFIX = ChatColor.GREEN + "Vitality: " + ChatColor.WHITE;
    final static String COURAGE_PREFIX = ChatColor.GREEN + "Courage: " + ChatColor.WHITE;
    final static String REPUTATION_PREFIX = ChatColor.GREEN + "Reputation: " + ChatColor.WHITE;

    private LowbrainPlayer p;
    private Scoreboard scoreboard;
    private HashMap<String, Score> layout;

    /**
     * constructor for stats board
     * @param p LowbrainPlayer
     */
    public StatsBoard(LowbrainPlayer p) {
        this.p = p;
        this.init();
    }

    private void loadLayout() {
        layout = new HashMap<>();

        layout.put(MANA, new Score(MANA, MANA_PREFIX , 14));
        layout.put(NEXT_LEVEL_IN, new Score(NEXT_LEVEL_IN, NEXT_LEVEL_IN_PREFIX , 13));
        layout.put(POINTS, new Score(POINTS, POINTS_PREFIX , 12));
        layout.put(SKILL_POINTS, new Score(SKILL_POINTS, SKILL_POINTS_PREFIX , 11));
        layout.put(KD, new Score(KD, KD_PREFIX , 10));
        layout.put(SPACER, new Score(SPACER, SPACER, 9));
        layout.put(DEXTERITY, new Score(DEXTERITY, DEXTERITY_PREFIX , 8));
        layout.put(AGILITY, new Score(AGILITY, AGILITY_PREFIX , 7));
        layout.put(INTELLIGENCE, new Score(INTELLIGENCE, INTELLIGENCE_PREFIX , 6));
        layout.put(DEFENCE, new Score(DEFENCE, DEFENCE_PREFIX , 5));
        layout.put(MAGIC_RESISTANCE, new Score(MAGIC_RESISTANCE, MAGIC_RESISTANCE_PREFIX , 4));
        layout.put(STRENGTH, new Score(STRENGTH, STRENGTH_PREFIX , 3));
        layout.put(VITALITY, new Score(VITALITY, VITALITY_PREFIX , 2));
        layout.put(COURAGE, new Score(COURAGE, COURAGE_PREFIX , 1));
        layout.put(REPUTATION, new Score(REPUTATION, REPUTATION_PREFIX , 0));
    }

    /**
     * initialize individual player scoreboard for stats
     */
    private void init(){
        if(scoreboard != null)
            return; // already initialize

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objectiveInfo = scoreboard.registerNewObjective(INFO_OBJECTIVE, "dummy");
        objectiveInfo.setDisplayName(INFO_OBJECTIVE);

        loadLayout();

        display();

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
        display();
    }

    /**
     * refresh individual player scoreboard stats
     */
    public void refresh(){
        if (scoreboard == null)
            return;

        Objective info = scoreboard.getObjective(INFO_OBJECTIVE);

        info.setDisplayName(INFO_OBJECTIVE + " [" + p.getLvl() + "]");

        layout.get(MANA).setValue("" + (int)p.getCurrentMana() + " / " + (int)p.getMaxMana()).updateObjective(info);
        layout.get(NEXT_LEVEL_IN).setValue((int)(p.getNextLvl() - p.getExperience())).updateObjective(info);
        layout.get(POINTS).setValue(p.getPoints()).updateObjective(info);
        layout.get(SKILL_POINTS).setValue(p.getSkillPoints()).updateObjective(info);
        layout.get(KD).setValue("" + p.getKills() + " / " + p.getDeaths()).updateObjective(info);
        layout.get(SPACER).setValue("").updateObjective(info);
        layout.get(DEXTERITY).setValue(p.getDexterity()).updateObjective(info);
        layout.get(AGILITY).setValue(p.getAgility()).updateObjective(info);
        layout.get(INTELLIGENCE).setValue(p.getIntelligence()).updateObjective(info);
        layout.get(DEFENCE).setValue(p.getDefence()).updateObjective(info);
        layout.get(MAGIC_RESISTANCE).setValue(p.getMagicResistance()).updateObjective(info);
        layout.get(STRENGTH).setValue(p.getStrength()).updateObjective(info);
        layout.get(VITALITY).setValue(p.getVitality()).updateObjective(info);
        layout.get(COURAGE).setValue(p.getCourage()).updateObjective(info);
        layout.get(REPUTATION).setValue(p.getReputation()).updateObjective(info);
    }

    public void display() {
        Objective info = scoreboard.getObjective(INFO_OBJECTIVE);

        if(p.isShowStats()) {
            info.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            info.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }
    }
}
