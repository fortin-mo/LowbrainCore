package lowbrain.mcrpg.commun;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Moofy on 18/07/2016.
 */
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

        onPlayerAttackEntity(ConfigurationSection config){
            if(config == null) throw new NullPointerException("ConfigurationSection for onPlayerAttackEntity cannot be null");
            chanceOfCreatingMagicAttack = new chanceOfCreatingMagicAttack(config.getConfigurationSection("chance_of_creating_magic_attack"));
            creatingMagicAttack = new creatingMagicAttack(config.getConfigurationSection("creating_magic_attack"));
            attackEntityBy = new attackEntityBy(config);
        }
        public class chanceOfCreatingMagicAttack{

            //chance_of_creating_magic_attact_:
            public float maximum;
            public float minimum;
            public float intelligence;
            public float dexterity;

            public chanceOfCreatingMagicAttack(ConfigurationSection config){
                this.maximum = (float)config.getDouble("maximum");
                this.minimum = (float)config.getDouble("minimum");
                this.intelligence = (float)config.getDouble("intelligence");
                this.dexterity = (float)config.getDouble("dexterity");
            }
        }

        public class creatingMagicAttack{

            //=
            public boolean enable;
            public float maximum_duration;
            public float minimum_duration;
            public float intelligence_on_duration;
            public float dexterity_on_duration;
            public float maximum_harm_amplifier;
            public float maximum_poison_amplifier;
            public float maximum_slow_amplifier;
            public float maximum_weakness_amplifier;
            public float maximum_wither_amplifier;
            public float minimum_harm_amplifier;
            public float minimum_poison_amplifier;
            public float minimum_slow_amplifier;
            public float minimum_weakness_amplifier;
            public float minimum_wither_amplifier;
            public float intelligence_on_harm_amplifier;
            public float intelligence_on_poison_amplifier;
            public float intelligence_on_slow_amplifier;
            public float intelligence_on_weakness_amplifier;
            public float intelligence_on_wither_amplifier;

            public creatingMagicAttack(ConfigurationSection config){
                this.enable = config.getBoolean("enable");
                this.maximum_duration = (float)config.getDouble("maximum_duration");
                this.minimum_duration = (float)config.getDouble("minimum_duration");
                this.intelligence_on_duration = (float)config.getDouble("intelligence_on_duration");
                this.dexterity_on_duration = (float)config.getDouble("dexterity_on_duration");
                this.maximum_harm_amplifier = (float)config.getDouble("maximum_harm_amplifier");
                this.maximum_poison_amplifier = (float)config.getDouble("maximum_poison_amplifier");
                this.maximum_slow_amplifier = (float)config.getDouble("maximum_slow_amplifier");
                this.maximum_weakness_amplifier = (float)config.getDouble("maximum_weakness_amplifier");
                this.maximum_wither_amplifier = (float)config.getDouble("maximum_wither_amplifier");
                this.minimum_harm_amplifier = (float)config.getDouble("minimum_harm_amplifier");
                this.minimum_poison_amplifier = (float)config.getDouble("minimum_poison_amplifier");
                this.minimum_slow_amplifier = (float)config.getDouble("minimum_slow_amplifier");
                this.minimum_weakness_amplifier = (float)config.getDouble("minimum_weakness_amplifier");
                this.minimum_wither_amplifier = (float)config.getDouble("minimum_wither_amplifier");
                this.intelligence_on_harm_amplifier = (float)config.getDouble("intelligence_on_harm_amplifier");
                this.intelligence_on_poison_amplifier = (float)config.getDouble("intelligence_on_poison_amplifier");
                this.intelligence_on_slow_amplifier = (float)config.getDouble("intelligence_on_slow_amplifier");
                this.intelligence_on_weakness_amplifier = (float)config.getDouble("intelligence_on_weakness_amplifier");
                this.intelligence_on_wither_amplifier = (float)config.getDouble("intelligence_on_wither_amplifier");

            }
        }

        public class attackEntityBy{
            //normal_attack_damage_=
            public boolean weapon_enable;
            public String weapon_function;
            public float weapon_strength;
            public float weapon_dexterity;
            public float weapon_maximum;
            public float weapon_minimum;
            public float weapon_range;

            //potion_attack_damage_=
            public boolean potion_enable;
            public String potion_function;
            public float potion_intelligence;
            public float potion_dexterity;
            public float potion_maximum;
            public float potion_minimum;
            public float potion_range;

            //arrow_attack_damage_=
            public boolean projectile_enable;
            public String projectile_function;
            public float projectile_strength;
            public float projectile_dexterity;
            public float projectile_maximum;
            public float projectile_minimum;
            public float projectile_range;

            public attackEntityBy(ConfigurationSection config){
                this.weapon_enable = config.getBoolean("by_weapon.enable");
                this.weapon_function = config.getString("by_weapon.function");
                this.weapon_strength = (float)config.getDouble("by_weapon.strength");
                this.weapon_dexterity = (float)config.getDouble("by_weapon.dexterity");
                this.weapon_maximum = (float)config.getDouble("by_weapon.maximum");
                this.weapon_minimum = (float)config.getDouble("by_weapon.minimum");
                this.weapon_range = (float)config.getDouble("by_weapon.range");

                this.potion_enable = config.getBoolean("by_potion.enable");
                this.potion_function = config.getString("by_potion.function");
                this.potion_intelligence = (float)config.getDouble("by_potion.intelligence");
                this.potion_dexterity = (float)config.getDouble("by_potion.dexterity");
                this.potion_maximum = (float)config.getDouble("by_potion.maximum");
                this.potion_minimum = (float)config.getDouble("by_potion.minimum");
                this.potion_range = (float)config.getDouble("by_potion.range");

                this.projectile_enable = config.getBoolean("by_projectile.enable");
                this.projectile_function = config.getString("by_projectile.function");
                this.projectile_strength = (float)config.getDouble("by_projectile.strength");
                this.projectile_dexterity = (float)config.getDouble("by_projectile.dexterity");
                this.projectile_maximum = (float)config.getDouble("by_projectile.maximum");
                this.projectile_minimum = (float)config.getDouble("by_projectile.minimum");
                this.projectile_range = (float)config.getDouble("by_projectile.range");
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
        public float intelligence;
        onPlayerConsumePotion(ConfigurationSection config){
            if(config == null)throw new NullPointerException("ConfigurationSection for onPlayerConsumePotion cannot be null");
            this.enable = config.getBoolean("enable");
            this.function = config.getString("function");
            this.range = (float)config.getDouble("range");
            this.maximum = (float)config.getDouble("maximum");
            this.minimum = (float)config.getDouble("minimum");
            this.intelligence = (float)config.getDouble("intelligence");
        }
    }

    public class onPlayerGetDamaged{
        public boolean magic_enable;
        public String magic_function;
        public float magic_maximum;
        public float magic_minimum;
        public float magic_intelligence;
        public float magic_magic_resistance;

        public boolean fire_enable;
        public String fire_function;
        public float fire_maximum;
        public float fire_minimum;
        public float fire_defence;
        public float fire_magic_resistance;
        public float fire_intelligence;

        public boolean explosion_enable;
        public String explosion_function;
        public float explosion_maximum;
        public float explosion_minimum;
        public float explosion_defence;
        public float explosion_strength;

        public boolean fall_enable;
        public String fall_function;
        public float fall_maximum;
        public float fall_minimum;
        public float fall_defence;
        public float fall_agility;

        public boolean contact_enable;
        public String contact_function;
        public float contact_maximum;
        public float contact_minimum;
        public float contact_defence;

        public boolean projectile_enable;
        public String projectile_function;
        public float projectile_maximum;
        public float projectile_minimum;
        public float projectile_defence;

        public boolean weapon_enable;
        public String weapon_function;
        public float weapon_maximum;
        public float weapon_minimum;
        public float weapon_defence;

        public chanceOfRemovingMagicEffect chanceOfRemovingMagicEffect;
        public reducingBadPotionEffect reducingBadPotionEffect;

        public onPlayerGetDamaged(ConfigurationSection config){
            if(config == null) throw new NullPointerException("ConfigurationSection for OnPLayerGetDamaged cannot be null");

            chanceOfRemovingMagicEffect = new chanceOfRemovingMagicEffect(config.getConfigurationSection("chance_of_removing_magic_effect"));
            reducingBadPotionEffect = new reducingBadPotionEffect(config.getConfigurationSection("reducing_bad_potion_effect"));

            this.magic_enable = config.getBoolean("by_magic.enable");
            this.magic_function = config.getString("by_magic.function");
            this.magic_maximum = (float)config.getDouble("by_magic.maximum");
            this.magic_minimum = (float)config.getDouble("by_magic.minimum");
            this.magic_intelligence = (float)config.getDouble("by_magic.intelligence");
            this.magic_magic_resistance = (float)config.getDouble("by_magic.magic_resistance");

            this.fire_enable = config.getBoolean("by_fire.enable");
            this.fire_function = config.getString("by_fire.function");
            this.fire_maximum = (float)config.getDouble("by_fire.maximum");
            this.fire_minimum = (float)config.getDouble("by_fire.minimum");
            this.fire_defence = (float)config.getDouble("by_fire.defence");
            this.fire_magic_resistance = (float)config.getDouble("by_fire.magic_resistance");
            this.fire_intelligence = (float)config.getDouble("by_fire.intelligence");

            this.explosion_enable = config.getBoolean("by_explosion.enable");
            this.explosion_function = config.getString("by_explosion.function");
            this.explosion_maximum = (float)config.getDouble("by_explosion.maximum");
            this.explosion_minimum = (float)config.getDouble("by_explosion.minimum");
            this.explosion_defence = (float)config.getDouble("by_explosion.defence");
            this.explosion_strength = (float)config.getDouble("by_explosion.strength");

            this.fall_enable = config.getBoolean("by_fall.enable");
            this.fall_function = config.getString("by_fall.function");
            this.fall_maximum = (float)config.getDouble("by_fall.maximum");
            this.fall_minimum = (float)config.getDouble("by_fall.minimum");
            this.fall_defence = (float)config.getDouble("by_fall.defence");
            this.fall_agility = (float)config.getDouble("by_fall.agility");

            this.contact_enable = config.getBoolean("by_contact.enable");
            this.contact_function = config.getString("by_contact.function");
            this.contact_maximum = (float)config.getDouble("by_contact.maximum");
            this.contact_minimum = (float)config.getDouble("by_contact.minimum");
            this.contact_defence = (float)config.getDouble("by_contact.defence");

            this.projectile_enable = config.getBoolean("by_projectile.enable");
            this.projectile_function = config.getString("by_projectile.function");
            this.projectile_maximum = (float)config.getDouble("by_projectile.maximum");
            this.projectile_minimum = (float)config.getDouble("by_projectile.minimum");
            this.projectile_defence = (float)config.getDouble("by_projectile.defence");

            this.weapon_enable = config.getBoolean("by_weapon.enable");
            this.weapon_function = config.getString("by_weapon.function");
            this.weapon_maximum = (float)config.getDouble("by_weapon.maximum");
            this.weapon_minimum = (float)config.getDouble("by_weapon.minimum");
            this.weapon_defence = (float)config.getDouble("by_weapon.defence");
        }

        public class chanceOfRemovingMagicEffect{
            //=
            public boolean enable;
            public String function;
            public float maximum;
            public float minimum;
            public float magic_resistance;
            public float intelligence;
            public float dexterity;

            public chanceOfRemovingMagicEffect(ConfigurationSection config){
                if(config == null) throw new NullPointerException("ConfigurationSection for chanceOfRemovingMagicEffect cannot be null");
                this.enable = config.getBoolean("enable");
                this.function = config.getString("function");
                this.maximum = (float)config.getDouble("maximum");
                this.minimum = (float)config.getDouble("minimum");
                this.magic_resistance = (float)config.getDouble("magic_resistance");
                this.intelligence = (float)config.getDouble("intelligence");
                this.dexterity = (float)config.getDouble("dexterity");
            }
        }

        public class reducingBadPotionEffect{
            public boolean enable;
            public String function;
            public float maximum;
            public float minimum;
            public float range;
            public float magic_resistance;
            public float intelligence;

            public reducingBadPotionEffect(ConfigurationSection config){
                if(config == null) throw new NullPointerException("ConfigurationSection for reducingBadPotionEffect cannot be null");
                this.enable = config.getBoolean("enable");
                this.function = config.getString("function");
                this.maximum = (float)config.getDouble("maximum");
                this.minimum = (float)config.getDouble("minimum");
                this.range = (float)config.getDouble("range");
                this.magic_resistance = (float)config.getDouble("magic_resistance");
                this.intelligence = (float)config.getDouble("intelligence");
            }
        }
    }

    public class playerAttributes{
        public boolean movement_speed_enable;
        public String movement_speed_function;
        public float movement_speed_maximum;
        public float movement_speed_minimum;
        public float movement_speed_agility;
        public float movement_speed_dexterity;

        public boolean attack_speed_enable;
        public String attack_speed_function;
        public float attack_speed_maximum;
        public float attack_speed_minimum;
        public float attack_speed_agility;
        public float attack_speed_dexterity;
        public float attack_speed_strength;

        public boolean total_mana_enable;
        public String total_mana_function;
        public float total_mana_intelligence;

        public boolean total_health_enable;
        public String total_health_function;
        public float total_health_health;

        public boolean mana_regen_enable;
        public String mana_regen_function;
        public float mana_regen_maximum;
        public float mana_regen_minimum;
        public float mana_regen_intelligence;

        public boolean luck_enable;
        public String luck_function;
        public float luck_maximum;
        public float luck_minimum;
        public float luck_intelligence;
        public float luck_dexterity;
        public float luck_agility;

        public boolean knockback_resistance_enable;
        public String knockback_resistance_function;
        public float knockback_resistance_maximum;
        public float knockback_resistance_minimum;
        public float knockback_resistance_strength;
        public float knockback_resistance_dexterity;
        public float knockback_resistance_agility;
        public float knockback_resistance_defence;

        public playerAttributes(ConfigurationSection section){
            if(section == null) throw new NullPointerException("ConfigurationSection for playerAttributes cannot be null");
            this.movement_speed_enable = section.getBoolean("movement_speed.enable");
            this.movement_speed_function = section.getString("movement_speed.function");
            this.movement_speed_maximum = (float)section.getDouble("movement_speed.maximum");
            this.movement_speed_minimum = (float)section.getDouble("movement_speed.minimum");
            this.movement_speed_agility = (float)section.getDouble("movement_speed.agility");
            this.movement_speed_dexterity = (float)section.getDouble("movement_speed.dexterity");

            this.attack_speed_enable = section.getBoolean("attack_speed.enable");
            this.attack_speed_function = section.getString("attack_speed.function");
            this.attack_speed_maximum = (float)section.getDouble("attack_speed.maximum");
            this.attack_speed_minimum = (float)section.getDouble("attack_speed.minimum");
            this.attack_speed_agility = (float)section.getDouble("attack_speed.agility");
            this.attack_speed_dexterity = (float)section.getDouble("attack_speed.dexterity");
            this.attack_speed_strength = (float)section.getDouble("attack_speed.strength");

            this.total_mana_enable = section.getBoolean("total_mana.enable");
            this.total_mana_function = section.getString("total_mana.function");
            this.total_mana_intelligence = (float)section.getDouble("total_mana.intelligence");
            this.total_health_enable = section.getBoolean("total_health.enable");
            this.total_health_function = section.getString("total_health.function");
            this.total_health_health = (float)section.getDouble("total_health.health");

            this.mana_regen_enable = section.getBoolean("mana_regen.enable");
            this.mana_regen_function = section.getString("mana_regen.function");
            this.mana_regen_maximum = (float)section.getDouble("mana_regen.maximum");
            this.mana_regen_minimum = (float)section.getDouble("mana_regen.minimum");
            this.mana_regen_intelligence = (float)section.getDouble("mana_regen.intelligence");

            this.luck_enable = section.getBoolean("luck.enable");
            this.luck_function = section.getString("luck.function");
            this.luck_maximum = (float)section.getDouble("luck.maximum");
            this.luck_minimum = (float)section.getDouble("luck.minimum");
            this.luck_intelligence = (float)section.getDouble("luck.intelligence");
            this.luck_dexterity = (float)section.getDouble("luck.dexterity");
            this.luck_agility = (float)section.getDouble("luck.agility");

            this.knockback_resistance_enable = section.getBoolean("knockback_resistance.enable");
            this.knockback_resistance_function = section.getString("knockback_resistance.function");
            this.knockback_resistance_maximum = (float)section.getDouble("knockback_resistance.maximum");
            this.knockback_resistance_minimum = (float)section.getDouble("knockback_resistance.minimum");
            this.knockback_resistance_strength = (float)section.getDouble("knockback_resistance.strength");
            this.knockback_resistance_dexterity = (float)section.getDouble("knockback_resistance.dexterity");
            this.knockback_resistance_agility = (float)section.getDouble("knockback_resistance.agility");
            this.knockback_resistance_defence = (float)section.getDouble("knockback_resistance.defence");
        }
    }

    public class onPlayerShootBow{
        public boolean enable;
        public String precision_function;
        public float precision_minimum;
        public float precision_maximum;
        public float precision_dexterity;
        public float precision_range;
        public String speed_function;
        public float speed_strength;
        public float speed_dexterity;
        public float speed_maximum;
        public float speed_minimum;
        public float speed_range;
        
        public onPlayerShootBow(ConfigurationSection section){
            if(section == null) throw new NullPointerException("ConfigurationSection for onPlayerShootBow cannot be null");
            this.enable = section.getBoolean("enable");
            this.precision_function = section.getString("precision.function");
            this.precision_range = (float)section.getDouble("precision.range");
            this.precision_minimum = (float)section.getDouble("precision.minimum");
            this.precision_maximum = (float)section.getDouble("precision.maximum");
            this.precision_dexterity = (float)section.getDouble("precision.dexterity");

            this.speed_function = section.getString("speed.function");
            this.speed_strength = (float)section.getDouble("speed.strength");
            this.speed_dexterity = (float)section.getDouble("speed.dexterity");
            this.speed_maximum = (float)section.getDouble("speed.maximum");
            this.speed_minimum = (float)section.getDouble("speed.minimum");
            this.speed_range = (float)section.getDouble("speed.range");
        }
    }

    public class onPlayerDies{

        public float xp_loss;
        public float items_drops_maximum;
        public float items_drops_minimum;
        public float intelligence ;
        public float agility;
        public boolean enable;
        public String function;

        public onPlayerDies(ConfigurationSection section){
            function = section.getString("function");
            enable = section.getBoolean("enable");
            xp_loss = (float)section.getDouble("xp_loss");
            items_drops_maximum =(float)section.getDouble("items_drops_maximum");
            items_drops_minimum =(float)section.getDouble("items_drops_minimum");
            intelligence =(float)section.getDouble("intelligence");
            agility =(float)section.getDouble("agility");
        }
    }
}
