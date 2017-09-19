package lowbrain.core.commun;

import lowbrain.library.config.YamlConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;

public class Internationalization extends YamlConfig {
    public Internationalization(String path, JavaPlugin plugin) {
        super(path, plugin);
    }

    public Internationalization(String path, JavaPlugin plugin, boolean load) {
        super(path, plugin, load);
    }

    /**
     * Format string from config file
     * @param path path to string in config file
     * @param args array of format arguments
     * @return formatted string
     */
    public String format(String path, Object[] args) {
        String str = this.getString(path, "");
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
    public String format(String path, Object arg) {
        if (arg == null)
            return this.format(path, new Object[0]);

        return this.format(path, new Object[]{arg});
    }

    /**
     * Format string from config file
     * @param path path to string in config file
     * @return string
     */
    public String format(String path) {
        return this.getString(path, "");
    }
}
