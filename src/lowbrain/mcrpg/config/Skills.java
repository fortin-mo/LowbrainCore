package lowbrain.mcrpg.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by Moofy on 10/08/2016.
 */
public class Skills extends absConfig {
    private static FileConfiguration instance;
    private static File classf;

    public static FileConfiguration getInstance(){
        if(instance == null){
            classf = new File(getDataFolder(),"skills.yml");

            if (!classf.exists()) {
                classf.getParentFile().mkdirs();
                saveResource("skills.yml", false);
            }

            instance = new YamlConfiguration();

            try {
                instance.load(classf);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return instance;
    }


}


