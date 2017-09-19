package lowbrain.core.rpg;

import org.bukkit.scoreboard.Objective;

public class Score {
    private String name;
    private String value;
    private int position;
    private String prefix;
    private String suffix;
    private String previous;

    public Score(String n, String prefix, int p) {
        name = n;
        this.prefix = prefix;
        position = p;
        previous = "";
        value = "";
        suffix = " ";
    }

    public Score(String n, String prefix, String v, int p) {
        name = n;
        this.prefix = prefix;
        value = v;
        previous = v;
        position = p;
        suffix = " ";
    }

    public Score setValue(Object o) {
        return this.setValue(o.toString());
    }

    public Score setValue(String value) {
        this.previous = this.value;
        this.value = value;
        return this;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public String getValue() {
        return value;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPrevious() {
        return previous;
    }

    public String getFullValue() {
        return prefix + value + suffix;
    }

    public String getFullPreviousValue() {
        return prefix + previous + suffix;
    }

    public void updateObjective(Objective o) {
        if (previous == value)
            return; // no need to do
        o.getScoreboard().resetScores(getFullPreviousValue());
        o.getScore(getFullValue()).setScore(getPosition());
    }
}
