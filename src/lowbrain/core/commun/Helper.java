package lowbrain.core.commun;

import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.LowbrainPlayer;
import org.bukkit.util.Vector;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static String FormatStringWithValues(String[] st, LowbrainPlayer p){
        if(st.length > 1 && p != null){
            for (int i = 1; i < st.length; i++) {
                st[i] = Integer.toString(p.getAttribute(st[i].trim().toLowerCase(),1));
            }
        }
        else {
            return st[0];
        }

        MessageFormat fmt = new MessageFormat(st[0]);
        return fmt.format(Arrays.copyOfRange(st,1, st.length));
    }

    /***
     * check if string is null or empty
     * @param s
     * @return
     */
    public static boolean StringIsNullOrEmpty(String s){
        return s == null || s.trim().length() == 0;
    }

    /**
     * generate a random float [min,max]
     * @param max max
     * @param min min
     * @return
     */
    public static float randomFloat(float min, float max){
        float range = (max - min);
        return ((float)Math.random() * range) + min;
    }

    /**
     * generate a int float [min,max]
     * @param max max
     * @param min min
     * @return
     */
    public static int randomInt(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    /**
     * parse string to int
     * @param s string
     * @param d default value
     * @return
     */
    public static int intTryParse(String s, Integer d){
        try {
            return Integer.parseInt(s);
        }catch (Exception e){
            return d;
        }
    }

    public static double doubleTryParse(String s, Double d){
        try {
            return Double.parseDouble(s);
        }catch (Exception e){
            return d;
        }
    }

    public static float floatTryParse(String s, Float d){
        try {
            return Float.parseFloat(s);
        }catch (Exception e){
            return d;
        }
    }

    /***
     * parse string to date
     * @param s
     * @param c
     * @return
     */
    public static Calendar dateTryParse(String s, Calendar c){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date date = sdf.parse(s);// all done
            Calendar cal = sdf.getCalendar();
            cal.setTime(date);
            return cal;
        }catch (Exception e){
            return c;
        }
    }

    /***
     * rotate vector on the xy plan
     * @param dir
     * @param angleD
     * @return
     */
    public static Vector rotateYAxis(Vector dir, double angleD) {
        double angleR = Math.toRadians(angleD);
        double x = dir.getX();
        double z = dir.getZ();
        double cos = Math.cos(angleR);
        double sin = Math.sin(angleR);
        return (new Vector(x*cos+z*(-sin), dir.getY(), x*sin+z*cos)).normalize();
    }

    /***
     * gets the X value ( y = ax + b ) depending on attributes influence and player attributes
     * @param variables
     * @param p
     * @return
     */
    public static float getXValue(HashMap<String,Float> variables, LowbrainPlayer p){
        float x = 0;
        for(Map.Entry<String, Float> inf : variables.entrySet()) {
            String n = inf.getKey().toLowerCase();
            float v = inf.getValue();
            x += (v * p.getAttribute(n,0));
        }
        return x;
    }

    /***
     * return max stats. if max stats <= 0 return 100
     * @return
     */
    private static int getMaxStats(){
        return Settings.getInstance().max_stats <= 0 ? 100 : Settings.getInstance().max_stats;
    }

    public static float Slope(float max, float min){
        return Slope(max,min,getMaxStats());
    }

    public static float Slope(float max, float min, float y){
        return Slope(max,min,y,Settings.getInstance().maths.function_type);
    }

    public static float Slope(float max, float min, float y, int functionType){
        float slope = 0;
        switch (functionType){
            case 1:
                slope = (max - min)/(float)Math.pow(y,2);
                break;
            case 2:
                slope = (max - min)/(float)Math.pow(y,0.5);
                break;
            default:
                slope = (max - min)/y;
                break;
        }
        return slope;
    }

    /**
     * evaluate value
     * @param max max
     * @param min min
     * @param x value
     * @return
     */
    public static float ValueFromFunction(float max, float min, float x){
        float result = 0;
        switch (Settings.getInstance().maths.function_type){
            case 1:
                result = Slope(max,min) * (float)Math.pow(x,2) + min;
                break;
            case 2:
                result = Slope(max,min) * (float)Math.pow(x,0.5) + min;
                break;
            default:
                result = Slope(max,min) * x + min;
                break;
        }
        return result;
    }

    public static float ValueFromFunction(float max, float min,HashMap<String,Float> variables, LowbrainPlayer p){
        return ValueFromFunction(max,min,getXValue(variables,p));
    }

    /**
     * get all nearby players of a player
     * @param p1 player one
     * @param max maximum distance
     * @return
     */
    public static List<LowbrainPlayer> getNearbyPlayers(LowbrainPlayer p1, double max){
        List<LowbrainPlayer> lst = new ArrayList<LowbrainPlayer>();

        for (LowbrainPlayer p2: LowbrainCore.getInstance().getPlayerHandler().getList().values()) {
            if(p1.equals(p2))continue;//if its the same player
            if(p1.getPlayer().getWorld().equals(p2.getPlayer().getWorld())){//check if they are in the same world
                double x = p1.getPlayer().getLocation().getX() - p2.getPlayer().getLocation().getX();
                double y = p1.getPlayer().getLocation().getY() - p2.getPlayer().getLocation().getY();
                double z = p1.getPlayer().getLocation().getZ() - p2.getPlayer().getLocation().getZ();

                double distance = Math.pow(x*x + y*y + z*z,0.5);

                if(distance <= max){
                    lst.add(p2);
                }
            }
        }

        return lst;
    }

    // ON PLAYER ATTACK

    public static float getBowArrowSpeed(LowbrainPlayer p){
        float result = p.getMultipliers().getBowArrowSpeed();

        if(Settings.getInstance().maths.onPlayerShootBow.speed_range > 0){
            result = Helper.randomFloat((result - Settings.getInstance().maths.onPlayerShootBow.speed_range),(result + Settings.getInstance().maths.onPlayerShootBow.speed_range));
            if(result < Settings.getInstance().maths.onPlayerShootBow.speed_minimum)result = Settings.getInstance().maths.onPlayerShootBow.speed_minimum;
            else if(result > Settings.getInstance().maths.onPlayerShootBow.speed_maximum) result = Settings.getInstance().maths.onPlayerShootBow.speed_maximum;
        }

        return result;
    }
    public static float getBowPrecision(LowbrainPlayer p){
        float result = p.getMultipliers().getBowPrecision();

        if(Settings.getInstance().maths.onPlayerShootBow.precision_range > 0){
            result = Helper.randomFloat(result - Settings.getInstance().maths.onPlayerShootBow.precision_range,result + Settings.getInstance().maths.onPlayerShootBow.precision_range);
            if(result < 0)result = 0;
            else if(result > 1) result = 1;
        }

        return result;
    }
    public static float getAttackByWeapon(LowbrainPlayer damager, double damage){
        float max = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_maximum;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_minimum;
        float range = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.weapon_range;

        float result = (float)damage * damager.getMultipliers().getAttackByWeapon();
        if(range > 0){
            result = Helper.randomFloat(result-range,result+range);
            if(result < 0)result = 0;
        }
        return result;
    }
    public static float getAttackByProjectile(LowbrainPlayer damager, double damage){
        float max = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_maximum;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_minimum;
        float range = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.projectile_range;

        float result = (float)damage * damager.getMultipliers().getAttackByProjectile();

        if(range > 0){
            result = Helper.randomFloat(result - range,result + range);
            if(result < 0)result = 0;
        }

        return result;
    }
    public static float getAttackByMagic(LowbrainPlayer damager, double damage){
        float max = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_maximum;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_minimum;
        float range = Settings.getInstance().maths.onPlayerAttackEntity.attackEntityBy.magic_range;

        float result = (float)damage * damager.getMultipliers().getAttackByMagic();

        if(range > 0){
            result = Helper.randomFloat(result - range,result + range);
            if(result < 0)result = 0;
        }
        return result;
    }
    public static float getCriticalHitChance(LowbrainPlayer damager){
        float max = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.maximumChance;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.minimumChance;
        float range = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.chanceRange;

        float result = damager.getMultipliers().getCriticalHitChance();

        if(range > 0){
            result = Helper.randomFloat(result - range,result + range);
        }
        return result;
    }
    public static float getCriticalHitMultiplier(LowbrainPlayer damager){
        float max = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.maximumDamageMultiplier;
        float min = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.minimumDamageMultiplier;
        float range = Settings.getInstance().maths.onPlayerAttackEntity.criticalHit.damageMultiplierRange;

        float result = damager.getMultipliers().getCriticalHitMultiplier();

        if(range > 0){
            result = Helper.randomFloat(result - range,result + range);
            result = result < min ? min : result > max ? max : result;
        }
        return result;
    }

    // ON PLAYER CONSUME POTION

    public static float getConsumedPotionMultiplier(LowbrainPlayer p){

        float max = Settings.getInstance().maths.onPlayerConsumePotion.maximum;
        float min = Settings.getInstance().maths.onPlayerConsumePotion.minimum;
        float range = Settings.getInstance().maths.onPlayerConsumePotion.range;

        float result = p.getMultipliers().getConsumedPotionMultiplier();

        if(range > 0){
            result = Helper.randomFloat(result - range,result + range);
            if(result < min)result = min;
            else if(result > max) result = max;
        }

        return result;
    }

    public static float getReducingPotionEffect(LowbrainPlayer damagee){
        if(Helper.StringIsNullOrEmpty(Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.function)){
            float min = Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.minimum;
            float max = Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.maximum;
            float range = Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.range;

            float reduction = damagee.getMultipliers().getReducingPotionEffect();

            float minReduction = reduction < (min - range) ? reduction + range : min;

            return Helper.randomFloat(reduction,minReduction);
        }
        else{
            String[] st = Settings.getInstance().maths.onPlayerGetDamaged.reducingBadPotionEffect.function.split(",");
            if(st.length > 1){
                return Helper.eval(Helper.FormatStringWithValues(st,damagee));
            }
            else{
                return Helper.eval(st[0]);
            }
        }
    }
}
