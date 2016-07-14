package lowbrain.mcrpg.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lowbrian.mcrpg.commun.RPGPlayer;


public class PlayerListener implements Listener {

	public static Main plugin;

    public PlayerListener(Main instance) {
        plugin = instance;
    }
    
    public void onPlayerJoin(PlayerJoinEvent e) throws IOException{
    	Player p = e.getPlayer();
        File userdata = new File(plugin.getDataFolder(), File.separator + "PlayerDB");
        File f = new File(userdata, File.separator + p.getUniqueId() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

        //When the player file is created for the first time...
        if (!f.exists()) { 
            playerData.createSection("Class");
            playerData.set("Class.isSet", false);
            playerData.set("Class.id", "");
            
            playerData.createSection("Stats");
            playerData.set("Stats.health", 0);
            playerData.set("Stats.lvl", 1);
            playerData.set("Stats.strength", 0);
            playerData.set("Stats.intelligence", 0);
            playerData.set("Stats.dexterity", 0);
            playerData.set("Stats.defence", 0);
            playerData.set("Stats.points", plugin.config.getInt("Settings.starting_points"));
            playerData.set("Stats.experience", 0);
            playerData.set("Stats.nextLvl",plugin.config.getDouble("Settings.first_lvl_exp"));
            
            playerData.save(f);
        }
        plugin.lstPlayer.add(new RPGPlayer(p));
    }
    
    public void onPlayerQuit(PlayerQuitEvent e){
    	Player p = e.getPlayer();
    	RPGPlayer rp = null;
    	for (int i = 0; i < plugin.lstPlayer.size(); i++) {
			if(plugin.lstPlayer.get(i).getPlayer().equals(p)){
				rp = plugin.lstPlayer.get(i);
				break;
			}
		}
    	if(rp != null){
    		rp.SaveData();
    	}
    	plugin.lstPlayer.remove(rp);
    }
}
