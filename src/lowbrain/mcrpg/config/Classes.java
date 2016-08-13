package lowbrain.mcrpg.config;

import lowbrain.mcrpg.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Moofy on 10/08/2016.
 */
public class Classes extends absConfig {
    private static FileConfiguration instance;
    private static File file;
    public static FileConfiguration getInstance(){
        if(instance == null){
            file = new File(getDataFolder(),"classes.yml");

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                saveResource("classes.yml", false);
            }

            instance = new YamlConfiguration();

            try {
                instance.load(file);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return instance;
    }
    public static void reload(){
        instance = null;
    }


}


