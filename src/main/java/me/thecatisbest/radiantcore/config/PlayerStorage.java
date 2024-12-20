package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.listeners.BuildersWand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerStorage {

    private final RadiantCore plugin;
    private static File file;
    private static FileConfiguration config;
    private static final HashMap<UUID, Boolean> flightMode = new HashMap<>();
    private static final HashMap<UUID, Boolean> hasAcceptRules = new HashMap<>();

    public PlayerStorage(RadiantCore plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        file = new File(plugin.getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveConfig() {
        try {
            config.save(file);
            BuildersWand.saveAllInventories();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFlyTime(UUID playerId, int time) {
        config.set(playerId.toString() + ".flightTime", time);
    }

    public int getFlyTime(UUID playerId) {
        return config.getInt(playerId.toString() + ".flightTime", 0);
    }

    public static void setAcceptRules(UUID playerId, Boolean hasAccept) {
        PlayerStorage.hasAcceptRules.put(playerId, hasAccept);
        config.set(playerId.toString() + ".acceptRules", hasAccept);
    }

    public static boolean getAcceptRules(UUID playerId) {
        return config.getBoolean(playerId.toString() + ".acceptRules", false);
    }

    public static void setFlightMode(UUID playerId, boolean flightMode) {
        PlayerStorage.flightMode.put(playerId, flightMode);
        config.set(playerId.toString() + ".flightMode", flightMode);
    }

    public static boolean getFlightMode(UUID playerId) {
        return config.getBoolean(playerId.toString() + ".flightMode", false);
    }

    public static void saveWandInventory(UUID playerId, Inventory inventory) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                items.add(item.clone());
            }
        }
        config.set(playerId.toString() + ".wandInventory", items);
    }

    public static void loadWandInventory(UUID playerId, Inventory inventory) {
        List<?> items = config.getList(playerId.toString() + ".wandInventory");
        if (items != null) {
            ItemStack[] contents = new ItemStack[27];  // 创建一个有27个槽位的新数组
            for (int i = 0; i < items.size(); i++) {
                contents[i] = (ItemStack) items.get(i);
            }
            inventory.setContents(contents);
        }
    }



    public static void setPlayerName(UUID playerId, String playerName) {
        config.set(playerId.toString() + ".playerName", playerName);
    }

    public String getPlayerName(UUID playerId) {
        return config.getString(playerId.toString() + ".playerName", "");
    }

    public static void updatePlayerData(Player player) {
        UUID playerId = player.getUniqueId();
        setPlayerName(playerId, player.getName());
        if (!getAcceptRules(playerId)) {
            setAcceptRules(playerId, getAcceptRules(playerId));
        } else {
            PlayerStorage.hasAcceptRules.put(playerId, getAcceptRules(playerId));
        }
        if (!getFlightMode(playerId)) {
            setFlightMode(playerId, getFlightMode(playerId));
        } else {
            PlayerStorage.flightMode.put(playerId, getFlightMode(playerId));
        }
    }

    public String getPlayerNameSafe(UUID playerId) {
        String storedName = getPlayerName(playerId);
        if (storedName.isEmpty()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                storedName = player.getName();
                setPlayerName(playerId, storedName);
                saveConfig();
            }
        }
        return storedName;
    }

    public void startAutoSaveTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin,
                PlayerStorage::saveConfig, 0L, 6000L); // 1200 ticks = 60 seconds
    }
}

