package lowbrain.mcrpg.Powa;

import lowbrain.mcrpg.Powa.Powa;
import lowbrain.mcrpg.commun.RPGPlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by moo on 2016-07-21.
 */
public class healing extends Powa {

    public healing(String name) {
        super(name);
    }

    @Override
    public boolean Cast(RPGPlayer from, RPGPlayer to) {
        try{
            PotionEffect p =  new PotionEffect(PotionEffectType.HEAL,this.getCastDuration(from),this.getCastAmplifier(from),true,true);
            p.apply(to.getPlayer());
            return true;
        }catch (Exception e){

        }
        return false;
    }
}
