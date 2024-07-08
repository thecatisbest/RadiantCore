package me.thecatisbest.radiantcore;

import lombok.Getter;
import me.thecatisbest.radiantcore.commands.RadiantCommand;
import me.thecatisbest.radiantcore.config.GeneralConfig;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.listeners.MushroomSoup;
import me.thecatisbest.radiantcore.listeners.PlayerListener;
import me.thecatisbest.radiantcore.listeners.SlimeMap;
import me.thecatisbest.radiantcore.listeners.SlimeballListener;
import me.thecatisbest.radiantcore.utilis.ItemUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class RadiantCore extends JavaPlugin {

    // Instance
    @Getter private static RadiantCore instance;
    @Getter private PlayerStorage playerStorage;
    @Getter private ItemUtils itemUtils;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        playerStorage = new PlayerStorage(this);
        itemUtils = new ItemUtils();
        playerStorage.startAutoSaveTask();
        reloadConfig();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new MushroomSoup(), this);
        this.getServer().getPluginManager().registerEvents(new SlimeMap(), this);
        this.getServer().getPluginManager().registerEvents(new SlimeballListener(), this);
        this.getCommand("radiant").setExecutor(new RadiantCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (playerStorage != null) {
            playerStorage.saveFlyTimes();
        }
    }

    @Override
    public void reloadConfig() {
        GeneralConfig.loadAndUpdate(this);
    }
}
