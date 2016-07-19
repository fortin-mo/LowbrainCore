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


    public MathSettings(FileConfiguration config) {
        this.next_lvl_multiplier = config.getDouble("Settings.math.next_lvl_multiplier");
        this.natural_xp_gain_multiplier = config.getDouble("Settings.math.natural_xp_gain_multiplier");
        this.killer_level_gain_multiplier = config.getDouble("Settings.math.on_player_kills_player.killer_level_gain_multiplier");
        this.level_difference_multiplier = config.getDouble("Settings.math.on_player_kills_player.killer_level_gain_multiplier");

    }
}
