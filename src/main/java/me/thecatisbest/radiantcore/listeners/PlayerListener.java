package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.config.GeneralConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerSneak(PlayerSwapHandItemsEvent event) {

        String raw_command = GeneralConfig.COMMANDS;

        if (event.getPlayer().isSneaking()) {
            event.setCancelled(GeneralConfig.SWAP);
            for (String command : raw_command.split(";")) {
                command = command.replaceAll("%player%", event.getPlayer().getDisplayName());
                event.getPlayer().performCommand(command);
            }
        }
    }
}
