package lowbrain.core.config;

import lowbrain.core.main.LowbrainCore;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Moofy on 10/08/2016.
 */
public abstract class AbstractConfig {
    protected static File getDataFolder(){
        return JavaPlugin.getPlugin(LowbrainCore.class).getDataFolder();
    }
    protected static void saveResource(String s, Boolean b){
        JavaPlugin.getPlugin(LowbrainCore.class).saveResource(s,b);
    }
}
