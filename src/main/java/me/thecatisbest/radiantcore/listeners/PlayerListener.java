package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.config.ConfigValue;
import me.thecatisbest.radiantcore.config.MainConfig;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.menus.RulesGUI;
import me.thecatisbest.radiantcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerStorage.updatePlayerData(event.getPlayer());
        if (!PlayerStorage.getAcceptRules(event.getPlayer().getUniqueId())) {
            new RulesGUI(event.getPlayer(), RadiantCore.getInstance()).openGUI();
        }
    }

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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.hasMetadata("RulesGUI")) {
            event.setCancelled(true);
            if (event.getSlot() == 31) {
                PlayerStorage.setAcceptRules(player.getUniqueId(), true);
                player.removeMetadata("RulesGUI", RadiantCore.getInstance());
                player.sendMessage(Utils.color("&a你完成了簽署。&f祝福你在暉長伺服器游玩愉快。"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        RulesGUI rulesGUI = new RulesGUI(player, RadiantCore.getInstance());
        if (!PlayerStorage.getAcceptRules(player.getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(RadiantCore.getInstance(), rulesGUI::openGUI, 5L);
        }
    }
}
