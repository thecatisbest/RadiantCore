package me.thecatisbest.radiantcore.listeners;

import com.Zrips.CMI.events.CMIAfkEnterEvent;
import com.Zrips.CMI.events.CMIAfkLeaveEvent;
import com.cryptomorin.xseries.XSound;
import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.config.ConfigValue;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.hooks.CMIUtil;
import me.thecatisbest.radiantcore.utils.ItemUtils;
import me.thecatisbest.radiantcore.utils.Utils;
import me.thecatisbest.radiantcore.utils.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class MushroomSoup implements Listener {

    public static final HashMap<UUID, Integer> flyTimes = new HashMap<>();
    public static final HashMap<UUID, Integer> tasks = new HashMap<>();
    public static final HashMap<UUID, Boolean> isAFK = new HashMap<>();
    private final int maxFlyTimes = 3 * 60 * 60; // 三小时的秒数
    private final int magicDuration = ConfigValue.MAGIC_MUSHROOM_SOUP_DURATION;
    private final int superMagicDuration = ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_DURATION;

    @EventHandler
    public void onPlayerUseSoup(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack item = event.getItem();
        String metadataValue = ItemBuilder.getPersistentMetadata(item);

        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(playerId); // 从文件中加载

        if (item != null && metadataValue != null && (metadataValue.equals(ItemUtils.Key.MAGIC_MUSHROOM_SOUP.getName()) ||
                metadataValue.equals(ItemUtils.Key.SUPER_MAGIC_MUSHROOM_SOUP.getName()))) {

            if (!isWorldAllowed(player.getWorld())) {
                player.sendMessage(Utils.color("&c你不能在這個世界使用蘑菇湯！"));
                event.setCancelled(true);
                return;
            }

            if (isAFK.containsKey(playerId)) {
                player.sendMessage(Utils.color("&c你不能在 AFK 狀態下使用蘑菇湯！"));
                event.setCancelled(true);
                return;
            }

            if (flyTimes.getOrDefault(playerId, savedTime) > maxFlyTimes) {
                player.sendMessage(Utils.color("&c你無法再延長飛行時間，因為已經達到最大效果時間 (三小時)！"));
                event.setCancelled(true);
                return;
            }
        }

        if (item != null && metadataValue != null && metadataValue.equals(ItemUtils.Key.MAGIC_MUSHROOM_SOUP.getName()) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            handleSoupUse(player, playerId, item, magicDuration);
            event.setCancelled(true);
        }

        if (item != null && metadataValue != null && metadataValue.equals(ItemUtils.Key.SUPER_MAGIC_MUSHROOM_SOUP.getName()) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            handleSoupUse(player, playerId, item, superMagicDuration);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (flyTimes.containsKey(playerId)) {
            // 防止萬一玩家快速斷開連接並重新連接 (雙重保障)
            if (tasks.containsKey(playerId)) {
                Bukkit.getScheduler().cancelTask(tasks.get(playerId));
                tasks.remove(playerId);
            }
            flyTimes.remove(playerId);
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
            if (flyTimes.getOrDefault(playerId, savedTime) > maxFlyTimes) {
                player.sendMessage(Utils.color("&c檢測到你的飛行時間已超過三小時，系統將爲你强制設定至三小時！"));
                RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, maxFlyTimes);
                flyTimes.put(playerId, maxFlyTimes);
            }
            if (!PlayerStorage.getFlightMode(playerId)) {
                return;
            }
            if (isWorldAllowed(player.getWorld())) {
                startFlying(player);
            }
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Bukkit.getScheduler().runTaskLater(RadiantCore.getInstance(), () -> {
            if (flyTimes.containsKey(playerId)) {
                if (isWorldAllowed(player.getWorld())) {
                    if (tasks.containsKey(playerId)) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    } else {
                        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(playerId); // 从文件中加载
                        if (savedTime > 0) {
                            flyTimes.put(playerId, savedTime);
                            startFlying(player);
                        }
                    }
                } else {
                    player.setAllowFlight(false);
                    player.setFlying(false);

                    int timeLeft = flyTimes.getOrDefault(playerId, 0);
                    RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, timeLeft); // 保存到文件

                    if (tasks.containsKey(playerId)) {
                        Bukkit.getScheduler().cancelTask(tasks.get(playerId));
                        tasks.remove(playerId);
                    }
                }
            }
        }, 20L);
    }

    @EventHandler
    public void onPlayerAFK(CMIAfkEnterEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        boolean isCMILoaded = CMIUtil.initCMI();
        if (!isCMILoaded) return;
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

        boolean isCMILoaded = CMIUtil.initCMI();
        if (!isCMILoaded) return;

        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(playerId); // 从文件中加载
        if (savedTime > 0) {
            flyTimes.put(playerId, savedTime);
            if (isWorldAllowed(player.getWorld())) {
                startFlying(player);
            }
        }
        isAFK.remove(playerId);
    }

    public static void startFlying(Player player) {
        UUID playerId = player.getUniqueId();
        player.setAllowFlight(true);
        player.setFlying(true);

        int timeLeft = flyTimes.getOrDefault(playerId, 0);

        int taskId = new BukkitRunnable() {
            int seconds = timeLeft;

            @Override
            public void run() {
                if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
                    return;
                }

                if (!player.isOnline()) {
                    this.cancel();
                    tasks.remove(playerId);
                    return;
                }

                if (!player.isFlying() && PlayerStorage.getFlightMode(playerId)) {
                    // If player is not flying and fly status is true, skip the countdown
                    return;
                }
                if (!PlayerStorage.getFlightMode(playerId)) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    this.cancel();
                    tasks.remove(playerId);
                    return;
                }

                if (seconds <= 0) {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(Utils.color("&c你的飛行效果已結束！"));
                    this.cancel();
                    tasks.remove(playerId);
                    flyTimes.remove(playerId);
                    RadiantCore.getInstance().getPlayerStorage().setFlyTime(playerId, 0); // 移除文件中的记录
                    return;
                } else {
                    if (seconds == 60 || seconds == 30 || seconds == 10 || seconds <= 5) {
                        player.sendMessage(Utils.color("&c你的飛行效果將在 &e" + seconds + " &c秒後結束！"));
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
        PlayerStorage.setFlightMode(playerId, true);

        if (isFirstUse) {
            player.sendMessage(Utils.color("&e你現在可以飛行 &6" + newTime + " &e秒"));
        } else {
            player.sendMessage(Utils.color("&e你的飛行時間已延長至 &6" + newTime + " &e秒"));
        }

        // 消耗掉蘑菇汤
        item.setAmount(item.getAmount() - 1);
    }

    public static boolean isWorldAllowed(World world) {
        String worldName = world.getName();
        switch (ConfigValue.MUSHROOM_SOUP_WORLD_TYPE_MODE.toUpperCase()) {
            case "BLACKLIST":
                return !ConfigValue.MUSHROOM_SOUP_WORLD_TYPE.contains(worldName);
            case "WHITELIST":
                return ConfigValue.MUSHROOM_SOUP_WORLD_TYPE.contains(worldName);
            case "DISABLED":
            default:
                return true;
        }
    }
}
