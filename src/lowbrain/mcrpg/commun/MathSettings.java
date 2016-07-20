package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Moofy on 18/07/2016.
 */
public class MathSettings {

    public float next_lvl_multiplier = 1.1F;
    public float natural_xp_gain_multiplier = 1F;
    public float killer_level_gain_multiplier = 0.5F;
    public float level_difference_multiplier = 0.5F;

    //on_player_attack_entity =
    //chance_of_creating_magic_attact_:
    public float chance_of_creating_magic_attack_maximum = 0.5F;
    public float chance_of_creating_magic_attack_minimum = 0.0F;
    public float chance_of_creating_magic_attack_intelligence_effect = 1.0F;
    public float chance_of_creating_magic_attack_dexterity_effect = 0.0F;

    //creating_magic_attack_=
    public boolean creating_magic_attack_enable = true;
    public float creating_magic_attack_maximum_duration = 10.0F;
    public float creating_magic_attack_minimum_duration = 0.0F;
    public float creating_magic_attack_intelligence_on_duration = 0.75F;
    public float creating_magic_attack_dexterity_on_duration = 0.25F;
    public float creating_magic_attack_maximum_harm_amplifier = 3.0F;
    public float creating_magic_attack_maximum_poison_amplifier = 4.0F;
    public float creating_magic_attack_maximum_slow_amplifier = 5.0F;
    public float creating_magic_attack_maximum_weakness_amplifier = 5.0F;
    public float creating_magic_attack_maximum_wither_amplifier = 5.0F;
    public float creating_magic_attack_minimum_harm_amplifier = 3.0F;
    public float creating_magic_attack_minimum_poison_amplifier = 4.0F;
    public float creating_magic_attack_minimum_slow_amplifier = 5.0F;
    public float creating_magic_attack_minimum_weakness_amplifier = 5.0F;
    public float creating_magic_attack_minimum_wither_amplifier = 5.0F;
    public float creating_magic_attack_intelligence_on_harm_amplifier = 1.0F;
    public float creating_magic_attack_intelligence_on_poison_amplifier = 1.0F;
    public float creating_magic_attack_intelligence_on_slow_amplifier = 1.0F;
    public float creating_magic_attack_intelligence_on_weakness_amplifier = 1.0F;
    public float creating_magic_attack_intelligence_on_wither_amplifier = 1.0F;

    //normal_attack_damage_=
    public boolean attack_by_weapon_enable = true;
    public String attack_by_weapon_function = "";
    public float attack_by_weapon_strength = 1.0F;
    public float attack_by_weapon_dexterity = 0.0F;
    public float attack_by_weapon_maximum = 2.5F;
    public float attack_by_weapon_minimum = 0.05F;
    public float attack_by_weapon_range = 2.0F;

    //potion_attack_damage_=
    public boolean attack_by_potion_enable = true;
    public String attack_by_potion_function = "";
    public float attack_by_potion_intelligence = 0.85F;
    public float attack_by_potion_dexterity = 0.15F;
    public float attack_by_potion_maximum = 2.5F;
    public float attack_by_potion_minimum = 0.05F;
    public float attack_by_potion_range = 2.0F;

    //arrow_attack_damage_=
    public boolean attack_by_projectile_enable = true;
    public String attack_by_projectile_function = "";
    public float attack_by_projectile_strength = 0.333F;
    public float attack_by_projectile_dexterity = 0.666F;
    public float attack_by_projectile_maximum = 2.5F;
    public float attack_by_projectile_minimum = 0.05F;
    public float attack_by_projectile_range = 2.0F;

    //chance_of_removing_magic_effect_=
    public boolean chance_of_removing_magic_effect_enable = true;
    public String chance_of_removing_magic_effect_function = "";
    public float chance_of_removing_magic_effect_maximum = 0.25F;
    public float chance_of_removing_magic_effect_minimum = 0.0F;
    public float chance_of_removing_magic_effect_magic_resistance = 0.7F;
    public float chance_of_removing_magic_effect_intelligence = 0.2F;
    public float chance_of_removing_magic_effect_dexterity = 0.1F;

    //on_player_shoot_bow_=
    public boolean on_player_shoot_bow_enable = true;
    public String on_player_shoot_bow_function = "";
    public float on_player_shoot_bow_min_dexterity_for_max_precision = 50F;
    public float on_player_shoot_bow_range = 1.5F;

    //on_player_consume_potion_=
    public boolean on_player_consume_potion_enable = true;
    public String on_player_consume_potion_function = "";
    public float on_player_consume_potion_range = 0.1F;
    public float on_player_consume_potion_maximum = 2F;
    public float on_player_consume_potion_minimum = 1F;
    public float on_player_consume_potion_intelligence = 1.0F;

    //reducing_bad_potion_effect_=
    public boolean reducing_bad_potion_effect_enable = true;
    public String reducing_bad_potion_effect_function = "";
    public float reducing_bad_potion_effect_maximum = 0.01F;
    public float reducing_bad_potion_effect_minimum = 1.0F;
    public float reducing_bad_potion_effect_range = 0.15F;
    public float reducing_bad_potion_effect_magic_resistance = 0.75F;
    public float reducing_bad_potion_effect_intelligence = 0.25F;

    public boolean damaged_by_magic_enable = true;
    public String damaged_by_magic_function = "";
    public float damaged_by_magic_maximum = 0.333F;
    public float damaged_by_magic_minimum = 1.0F;
    public float damaged_by_magic_intelligence = 0.15F;
    public float damaged_by_magic_magic_resistance = 0.85F;

    public boolean damaged_by_fire_enable = true;
    public String damaged_by_fire_function = "";
    public float damaged_by_fire_maximum = 0.333F;
    public float damaged_by_fire_minimum = 1.0F;
    public float damaged_by_fire_defence = 0.25F;
    public float damaged_by_fire_magic_resistance = 0.6F;
    public float damaged_by_fire_intelligence = 0.15F;

    public boolean damaged_by_explosion_enable = true;
    public String damaged_by_explosion_function = "";
    public float damaged_by_explosion_maximum = 0.3333F;
    public float damaged_by_explosion_minimum = 1.0F;
    public float damaged_by_explosion_defence = 0.85F;
    public float damaged_by_explosion_strength = 0.15F;

    public boolean damaged_by_fall_enable = true;
    public String damaged_by_fall_function = "";
    public float damaged_by_fall_maximum = 0.5F;
    public float damaged_by_fall_minimum = 1.0F;
    public float damaged_by_fall_defence = 0.25F;
    public float damaged_by_fall_agility = 0.75F;

    public boolean damaged_by_contact_enable = true;
    public String damaged_by_contact_function = "";
    public float damaged_by_contact_maximum = 0.3333F;
    public float damaged_by_contact_minimum = 1.0F;
    public float damaged_by_contact_defence = 1.0F;

    public boolean damaged_by_projectile_enable = true;
    public String damaged_by_projectile_function = "";
    public float damaged_by_projectile_maximum = 0.3333F;
    public float damaged_by_projectile_minimum = 1.0F;
    public float damaged_by_projectile_defence = 1.0F;

    public boolean damaged_by_weapon_enable = true;
    public String damaged_by_weapon_function = "";
    public float damaged_by_weapon_maximum = 0.3333F;
    public float damaged_by_weapon_minimum = 1.0F;
    public float damaged_by_weapon_defence = 1.0F;

    public boolean attribute_movement_speed_enable = true;
    public String attribute_movement_speed_function = "";
    public float attribute_movement_speed_maximum = 0.4F;
    public float attribute_movement_speed_minimum = 0.15F;
    public float attribute_movement_speed_agility = 1.0F;
    public float attribute_movement_speed_dexterity = 0.0F;

    public boolean attribute_attack_speed_enable = true;
    public String attribute_attack_speed_function = "";
    public float attribute_attack_speed_maximum = 15.0F;
    public float attribute_attack_speed_minimum = 2.0F;
    public float attribute_attack_speed_agility = 0.85F;
    public float attribute_attack_speed_dexterity = 0.15F;
    public float attribute_attack_speed_strength = 0.0F;

    public boolean attribute_total_mana_enable = true;
    public String attribute_total_mana_function = "";
    public float attribute_total_mana_intelligence = 1.0F;

    public boolean attribute_total_health_enable = true;
    public String attribute_total_health_function = "";
    public float attribute_total_health_health = 1.0F;

    public boolean attribute_mana_regen_enable = true;
    public String attribute_mana_regen_function = "";
    public float attribute_mana_regen_maximum = 5.0F;
    public float attribute_mana_regen_minimum = 0.0F;
    public float attribute_mana_regen_intelligence = 1.0F;

    public boolean attribute_luck_enable = true;
    public String attribute_luck_function = "";
    public float attribute_luck_maximum = 512F;
    public float attribute_luck_minimum = 0F;
    public float attribute_luck_intelligence = 0.85F;
    public float attribute_luck_dexterity = 0.0F;
    public float attribute_luck_agility = 0.15F;

    public boolean attribute_knockback_resistance_enable = true;
    public String attribute_knockback_resistance_function = "";
    public float attribute_knockback_resistance_maximum = 0.99F;
    public float attribute_knockback_resistance_minimum = 0.0F;
    public float attribute_knockback_resistance_strength = 0.5F;
    public float attribute_knockback_resistance_dexterity = 0.0F;
    public float attribute_knockback_resistance_agility = 0.0F;
    public float attribute_knockback_resistance_defence = 0.5F;

    public boolean attribute_mob_follow_range_enable = true;
    public String attribute_mob_follow_range_function = "";
    public float attribute_mob_follow_range_maximum = 1F;
    public float attribute_mob_follow_range_minimum = 64F;
    public float attribute_mob_follow_range_agility = 0.5F;
    public float attribute_mob_follow_range_intelligence = 0.25F;
    public float attribute_mob_follow_range_dexterity = 0.25F;

    public MathSettings(FileConfiguration config) {

    }
}
