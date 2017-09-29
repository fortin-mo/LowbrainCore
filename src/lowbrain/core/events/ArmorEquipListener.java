package lowbrain.core.events;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.LowbrainPlayer;
import lowbrain.library.fn;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Moofy on 25/08/2016.
 */
public class ArmorEquipListener implements Listener {

    public static LowbrainCore plugin;

    public ArmorEquipListener(LowbrainCore instance) {
        plugin = instance;
    }

    /***
     * called when player equip or unequip a armor (foot, chest, shield, helmet, legs)
     * @param e
     */
    @EventHandler
    public void onPlayerEquipArmor(ArmorEquipEvent e){
        LowbrainPlayer rp = LowbrainCore.getInstance().getPlayerHandler().getList().get(e.getPlayer().getUniqueId());
        if(rp == null)
            return;

        if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR){

            String requirements =  rp.canEquipItemString(e.getNewArmorPiece());
            if(!fn.StringIsNullOrEmpty(requirements)) {
                rp.sendMessage(LowbrainCore.getInstance().getConfigHandler().localization().format("cannot_equit_armor_or_item", requirements));
                e.setCancelled(true);
            }
        }
    }
}
