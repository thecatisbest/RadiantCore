package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utilis.ItemUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class GrapplingHook implements Listener {

    private final ItemUtils itemUtils = RadiantCore.getInstance().getItemUtils();
    private final Map<Player, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // process grappling hook ability
        if (event.getState() == PlayerFishEvent.State.REEL_IN || event.getState() == PlayerFishEvent.State.CAUGHT_FISH || event.getState() == PlayerFishEvent.State.IN_GROUND) {
            if (item.isSimilar(this.itemUtils.grappling_hook.toItemStack())) {
                if (this.cooldowns.getOrDefault(player, 0L) + 100L > System.currentTimeMillis()) return;
                this.cooldowns.put(player, System.currentTimeMillis());

                this.itemUtils.grappling_hook.repairItem();
                Location playerLocation = player.getLocation();
                Location eventLocation = event.getHook().getLocation();
                Vector vector = new Vector(eventLocation.getX() - playerLocation.getX(), 1, eventLocation.getZ() - playerLocation.getZ());
                player.setVelocity(vector);
            }
        }
    }
}
