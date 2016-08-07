package lowbrain.mcrpg.commun;

import lowbrain.mcrpg.rpg.RPGPlayer;

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

    public static String FormatStringWithValues(String[] st, RPGPlayer p){

        if(st.length > 1){
            for (int i = 1; i < st.length; i++) {
                switch (st[i].trim().toLowerCase()){
                    case "health":
                        st[i] = p != null ? Integer.toString(p.getHealth()) : "1";
                        break;
                    case "strength":
                        st[i] = p != null ? Integer.toString(p.getStrength()) : "1";
                        break;
                    case "intelligence":
                        st[i] = p != null ? Integer.toString(p.getIntelligence()) : "1";
                        break;
                    case "dexterity":
                        st[i] = p != null ? Integer.toString(p.getDexterity()) : "1";
                        break;
                    case "magic_resistance":
                        st[i] = p != null ? Integer.toString(p.getMagicResistance()) : "1";
                        break;
                    case "defence":
                        st[i] = p != null ? Integer.toString(p.getDeaths()) : "1";
                        break;
                    case "agility":
                        st[i] = p != null ? Integer.toString(p.getAgility()) : "1";
                        break;
                    default:
                        st[i] = "1";
                        break;
                }
            }
        }
        else {
            return st[0];
        }

        MessageFormat fmt = new MessageFormat(st[0]);
        return fmt.format(Arrays.copyOfRange(st,1, st.length));
    }

    public static boolean StringIsNullOrEmpty(String s){
        return s == null || s.trim().length() == 0;
    }

    /**
     * generate a random float [min,max]
     * @param max max
     * @param min min
     * @return
     */
    public static float randomFloat(float max, float min){
        float range = (max - min);
        return ((float)Math.random() * range) + min;
    }

    /**
     * generate a int float [min,max]
     * @param max max
     * @param min min
     * @return
     */
    public static int randomInt(int max, int min){
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
}
