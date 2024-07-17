package me.thecatisbest.radiantcore;

import lombok.Getter;
import lombok.NonNull;
import me.thecatisbest.radiantcore.commands.RadiantCommand;
import me.thecatisbest.radiantcore.config.LoadConfigs;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.listeners.*;
import me.thecatisbest.radiantcore.utilis.ItemUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RadiantCore extends JavaPlugin {

    // Instance
    @Getter private static RadiantCore instance;
    @Getter private PlayerStorage playerStorage;
    @Getter private ItemUtils itemUtils;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        playerStorage = new PlayerStorage(this);
        itemUtils = new ItemUtils();
        playerStorage.startAutoSaveTask();
        LoadConfigs.loadConfigs();
        this.adventure = BukkitAudiences.create(this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new MushroomSoup(), this);
        this.getServer().getPluginManager().registerEvents(new BuildersWand(), this);
        this.getServer().getPluginManager().registerEvents(new GrapplingHook(), this);
        this.getServer().getPluginManager().registerEvents(new SlimeMap(), this);
        this.getServer().getPluginManager().registerEvents(new SlimeballListener(), this);
        this.getCommand("radiant").setExecutor(new RadiantCommand());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders().register();
            Bukkit.getLogger().info("PlaceholderAPI found! PAPI placeholders will work!");
        } else {
            Bukkit.getLogger().info("PlaceholderAPI was not Found! PAPI placeholders won't work!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (playerStorage != null) {
            playerStorage.saveFlyTimes();
        }
    }

    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}
