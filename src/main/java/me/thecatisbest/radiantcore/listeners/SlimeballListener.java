package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SlimeballListener implements Listener {

    @EventHandler
    public void slimeballRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        
        if (player.getInventory().getItemInMainHand().getType() != Material.SLIME_BALL)
            return;
        if (player.hasPermission("radiant.use.slimeball") && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getWorld().getChunkAt(player.getLocation()).isSlimeChunk()) {
                player.sendMessage(Utils.color("&a你現在站的地方是史萊姆區塊内"));
            } else {
                player.sendMessage(Utils.color("&c你現在站的地方不是史萊姆區塊内"));
            }
        }
    }
}
