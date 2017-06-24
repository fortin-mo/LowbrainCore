package lowbrain.core.commun;

import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.LowbrainPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Moofy on 19/07/2016.
 */


public class Helper {

    /**
     * evaluate string
     * @param str string
     * @return return value from evaluation
     */
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

    /**
     * reformat string with values from player attributes
     * @param st string
     * @param p LowbrainPlayer
     * @return
     */
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

    /**
     * parse string to double with default value
     * @param s string
     * @param d default value
     * @return
     */
    public static double doubleTryParse(String s, Double d){
        try {
            return Double.parseDouble(s);
        }catch (Exception e){
            return d;
        }
    }

    /**
     * parse string to float with default value
     * @param s string
     * @param d default value
     * @return
     */
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
        return Settings.getInstance().getMaxStats() <= 0 ? 100 : Settings.getInstance().getMaxStats();
    }

    /**
     * compute slope
     * @param max maximum value
     * @param min minimum value
     * @return slope using maxStats and default function type
     */
    public static float Slope(float max, float min){
        return Slope(max,min,getMaxStats());
    }

    /**
     * compute slope
     * @param max maximum value
     * @param min minimum value
     * @return slope using maxStats and default function type
     */
    public static float Slope(float max, float min, FunctionType functionType){
        return Slope(max,min,getMaxStats(), functionType);
    }

    /**
     * compute slope
     * @param max maximum value
     * @param min minimum value
     * @param y y value
     * @return slope using default function type
     */
    public static float Slope(float max, float min, float y){
        return Slope(max,min,y, Settings.getInstance().getParameters().getFunctionType());
    }

    /**
     *
     * @param max maximum value
     * @param min minimum value
     * @param y y value
     * @param functionType function type to use
     * @return slope
     */
    public static float Slope(float max, float min, float y, FunctionType functionType){
        float slope = 0;
        switch (functionType){
            case LINEAR:
                slope = (max - min)/y;
                break;
            default:
            case CUBIC:
                slope = (max - min)/(float)Math.pow(y,2);
                break;
            case SQUARE:
                slope = (max - min)/(float)Math.pow(y,0.5);
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
    public static float valueFromFunction(float max, float min, float x){
        return valueFromFunction(max, min, x, null);
    }

    public static float valueFromFunction(float max, float min, float x, FunctionType functionType){
        float result = 0;

        FunctionType usedFT = functionType == null ? Settings.getInstance().getParameters().getFunctionType() : functionType;

        switch (usedFT){
            case LINEAR:
                result = Slope(max,min,usedFT) * x + min;
                break;
            default:
            case CUBIC:
                result = Slope(max,min,usedFT) * (float)Math.pow(x,2) + min;
                break;
            case SQUARE:
                result = Slope(max,min,usedFT) * (float)Math.pow(x,0.5) + min;
                break;
        }
        return result;
    }


    public static float valueFromFunction(float max, float min, HashMap<String,Float> variables, LowbrainPlayer p){
        return valueFromFunction(max,min,getXValue(variables,p));
    }

    public static float valueFromFunction(float max, float min, HashMap<String,Float> variables, LowbrainPlayer p, FunctionType functionType){
        return valueFromFunction(max,min,getXValue(variables,p), functionType);
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

                if(max < 0){
                    lst.add(p2);
                    continue;
                }

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

    /**
     * return the distance between two players
     * @param p1 player one
     * @param p2 player two
     * @return distance between the two players
     */
    public static double getDistanceBetweenTwoPlayers(Player p1, Player p2){
        double dist = 0;

        if(!p1.getWorld().equals(p2.getWorld())){
           return -1;
        }

        double x = p1.getLocation().getX() - p2.getLocation().getX();
        double y = p1.getLocation().getY() - p2.getLocation().getY();
        double z = p1.getLocation().getZ() - p2.getLocation().getZ();

        dist = Math.pow(x*x + y*y + z*z, 0.5);

        return dist;
    }

    public static float getRandomBetween(float min, float max, float value, float range, boolean outBound) {
        range = +range;
        if (range == 0) {
            return value;
        }

        if (min > value) min = value;
        if (max < value) max = value;

        float random = Helper.randomFloat(value - range, value + range);

        if (outBound) {
            return random;
        }

        if (random > max) {
            return max;
        }

        if (random < min) {
            return min;
        }

        return random;
    }

    public static float getRandomBetween(float min, float max, float value, float range) {
        return Helper.getRandomBetween(min, max, value, range, false);
    }

    public static float getRandomBetween(float value, float range) {
        return getRandomBetween(value, value, value, range, true);
    }
}
