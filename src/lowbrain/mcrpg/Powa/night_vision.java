package lowbrain.mcrpg.Powa;

import lowbrain.mcrpg.commun.RPGPlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by moo on 2016-07-21.
 */
public class night_vision extends Powa {

    public night_vision(String name) {
        super(name);
    }

    @Override
    public boolean Cast(RPGPlayer from, RPGPlayer to) {
        try{
            PotionEffect p =  new PotionEffect(PotionEffectType.NIGHT_VISION,10,10,true,true);
            p.apply(to.getPlayer());
            return true;
        }catch (Exception e){

        }
        return false;
    }
}
