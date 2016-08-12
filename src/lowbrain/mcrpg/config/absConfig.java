package lowbrain.mcrpg.config;

import lowbrain.mcrpg.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Moofy on 10/08/2016.
 */
public abstract class absConfig {
    protected static File getDataFolder(){
        return JavaPlugin.getPlugin(Main.class).getDataFolder();
    }
    protected static void saveResource(String s, Boolean b){
        JavaPlugin.getPlugin(Main.class).saveResource(s,b);
    }
}
