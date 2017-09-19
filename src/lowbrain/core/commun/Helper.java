package lowbrain.core.commun;

import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.LowbrainPlayer;
import lowbrain.library.FunctionType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Moofy on 19/07/2016.
 */


public class Helper {
    /**
     * reformat string with values from player attributes
     * @param st string
     * @param p LowbrainPlayer
     * @return formatted string
     */
    public static String FormatStringWithValues(String[] st, LowbrainPlayer p){
        if(st.length > 1 && p != null){
            for (int i = 1; i < st.length; i++)
                st[i] = Integer.toString(p.getAttribute(st[i].trim().toLowerCase(),1));

        }
        else {
            return st[0];
        }

        MessageFormat fmt = new MessageFormat(st[0]);
        return fmt.format(Arrays.copyOfRange(st,1, st.length));
    }

    /***
     * gets the X value ( y = ax + b ) depending on attributes influence and player attributes
     * @param variables list of variables and their contribution
     * @param p LowbrainPlayer
     * @return x value from attributes
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
     * @return max stats
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
     * @param functionType function type
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
     * @return value
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
}
