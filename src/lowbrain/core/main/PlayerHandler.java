package lowbrain.core.main;

import lowbrain.core.rpg.LowbrainPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player handler class
 * this class handles the connected players
 */
public class PlayerHandler {
    private LowbrainCore lowbrainCore;
    private Map<UUID, LowbrainPlayer> list;
    private int averageLevel;

    /**
     * initialize the handler
     * @param lowbrainCore instance of the pluging
     */
    public PlayerHandler(LowbrainCore lowbrainCore){
        this.lowbrainCore = lowbrainCore;
        list = new HashMap<>();
    }

    /**
     * remove player using bukking player instance
     * @param p Bukkit.Player
     * @return true
     */
    public boolean remove(Player p){
        return remove(p != null ? p.getUniqueId() : null);
    }

    /**
     * remove player using uuid
     * @param uuid Bukkit.Player.getUuid
     * @return true
     */
    public boolean remove(UUID uuid){
        return remove(getList().getOrDefault(uuid,null));
    }

    /**
     * remove player using LowbrainPlayer instance
     * @param p
     * @return true
     */
    public boolean remove(LowbrainPlayer p){
        if(p != null){
            p.disconnect();
            getList().remove(p.getPlayer().getUniqueId());
            computeAverageLevel();
        }
        return true;
    }

    /**
     * add new player with bukkit player instance
     * @param p bukkit.player
     * @return true
     */
    public boolean add(Player p){
        if(p == null) return false;
        getList().put(p.getUniqueId(), new LowbrainPlayer(p));
        computeAverageLevel();
        return true;
    }

    /**
     * add new player using uuid.
     * @param uuid bukkit.player.getuuid
     * @return
     */
    public boolean add(UUID uuid){
        if(uuid == null) return false;
        return add(Bukkit.getServer().getPlayer(uuid));
    }

    /**
     * add player using LowbrainPlayer instance
     * @param p LowbrainPlayer
     * @return true
     */
    public boolean add(LowbrainPlayer p){
        if(p == null) return false;
        if(p.getPlayer() == null) return false;
        getList().put(p.getPlayer().getUniqueId(),p);
        computeAverageLevel();
        return true;
    }

    /**
     * Save data of all connected player
     * @return
     */
    public boolean saveData(){
        for (LowbrainPlayer p:
                getList().values()) {
            p.saveData();
        }
        return true;
    }

    /**
     * reload all data from players
     * @return
     */
    public boolean reload() {
        for (LowbrainPlayer p:
                this.getList().values()) {
            p.reload();
        }
        return true;
    }

    /**
     * return the list of connected players
     * @return Map<UUID, LowbrainPlayer>
     */
    public Map<UUID, LowbrainPlayer> getList() {
        return list;
    }

    /**
     * return the average level
     * @return
     */
    public int getAverageLevel(){
        return this.getAverageLevel();
    }

    /**
     * compute the average level of connected players
     * is called when a player is removed of added
     * @return the average level
     */
    public int computeAverageLevel(){
        Double avg = 0.0;
        for (LowbrainPlayer rp :
                getList().values()) {
            avg += rp.getLvl();
        }

        avg = avg / getList().size();

        this.averageLevel = avg.intValue();

        return this.averageLevel;
    }
}
