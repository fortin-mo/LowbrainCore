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

    //reducing_bad_potion_effect_=
    public double reducing_bad_potion_effect_maximum = 0.01;
    public double reducing_bad_potion_effect_minimum = 1.0;
    public double reducing_bad_potion_effect_range = 0.15;
    public double reducing_bad_potion_effect_magic_resistance_effect = 0.75;
    public double reducing_bad_potion_effect_intelligence_effect = 0.25;

    //normal_attack_damage_=
    public double normal_attack_damage_strength_effect_on_multiplier = 1.0;
    public double normal_attack_damage_dexterity_effect_on_multiplier = 0.0;
    public double normal_attack_damage_maximum_damage_multiplier = 2.5;
    public double normal_attack_damage_minimum_damage_multiplier = 0.05;
    public double normal_attack_damage_range = 2.0;

    //potion_attack_damage_=
    public double potion_attack_damage_intelligence_effect_on_multiplier = 85;
    public double potion_attack_damage_dexterity_effect_on_multiplier = 0.15;
    public double potion_attack_damage_maximum_damage_multiplier = 2.5;
    public double potion_attack_damage_minimum_damage_multiplier = 0.05;
    public double potion_attack_damage_range = 2.0;

    //arrow_attack_damage_=
    public double arrow_attack_damage_strength_effect_on_multiplier = 0.333;
    public double arrow_attack_damage_dexterity_effect_on_multiplier = 0.666;
    public double arrow_attack_damage_maximum_damage_multiplier = 2.5;
    public double arrow_attack_damage_minimum_damage_multiplier = 0.05;
    public double arrow_attack_damage_range = 2.0;

    //normal_and_arrow_attack_defence_=
    public double normal_and_arrow_attack_defence_defence_effect = 1.0;
    public double normal_and_arrow_attack_defence_maximum_damage_multiplier = 0.3333;
    public double normal_and_arrow_attack_defence_minimum_damage_multiplier = 1.0;

    //potion_attack_defence_=
    public double potion_attack_defence_intelligence_effect = 0.15;
    public double potion_attack_defence_magic_resistance_effect = 0.85;
    public double potion_attack_defence_maximum_damage_multiplier = 0.3333;
    public double potion_attack_defence_minimum_damage_multiplier = 1.0;

    //chance_of_removing_magic_effect_=
    public double chance_of_removing_magic_effect_maximum = 0.25;
    public double chance_of_removing_magic_effect_minimum = 0.0;
    public double chance_of_removing_magic_effect_magic_resistance_effect_on_multiplier = 0.7;
    public double chance_of_removing_magic_effect_intelligence_effect_on_multiplier = 0.2;
    public double chance_of_removing_magic_effect_dexterity_effect_on_multiplier = 0.1;

    //on_player_shoot_bow_=
    public double on_player_shoot_bow_min_dexterity_for_max_precision = 50;
    public double on_player_shoot_bow_range = 1.5;

    //on_player_consume_potion_=
    public double on_player_consume_potion_multiplier_range = 0.1;
    public double on_player_consume_potion_maximum_multiplier = 2;
    public double on_player_consume_potion_minimum_multiplier = 1;
    public double on_player_consume_potion_intelligence_effect = 1.0;


    public MathSettings(FileConfiguration config) {
        this.next_lvl_multiplier = config.getDouble("Settings.math.next_lvl_multiplier");
        this.natural_xp_gain_multiplier = config.getDouble("Settings.math.natural_xp_gain_multiplier");
        this.killer_level_gain_multiplier = config.getDouble("Settings.math.on_player_kills_player.killer_level_gain_multiplier");
        this.level_difference_multiplier = config.getDouble("Settings.math.on_player_kills_player.killer_level_gain_multiplier");

    }
}
