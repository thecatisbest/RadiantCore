package me.thecatisbest.radiantcore.listeners;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.api.ResidencePlayerInterface;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import me.thecatisbest.radiantcore.config.ConfigValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class InvisibleItemFrame implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onItemFrameInteract(PlayerInteractEntityEvent event){
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        if (!resCheck(player, event.getRightClicked().getLocation())) return;

        if (!ConfigValue.SHIFT_RIGHT_INVIS_FRAMES.equals(true)) return;

        Entity entity = event.getRightClicked();
        if (!(entity instanceof ItemFrame itemFrame)) return;
        if (itemFrame.getItem().getType().equals(Material.AIR)) return;
        if (!player.hasPermission("radiant.invisibleframes")) return;

        event.setCancelled(true);
        itemFrame.setVisible(!itemFrame.isVisible());
        itemFrame.setFixed(!itemFrame.isFixed());
    }

    public boolean resCheck(Player player, Location location) {
        ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(location);

        if (residence == null) {
            return true;
        }

        if (residence.getOwnerUUID().equals(player.getUniqueId()) || player.isOp()) {
            return true;
        }

        if (!residence.getPermissions().playerHas(player, Flags.valueOf("place") , true)) {
            return false;
        }

        return true;
    }
}
