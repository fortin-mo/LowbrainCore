package lowbrain.core.abstraction;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Mooffy on 2017-07-20.
 */
public abstract class Playable {
    protected Player player;

    protected Playable(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void sendMessage(String msg){
        sendMessage(msg, ChatColor.GREEN,"");
    }

    public void sendMessage(String msg, ChatColor color){
        sendMessage(msg, color,"");
    }

    public void sendMessage(String msg, String prefix){
        sendMessage(msg,ChatColor.GREEN, prefix);
    }

    public void sendMessage(String msg, ChatColor color, String prefix){
        this.getPlayer().sendMessage(prefix + color + msg);
    }
}
