package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utilis.ItemUtils;
import me.thecatisbest.radiantcore.utilis.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class GrapplingHook implements Listener {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        String metadataValue = ItemBuilder.getPersistentMetadata(item);

        if (metadataValue != null && metadataValue.equals(ItemUtils.Key.GRAPPLING_HOOK.getName())) {
            long currentTime = System.currentTimeMillis();
            long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);

            if (currentTime - lastUsed < 500) {
                return;
            }
            if (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)
                    || event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)
                    || event.getState().equals(PlayerFishEvent.State.IN_GROUND)) {
                Location playerLocation = player.getLocation();
                Location eventLocation = event.getHook().getLocation();
                Vector vector = new Vector(eventLocation.getX() - playerLocation.getX(), 1, eventLocation.getZ() - playerLocation.getZ());
                player.setVelocity(vector);
                cooldowns.put(player.getUniqueId(), currentTime); // Update last used time
                // Schedule a task to remove the player from cooldowns map after 0.4 seconds
                Bukkit.getScheduler().runTaskLater(RadiantCore.getInstance(), () -> cooldowns.remove(player.getUniqueId()), 8); // 10 ticks = 0.5 seconds
            }
        }
    }
}
