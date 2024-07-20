package me.thecatisbest.radiantcore.listeners;

import com.Zrips.CMI.events.CMIAfkEnterEvent;
import com.Zrips.CMI.events.CMIAfkLeaveEvent;
import com.cryptomorin.xseries.XSound;
import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.config.ConfigValue;
import me.thecatisbest.radiantcore.utilis.ItemUtils;
import me.thecatisbest.radiantcore.utilis.Utilis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class MushroomSoup implements Listener {

    private final HashMap<UUID, Integer> flyTimes = new HashMap<>();
    private final HashMap<UUID, Integer> tasks = new HashMap<>();
    private final HashMap<UUID, Boolean> isAFK = new HashMap<>();
    private final int magicDuration = ConfigValue.MAGIC_MUSHROOM_SOUP_DURATION;
    private final int superMagicDuration = ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_DURATION;
    private final ItemUtils itemUtils = RadiantCore.getInstance().getItemUtils();

    @EventHandler
    public void onPlayerUseSoup(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack item = event.getItem();

        int maxFlyTimes = 24 * 60 * 60; // 一天的秒数
        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(playerId); // 从文件中加载
        
        if (item != null && (item.isSimilar(itemUtils.magic_mushroom_soup.toItemStack()) ||
                item.isSimilar(itemUtils.super_magic_mushroom_soup.toItemStack()))) {
            if ((isAFK.containsKey(playerId))){
                player.sendMessage(Utilis.color("&c你不能在 AFK 狀態下使用蘑菇湯！"));
                event.setCancelled(true);
                return;
            }
        }

        if (flyTimes.getOrDefault(playerId, savedTime) > maxFlyTimes) {
            player.sendMessage(Utilis.color("&c你無法再延長飛行時間，因為已經達到最大效果時間 (一天)！"));
            event.setCancelled(true);
            return;
        }

            if (item != null && item.isSimilar(this.itemUtils.magic_mushroom_soup.toItemStack()) &&
                    (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                handleSoupUse(player, playerId, item, magicDuration);
                event.setCancelled(true);
        }

        if (item != null && item.isSimilar(this.itemUtils.super_magic_mushroom_soup.toItemStack()) &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            handleSoupUse(player, playerId, item, superMagicDuration);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (flyTimes.containsKey(playerId)) {
            int timeLeft = flyTimes.getOrDefault(playerId, 0);
            RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, timeLeft); // 保存到文件
            if (tasks.containsKey(playerId)) {
                Bukkit.getScheduler().cancelTask(tasks.get(playerId));
                tasks.remove(playerId);
            }
        }
        isAFK.remove(playerId);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(playerId); // 从文件中加载
        if (savedTime > 0) {
            flyTimes.put(playerId, savedTime);
            startFlying(player);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (flyTimes.containsKey(playerId)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    @EventHandler
    public void onPlayerAFK(CMIAfkEnterEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (tasks.containsKey(playerId)) {
            Bukkit.getScheduler().cancelTask(tasks.get(playerId));
            tasks.remove(playerId);
        }
        isAFK.put(playerId, true);
    }

    @EventHandler
    public void onPlayerLeaveAFK(CMIAfkLeaveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(playerId); // 从文件中加载
        if (savedTime > 0) {
            flyTimes.put(playerId, savedTime);
            startFlying(player);
        }
        isAFK.remove(playerId);
    }

    private void startFlying(Player player) {
        UUID playerId = player.getUniqueId();
        player.setAllowFlight(true);
        player.setFlying(true);

        int timeLeft = flyTimes.getOrDefault(playerId, 0);

        int taskId = new BukkitRunnable() {
            int seconds = timeLeft;

            @Override
            public void run() {
                if (seconds <= 0) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(Utilis.color("&c你的飛行效果已結束！"));
                    this.cancel();
                    tasks.remove(playerId);
                    flyTimes.remove(playerId);
                    RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, 0); // 移除文件中的记录
                    return;
                } else {
                    if (seconds == 60 || seconds == 30 || seconds == 10 || seconds <= 5) {
                        player.sendMessage(Utilis.color("&c你的飛行效果將在 &e" + seconds + " &c秒後結束！"));
                    }
                    if (seconds > 0) {
                        flyTimes.put(playerId, seconds);
                        RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, seconds); // 保存到文件
                    }
                }
                seconds--;
            }
        }.runTaskTimer(RadiantCore.getInstance(), 0L, 20L).getTaskId();

        tasks.put(playerId, taskId);
    }

    private void handleSoupUse(Player player, UUID playerId, ItemStack item, int duration) {
        int currentTime = flyTimes.getOrDefault(playerId, 0);
        int newTime = currentTime + duration;

        boolean isFirstUse = currentTime == 0;

        flyTimes.put(playerId, newTime);
        RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, newTime); // 保存到文件
        XSound.ENTITY_GENERIC_DRINK.play(player);

        if (tasks.containsKey(playerId)) {
            Bukkit.getScheduler().cancelTask(tasks.get(playerId));
            tasks.remove(playerId);
        }
        startFlying(player);

        if (isFirstUse) {
            player.sendMessage(Utilis.color("&e你現在可以飛行 &6" + newTime + " &e秒"));
        } else {
            player.sendMessage(Utilis.color("&e你的飛行時間已延長至 &6" + newTime + " &e秒"));
        }

        // 消耗掉蘑菇汤
        item.setAmount(item.getAmount() - 1);
    }
}
