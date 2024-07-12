package me.thecatisbest.radiantcore.listeners;

import com.Zrips.CMI.events.CMIAfkEnterEvent;
import com.Zrips.CMI.events.CMIAfkLeaveEvent;
import com.cryptomorin.xseries.XSound;
import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.config.GeneralConfig;
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
    private final int magicDuration = GeneralConfig.MAGIC_MUSHROOM_SOUP_DURATION;
    private final int super_magicDuration = GeneralConfig.SUPER_MAGIC_MUSHROOM_SOUP_DURATION;
    private final ItemUtils itemUtils = RadiantCore.getInstance().getItemUtils();

    @EventHandler
    public void onPlayerUseSoup(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack item = event.getItem();

        if (item != null && item.isSimilar(this.itemUtils.magic_mushroom_soup.toItemStack()) &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            handleSoupUse(player, playerId, item, magicDuration);
        }

        if (item != null && item.isSimilar(this.itemUtils.super_magic_mushroom_soup.toItemStack()) &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            handleSoupUse(player, playerId, item, super_magicDuration);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (flyTimes.containsKey(playerId)) {
            int timeLeft = flyTimes.getOrDefault(playerId, magicDuration);
            RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, timeLeft); // 保存到文件
            flyTimes.put(playerId, timeLeft);
            if (tasks.containsKey(playerId)) {
                Bukkit.getScheduler().cancelTask(tasks.get(playerId));
                tasks.remove(playerId);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(playerId); // 從文件中加載
        if (savedTime > 0) {
            flyTimes.put(playerId, savedTime);
            startFlying(player);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (tasks.containsKey(playerId)) {
            Bukkit.getScheduler().cancelTask(tasks.get(playerId));
            startFlying(player);
        }
    }

    @EventHandler
    public void onPlayerAFK(CMIAfkEnterEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (tasks.containsKey(playerId)) {
            Bukkit.getScheduler().cancelTask(tasks.get(playerId));
        }
    }

    @EventHandler
    public void onPlayerLeaveAFK(CMIAfkLeaveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(playerId); // 從文件中加載
        if (savedTime > 0) {
            flyTimes.put(playerId, savedTime);
            startFlying(player);
        }
    }

    private void startFlying(Player player) {
        UUID playerId = player.getUniqueId();
        player.setAllowFlight(true);
        player.setFlying(true);

        int timeLeft = flyTimes.getOrDefault(playerId, magicDuration);
        flyTimes.remove(playerId);

        int taskId = new BukkitRunnable() {
            int seconds = timeLeft;

            @Override
            public void run() {
                if (seconds == 0) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(Utilis.color("&c你的飛行效果已結束！"));
                    this.cancel();
                    tasks.remove(playerId);
                    flyTimes.remove(playerId);
                    RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, 0); // 移除文件中的記錄
                    return;
                } else {
                    if (seconds == 60 || seconds == 30 || seconds == 10 || seconds <= 5) {
                        player.sendMessage(Utilis.color("&c你的飛行效果將在 &e" + seconds + " &c秒後結束！"));
                    }
                    if (seconds >= 1) {
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
        if (flyTimes.containsKey(playerId)) {
            int timeLeft = flyTimes.getOrDefault(playerId, duration) + duration;

            flyTimes.put(playerId, timeLeft);
            RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, timeLeft); // 保存到文件
            XSound.ENTITY_GENERIC_DRINK.play(player);
            player.sendMessage(Utilis.color("&e你的飛行時間已延長至 &6" + timeLeft + " &e秒"));
            if (tasks.containsKey(playerId)) {
                Bukkit.getScheduler().cancelTask(tasks.get(playerId));
            }
            startFlying(player);
        } else {
            startFlying(player);

            int timeLeft = flyTimes.getOrDefault(playerId, duration);
            RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, timeLeft); // 保存到文件
            XSound.ENTITY_GENERIC_DRINK.play(player);
            player.sendMessage(Utilis.color("&e你現在可以飛行 &6" + timeLeft + " &e秒"));
        }

        // 消耗掉头颅
        item.setAmount(item.getAmount() - 1);
    }
}


