package lowbrain.core.main;

import lowbrain.core.rpg.LowbrainPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Moofy on 07/09/2016.
 */
public class PlayerHandler {
    private LowbrainCore lowbrainCore;
    private Map<UUID, LowbrainPlayer> list;
    private int averageLevel;

    public PlayerHandler(LowbrainCore lowbrainCore){
        this.lowbrainCore = lowbrainCore;
        list = new HashMap<>();
    }

    public boolean remove(Player p){
        return remove(p != null ? p.getUniqueId() : null);
    }

    public boolean remove(UUID uuid){
        return remove(getList().getOrDefault(uuid,null));
    }

    public boolean remove(LowbrainPlayer p){
        if(p != null){
            p.disconnect();
            getList().remove(p.getPlayer().getUniqueId());
            computeAverageLevel();
        }
        return true;
    }

    public boolean add(Player p){
        if(p == null) return false;
        getList().put(p.getUniqueId(), new LowbrainPlayer(p));
        computeAverageLevel();
        return true;
    }

    public boolean add(UUID uuid){
        if(uuid == null) return false;
        return add(Bukkit.getServer().getPlayer(uuid));
    }

    public boolean add(LowbrainPlayer p){
        if(p == null) return false;
        if(p.getPlayer() == null) return false;
        getList().put(p.getPlayer().getUniqueId(),p);
        computeAverageLevel();
        return true;
    }

    public boolean saveData(){
        for (LowbrainPlayer p:
                getList().values()) {
            p.saveData();
        }
        return true;
    }

    public boolean reload() {
        for (LowbrainPlayer p:
                this.getList().values()) {
            p.reload();
        }
        return true;
    }

    public Map<UUID, LowbrainPlayer> getList() {
        return list;
    }

    public int getAverageLevel(){
        return this.getAverageLevel();
    }

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
