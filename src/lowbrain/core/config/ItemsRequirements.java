package lowbrain.core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by Moofy on 10/08/2016.
 */
public class ItemsRequirements extends AbstractConfig {
    private static FileConfiguration instance;
    private static File file;
    public static FileConfiguration getInstance(){
        if(instance == null){
            file = new File(getDataFolder(),"itemsrequirements.yml");

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                saveResource("itemsrequirements.yml", false);
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


