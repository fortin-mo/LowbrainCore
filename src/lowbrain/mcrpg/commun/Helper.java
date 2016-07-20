package lowbrain.mcrpg.commun;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Painting;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Created by Moofy on 19/07/2016.
 */


public class Helper {

    public static float eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            float parse() {
                nextChar();
                float x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            float parseExpression() {
                float x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            float parseTerm() {
                float x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            float parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                float x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Float.parseFloat(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = (float)Math.sqrt(x);
                    else if (func.equals("sin")) x = (float)Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = (float)Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = (float)Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = (float)Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public static double Gradient(double max, double min, Config config)
    {
        int maxstats = config.max_stats <= 0 ? 100 : config.max_stats;
        return (max - min)/maxstats;
    }

    //ON PLAYER GET DAMAGED

    public static float getDamagedByFire(RPGPlayer damagee, Config config){

        if(StringIsNullOrEmplty(config.math.damaged_by_fire_function)){
            float max = config.math.damaged_by_fire_maximum;
            float min = config.math.damaged_by_fire_minimum;
            double x = damagee.getMagicResistance() * config.math.damaged_by_fire_magic_resistance
                    + damagee.getDefence() * config.math.damaged_by_fire_defence
                    + damagee.getIntelligence() * config.math.damaged_by_fire_intelligence;

            return (float)(Gradient(max,min,config) * x + min);
        }
        else{
            String[] st = config.math.damaged_by_fire_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getDamagedByContact(RPGPlayer damagee, Config config){

        if(StringIsNullOrEmplty(config.math.damaged_by_contact_function)){
            float max = config.math.damaged_by_contact_maximum;
            float min = config.math.damaged_by_contact_minimum;
            double x = damagee.getDefence() * config.math.damaged_by_contact_defence;

            return (float)(Gradient(max,min,config) * x + min);
        }
        else{
            String[] st = config.math.damaged_by_contact_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getDamagedByWeapon(RPGPlayer damagee, Config config){

        if(StringIsNullOrEmplty(config.math.damaged_by_weapon_function)){
            float max = config.math.damaged_by_weapon_maximum;
            float min = config.math.damaged_by_weapon_minimum;
            double x = damagee.getDefence() * config.math.damaged_by_weapon_defence;

            return (float)(Gradient(max,min,config) * x + min);
        }
        else{
            String[] st = config.math.damaged_by_weapon_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getDamagedByMagic(RPGPlayer damagee, Config config){

        if(StringIsNullOrEmplty(config.math.damaged_by_magic_function)){
            float max = config.math.damaged_by_magic_maximum;
            float min = config.math.damaged_by_magic_minimum;
            double x = (damagee.getMagicResistance() * config.math.damaged_by_magic_magic_resistance
                    + damagee.getIntelligence() * config.math.damaged_by_magic_intelligence);

            return (float)(Gradient(max,min,config) * x + min);
        }
        else{
            String[] st = config.math.damaged_by_magic_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getDamagedByFall(RPGPlayer damagee, Config config){

        if(StringIsNullOrEmplty(config.math.damaged_by_fall_function)){
            float max = config.math.damaged_by_fall_maximum;
            float min = config.math.damaged_by_fall_minimum;
            double x = damagee.getAgility() * config.math.damaged_by_fall_agility
                    + damagee.getDefence() * config.math.damaged_by_fall_defence;

            return (float)(Gradient(max,min,config) * x + min);
        }
        else{
            String[] st = config.math.damaged_by_fall_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getDamagedByExplosion(RPGPlayer damagee, Config config){

        if(StringIsNullOrEmplty(config.math.damaged_by_explosion_function)){
            float max = config.math.damaged_by_explosion_maximum;
            float min = config.math.damaged_by_explosion_minimum;
            double x = damagee.getDefence() * config.math.damaged_by_explosion_defence
                    + damagee.getStrength() * config.math.damaged_by_explosion_strength;

            return (float)(Gradient(max,min,config) * x + min);
        }
        else{
            String[] st = config.math.damaged_by_explosion_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getDamagedByProjectile(RPGPlayer damagee, Config config){

        if(StringIsNullOrEmplty(config.math.damaged_by_projectile_function)){
            float max = config.math.damaged_by_projectile_maximum;
            float min = config.math.damaged_by_projectile_minimum;
            double x = damagee.getDefence() * config.math.damaged_by_projectile_defence;

            return (float)(Gradient(max,min,config) * x + min);
        }
        else{
            String[] st = config.math.damaged_by_projectile_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getChangeOfRemovingPotionEffect(RPGPlayer damagee, Config config){

        if(StringIsNullOrEmplty(config.math.reducing_bad_potion_effect_function)){
            float minChance = config.math.chance_of_removing_magic_effect_minimum;
            float maxChance = config.math.chance_of_removing_magic_effect_maximum;

            return (float)(Gradient(maxChance,minChance,config)
                    * (damagee.getMagicResistance()* config.math.chance_of_removing_magic_effect_magic_resistance
                    + damagee.getIntelligence() * config.math.chance_of_removing_magic_effect_intelligence
                    + damagee.getDexterity() * config.math.chance_of_removing_magic_effect_dexterity)
                    +minChance);
        }
        else{
            String[] st = config.math.chance_of_removing_magic_effect_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getReducingPotionEffect(RPGPlayer damagee, Config config){
        if(StringIsNullOrEmplty(config.math.reducing_bad_potion_effect_function)){
            float min = config.math.reducing_bad_potion_effect_minimum;
            float max = config.math.reducing_bad_potion_effect_maximum;
            float range = config.math.reducing_bad_potion_effect_range;

            float reduction = (float)Gradient(max,min,config)
                    * (damagee.getMagicResistance()* config.math.reducing_bad_potion_effect_magic_resistance
                    + damagee.getIntelligence()) * config.math.reducing_bad_potion_effect_intelligence
                    + min;

            float minReduction = reduction < (min - range) ? reduction + range : min;

            return (float)(reduction + (Math.random() * minReduction));
        }
        else{
            String[] st = config.math.reducing_bad_potion_effect_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,damagee));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    //PLAYER ATTRIBUTES

    public static float getPlayerMaxHealth(RPGPlayer p, Config config){
        if(StringIsNullOrEmplty(config.math.attribute_total_health_function)) {
            return (float)Gradient(p.getRpgRace().getMax_health(), p.getRpgRace().getBase_health(),config)
                    * p.getHealth() * config.math.attribute_total_health_health
                    + p.getRpgRace().getBase_health();
        }
        else{
            String[] st = config.math.attribute_total_health_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,p));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getPlayerMaxMana(RPGPlayer p, Config config){
        if(StringIsNullOrEmplty(config.math.attribute_total_mana_function)) {
           return (float) Gradient(p.getRpgRace().getMax_mana(), p.getRpgRace().getBase_mana(), config)
                    * p.getIntelligence() * config.math.attribute_total_mana_intelligence
                    + p.getRpgRace().getBase_mana();
        }
        else{
            String[] st = config.math.attribute_total_mana_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,p));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getPlayerManaRegen(RPGPlayer p, Config config){
        if(StringIsNullOrEmplty(config.math.attribute_mana_regen_function)) {
            return (float)Gradient(config.math.attribute_mana_regen_maximum,config.math.attribute_mana_regen_minimum,config)
                    * p.getIntelligence() * config.math.attribute_mana_regen_intelligence
                    + config.math.attribute_mana_regen_minimum;
        }
        else{
            String[] st = config.math.attribute_mana_regen_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,p));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getPlayerAttackSpeed(RPGPlayer p, Config config){
        if(StringIsNullOrEmplty(config.math.attribute_attack_speed_function)) {
            return (float)(Gradient(config.math.attribute_attack_speed_maximum, config.math.attribute_attack_speed_minimum,config)
                    * (p.getAgility() * config.math.attribute_attack_speed_agility
                    + p.getDexterity() * config.math.attribute_attack_speed_dexterity
                    + p.getStrength() * config.math.attribute_attack_speed_strength)
                    + config.math.attribute_attack_speed_minimum);
        }
        else{
            String[] st = config.math.attribute_attack_speed_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,p));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getPlayerMovementSpeed(RPGPlayer p, Config config){
        if(StringIsNullOrEmplty(config.math.attribute_movement_speed_function)) {
            return (float)Gradient(config.math.attribute_movement_speed_maximum,config.math.attribute_movement_speed_minimum,config)
                    * (p.getAgility() * config.math.attribute_movement_speed_agility
                    + p.getDexterity() * config.math.attribute_movement_speed_dexterity)
                    + config.math.attribute_movement_speed_minimum;
        }
        else{
            String[] st = config.math.attribute_movement_speed_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,p));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getPlayerMobFollowRange(RPGPlayer p, Config config){
        if(StringIsNullOrEmplty(config.math.attribute_mob_follow_range_function)) {
            return (float)Gradient(config.math.attribute_mob_follow_range_maximum,config.math.attribute_mob_follow_range_minimum,config)
                    * (p.getAgility() * config.math.attribute_mob_follow_range_agility
                    + p.getIntelligence() * config.math.attribute_mob_follow_range_intelligence
                    + p.getDexterity() * config.math.attribute_mob_follow_range_dexterity)
                    + config.math.attribute_mob_follow_range_minimum;
        }
        else{
            String[] st = config.math.attribute_mob_follow_range_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,p));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getPlayerKnockbackResistance(RPGPlayer p, Config config){
        if(StringIsNullOrEmplty(config.math.attribute_knockback_resistance_function)) {
            return (float)Gradient(config.math.attribute_knockback_resistance_maximum,config.math.attribute_knockback_resistance_minimum,config)
                    * (p.getStrength() * config.math.attribute_knockback_resistance_strength
                    + p.getDefence() * config.math.attribute_knockback_resistance_defence
                    + p.getDexterity() * config.math.attribute_knockback_resistance_dexterity
                    + p.getAgility() * config.math.attribute_knockback_resistance_agility);
        }
        else{
            String[] st = config.math.attribute_knockback_resistance_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,p));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    public static float getPlayerLuck(RPGPlayer p, Config config){
        if(StringIsNullOrEmplty(config.math.attribute_luck_function)) {
            return (float)Gradient(config.math.attribute_luck_maximum,config.math.attribute_luck_minimum,config)
                    * (p.getAgility() * config.math.attribute_luck_agility
                    + p.getIntelligence() * config.math.attribute_luck_intelligence
                    + p.getDexterity() * config.math.attribute_luck_dexterity);
        }
        else{
            String[] st = config.math.attribute_luck_function.split(",");
            if(st.length > 1){
                return eval(FormatStringWithValues(st,p));
            }
            else{
                return eval(st[0]);
            }
        }
    }

    //PRIVATES

    private static String FormatStringWithValues(String[] st, RPGPlayer p){

        for (int i = 1; i < st.length; i++) {
            switch (st[i].trim().toLowerCase()){
                case "health":
                    st[i] = Integer.toString(p.getHealth());
                    break;
                case "strength":
                    st[i] = Integer.toString(p.getStrength());
                    break;
                case "intelligence":
                    st[i] = Integer.toString(p.getIntelligence());
                    break;
                case "dexterity":
                    st[i] = Integer.toString(p.getDexterity());
                    break;
                case "magic_resistance":
                    st[i] = Integer.toString(p.getMagicResistance());
                    break;
                case "defence":
                    st[i] = Integer.toString(p.getDeaths());
                    break;
                case "agility":
                    st[i] = Integer.toString(p.getAgility());
                    break;
                default:
                    st[i] = "0";
                    break;
            }
        }

        MessageFormat fmt = new MessageFormat(st[0]);
        return fmt.format(Arrays.copyOfRange(st,1, st.length));
    }

    public static boolean StringIsNullOrEmplty(String s){
        return s == null || s.trim().isEmpty();
    }

}
