package lowbrain.mcrpg.Powa;

import lowbrain.mcrpg.commun.RPGPlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by moo on 2016-07-21.
 */
public class strength extends Powa {

    public strength(String name) {
        super(name);
    }

    @Override
    public boolean Cast(RPGPlayer from, RPGPlayer to) {
        try{
            PotionEffect p =  new PotionEffect(PotionEffectType.INCREASE_DAMAGE,10,10,true,true);
            p.apply(to.getPlayer());
            return true;
        }catch (Exception e){

        }
        return false;
    }
}
