package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.RadiantCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerStorage {

    private final RadiantCore plugin;
    private File flyTimesFile;
    private FileConfiguration flyTimesConfig;

    public PlayerStorage(RadiantCore plugin) {
        this.plugin = plugin;
        loadFlyTimes();
    }

    public void loadFlyTimes() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        flyTimesFile = new File(plugin.getDataFolder(), "data.yml");
        if (!flyTimesFile.exists()) {
            try {
                flyTimesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        flyTimesConfig = YamlConfiguration.loadConfiguration(flyTimesFile);
    }

    public void saveFlyTimes() {
        try {
            flyTimesConfig.save(flyTimesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFlyTime(UUID playerId, int time) {
        flyTimesConfig.set(playerId.toString() + ".flightTime", time);
    }

    public int getFlyTime(UUID playerId) {
        return flyTimesConfig.getInt(playerId.toString() + ".flightTime", 0);
    }

    public void startAutoSaveTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveFlyTimes, 0L, 6000L); // 1200 ticks = 60 seconds
    }
}

