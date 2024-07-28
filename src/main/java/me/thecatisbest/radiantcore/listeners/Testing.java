/*
package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.utilis.ItemUtils;
import me.thecatisbest.radiantcore.utilis.builder.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Testing implements Listener {

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            String metadataValue = ItemBuilder.getPersistentMetadata(item);

            if (item != null && metadataValue != null) {
                player.sendMessage("§aYou are holding an item with metadata: " + metadataValue);
            }
        }
    }

    @EventHandler
    public void onPlayerCustomItem(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            String metadataValue = ItemBuilder.getPersistentMetadata(item);

            if (item != null && metadataValue != null && metadataValue.equals(ItemUtils.Key.TESTING.getName())) {
                player.sendMessage("§aYou are holding a custom item with the correct metadata!");
            }
        }
    }
}
 */
