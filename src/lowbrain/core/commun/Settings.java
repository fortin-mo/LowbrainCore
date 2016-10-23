package lowbrain.core.commun;

import it.unimi.dsi.fastutil.Hash;
import lowbrain.core.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.jar.Pack200;

/**
 * Created by Moofy on 14/07/2016.
 */
public class Settings {

    private static Settings instance;

    public boolean op_bypass_permission;
    public float first_lvl_exp;
    public int max_lvl;
    public int max_stats;
    public boolean can_switch_class;
    public boolean can_switch_race;
    public boolean allow_deduction_points;
    public int points_per_lvl;
    public int starting_points;
    public boolean auto_save;
    public int save_interval;
    public boolean allow_stats_reset;
    public boolean allow_complete_reset;
    public boolean debug;
    public int mana_regen_interval;
    public int starting_skill_points;
    public int skill_points_level_interval;
    public int skill_points_per_interval;
    public boolean disable_mob_no_tick_damage;
    public boolean hard_core_enable;
    public int hard_core_max_deaths;

    public boolean asd_enable;
    public int asd_easy_from;
    public int asd_easy_to;
    public int asd_medium_from;
    public int asd_medium_to;
    public int asd_hard_from;
    public int asd_hard_to;

    public boolean group_xp_enable;
    public float group_xp_range;
    public float group_xp_main;
    public float group_xp_others;

    public MathSettings maths;

    private Settings(FileConfiguration config){
        hard_core_enable = config.getBoolean("hard_core.enable");
        hard_core_max_deaths = config.getInt("hard_core.max_deaths");
        disable_mob_no_tick_damage = config.getBoolean("disable_mob_no_tick_damage");
        op_bypass_permission = config.getBoolean("op_bypass_permission");
        allow_deduction_points = config.getBoolean("allow_deduction_points");
        can_switch_class = config.getBoolean("can_switch_class");
        can_switch_race = config.getBoolean("can_switch_race");
        first_lvl_exp = (float)config.getDouble("first_lvl_exp");
        max_lvl = config.getInt("max_lvl");
        max_stats = config.getInt("max_stats");
        points_per_lvl = config.getInt("points_per_lvl");
        starting_points = config.getInt("starting_points");
        auto_save = config.getBoolean("auto_save");
        save_interval = config.getInt("save_interval");
        allow_stats_reset = config.getBoolean("allow_stats_reset");
        allow_complete_reset = config.getBoolean("allow_complete_reset");
        debug = config.getBoolean("debug");
        mana_regen_interval = config.getInt("mana_regen_interval");

        asd_enable = config.getBoolean("automatic_server_difficulty.enable");
        asd_easy_from = config.getInt("automatic_server_difficulty.easy.from");
        asd_easy_to = config.getInt("automatic_server_difficulty.easy.to");
        asd_medium_from = config.getInt("automatic_server_difficulty.medium.from");
        asd_medium_to = config.getInt("automatic_server_difficulty.medium.to");
        asd_hard_from = config.getInt("automatic_server_difficulty.hard.from");
        asd_hard_to = config.getInt("automatic_server_difficulty.hard.to");

        group_xp_enable = config.getBoolean("group_xp_enable.enable");
        group_xp_range = (float)config.getDouble("group_xp.range");
        group_xp_main = (float)config.getDouble("group_xp.main");
        group_xp_others = (float)config.getDouble("group_xp.others");

        starting_skill_points = config.getInt("starting_skill_points");
        skill_points_level_interval = config.getInt("skill_points_level_interval");
        skill_points_per_interval = config.getInt("skill_points_per_interval");
        maths = new MathSettings(config);
    }

    public static Settings getInstance(){
        if(instance == null){
            instance = new Settings(Config.getInstance());
        }
        return instance;
    }

    public static void reload(){
        instance = null;
    }

    public class MathSettings {

        public float next_lvl_multiplier;
        public float natural_xp_gain_multiplier;
        public float killer_level_gain_multiplier;
        public float level_difference_multiplier;
        public float killer_base_exp;
        public boolean player_kills_player_exp_enable;
        public int function_type;

        public onPlayerConsumePotion onPlayerConsumePotion;
        public onPlayerGetDamaged onPlayerGetDamaged;
        public playerAttributes playerAttributes;
        public onPlayerShootBow onPlayerShootBow;
        public onPlayerAttackEntity onPlayerAttackEntity;
        public onPlayerDies onPlayerDies;

        public MathSettings(FileConfiguration config) {

            this.function_type = config.getInt("maths.function_type");
            this.next_lvl_multiplier = (float)config.getDouble("maths.next_lvl_multiplier");
            this.natural_xp_gain_multiplier = (float)config.getDouble("maths.natural_xp_gain_multiplier");
            this.killer_level_gain_multiplier = (float)config.getDouble("maths.on_player_kills_player.killer_level_gain_multiplier");
            this.level_difference_multiplier = (float)config.getDouble("maths.on_player_kills_player.level_difference_multiplier");
            this.killer_base_exp = (float)config.getDouble("maths.on_player_kills_player.killer_base_exp");
            this.player_kills_player_exp_enable = config.getBoolean("maths.on_player_kills_player.enable");

            onPlayerAttackEntity = new onPlayerAttackEntity(config.getConfigurationSection("maths.on_player_attack_entity"));
            onPlayerConsumePotion = new onPlayerConsumePotion(config.getConfigurationSection("maths.on_player_consume_potion"));
            onPlayerShootBow = new onPlayerShootBow(config.getConfigurationSection("maths.on_player_shoot_bow"));
            playerAttributes = new playerAttributes(config.getConfigurationSection("maths.player_attributes"));
            onPlayerGetDamaged = new onPlayerGetDamaged(config.getConfigurationSection("maths.on_player_get_damaged"));
            onPlayerDies = new onPlayerDies(config.getConfigurationSection("maths.on_player_dies"));
        }

        public class onPlayerAttackEntity{
            public chanceOfCreatingMagicAttack chanceOfCreatingMagicAttack;
            public creatingMagicAttack creatingMagicAttack;
            public attackEntityBy attackEntityBy;
            public criticalHit criticalHit;
            public chanceOfMissing chanceOfMissing;
            public backStab backStab;

            onPlayerAttackEntity(ConfigurationSection config){
                if(config == null) throw new NullPointerException("ConfigurationSection for onPlayerAttackEntity cannot be null");
                chanceOfCreatingMagicAttack = new chanceOfCreatingMagicAttack(config.getConfigurationSection("chance_of_creating_magic_attack"));
                creatingMagicAttack = new creatingMagicAttack(config.getConfigurationSection("creating_magic_attack"));
                attackEntityBy = new attackEntityBy(config);
                criticalHit = new criticalHit(config.getConfigurationSection("critical_hit"));
                chanceOfMissing = new chanceOfMissing(config.getConfigurationSection("chance_of_missing"));
                backStab = new backStab(config.getConfigurationSection("backstab"));
            }

            public class criticalHit {
                public boolean enable;
                public float maximumChance;
                public float minimumChance;
                public float chanceRange;
                public HashMap<String,Float> chanceVariables;
                public float maximumDamageMultiplier;
                public float minimumDamageMultiplier;
                public float damageMultiplierRange;
                public HashMap<String,Float> damageMultiplierVariables;
                public String chanceFunction;
                public String damageMultiplierFunction;

                public criticalHit(ConfigurationSection config){
                    chanceVariables = new HashMap<>();
                    damageMultiplierVariables = new HashMap<>();
                    enable = config.getBoolean("enable");
                    chanceRange = (float)config.getDouble("chance.range");
                    maximumChance = (float)config.getDouble("chance.maximum");
                    minimumChance = (float)config.getDouble("chance.minimum");
                    maximumDamageMultiplier = (float)config.getDouble("damage_multiplier.maximum");
                    minimumDamageMultiplier = (float)config.getDouble("damage_multiplier.minimum");
                    damageMultiplierRange = (float)config.getDouble("damage_multiplier.range");
                    chanceFunction = config.getString("chance.function");
                    damageMultiplierFunction = config.getString("damage_multiplier.function");


                    ConfigurationSection chanceVar = config.getConfigurationSection("chance.variables");
                    if(chanceVar != null){
                        for (String k :
                                chanceVar.getKeys(false)) {
                            chanceVariables.put(k,(float)chanceVar.getDouble(k,0));
                        }
                    }

                    ConfigurationSection dmVar = config.getConfigurationSection("damage_multiplier.variables");
                    if(dmVar != null){
                        for (String k :
                                dmVar.getKeys(false)) {
                            damageMultiplierVariables.put(k,(float)dmVar.getDouble(k,0));
                        }
                    }

                }
            }

            public class chanceOfMissing{
                public boolean enable;
                public String function;
                public float maximum;
                public float minimum;
                public HashMap<String,Float> variables;

                public chanceOfMissing(ConfigurationSection config){
                    variables = new HashMap<>();
                    enable = config.getBoolean("enable");
                    maximum  = (float)config.getDouble("maximum");
                    minimum  = (float)config.getDouble("minimum");
                    function = config.getString("function");
                    ConfigurationSection vars = config.getConfigurationSection("variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                }
            }

            public class chanceOfCreatingMagicAttack{

                //chance_of_creating_magic_attact_:
                public float maximum;
                public float minimum;
                public HashMap<String,Float> variables;

                public chanceOfCreatingMagicAttack(ConfigurationSection config){
                    this.maximum = (float)config.getDouble("maximum");
                    this.minimum = (float)config.getDouble("minimum");
                    variables = new HashMap<>();
                    ConfigurationSection vars = config.getConfigurationSection("variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            variables.put(var,(float)vars.getDouble(var));
                        }
                    }
                }
            }

            public class creatingMagicAttack{

                //=
                public boolean enable;
                public float maximum_duration;
                public float minimum_duration;
                public HashMap<String,Float> duration_variables;

                public boolean harm_enable;
                public boolean slow_enable;
                public boolean poison_enable;
                public boolean weakness_enable;
                public boolean wither_enable;
                public boolean blindness_enable;
                public boolean confusion_enable;


                public float harm_amplifier_maximum;
                public float poison_amplifier_maximum;
                public float slow_amplifier_maximum;
                public float weakness_amplifier_maximum;
                public float wither_amplifier_maximum;
                public float harm_amplifier_minimum;
                public float poison_amplifier_minimum;
                public float slow_amplifier_minimum;
                public float weakness_amplifier_minimum;
                public float wither_amplifier_minimum;
                public HashMap<String,Float> harm_amplifier_variables;
                public HashMap<String,Float> poison_amplifier_variables;
                public HashMap<String,Float> wither_amplifier_variables;
                public HashMap<String,Float> slow_amplifier_variables;
                public HashMap<String,Float> weakness_amplifier_variables;

                public creatingMagicAttack(ConfigurationSection config){

                    harm_amplifier_variables = new HashMap<>();
                    poison_amplifier_variables = new HashMap<>();
                    wither_amplifier_variables = new HashMap<>();
                    slow_amplifier_variables = new HashMap<>();
                    weakness_amplifier_variables = new HashMap<>();
                    duration_variables = new HashMap<>();

                    harm_enable = config.getBoolean("harm.enable");
                    slow_enable = config.getBoolean("slow.enable");
                    poison_enable = config.getBoolean("poison.enable");
                    weakness_enable = config.getBoolean("weakness.enable");
                    wither_enable = config.getBoolean("wither.enable");
                    blindness_enable = config.getBoolean("wither.enable");
                    confusion_enable = config.getBoolean("wither.enable");

                    this.enable = config.getBoolean("enable");
                    this.maximum_duration = (float)config.getDouble("duration.maximum");
                    this.minimum_duration = (float)config.getDouble("duration.minimum");

                    this.harm_amplifier_maximum = (float)config.getDouble("harm.maximum");
                    this.poison_amplifier_maximum = (float)config.getDouble("poison.maximum");
                    this.slow_amplifier_maximum = (float)config.getDouble("slow.maximum");
                    this.weakness_amplifier_maximum = (float)config.getDouble("weakness.maximum");
                    this.wither_amplifier_maximum = (float)config.getDouble("wither.maximum");
                    this.harm_amplifier_minimum = (float)config.getDouble("harm.minimum");
                    this.poison_amplifier_minimum = (float)config.getDouble("poison.minimum");
                    this.slow_amplifier_minimum = (float)config.getDouble("slow.minimum");
                    this.weakness_amplifier_minimum = (float)config.getDouble("weakness,minimum");
                    this.wither_amplifier_minimum = (float)config.getDouble("wither.minimum");

                    ConfigurationSection vars;
                    vars = config.getConfigurationSection("harm.variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            harm_amplifier_variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                    vars = config.getConfigurationSection("poison.variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            poison_amplifier_variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                    vars = config.getConfigurationSection("wither.variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            wither_amplifier_variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                    vars = config.getConfigurationSection("slow.variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            slow_amplifier_variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                    vars = config.getConfigurationSection("weakness.variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            weakness_amplifier_variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                    vars = config.getConfigurationSection("duration.variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            duration_variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                }
            }

            public class attackEntityBy{
                //normal_attack_damage_=
                public boolean weapon_enable;
                public String weapon_function;
                public HashMap<String,Float> weapon_variables;
                public float weapon_maximum;
                public float weapon_minimum;
                public float weapon_range;

                //magic_attack_damage_=
                public boolean magic_enable;
                public String magic_function;
                public HashMap<String,Float> magic_variables;
                public float magic_maximum;
                public float magic_minimum;
                public float magic_range;

                //arrow_attack_damage_=
                public boolean projectile_enable;
                public String projectile_function;
                public HashMap<String,Float> projectile_variables;
                public float projectile_maximum;
                public float projectile_minimum;
                public float projectile_range;

                public attackEntityBy(ConfigurationSection config){

                    projectile_variables = new HashMap<>();
                    weapon_variables = new HashMap<>();
                    magic_variables = new HashMap<>();

                    this.weapon_enable = config.getBoolean("by_weapon.enable");
                    this.weapon_function = config.getString("by_weapon.function");
                    this.weapon_maximum = (float)config.getDouble("by_weapon.maximum");
                    this.weapon_minimum = (float)config.getDouble("by_weapon.minimum");
                    this.weapon_range = (float)config.getDouble("by_weapon.range");
                    ConfigurationSection weapSection = config.getConfigurationSection("by_weapon.variables");
                    if(weapSection != null){
                        for (String key :
                                weapSection.getKeys(false)) {
                            weapon_variables.put(key,(float)weapSection.getDouble(key));
                        }
                    }


                    this.magic_enable = config.getBoolean("by_magic.enable");
                    this.magic_function = config.getString("by_magic.function");
                    this.magic_maximum = (float)config.getDouble("by_magic.maximum");
                    this.magic_minimum = (float)config.getDouble("by_magic.minimum");
                    this.magic_range = (float)config.getDouble("by_magic.range");
                    ConfigurationSection magicSection = config.getConfigurationSection("by_magic.variables");
                    if(magicSection != null){
                        for (String key :
                                magicSection.getKeys(false)) {
                            magic_variables.put(key,(float)magicSection.getDouble(key));
                        }
                    }

                    this.projectile_enable = config.getBoolean("by_projectile.enable");
                    this.projectile_function = config.getString("by_projectile.function");
                    this.projectile_maximum = (float)config.getDouble("by_projectile.maximum");
                    this.projectile_minimum = (float)config.getDouble("by_projectile.minimum");
                    this.projectile_range = (float)config.getDouble("by_projectile.range");
                    ConfigurationSection projSection = config.getConfigurationSection("by_projectile.variables");
                    if(projSection != null){
                        for (String key :
                                projSection.getKeys(false)) {
                            projectile_variables.put(key,(float)projSection.getDouble(key));
                        }
                    }
                }
            }

            public class backStab{
                public boolean enable;
                public String function;
                public float maximum;
                public float minimum;
                public HashMap<String, Float> variables;
                public float range;

                public backStab(ConfigurationSection config){
                    variables = new HashMap<>();
                    enable = config.getBoolean("enable");
                    range = (float)config.getDouble("range");
                    maximum  = (float)config.getDouble("maximum");
                    minimum  = (float)config.getDouble("minimum");
                    function = config.getString("function");
                    ConfigurationSection vars = config.getConfigurationSection("variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                }
            }
        }

        public class onPlayerConsumePotion{
            //=
            public boolean enable;
            public String function;
            public float range;
            public float maximum;
            public float minimum;
            public HashMap<String,Float> variables;
            onPlayerConsumePotion(ConfigurationSection config){
                variables = new HashMap<>();
                if(config == null)throw new NullPointerException("ConfigurationSection for onPlayerConsumePotion cannot be null");
                this.enable = config.getBoolean("enable");
                this.function = config.getString("function");
                this.range = (float)config.getDouble("range");
                this.maximum = (float)config.getDouble("maximum");
                this.minimum = (float)config.getDouble("minimum");
                ConfigurationSection sec = config.getConfigurationSection("variables");
                if(sec != null){
                    for (String key :
                            sec.getKeys(false)) {
                        variables.put(key,(float)sec.getDouble(key));
                    }
                }
            }
        }

        public class onPlayerGetDamaged{

            public boolean by_magic_enable;
            public String by_magic_function;
            public float by_magic_maximum;
            public float by_magic_minimum;
            public HashMap<String,Float> by_magic_variables;

            public boolean by_poison_enable;
            public String by_poison_function;
            public float by_poison_maximum;
            public float by_poison_minimum;
            public HashMap<String,Float> by_poison_variables;

            public boolean by_wither_enable;
            public String by_wither_function;
            public float by_wither_maximum;
            public float by_wither_minimum;
            public HashMap<String,Float> by_wither_variables;

            public boolean by_void_enable;
            public String by_void_function;
            public float by_void_maximum;
            public float by_void_minimum;
            public HashMap<String,Float> by_void_variables;

            public boolean by_fire_enable;
            public String by_fire_function;
            public float by_fire_maximum;
            public float by_fire_minimum;
            public HashMap<String,Float> by_fire_variables;

            public boolean by_lava_enable;
            public String by_lava_function;
            public float by_lava_maximum;
            public float by_lava_minimum;
            public HashMap<String,Float> by_lava_variables;

            public boolean by_lightning_enable;
            public String by_lightning_function;
            public float by_lightning_maximum;
            public float by_lightning_minimum;
            public HashMap<String,Float> by_lightning_variables;

            public boolean by_hot_floor_enable;
            public String by_hot_floor_function;
            public float by_hot_floor_maximum;
            public float by_hot_floor_minimum;
            public HashMap<String,Float> by_hot_floor_variables;

            public boolean by_fire_tick_enable;
            public String by_fire_tick_function;
            public float by_fire_tick_maximum;
            public float by_fire_tick_minimum;
            public HashMap<String,Float> by_fire_tick_variables;

            public boolean by_explosion_enable;
            public String by_explosion_function;
            public float by_explosion_maximum;
            public float by_explosion_minimum;
            public HashMap<String,Float> by_explosion_variables;

            public boolean by_fall_enable;
            public String by_fall_function;
            public float by_fall_maximum;
            public float by_fall_minimum;
            public HashMap<String,Float> by_fall_variables;

            public boolean by_fly_into_wall_enable;
            public String by_fly_into_wall_function;
            public float by_fly_into_wall_maximum;
            public float by_fly_into_wall_minimum;
            public HashMap<String,Float> by_fly_into_wall_variables;


            public boolean by_contact_enable;
            public String by_contact_function;
            public float by_contact_maximum;
            public float by_contact_minimum;
            public HashMap<String,Float> by_contact_variables;

            public boolean by_arrow_enable;
            public String by_arrow_function;
            public float by_arrow_maximum;
            public float by_arrow_minimum;
            public HashMap<String,Float> by_arrow_variables;

            public boolean by_projectile_enable;
            public String by_projectile_function;
            public float by_projectile_maximum;
            public float by_projectile_minimum;
            public HashMap<String,Float> by_projectile_variables;

            public boolean by_weapon_enable;
            public String by_weapon_function;
            public float by_weapon_maximum;
            public float by_weapon_minimum;
            public HashMap<String,Float> by_weapon_variables;

            public boolean by_suffocation_enable;
            public String by_suffocation_function;
            public float by_suffocation_maximum;
            public float by_suffocation_minimum;
            public HashMap<String,Float> by_suffocation_variables;

            public boolean by_starvation_enable;
            public String by_starvation_function;
            public float by_starvation_maximum;
            public float by_starvation_minimum;
            public HashMap<String,Float> by_starvation_variables;

            public boolean by_drowning_enable;
            public String by_drowning_function;
            public float by_drowning_maximum;
            public float by_drowning_minimum;
            public HashMap<String,Float> by_drowning_variables;

            public boolean by_default_enable;
            public String by_default_function;
            public float by_default_maximum;
            public float by_default_minimum;
            public HashMap<String,Float> by_default_variables;

            public chanceOfRemovingMagicEffect chanceOfRemovingMagicEffect;
            public reducingBadPotionEffect reducingBadPotionEffect;
            public chanceOfDodging chanceOfDodging;

            public onPlayerGetDamaged(ConfigurationSection config){
                if(config == null) throw new NullPointerException("ConfigurationSection for OnPLayerGetDamaged cannot be null");

                chanceOfRemovingMagicEffect = new chanceOfRemovingMagicEffect(config.getConfigurationSection("chance_of_removing_magic_effect"));
                reducingBadPotionEffect = new reducingBadPotionEffect(config.getConfigurationSection("reducing_bad_potion_effect"));
                chanceOfDodging = new chanceOfDodging(config.getConfigurationSection("chance_of_dodging"));


                by_magic_variables = new HashMap<>();
                by_poison_variables = new HashMap<>();
                by_wither_variables = new HashMap<>();
                by_void_variables = new HashMap<>();
                by_fire_variables = new HashMap<>();
                by_lava_variables = new HashMap<>();
                by_lightning_variables = new HashMap<>();
                by_hot_floor_variables = new HashMap<>();
                by_fire_tick_variables = new HashMap<>();
                by_explosion_variables = new HashMap<>();
                by_fall_variables = new HashMap<>();
                by_fly_into_wall_variables = new HashMap<>();
                by_contact_variables = new HashMap<>();
                by_arrow_variables = new HashMap<>();
                by_projectile_variables = new HashMap<>();
                by_weapon_variables = new HashMap<>();
                by_suffocation_variables = new HashMap<>();
                by_starvation_variables = new HashMap<>();
                by_drowning_variables = new HashMap<>();
                by_default_variables = new HashMap<>();

                ConfigurationSection secVariables;

                this.by_magic_enable = config.getBoolean("by_magic.enable");
                this.by_magic_function = config.getString("by_magic.function");
                this.by_magic_maximum = (float)config.getDouble("by_magic.maximum");
                this.by_magic_minimum = (float)config.getDouble("by_magic.minimum");
                secVariables = config.getConfigurationSection("by_magic.variables");
                if(secVariables != null){
                    for (String var:
                         secVariables.getKeys(false)) {
                        by_magic_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_poison_enable = config.getBoolean("by_poison.enable");
                this.by_poison_function = config.getString("by_poison.function");
                this.by_poison_maximum = (float)config.getDouble("by_poison.maximum");
                this.by_poison_minimum = (float)config.getDouble("by_poison.minimum");
                secVariables = config.getConfigurationSection("by_poison.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_poison_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_wither_enable = config.getBoolean("by_wither.enable");
                this.by_wither_function = config.getString("by_wither.function");
                this.by_wither_maximum = (float)config.getDouble("by_wither.maximum");
                this.by_wither_minimum = (float)config.getDouble("by_wither.minimum");
                secVariables = config.getConfigurationSection("by_wither.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_wither_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_void_enable = config.getBoolean("by_void.enable");
                this.by_void_function = config.getString("by_void.function");
                this.by_void_maximum = (float)config.getDouble("by_void.maximum");
                this.by_void_minimum = (float)config.getDouble("by_void.minimum");
                secVariables = config.getConfigurationSection("by_void.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_void_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_fire_enable = config.getBoolean("by_fire.enable");
                this.by_fire_function = config.getString("by_fire.function");
                this.by_fire_maximum = (float)config.getDouble("by_fire.maximum");
                this.by_fire_minimum = (float)config.getDouble("by_fire.minimum");
                secVariables = config.getConfigurationSection("by_fire.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_fire_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_lava_enable = config.getBoolean("by_lava.enable");
                this.by_lava_function = config.getString("by_lava.function");
                this.by_lava_maximum = (float)config.getDouble("by_lava.maximum");
                this.by_lava_minimum = (float)config.getDouble("by_lava.minimum");
                secVariables = config.getConfigurationSection("by_lava.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_lava_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_lightning_enable = config.getBoolean("by_lightning.enable");
                this.by_lightning_function = config.getString("by_lightning.function");
                this.by_lightning_maximum = (float)config.getDouble("by_lightning.maximum");
                this.by_lightning_minimum = (float)config.getDouble("by_lightning.minimum");
                secVariables = config.getConfigurationSection("by_lightning.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_lightning_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_hot_floor_enable = config.getBoolean("by_hot_floor.enable");
                this.by_hot_floor_function = config.getString("by_hot_floor.function");
                this.by_hot_floor_maximum = (float)config.getDouble("by_hot_floor.maximum");
                this.by_hot_floor_minimum = (float)config.getDouble("by_hot_floor.minimum");
                secVariables = config.getConfigurationSection("by_hot_floor.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_hot_floor_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_fire_tick_enable = config.getBoolean("by_fire_tick.enable");
                this.by_fire_tick_function = config.getString("by_fire_tick.function");
                this.by_fire_tick_maximum = (float)config.getDouble("by_fire_tick.maximum");
                this.by_fire_tick_minimum = (float)config.getDouble("by_fire_tick.minimum");
                secVariables = config.getConfigurationSection("by_fire_tick.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_fire_tick_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_explosion_enable = config.getBoolean("by_explosion.enable");
                this.by_explosion_function = config.getString("by_explosion.function");
                this.by_explosion_maximum = (float)config.getDouble("by_explosion.maximum");
                this.by_explosion_minimum = (float)config.getDouble("by_explosion.minimum");
                secVariables = config.getConfigurationSection("by_explosion.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_explosion_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_fall_enable = config.getBoolean("by_fall.enable");
                this.by_fall_function = config.getString("by_fall.function");
                this.by_fall_maximum = (float)config.getDouble("by_fall.maximum");
                this.by_fall_minimum = (float)config.getDouble("by_fall.minimum");
                secVariables = config.getConfigurationSection("by_fall.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_fall_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_fly_into_wall_enable = config.getBoolean("by_fly_into_wall.enable");
                this.by_fly_into_wall_function = config.getString("by_fly_into_wall.function");
                this.by_fly_into_wall_maximum = (float)config.getDouble("by_fly_into_wall.maximum");
                this.by_fly_into_wall_minimum = (float)config.getDouble("by_fly_into_wall.minimum");
                secVariables = config.getConfigurationSection("by_fly_into_wall.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_fly_into_wall_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_contact_enable = config.getBoolean("by_contact.enable");
                this.by_contact_function = config.getString("by_contact.function");
                this.by_contact_maximum = (float)config.getDouble("by_contact.maximum");
                this.by_contact_minimum = (float)config.getDouble("by_contact.minimum");
                secVariables = config.getConfigurationSection("by_contact.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_contact_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_arrow_enable = config.getBoolean("by_arrow.enable");
                this.by_arrow_function = config.getString("by_arrow.function");
                this.by_arrow_maximum = (float)config.getDouble("by_arrow.maximum");
                this.by_arrow_minimum = (float)config.getDouble("by_arrow.minimum");
                secVariables = config.getConfigurationSection("by_arrow.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_arrow_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_projectile_enable = config.getBoolean("by_projectile.enable");
                this.by_projectile_function = config.getString("by_projectile.function");
                this.by_projectile_maximum = (float)config.getDouble("by_projectile.maximum");
                this.by_projectile_minimum = (float)config.getDouble("by_projectile.minimum");
                secVariables = config.getConfigurationSection("by_projectile.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_projectile_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }
 
                this.by_weapon_enable = config.getBoolean("by_weapon.enable");
                this.by_weapon_function = config.getString("by_weapon.function");
                this.by_weapon_maximum = (float)config.getDouble("by_weapon.maximum");
                this.by_weapon_minimum = (float)config.getDouble("by_weapon.minimum");
                secVariables = config.getConfigurationSection("by_weapon.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_weapon_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_suffocation_enable = config.getBoolean("by_suffocation.enable");
                this.by_suffocation_function = config.getString("by_suffocation.function");
                this.by_suffocation_maximum = (float)config.getDouble("by_suffocation.maximum");
                this.by_suffocation_minimum = (float)config.getDouble("by_suffocation.minimum");
                secVariables = config.getConfigurationSection("by_suffocation.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_suffocation_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_starvation_enable = config.getBoolean("by_starvation.enable");
                this.by_starvation_function = config.getString("by_starvation.function");
                this.by_starvation_maximum = (float)config.getDouble("by_starvation.maximum");
                this.by_starvation_minimum = (float)config.getDouble("by_starvation.minimum");
                secVariables = config.getConfigurationSection("by_starvation.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_starvation_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_drowning_enable = config.getBoolean("by_drowning.enable");
                this.by_drowning_function = config.getString("by_drowning.function");
                this.by_drowning_maximum = (float)config.getDouble("by_drowning.maximum");
                this.by_drowning_minimum = (float)config.getDouble("by_drowning.minimum");
                secVariables = config.getConfigurationSection("by_drowning.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_drowning_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }

                this.by_default_enable = config.getBoolean("by_default.enable");
                this.by_default_function = config.getString("by_default.function");
                this.by_default_maximum = (float)config.getDouble("by_default.maximum");
                this.by_default_minimum = (float)config.getDouble("by_default.minimum");
                secVariables = config.getConfigurationSection("by_default.variables");
                if(secVariables != null){
                    for (String var:
                            secVariables.getKeys(false)) {
                        by_default_variables.put(var,(float)secVariables.getDouble(var));
                    }
                }
            }

            public class chanceOfDodging{
                public boolean enable;
                public String function;
                public float maximum;
                public float minimum;
                public HashMap<String,Float> variables;

                public chanceOfDodging(ConfigurationSection config){
                    variables = new HashMap<>();
                    enable = config.getBoolean("enable");
                    maximum  = (float)config.getDouble("maximum");
                    minimum  = (float)config.getDouble("minimum");
                    function = config.getString("function");
                    ConfigurationSection vars = config.getConfigurationSection("variables");
                    if(vars != null) {
                        for (String var :
                                vars.getKeys(false)) {
                            variables.put(var,(float)vars.getDouble(var));
                        }
                    }

                }
            }

            public class chanceOfRemovingMagicEffect{
                //=
                public boolean enable;
                public String function;
                public float maximum;
                public float minimum;
                public HashMap<String,Float> variables;

                public chanceOfRemovingMagicEffect(ConfigurationSection config){
                    if(config == null) throw new NullPointerException("ConfigurationSection for chanceOfRemovingMagicEffect cannot be null");
                    variables = new HashMap<>();
                    this.enable = config.getBoolean("enable");
                    this.function = config.getString("function");
                    this.maximum = (float)config.getDouble("maximum");
                    this.minimum = (float)config.getDouble("minimum");
                    ConfigurationSection secVariables = config.getConfigurationSection("variables");
                    if(secVariables != null){
                        for (String var:
                                secVariables.getKeys(false)) {
                            variables.put(var,(float)secVariables.getDouble(var));
                        }
                    }
                }
            }

            public class reducingBadPotionEffect{
                public boolean enable;
                public String function;
                public float maximum;
                public float minimum;
                public float range;
                public HashMap<String,Float> variables;

                public reducingBadPotionEffect(ConfigurationSection config){
                    if(config == null) throw new NullPointerException("ConfigurationSection for reducingBadPotionEffect cannot be null");
                    variables = new HashMap<>();
                    this.enable = config.getBoolean("enable");
                    this.function = config.getString("function");
                    this.maximum = (float)config.getDouble("maximum");
                    this.minimum = (float)config.getDouble("minimum");
                    this.range = (float)config.getDouble("range");
                    ConfigurationSection secVariables = config.getConfigurationSection("variables");
                    if(secVariables != null){
                        for (String var:
                                secVariables.getKeys(false)) {
                            variables.put(var,(float)secVariables.getDouble(var));
                        }
                    }
                }
            }
        }

        public class playerAttributes{
            public boolean movement_speed_enable;
            public String movement_speed_function;
            public float movement_speed_maximum;
            public float movement_speed_minimum;
            public HashMap<String,Float> movement_speed_variables;

            public boolean attack_speed_enable;
            public String attack_speed_function;
            public float attack_speed_maximum;
            public float attack_speed_minimum;
            public HashMap<String,Float> attack_speed_variables;

            public boolean total_mana_enable;
            public String total_mana_function;
            public HashMap<String,Float> total_mana_variables;

            public boolean total_health_enable;
            public String total_health_function;
            public HashMap<String,Float> total_health_variables;

            public boolean mana_regen_enable;
            public String mana_regen_function;
            public float mana_regen_maximum;
            public float mana_regen_minimum;
            public HashMap<String,Float> mana_regen_variables;

            public boolean luck_enable;
            public String luck_function;
            public float luck_maximum;
            public float luck_minimum;
            public HashMap<String,Float> luck_variables;

            public boolean knockback_resistance_enable;
            public String knockback_resistance_function;
            public float knockback_resistance_maximum;
            public float knockback_resistance_minimum;
            public HashMap<String,Float> knockback_resistance_variables;

            public playerAttributes(ConfigurationSection section){
                if(section == null) throw new NullPointerException("ConfigurationSection for playerAttributes cannot be null");

                movement_speed_variables = new HashMap<>();
                attack_speed_variables = new HashMap<>();
                total_mana_variables = new HashMap<>();
                mana_regen_variables = new HashMap<>();
                knockback_resistance_variables = new HashMap<>();
                luck_variables = new HashMap<>();
                total_health_variables = new HashMap<>();



                this.movement_speed_enable = section.getBoolean("movement_speed.enable",true);
                this.movement_speed_function = section.getString("movement_speed.function","");
                this.movement_speed_maximum = (float)section.getDouble("movement_speed.maximum",0);
                this.movement_speed_minimum = (float)section.getDouble("movement_speed.minimum",0);
                ConfigurationSection mvnSpeedSection = section.getConfigurationSection("movement_speed.variables");
                if(mvnSpeedSection != null){
                    for (String var :
                            mvnSpeedSection.getKeys(false)) {
                        movement_speed_variables.put(var,(float)mvnSpeedSection.getDouble(var));
                    }
                }

                this.attack_speed_enable = section.getBoolean("attack_speed.enable");
                this.attack_speed_function = section.getString("attack_speed.function");
                this.attack_speed_maximum = (float)section.getDouble("attack_speed.maximum");
                this.attack_speed_minimum = (float)section.getDouble("attack_speed.minimum");
                ConfigurationSection attackspeedSection = section.getConfigurationSection("attack_speed.variables");
                if(attackspeedSection != null){
                    for (String var :
                            attackspeedSection.getKeys(false)) {
                        attack_speed_variables.put(var,(float)attackspeedSection.getDouble(var));
                    }
                }

                this.total_mana_enable = section.getBoolean("total_mana.enable");
                this.total_mana_function = section.getString("total_mana.function");
                ConfigurationSection manaSection = section.getConfigurationSection("total_mana.variables");
                if(manaSection != null){
                    for (String var :
                            manaSection.getKeys(false)) {
                        total_mana_variables.put(var,(float)manaSection.getDouble(var));
                    }
                }

                this.total_health_enable = section.getBoolean("total_health.enable");
                this.total_health_function = section.getString("total_health.function");
                ConfigurationSection healthSection = section.getConfigurationSection("total_health.variables");
                if(healthSection != null){
                    for (String var :
                            healthSection.getKeys(false)) {
                        total_health_variables.put(var,(float)healthSection.getDouble(var));
                    }
                }

                this.mana_regen_enable = section.getBoolean("mana_regen.enable");
                this.mana_regen_function = section.getString("mana_regen.function");
                this.mana_regen_maximum = (float)section.getDouble("mana_regen.maximum");
                this.mana_regen_minimum = (float)section.getDouble("mana_regen.minimum");
                ConfigurationSection manaregenSection = section.getConfigurationSection("mana_regen.variables");
                if(manaregenSection != null){
                    for (String var :
                            manaregenSection.getKeys(false)) {
                        mana_regen_variables.put(var,(float)manaregenSection.getDouble(var));
                    }
                }

                this.luck_enable = section.getBoolean("luck.enable");
                this.luck_function = section.getString("luck.function");
                this.luck_maximum = (float)section.getDouble("luck.maximum");
                this.luck_minimum = (float)section.getDouble("luck.minimum");
                ConfigurationSection luckSection = section.getConfigurationSection("luck.variables");
                if(luckSection != null){
                    for (String var :
                            luckSection.getKeys(false)) {
                        luck_variables.put(var,(float)luckSection.getDouble(var));
                    }
                }

                this.knockback_resistance_enable = section.getBoolean("knockback_resistance.enable");
                this.knockback_resistance_function = section.getString("knockback_resistance.function");
                this.knockback_resistance_maximum = (float)section.getDouble("knockback_resistance.maximum");
                this.knockback_resistance_minimum = (float)section.getDouble("knockback_resistance.minimum");
                ConfigurationSection kockbackSection = section.getConfigurationSection("knockback_resistance.variables");
                if(kockbackSection != null){
                    for (String var :
                            kockbackSection.getKeys(false)) {
                        knockback_resistance_variables.put(var,(float)kockbackSection.getDouble(var));
                    }
                }
            }
        }

        public class onPlayerShootBow{
            public boolean enable;
            public String precision_function;
            public float precision_minimum;
            public float precision_maximum;
            public HashMap<String,Float> precision_variables;
            public float precision_range;
            public String speed_function;
            public HashMap<String,Float> speed_variables;
            public float speed_maximum;
            public float speed_minimum;
            public float speed_range;

            public onPlayerShootBow(ConfigurationSection section){
                if(section == null) throw new NullPointerException("ConfigurationSection for onPlayerShootBow cannot be null");

                precision_variables = new HashMap<>();
                speed_variables = new HashMap<>();
                this.enable = section.getBoolean("enable");
                this.precision_function = section.getString("precision.function");
                this.precision_range = (float)section.getDouble("precision.range");
                this.precision_minimum = (float)section.getDouble("precision.minimum");
                this.precision_maximum = (float)section.getDouble("precision.maximum");
                ConfigurationSection precSection = section.getConfigurationSection("precision.variables");
                if(precSection != null){
                    for (String var :
                            precSection.getKeys(false)) {
                        precision_variables.put(var,(float)precSection.getDouble(var));
                    }
                }

                this.speed_function = section.getString("speed.function");
                this.speed_maximum = (float)section.getDouble("speed.maximum");
                this.speed_minimum = (float)section.getDouble("speed.minimum");
                this.speed_range = (float)section.getDouble("speed.range");
                ConfigurationSection speedSection = section.getConfigurationSection("speed.variables");
                if(speedSection != null){
                    for (String var :
                            speedSection.getKeys(false)) {
                        speed_variables.put(var,(float)speedSection.getDouble(var));
                    }
                }
            }
        }

        public class onPlayerDies{

            public float xp_loss;
            public float items_drops_maximum;
            public float items_drops_minimum;
            public boolean items_drops_enable;
            public HashMap<String,Float> variables;
            public boolean enable;
            public String function;

            public onPlayerDies(ConfigurationSection section){
                variables = new HashMap<>();
                function = section.getString("function");
                enable = section.getBoolean("enable");
                xp_loss = (float)section.getDouble("xp_loss");
                items_drops_maximum =(float)section.getDouble("items_drops_maximum");
                items_drops_minimum =(float)section.getDouble("items_drops_minimum");
                items_drops_enable = section.getBoolean("items_drops_enable");
                ConfigurationSection variablesSection = section.getConfigurationSection("variables");
                if(variablesSection != null){
                    for (String var :
                            variablesSection.getKeys(false)) {
                        variables.put(var,(float)variablesSection.getDouble(var));
                    }
                }
            }
        }
    }

}


