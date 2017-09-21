package lowbrain.core.handlers;

import lowbrain.core.main.LowbrainCore;
import lowbrain.library.config.YamlConfig;
import lowbrain.library.config.YamlLocalize;

public class ConfigHandler {

    private YamlConfig classes;
    private YamlConfig config;
    private YamlConfig parameters;
    private YamlLocalize localization;
    private YamlConfig itemsRequirements;
    private YamlConfig mobsxp;
    private YamlConfig powers;
    private YamlConfig races;
    private YamlConfig skills;

    private LowbrainCore plugin;
    private boolean loaded;

    public ConfigHandler(LowbrainCore plugin) {
        this.plugin = plugin;
        this.loaded = false;

        classes = new YamlConfig("classes.yml", plugin, false);
        config = new YamlConfig("config.yml", plugin, false);
        parameters = new YamlConfig("default_parameters.yml", plugin, false);
        localization = new YamlLocalize("localization.yml", plugin, false);
        itemsRequirements = new YamlConfig("itemsrequirements.yml", plugin, false);
        mobsxp = new YamlConfig("mobsxp.yml", plugin, false);
        powers = new YamlConfig("powers.yml", plugin, false);
        races = new YamlConfig("races.yml", plugin, false);
        skills = new YamlConfig("skills.yml", plugin, false);
    }

    public ConfigHandler load() {
        return this.load(false);
    }

    public ConfigHandler load(boolean reload) {
        if (!this.loaded || reload)
            reload();

        return this;
    }

    public ConfigHandler reload() {
        classes.reload();
        config.reload();
        parameters.reload();
        localization.reload();
        itemsRequirements.reload();
        mobsxp.reload();
        powers.reload();
        races.reload();
        skills.reload();
        this.loaded = true;

        return this;
    }


    public YamlConfig classes() {
        return classes;
    }

    public YamlConfig config() {
        return config;
    }

    public YamlConfig parameters() {
        return parameters;
    }

    public YamlLocalize internationalization() {
        return localization;
    }

    public YamlConfig itemsRequirements() {
        return itemsRequirements;
    }

    public YamlConfig mobsxp() {
        return mobsxp;
    }

    public YamlConfig powers() {
        return powers;
    }

    public YamlConfig races() {
        return races;
    }

    public YamlConfig skills() {
        return skills;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
