package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.config.ConfigValue;
import me.thecatisbest.radiantcore.utils.ItemUtils;
import me.thecatisbest.radiantcore.utils.Utils;
import me.thecatisbest.radiantcore.utils.builder.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GrapplingHook implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        String metadataValue = ItemBuilder.getPersistentMetadata(item);

        if (metadataValue != null && metadataValue.equals(ItemUtils.Key.GRAPPLING_HOOK.getName())) {
            if (!isWorldAllowed(player.getWorld())) {
                player.sendMessage(Utils.color("&c你不能在這個世界使用抓鈎！"));
                return;
            }
            if (event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)
                    || event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)
                    || event.getState().equals(PlayerFishEvent.State.IN_GROUND)) {
                Location playerLocation = player.getLocation();
                Location eventLocation = event.getHook().getLocation();
                Vector vector = new Vector(eventLocation.getX() - playerLocation.getX(), 1, eventLocation.getZ() - playerLocation.getZ());
                player.setVelocity(vector);
            }
        }
    }

    private boolean isWorldAllowed(World world) {
        String worldName = world.getName();
        switch (ConfigValue.GRAPPLING_HOOK_WORLD_TYPE_MODE.toUpperCase()) {
            case "BLACKLIST":
                return !ConfigValue.GRAPPLING_HOOK_WORLD_TYPE.contains(worldName);
            case "WHITELIST":
                return ConfigValue.GRAPPLING_HOOK_WORLD_TYPE.contains(worldName);
            case "DISABLED":
            default:
                return true;
        }
    }
}
