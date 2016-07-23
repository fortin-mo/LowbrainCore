package lowbrain.mcrpg.commun;

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

    //on_player_attack_entity =
    //chance_of_creating_magic_attact_:
    public float chance_of_creating_magic_attack_maximum;
    public float chance_of_creating_magic_attack_minimum;
    public float chance_of_creating_magic_attack_intelligence;
    public float chance_of_creating_magic_attack_dexterity;

    //creating_magic_attack_=
    public boolean creating_magic_attack_enable;
    public float creating_magic_attack_maximum_duration;
    public float creating_magic_attack_minimum_duration;
    public float creating_magic_attack_intelligence_on_duration;
    public float creating_magic_attack_dexterity_on_duration;
    public float creating_magic_attack_maximum_harm_amplifier;
    public float creating_magic_attack_maximum_poison_amplifier;
    public float creating_magic_attack_maximum_slow_amplifier;
    public float creating_magic_attack_maximum_weakness_amplifier;
    public float creating_magic_attack_maximum_wither_amplifier;
    public float creating_magic_attack_minimum_harm_amplifier;
    public float creating_magic_attack_minimum_poison_amplifier;
    public float creating_magic_attack_minimum_slow_amplifier;
    public float creating_magic_attack_minimum_weakness_amplifier;
    public float creating_magic_attack_minimum_wither_amplifier;
    public float creating_magic_attack_intelligence_on_harm_amplifier;
    public float creating_magic_attack_intelligence_on_poison_amplifier;
    public float creating_magic_attack_intelligence_on_slow_amplifier;
    public float creating_magic_attack_intelligence_on_weakness_amplifier;
    public float creating_magic_attack_intelligence_on_wither_amplifier;

    //normal_attack_damage_=
    public boolean attack_by_weapon_enable;
    public String attack_by_weapon_function;
    public float attack_by_weapon_strength;
    public float attack_by_weapon_dexterity;
    public float attack_by_weapon_maximum;
    public float attack_by_weapon_minimum;
    public float attack_by_weapon_range;

    //potion_attack_damage_=
    public boolean attack_by_potion_enable;
    public String attack_by_potion_function;
    public float attack_by_potion_intelligence;
    public float attack_by_potion_dexterity;
    public float attack_by_potion_maximum;
    public float attack_by_potion_minimum;
    public float attack_by_potion_range;

    //arrow_attack_damage_=
    public boolean attack_by_projectile_enable;
    public String attack_by_projectile_function;
    public float attack_by_projectile_strength;
    public float attack_by_projectile_dexterity;
    public float attack_by_projectile_maximum;
    public float attack_by_projectile_minimum;
    public float attack_by_projectile_range;

    //chance_of_removing_magic_effect_=
    public boolean chance_of_removing_magic_effect_enable;
    public String chance_of_removing_magic_effect_function;
    public float chance_of_removing_magic_effect_maximum;
    public float chance_of_removing_magic_effect_minimum;
    public float chance_of_removing_magic_effect_magic_resistance;
    public float chance_of_removing_magic_effect_intelligence;
    public float chance_of_removing_magic_effect_dexterity;

    //on_player_shoot_bow_=
    public boolean on_player_shoot_bow_enable;
    public String on_player_shoot_bow_precision_function;
    public float on_player_shoot_bow_precision_min_dexterity;
    public float on_player_shoot_bow_precision_range;
    public String on_player_shoot_bow_speed_function;
    public float on_player_shoot_bow_speed_strength;
    public float on_player_shoot_bow_speed_dexterity;
    public float on_player_shoot_bow_speed_maximum;
    public float on_player_shoot_bow_speed_minimum;
    public float on_player_shoot_bow_speed_range;

    //on_player_consume_potion_=
    public boolean on_player_consume_potion_enable;
    public String on_player_consume_potion_function;
    public float on_player_consume_potion_range;
    public float on_player_consume_potion_maximum;
    public float on_player_consume_potion_minimum;
    public float on_player_consume_potion_intelligence;

    //reducing_bad_potion_effect_=
    public boolean reducing_bad_potion_effect_enable;
    public String reducing_bad_potion_effect_function;
    public float reducing_bad_potion_effect_maximum;
    public float reducing_bad_potion_effect_minimum;
    public float reducing_bad_potion_effect_range;
    public float reducing_bad_potion_effect_magic_resistance;
    public float reducing_bad_potion_effect_intelligence;

    public boolean damaged_by_magic_enable;
    public String damaged_by_magic_function;
    public float damaged_by_magic_maximum;
    public float damaged_by_magic_minimum;
    public float damaged_by_magic_intelligence;
    public float damaged_by_magic_magic_resistance;

    public boolean damaged_by_fire_enable;
    public String damaged_by_fire_function;
    public float damaged_by_fire_maximum;
    public float damaged_by_fire_minimum;
    public float damaged_by_fire_defence;
    public float damaged_by_fire_magic_resistance;
    public float damaged_by_fire_intelligence;

    public boolean damaged_by_explosion_enable;
    public String damaged_by_explosion_function;
    public float damaged_by_explosion_maximum;
    public float damaged_by_explosion_minimum;
    public float damaged_by_explosion_defence;
    public float damaged_by_explosion_strength;

    public boolean damaged_by_fall_enable;
    public String damaged_by_fall_function;
    public float damaged_by_fall_maximum;
    public float damaged_by_fall_minimum;
    public float damaged_by_fall_defence;
    public float damaged_by_fall_agility;

    public boolean damaged_by_contact_enable;
    public String damaged_by_contact_function;
    public float damaged_by_contact_maximum;
    public float damaged_by_contact_minimum;
    public float damaged_by_contact_defence;

    public boolean damaged_by_projectile_enable;
    public String damaged_by_projectile_function;
    public float damaged_by_projectile_maximum;
    public float damaged_by_projectile_minimum;
    public float damaged_by_projectile_defence;

    public boolean damaged_by_weapon_enable;
    public String damaged_by_weapon_function;
    public float damaged_by_weapon_maximum;
    public float damaged_by_weapon_minimum;
    public float damaged_by_weapon_defence;

    public boolean attribute_movement_speed_enable;
    public String attribute_movement_speed_function;
    public float attribute_movement_speed_maximum;
    public float attribute_movement_speed_minimum;
    public float attribute_movement_speed_agility;
    public float attribute_movement_speed_dexterity;

    public boolean attribute_attack_speed_enable;
    public String attribute_attack_speed_function;
    public float attribute_attack_speed_maximum;
    public float attribute_attack_speed_minimum;
    public float attribute_attack_speed_agility;
    public float attribute_attack_speed_dexterity;
    public float attribute_attack_speed_strength;

    public boolean attribute_total_mana_enable;
    public String attribute_total_mana_function;
    public float attribute_total_mana_intelligence;

    public boolean attribute_total_health_enable;
    public String attribute_total_health_function;
    public float attribute_total_health_health;

    public boolean attribute_mana_regen_enable;
    public String attribute_mana_regen_function;
    public float attribute_mana_regen_maximum;
    public float attribute_mana_regen_minimum;
    public float attribute_mana_regen_intelligence;

    public boolean attribute_luck_enable;
    public String attribute_luck_function;
    public float attribute_luck_maximum;
    public float attribute_luck_minimum;
    public float attribute_luck_intelligence;
    public float attribute_luck_dexterity;
    public float attribute_luck_agility;

    public boolean attribute_knockback_resistance_enable;
    public String attribute_knockback_resistance_function;
    public float attribute_knockback_resistance_maximum;
    public float attribute_knockback_resistance_minimum;
    public float attribute_knockback_resistance_strength;
    public float attribute_knockback_resistance_dexterity;
    public float attribute_knockback_resistance_agility;
    public float attribute_knockback_resistance_defence;

    public MathSettings(FileConfiguration config) {
        this.next_lvl_multiplier = (float)config.getDouble("settings.maths.next_lvl_multiplier");
        this.natural_xp_gain_multiplier = (float)config.getDouble("settings.maths.natural_xp_gain_multiplier");
        this.killer_level_gain_multiplier = (float)config.getDouble("settings.maths.on_player_kills_player.killer_level_gain_multiplier");
        this.level_difference_multiplier = (float)config.getDouble("settings.maths.on_player_kills_player.level_difference_multiplier");
        this.killer_base_exp = (float)config.getDouble("settings.maths.on_player_kills_player.killer_base_exp");
        this.player_kills_player_exp_enable = config.getBoolean("settings.maths.on_player_kills_player.enable");
        
        this.chance_of_creating_magic_attack_maximum = (float)config.getDouble("settings.maths.on_player_attack_entity.chance_of_creating_magic_attack.maximum");
        this.chance_of_creating_magic_attack_minimum = (float)config.getDouble("settings.maths.on_player_attack_entity.chance_of_creating_magic_attack.minimum");
        this.chance_of_creating_magic_attack_intelligence = (float)config.getDouble("settings.maths.on_player_attack_entity.chance_of_creating_magic_attack.intelligence");
        this.chance_of_creating_magic_attack_dexterity = (float)config.getDouble("settings.maths.on_player_attack_entity.chance_of_creating_magic_attack.dexterity");
        
        this.creating_magic_attack_enable = config.getBoolean("settings.maths.on_player_attack_entity.creating_magic_attact.enable");
        this.creating_magic_attack_maximum_duration = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.maximum_duration");
        this.creating_magic_attack_minimum_duration = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.minimum_duration");
        this.creating_magic_attack_intelligence_on_duration = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.intelligence_on_duration");
        this.creating_magic_attack_dexterity_on_duration = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.dexterity_on_duration");
        this.creating_magic_attack_maximum_harm_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.maximum_harm_amplifier");
        this.creating_magic_attack_maximum_poison_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.maximum_poison_amplifier");
        this.creating_magic_attack_maximum_slow_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.maximum_slow_amplifier");
        this.creating_magic_attack_maximum_weakness_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.maximum_weakness_amplifier");
        this.creating_magic_attack_maximum_wither_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.maximum_wither_amplifier");
        this.creating_magic_attack_minimum_harm_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.minimum_harm_amplifier");
        this.creating_magic_attack_minimum_poison_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.minimum_poison_amplifier");
        this.creating_magic_attack_minimum_slow_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.minimum_slow_amplifier");
        this.creating_magic_attack_minimum_weakness_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.minimum_weakness_amplifier");
        this.creating_magic_attack_minimum_wither_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.minimum_wither_amplifier");
        this.creating_magic_attack_intelligence_on_harm_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.intelligence_on_harm_amplifier");
        this.creating_magic_attack_intelligence_on_poison_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.intelligence_on_poison_amplifier");
        this.creating_magic_attack_intelligence_on_slow_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.intelligence_on_slow_amplifier");
        this.creating_magic_attack_intelligence_on_weakness_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.intelligence_on_weakness_amplifier");
        this.creating_magic_attack_intelligence_on_wither_amplifier = (float)config.getDouble("settings.maths.on_player_attack_entity.creating_magic_attack.intelligence_on_wither_amplifier");
        
        this.attack_by_weapon_enable = config.getBoolean("settings.maths.on_player_attack_entity.by_weapon.enable");
        this.attack_by_weapon_function = config.getString("settings.maths.on_player_attack_entity.by_weapon.function");
        this.attack_by_weapon_strength = (float)config.getDouble("settings.maths.on_player_attack_entity.by_weapon.strength");
        this.attack_by_weapon_dexterity = (float)config.getDouble("settings.maths.on_player_attack_entity.by_weapon.dexterity");
        this.attack_by_weapon_maximum = (float)config.getDouble("settings.maths.on_player_attack_entity.by_weapon.maximum");
        this.attack_by_weapon_minimum = (float)config.getDouble("settings.maths.on_player_attack_entity.by_weapon.minimum");
        this.attack_by_weapon_range = (float)config.getDouble("settings.maths.on_player_attack_entity.by_weapon.range");
        
        this.attack_by_potion_enable = config.getBoolean("settings.maths.on_player_attack_entity.by_potion.enable");
        this.attack_by_potion_function = config.getString("settings.maths.on_player_attack_entity.by_potion.function");
        this.attack_by_potion_intelligence = (float)config.getDouble("settings.maths.on_player_attack_entity.by_potion.intelligence");
        this.attack_by_potion_dexterity = (float)config.getDouble("settings.maths.on_player_attack_entity.by_potion.dexterity");
        this.attack_by_potion_maximum = (float)config.getDouble("settings.maths.on_player_attack_entity.by_potion.maximum");
        this.attack_by_potion_minimum = (float)config.getDouble("settings.maths.on_player_attack_entity.by_potion.minimum");
        this.attack_by_potion_range = (float)config.getDouble("settings.maths.on_player_attack_entity.by_potion.range");
        
        this.attack_by_projectile_enable = config.getBoolean("settings.maths.on_player_attack_entity.by_projectile.enable");
        this.attack_by_projectile_function = config.getString("settings.maths.on_player_attack_entity.by_projectile.function");
        this.attack_by_projectile_strength = (float)config.getDouble("settings.maths.on_player_attack_entity.by_projectile.strength");
        this.attack_by_projectile_dexterity = (float)config.getDouble("settings.maths.on_player_attack_entity.by_projectile.dexterity");
        this.attack_by_projectile_maximum = (float)config.getDouble("settings.maths.on_player_attack_entity.by_projectile.maximum");
        this.attack_by_projectile_minimum = (float)config.getDouble("settings.maths.on_player_attack_entity.by_projectile.minimum");
        this.attack_by_projectile_range = (float)config.getDouble("settings.maths.on_player_attack_entity.by_projectile.range");
        
        this.on_player_shoot_bow_enable = config.getBoolean("settings.maths.on_player_shoot_bow.enable");
        this.on_player_shoot_bow_precision_function = config.getString("settings.maths.on_player_shoot_bow.precision.function");
        this.on_player_shoot_bow_precision_min_dexterity = (float)config.getDouble("settings.maths.on_player_shoot_bow.precision.min_dexterity");
        this.on_player_shoot_bow_precision_range = (float)config.getDouble("settings.maths.on_player_shoot_bow.precision.range");
        this.on_player_shoot_bow_speed_function = config.getString("settings.maths.on_player_shoot_bow.speed.function");
        this.on_player_shoot_bow_speed_strength = (float)config.getDouble("settings.maths.on_player_shoot_bow.speed.strength");
        this.on_player_shoot_bow_speed_dexterity = (float)config.getDouble("settings.maths.on_player_shoot_bow.speed.dexterity");
        this.on_player_shoot_bow_speed_maximum = (float)config.getDouble("settings.maths.on_player_shoot_bow.speed.maximum");
        this.on_player_shoot_bow_speed_minimum = (float)config.getDouble("settings.maths.on_player_shoot_bow.speed.minimum");
        this.on_player_shoot_bow_speed_range = (float)config.getDouble("settings.maths.on_player_shoot_bow.speed.range");

        this.on_player_consume_potion_enable = config.getBoolean("settings.maths.on_player_consume_potion.enable");
        this.on_player_consume_potion_function = config.getString("settings.maths.on_player_consume_potion.function");
        this.on_player_consume_potion_range = (float)config.getDouble("settings.maths.on_player_consume_potion.range");
        this.on_player_consume_potion_maximum = (float)config.getDouble("settings.maths.on_player_consume_potion.maximum");
        this.on_player_consume_potion_minimum = (float)config.getDouble("settings.mathson_player_consume_potion.minimum");
        this.on_player_consume_potion_intelligence = (float)config.getDouble("settings.maths.on_player_consume_potion.intelligence");
        
        this.chance_of_removing_magic_effect_enable = config.getBoolean("settings.maths.on_player_get_damaged.chance_of_removing_magic_effect.enable");
        this.chance_of_removing_magic_effect_function = config.getString("settings.maths.on_player_get_damaged.chance_of_removing_magic_effect.function");
        this.chance_of_removing_magic_effect_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.chance_of_removing_magic_effect.maximum");
        this.chance_of_removing_magic_effect_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.chance_of_removing_magic_effect.minimum");
        this.chance_of_removing_magic_effect_magic_resistance = (float)config.getDouble("settings.maths.on_player_get_damaged.chance_of_removing_magic_effect.magic_resistance");
        this.chance_of_removing_magic_effect_intelligence = (float)config.getDouble("settings.maths.on_player_get_damaged.chance_of_removing_magic_effect.intelligence");
        this.chance_of_removing_magic_effect_dexterity = (float)config.getDouble("settings.maths.on_player_get_damaged.chance_of_removing_magic_effect.dexterity");
        
        this.reducing_bad_potion_effect_enable = config.getBoolean("settings.maths.on_player_get_damaged.reducing_bad_potion_effect.enable");
        this.reducing_bad_potion_effect_function = config.getString("settings.maths.on_player_get_damaged.reducing_bad_potion_effect.function");
        this.reducing_bad_potion_effect_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.reducing_bad_potion_effect.maximum");
        this.reducing_bad_potion_effect_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.reducing_bad_potion_effect.minimum");
        this.reducing_bad_potion_effect_range = (float)config.getDouble("settings.maths.on_player_get_damaged.reducing_bad_potion_effect.range");
        this.reducing_bad_potion_effect_magic_resistance = (float)config.getDouble("settings.maths.on_player_get_damaged.reducing_bad_potion_effect.magic_resistance");
        this.reducing_bad_potion_effect_intelligence = (float)config.getDouble("settings.maths.on_player_get_damaged.reducing_bad_potion_effect.intelligence");
        
        this.damaged_by_magic_enable = config.getBoolean("settings.maths.on_player_get_damaged.by_magic.enable");
        this.damaged_by_magic_function = config.getString("settings.maths.on_player_get_damaged.by_magic.function");
        this.damaged_by_magic_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_magic.maximum");
        this.damaged_by_magic_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_magic.minimum");
        this.damaged_by_magic_intelligence = (float)config.getDouble("settings.maths.on_player_get_damaged.by_magic.intelligence");
        this.damaged_by_magic_magic_resistance = (float)config.getDouble("settings.maths.on_player_get_damaged.by_magic.magic_resistance");
        
        this.damaged_by_fire_enable = config.getBoolean("settings.maths.on_player_get_damaged.by_fire.enable");
        this.damaged_by_fire_function = config.getString("settings.maths.on_player_get_damaged.by_fire.function");
        this.damaged_by_fire_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fire.maximum");
        this.damaged_by_fire_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fire.minimum");
        this.damaged_by_fire_defence = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fire.defence");
        this.damaged_by_fire_magic_resistance = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fire.magic_resistance");
        this.damaged_by_fire_intelligence = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fire.intelligence");
        
        this.damaged_by_explosion_enable = config.getBoolean("settings.maths.on_player_get_damaged.by_explosion.enable");
        this.damaged_by_explosion_function = config.getString("settings.maths.on_player_get_damaged.by_explosion.function");
        this.damaged_by_explosion_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_explosion.maximum");
        this.damaged_by_explosion_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_explosion.minimum");
        this.damaged_by_explosion_defence = (float)config.getDouble("settings.maths.on_player_get_damaged.by_explosion.defence");
        this.damaged_by_explosion_strength = (float)config.getDouble("settings.maths.on_player_get_damaged.by_explosion.strength");
        
        this.damaged_by_fall_enable = config.getBoolean("settings.maths.on_player_get_damaged.by_fall.enable");
        this.damaged_by_fall_function = config.getString("settings.maths.on_player_get_damaged.by_fall.function");
        this.damaged_by_fall_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fall.maximum");
        this.damaged_by_fall_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fall.minimum");
        this.damaged_by_fall_defence = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fall.defence");
        this.damaged_by_fall_agility = (float)config.getDouble("settings.maths.on_player_get_damaged.by_fall.agility");
        
        this.damaged_by_contact_enable = config.getBoolean("settings.maths.on_player_get_damaged.by_contact.enable");
        this.damaged_by_contact_function = config.getString("settings.maths.on_player_get_damaged.by_contact.function");
        this.damaged_by_contact_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_contact.maximum");
        this.damaged_by_contact_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_contact.minimum");
        this.damaged_by_contact_defence = (float)config.getDouble("settings.maths.on_player_get_damaged.by_contact.defence");
        
        this.damaged_by_projectile_enable = config.getBoolean("settings.maths.on_player_get_damaged.by_projectile.enable");
        this.damaged_by_projectile_function = config.getString("settings.maths.on_player_get_damaged.by_projectile.function");
        this.damaged_by_projectile_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_projectile.maximum");
        this.damaged_by_projectile_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_projectile.minimum");
        this.damaged_by_projectile_defence = (float)config.getDouble("settings.maths.on_player_get_damaged.by_projectile.defence");
        
        this.damaged_by_weapon_enable = config.getBoolean("settings.maths.on_player_get_damaged.by_weapon.enable");
        this.damaged_by_weapon_function = config.getString("settings.maths.on_player_get_damaged.by_weapon.function");
        this.damaged_by_weapon_maximum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_weapon.maximum");
        this.damaged_by_weapon_minimum = (float)config.getDouble("settings.maths.on_player_get_damaged.by_weapon.minimum");
        this.damaged_by_weapon_defence = (float)config.getDouble("settings.maths.on_player_get_damaged.by_weapon.defence");

        this.attribute_movement_speed_enable = config.getBoolean("settings.maths.player_attributes.movement_speed.enable");
        this.attribute_movement_speed_function = config.getString("settings.maths.player_attributes.movement_speed.function");
        this.attribute_movement_speed_maximum = (float)config.getDouble("settings.maths.player_attributes.movement_speed.maximum");
        this.attribute_movement_speed_minimum = (float)config.getDouble("settings.maths.player_attributes.movement_speed.minimum");
        this.attribute_movement_speed_agility = (float)config.getDouble("settings.maths.player_attributes.movement_speed.agility");
        this.attribute_movement_speed_dexterity = (float)config.getDouble("settings.maths.player_attributes.movement_speed.dexterity");
        
        this.attribute_attack_speed_enable = config.getBoolean("settings.maths.player_attributes.attack_speed.enable");
        this.attribute_attack_speed_function = config.getString("settings.maths.player_attributes.attack_speed.function");
        this.attribute_attack_speed_maximum = (float)config.getDouble("settings.maths.player_attributes.attack_speed.maximum");
        this.attribute_attack_speed_minimum = (float)config.getDouble("settings.maths.player_attributes.attack_speed.minimum");
        this.attribute_attack_speed_agility = (float)config.getDouble("settings.maths.player_attributes.attack_speed.agility");
        this.attribute_attack_speed_dexterity = (float)config.getDouble("settings.maths.player_attributes.attack_speed.dexterity");
        this.attribute_attack_speed_strength = (float)config.getDouble("settings.maths.player_attributes.attack_speed.strength");
        
        this.attribute_total_mana_enable = config.getBoolean("settings.maths.player_attributes.total_mana.enable");
        this.attribute_total_mana_function = config.getString("settings.maths.player_attributes.total_mana.function");
        this.attribute_total_mana_intelligence = (float)config.getDouble("settings.maths.player_attributes.total_mana.intelligence");
        this.attribute_total_health_enable = config.getBoolean("settings.maths.player_attributes.total_health.enable");
        this.attribute_total_health_function = config.getString("settings.maths.player_attributes.total_health.function");
        this.attribute_total_health_health = (float)config.getDouble("settings.maths.player_attributes.total_health.health");
        
        this.attribute_mana_regen_enable = config.getBoolean("settings.maths.player_attributes.mana_regen.enable");
        this.attribute_mana_regen_function = config.getString("settings.maths.player_attributes.mana_regen.function");
        this.attribute_mana_regen_maximum = (float)config.getDouble("settings.maths.player_attributes.mana_regen.maximum");
        this.attribute_mana_regen_minimum = (float)config.getDouble("settings.maths.player_attributes.mana_regen.minimum");
        this.attribute_mana_regen_intelligence = (float)config.getDouble("settings.maths.player_attributes.mana_regen.intelligence");
        
        this.attribute_luck_enable = config.getBoolean("settings.maths.player_attributes.luck.enable");
        this.attribute_luck_function = config.getString("settings.maths.player_attributes.luck.function");
        this.attribute_luck_maximum = (float)config.getDouble("settings.maths.player_attributes.luck.maximum");
        this.attribute_luck_minimum = (float)config.getDouble("settings.maths.player_attributes.luck.minimum");
        this.attribute_luck_intelligence = (float)config.getDouble("settings.maths.player_attributes.luck.intelligence");
        this.attribute_luck_dexterity = (float)config.getDouble("settings.maths.player_attributes.luck.dexterity");
        this.attribute_luck_agility = (float)config.getDouble("settings.maths.player_attributes.luck.agility");
        
        this.attribute_knockback_resistance_enable = config.getBoolean("settings.maths.player_attributes.knockback_resistance.enable");
        this.attribute_knockback_resistance_function = config.getString("settings.maths.player_attributes.knockback_resistance.function");
        this.attribute_knockback_resistance_maximum = (float)config.getDouble("settings.maths.player_attributes.knockback_resistance.maximum");
        this.attribute_knockback_resistance_minimum = (float)config.getDouble("settings.maths.player_attributes.knockback_resistance.minimum");
        this.attribute_knockback_resistance_strength = (float)config.getDouble("settings.maths.player_attributes.knockback_resistance.strength");
        this.attribute_knockback_resistance_dexterity = (float)config.getDouble("settings.maths.player_attributes.knockback_resistance.dexterity");
        this.attribute_knockback_resistance_agility = (float)config.getDouble("settings.maths.player_attributes.knockback_resistance.agility");
        this.attribute_knockback_resistance_defence = (float)config.getDouble("settings.maths.player_attributes.knockback_resistance.defence");
    }
}
