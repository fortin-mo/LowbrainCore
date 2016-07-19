package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Moofy on 18/07/2016.
 */
public class MathSettings {

    public double next_lvl_multiplier = 1.1;
    public double natural_xp_gain_multiplier = 1;
    public double killer_level_gain_multiplier = 0.5;
    public double level_difference_multiplier = 0.5;

    //on_player_attack_entity =
    //chance_of_creating_magic_attact_:
    public double chance_of_creating_magic_attack_maximum = 0.5;
    public double chance_of_creating_magic_attack_minimum = 0.0;
    public double chance_of_creating_magic_attack_intelligence_effect = 1.0;
    public double chance_of_creating_magic_attack_dexterity_effect = 0.0;

    //creating_magic_attack_=
    public double creating_magic_attack_maximum_duration = 10.0;
    public double creating_magic_attack_minimum_duration = 0.0;
    public double creating_magic_attack_intelligence_on_duration = 0.75;
    public double creating_magic_attack_dexterity_on_duration = 0.25;
    public double creating_magic_attack_maximum_harm_amplifier = 3.0;
    public double creating_magic_attack_maximum_poison_amplifier = 4.0;
    public double creating_magic_attack_maximum_slow_amplifier = 5.0;
    public double creating_magic_attack_maximum_weakness_amplifier = 5.0;
    public double creating_magic_attack_maximum_wither_amplifier = 5.0;
    public double creating_magic_attack_minimum_harm_amplifier = 3.0;
    public double creating_magic_attack_minimum_poison_amplifier = 4.0;
    public double creating_magic_attack_minimum_slow_amplifier = 5.0;
    public double creating_magic_attack_minimum_weakness_amplifier = 5.0;
    public double creating_magic_attack_minimum_wither_amplifier = 5.0;
    public double creating_magic_attack_intelligence_on_harm_amplifier = 1.0;
    public double creating_magic_attack_intelligence_on_poison_amplifier = 1.0;
    public double creating_magic_attack_intelligence_on_slow_amplifier = 1.0;
    public double creating_magic_attack_intelligence_on_weakness_amplifier = 1.0;
    public double creating_magic_attack_intelligence_on_wither_amplifier = 1.0;

    //normal_attack_damage_=
    public double attack_by_weapon_strength = 1.0;
    public double attack_by_weapon_dexterity = 0.0;
    public double attack_by_weapon_maximum = 2.5;
    public double attack_by_weapon_minimum = 0.05;
    public double attack_by_weapon_range = 2.0;

    //potion_attack_damage_=
    public double attack_by_potion_intelligence = 0.85;
    public double attack_by_potion_dexterity = 0.15;
    public double attack_by_potion_maximum = 2.5;
    public double attack_by_potion_minimum = 0.05;
    public double attack_by_potion_range = 2.0;

    //arrow_attack_damage_=
    public double attack_by_projectile_strength = 0.333;
    public double attack_by_projectile_dexterity = 0.666;
    public double attack_by_projectile_maximum = 2.5;
    public double attack_by_projectile_minimum = 0.05;
    public double attack_by_projectile_range = 2.0;

    //chance_of_removing_magic_effect_=
    public double chance_of_removing_magic_effect_maximum = 0.25;
    public double chance_of_removing_magic_effect_minimum = 0.0;
    public double chance_of_removing_magic_effect_magic_resistance = 0.7;
    public double chance_of_removing_magic_effect_intelligence = 0.2;
    public double chance_of_removing_magic_effect_dexterity = 0.1;

    //on_player_shoot_bow_=
    public double on_player_shoot_bow_min_dexterity_for_max_precision = 50;
    public double on_player_shoot_bow_range = 1.5;

    //on_player_consume_potion_=
    public double on_player_consume_potion_range = 0.1;
    public double on_player_consume_potion_maximum = 2;
    public double on_player_consume_potion_minimum = 1;
    public double on_player_consume_potion_intelligence = 1.0;

    //reducing_bad_potion_effect_=
    public double reducing_bad_potion_effect_maximum = 0.01;
    public double reducing_bad_potion_effect_minimum = 1.0;
    public double reducing_bad_potion_effect_range = 0.15;
    public double reducing_bad_potion_effect_magic_resistance = 0.75;
    public double reducing_bad_potion_effect_intelligence = 0.25;

    public double damaged_by_magic_maximum = 0.333;
    public double damaged_by_magic_minimum = 1.0;
    public double damaged_by_magic_intelligence = 0.15;
    public double damaged_by_magic_magic_resistance = 0.85;

    public double damaged_by_fire_maximum = 0.333;
    public double damaged_by_fire_minimum = 1.0;
    public double damaged_by_fire_defence = 0.25;
    public double damaged_by_fire_magic_resistance = 0.6;
    public double damaged_by_fire_intelligence = 0.15;

    public double damaged_by_explosion_maximum = 0.3333;
    public double damaged_by_explosion_minimum = 1.0;
    public double damaged_by_explosion_defence = 0.85;
    public double damaged_by_explosion_strength = 0.15;

    public double damaged_by_fall_maximum = 0.5;
    public double damaged_by_fall_minimum = 1.0;
    public double damaged_by_fall_defence = 0.25;
    public double damaged_by_fall_agility = 0.75;

    public double damaged_by_contact_maximum = 0.3333;
    public double damaged_by_contact_minimum = 1.0;
    public double damaged_by_contact_defence = 1.0;

    public double damaged_by_projectile_maximum = 0.3333;
    public double damaged_by_projectile_minimum = 1.0;
    public double damaged_by_projectile_defence = 1.0;

    public double damaged_by_weapon_maximum = 0.3333;
    public double damaged_by_weapon_minimum = 1.0;
    public double damaged_by_weapon_defence = 1.0;


    public double attribute_movement_speed_maximum = 0.4;
    public double attribute_movement_speed_minimum = 0.15;
    public double attribute_movement_speed_agility = 1.0;
    public double attribute_movement_speed_dexterity = 0.0;

    public double attribute_attack_speed_maximum = 15.0;
    public double attribute_attack_speed_minimum = 2.0;
    public double attribute_attack_speed_agility = 0.85;
    public double attribute_attack_speed_dexterity = 0.15;
    public double attribute_attack_speed_strength = 0.0;

    public double attribute_total_mana_maximum = 45.0;
    public double attribute_total_mana_minimum = 5.0;
    public double attribute_total_mana_intelligence = 1.0;

    public double attribute_total_health_maximum = 50.0;
    public double attribute_total_health_minimum = 10.0;
    public double attribute_total_health_health = 1.0;

    public double attribute_mana_regen_maximum = 5.0;
    public double attribute_mana_regen_minimum = 0.0;
    public double attribute_mana_regen_intelligence = 1.0;

    public double attribute_luck_maximum = 512;
    public double attribute_luck_minimum = 0;
    public double attribute_luck_intelligence = 0.85;
    public double attribute_luck_dexterity = 0.0;
    public double attribute_luck_agility = 0.15;

    public double attribute_knockback_resistance_maximum = 0.99;
    public double attribute_knockback_resistance_minimum = 0.0;
    public double attribute_knockback_resistance_strength = 0.5;
    public double attribute_knockback_resistance_dexterity = 0.0;
    public double attribute_knockback_resistance_agility = 0.0;
    public double attribute_knockback_resistance_defence = 0.5;

    public double attribute_mob_follow_range_maximum = 1;
    public double attribute_mob_follow_range_minimum = 64;
    public double attribute_mob_follow_range_agility = 0.5;
    public double attribute_mob_follow_range_intelligence = 0.25;
    public double attribute_mob_follow_range_dexterity = 0.25;

    public MathSettings(FileConfiguration config) {
        this.next_lvl_multiplier = config.getDouble("Settings.math.next_lvl_multiplier");
        this.natural_xp_gain_multiplier = config.getDouble("Settings.math.natural_xp_gain_multiplier");
        this.killer_level_gain_multiplier = config.getDouble("Settings.math.on_player_kills_player.killer_level_gain_multiplier");
        this.level_difference_multiplier = config.getDouble("Settings.math.on_player_kills_player.killer_level_gain_multiplier");

        this.chance_of_creating_magic_attack_maximum = chance_of_creating_magic_attack_maximum;
        this.chance_of_creating_magic_attack_minimum = chance_of_creating_magic_attack_minimum;
        this.chance_of_creating_magic_attack_intelligence_effect = chance_of_creating_magic_attack_intelligence_effect;
        this.chance_of_creating_magic_attack_dexterity_effect = chance_of_creating_magic_attack_dexterity_effect;
        this.creating_magic_attack_maximum_duration = creating_magic_attack_maximum_duration;
        this.creating_magic_attack_minimum_duration = creating_magic_attack_minimum_duration;
        this.creating_magic_attack_intelligence_on_duration = creating_magic_attack_intelligence_on_duration;
        this.creating_magic_attack_dexterity_on_duration = creating_magic_attack_dexterity_on_duration;
        this.creating_magic_attack_maximum_harm_amplifier = creating_magic_attack_maximum_harm_amplifier;
        this.creating_magic_attack_maximum_poison_amplifier = creating_magic_attack_maximum_poison_amplifier;
        this.creating_magic_attack_maximum_slow_amplifier = creating_magic_attack_maximum_slow_amplifier;
        this.creating_magic_attack_maximum_weakness_amplifier = creating_magic_attack_maximum_weakness_amplifier;
        this.creating_magic_attack_maximum_wither_amplifier = creating_magic_attack_maximum_wither_amplifier;
        this.creating_magic_attack_minimum_harm_amplifier = creating_magic_attack_minimum_harm_amplifier;
        this.creating_magic_attack_minimum_poison_amplifier = creating_magic_attack_minimum_poison_amplifier;
        this.creating_magic_attack_minimum_slow_amplifier = creating_magic_attack_minimum_slow_amplifier;
        this.creating_magic_attack_minimum_weakness_amplifier = creating_magic_attack_minimum_weakness_amplifier;
        this.creating_magic_attack_minimum_wither_amplifier = creating_magic_attack_minimum_wither_amplifier;
        this.creating_magic_attack_intelligence_on_harm_amplifier = creating_magic_attack_intelligence_on_harm_amplifier;
        this.creating_magic_attack_intelligence_on_poison_amplifier = creating_magic_attack_intelligence_on_poison_amplifier;
        this.creating_magic_attack_intelligence_on_slow_amplifier = creating_magic_attack_intelligence_on_slow_amplifier;
        this.creating_magic_attack_intelligence_on_weakness_amplifier = creating_magic_attack_intelligence_on_weakness_amplifier;
        this.creating_magic_attack_intelligence_on_wither_amplifier = creating_magic_attack_intelligence_on_wither_amplifier;
        this.attack_by_weapon_strength = attack_by_weapon_strength;
        this.attack_by_weapon_dexterity = attack_by_weapon_dexterity;
        this.attack_by_weapon_maximum = attack_by_weapon_maximum;
        this.attack_by_weapon_minimum = attack_by_weapon_minimum;
        this.attack_by_weapon_range = attack_by_weapon_range;
        this.attack_by_potion_intelligence = attack_by_potion_intelligence;
        this.attack_by_potion_dexterity = attack_by_potion_dexterity;
        this.attack_by_potion_maximum = attack_by_potion_maximum;
        this.attack_by_potion_minimum = attack_by_potion_minimum;
        this.attack_by_potion_range = attack_by_potion_range;
        this.attack_by_projectile_strength = attack_by_projectile_strength;
        this.attack_by_projectile_dexterity = attack_by_projectile_dexterity;
        this.attack_by_projectile_maximum = attack_by_projectile_maximum;
        this.attack_by_projectile_minimum = attack_by_projectile_minimum;
        this.attack_by_projectile_range = attack_by_projectile_range;
        this.chance_of_removing_magic_effect_maximum = chance_of_removing_magic_effect_maximum;
        this.chance_of_removing_magic_effect_minimum = chance_of_removing_magic_effect_minimum;
        this.chance_of_removing_magic_effect_magic_resistance = chance_of_removing_magic_effect_magic_resistance;
        this.chance_of_removing_magic_effect_intelligence = chance_of_removing_magic_effect_intelligence;
        this.chance_of_removing_magic_effect_dexterity = chance_of_removing_magic_effect_dexterity;
        this.on_player_shoot_bow_min_dexterity_for_max_precision = on_player_shoot_bow_min_dexterity_for_max_precision;
        this.on_player_shoot_bow_range = on_player_shoot_bow_range;
        this.on_player_consume_potion_range = on_player_consume_potion_range;
        this.on_player_consume_potion_maximum = on_player_consume_potion_maximum;
        this.on_player_consume_potion_minimum = on_player_consume_potion_minimum;
        this.on_player_consume_potion_intelligence = on_player_consume_potion_intelligence;
        this.reducing_bad_potion_effect_maximum = reducing_bad_potion_effect_maximum;
        this.reducing_bad_potion_effect_minimum = reducing_bad_potion_effect_minimum;
        this.reducing_bad_potion_effect_range = reducing_bad_potion_effect_range;
        this.reducing_bad_potion_effect_magic_resistance = reducing_bad_potion_effect_magic_resistance;
        this.reducing_bad_potion_effect_intelligence = reducing_bad_potion_effect_intelligence;
        this.damaged_by_magic_maximum = damaged_by_magic_maximum;
        this.damaged_by_magic_minimum = damaged_by_magic_minimum;
        this.damaged_by_magic_intelligence = damaged_by_magic_intelligence;
        this.damaged_by_magic_magic_resistance = damaged_by_magic_magic_resistance;
        this.damaged_by_fire_maximum = damaged_by_fire_maximum;
        this.damaged_by_fire_minimum = damaged_by_fire_minimum;
        this.damaged_by_fire_defence = damaged_by_fire_defence;
        this.damaged_by_fire_magic_resistance = damaged_by_fire_magic_resistance;
        this.damaged_by_fire_intelligence = damaged_by_fire_intelligence;
        this.damaged_by_explosion_maximum = damaged_by_explosion_maximum;
        this.damaged_by_explosion_minimum = damaged_by_explosion_minimum;
        this.damaged_by_explosion_defence = damaged_by_explosion_defence;
        this.damaged_by_explosion_strength = damaged_by_explosion_strength;
        this.damaged_by_fall_maximum = damaged_by_fall_maximum;
        this.damaged_by_fall_minimum = damaged_by_fall_minimum;
        this.damaged_by_fall_defence = damaged_by_fall_defence;
        this.damaged_by_fall_agility = damaged_by_fall_agility;
        this.damaged_by_contact_maximum = damaged_by_contact_maximum;
        this.damaged_by_contact_minimum = damaged_by_contact_minimum;
        this.damaged_by_contact_defence = damaged_by_contact_defence;
        this.damaged_by_projectile_maximum = damaged_by_projectile_maximum;
        this.damaged_by_projectile_minimum = damaged_by_projectile_minimum;
        this.damaged_by_projectile_defence = damaged_by_projectile_defence;
        this.damaged_by_weapon_maximum = damaged_by_weapon_maximum;
        this.damaged_by_weapon_minimum = damaged_by_weapon_minimum;
        this.damaged_by_weapon_defence = damaged_by_weapon_defence;
        this.attribute_movement_speed_maximum = attribute_movement_speed_maximum;
        this.attribute_movement_speed_minimum = attribute_movement_speed_minimum;
        this.attribute_movement_speed_agility = attribute_movement_speed_agility;
        this.attribute_movement_speed_dexterity = attribute_movement_speed_dexterity;
        this.attribute_attack_speed_maximum = attribute_attack_speed_maximum;
        this.attribute_attack_speed_minimum = attribute_attack_speed_minimum;
        this.attribute_attack_speed_agility = attribute_attack_speed_agility;
        this.attribute_attack_speed_dexterity = attribute_attack_speed_dexterity;
        this.attribute_attack_speed_strength = attribute_attack_speed_strength;
        this.attribute_total_mana_maximum = attribute_total_mana_maximum;
        this.attribute_total_mana_minimum = attribute_total_mana_minimum;
        this.attribute_total_mana_intelligence = attribute_total_mana_intelligence;
        this.attribute_total_health_maximum = attribute_total_health_maximum;
        this.attribute_total_health_minimum = attribute_total_health_minimum;
        this.attribute_total_health_health = attribute_total_health_health;
        this.attribute_mana_regen_maximum = attribute_mana_regen_maximum;
        this.attribute_mana_regen_minimum = attribute_mana_regen_minimum;
        this.attribute_mana_regen_intelligence = attribute_mana_regen_intelligence;
        this.attribute_luck_maximum = attribute_luck_maximum;
        this.attribute_luck_minimum = attribute_luck_minimum;
        this.attribute_luck_intelligence = attribute_luck_intelligence;
        this.attribute_luck_dexterity = attribute_luck_dexterity;
        this.attribute_luck_agility = attribute_luck_agility;
        this.attribute_knockback_resistance_maximum = attribute_knockback_resistance_maximum;
        this.attribute_knockback_resistance_minimum = attribute_knockback_resistance_minimum;
        this.attribute_knockback_resistance_strength = attribute_knockback_resistance_strength;
        this.attribute_knockback_resistance_dexterity = attribute_knockback_resistance_dexterity;
        this.attribute_knockback_resistance_agility = attribute_knockback_resistance_agility;
        this.attribute_knockback_resistance_defence = attribute_knockback_resistance_defence;
        this.attribute_mob_follow_range_maximum = attribute_mob_follow_range_maximum;
        this.attribute_mob_follow_range_minimum = attribute_mob_follow_range_minimum;
        this.attribute_mob_follow_range_agility = attribute_mob_follow_range_agility;
        this.attribute_mob_follow_range_intelligence = attribute_mob_follow_range_intelligence;
        this.attribute_mob_follow_range_dexterity = attribute_mob_follow_range_dexterity;
    }
}
