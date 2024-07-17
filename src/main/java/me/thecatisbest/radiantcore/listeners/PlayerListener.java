package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.config.ConfigValue;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerSneak(PlayerSwapHandItemsEvent event) {

        String raw_command = ConfigValue.COMMANDS;

        if (event.getPlayer().isSneaking()) {
            event.setCancelled(ConfigValue.SWAP);
            for (String command : raw_command.split(";")) {
                command = command.replaceAll("%player%", event.getPlayer().getDisplayName());
                event.getPlayer().performCommand(command);
            }
        }
    }
}
