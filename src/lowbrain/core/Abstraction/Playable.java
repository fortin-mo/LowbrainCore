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
        this.getPlayer().sendMessage(msg);
    }
}
