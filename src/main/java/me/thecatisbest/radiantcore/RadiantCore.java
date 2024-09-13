package me.thecatisbest.radiantcore;

import lombok.Getter;
import lombok.NonNull;
import me.thecatisbest.radiantcore.commands.RadiantCommand;
import me.thecatisbest.radiantcore.config.LoadConfigs;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.listeners.*;
import me.thecatisbest.radiantcore.menus.RulesGUI;
import me.thecatisbest.radiantcore.utils.ItemUtils;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class RadiantCore extends JavaPlugin {

    // Instance
    @Getter private static RadiantCore instance;
    @Getter private CoreProtectAPI coreProtectAPI;
    @Getter private PlayerStorage playerStorage;
    @Getter private ItemUtils itemUtils;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        coreProtectAPI = getCoreProtect();
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
        PlayerStorage.saveConfig();
        LoadConfigs.loadConfigs();
    }

    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (!(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (!CoreProtect.isEnabled()) {
            return null;
        }

        return CoreProtect;
    }

    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}
