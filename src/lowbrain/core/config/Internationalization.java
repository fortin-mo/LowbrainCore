package lowbrain.core.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.MessageFormat;

/**
 * Created by Moofy on 10/08/2016.
 */
public class Internationalization extends AbstractConfig {
    private static FileConfiguration instance;
    private static File file;
    public static FileConfiguration getInstance(){
        if(instance == null){
            file = new File(getDataFolder(),"internationalization.yml");

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                saveResource("internationalization.yml", false);
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

    /**
     * Format string from config file
     * @param path path to string in config file
     * @param args array of format arguments
     * @return formated string
     */
    public static String format(String path, Object[] args) {
        String str = Internationalization.getInstance().getString(path, "");
        if (str.isEmpty())
            return "";

        if (args == null)
            args = new Object[0];

        MessageFormat fmt = new MessageFormat(str);
        return fmt.format(args);
    }

    /**
     * Format string from config file
     * @param path path to string in config file
     * @param arg format arguments
     * @return formated string
     */
    public static String format(String path, Object arg) {
        if (arg == null)
            return Internationalization.format(path, new Object[0]);

        return Internationalization.format(path, new Object[]{arg});
    }

    /**
     * Format string from config file
     * @param path path to string in config file
     * @return string
     */
    public static String format(String path) {
        return Internationalization.getInstance().getString(path, "");
    }


}


