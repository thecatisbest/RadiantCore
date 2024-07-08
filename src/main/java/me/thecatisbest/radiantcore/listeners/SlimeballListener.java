package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.utilis.Utilis;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class SlimeballListener implements Listener {

    private final Map<Player, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void slimeballRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        
        if (player.getInventory().getItemInMainHand().getType() != Material.SLIME_BALL)
            return;
        if (player.hasPermission("radiant.use.slimeball") && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (this.cooldowns.getOrDefault(player, 0L) + 100L > System.currentTimeMillis())
                return;
            this.cooldowns.put(player, System.currentTimeMillis());
            if (player.getWorld().getChunkAt(player.getLocation()).isSlimeChunk()) {
                player.sendMessage(Utilis.color("&a你現在站的地方是史萊姆區塊内"));
            } else {
                player.sendMessage(Utilis.color("&c你現在站的地方不是史萊姆區塊内"));
            }
        }
    }
}
