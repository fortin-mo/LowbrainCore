package lowbrain.mcrpg.Powa;

/**
 * Created by moo on 2016-07-21.
 */
public class PowaFactory {
    public static Powa createPowa(String n){
        switch (n){
            case"healing":
                return new healing(n);

            case"fire_resistance":
                return new fire_resistance(n);

            case"resistance":
                return new resistance(n);

            case"water_breathing":
                return new water_breathing(n);
                
            case"invisibility":
                return new invisibility(n);
                
            case"regeneration":
                return new regeneration(n);
                
            case"jump_boost":
                return new jump_boost(n);
                
            case"strength":
                return new strength(n);
                
            case"haste":
                return new haste(n);
                
            case"speed":
                return new speed(n);
                
            case"night_vision":
                return new night_vision(n);
                
            case"health_boost":
                return new health_boost(n);
                
            case"absorption":
                return new absorption(n);
                
            case"saturation":
                return new saturation(n);
                
            default:
                return null;
        }
    }
}
