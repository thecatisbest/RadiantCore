package me.thecatisbest.radiantcore;

import lombok.Getter;
import lombok.NonNull;
import me.thecatisbest.radiantcore.commands.RadiantCommand;
import me.thecatisbest.radiantcore.config.LoadConfigs;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.hooks.CMIUtil;
import me.thecatisbest.radiantcore.hooks.CoreProtectUtil;
import me.thecatisbest.radiantcore.hooks.PAPIUtil;
import me.thecatisbest.radiantcore.hooks.ResidenceUtil;
import me.thecatisbest.radiantcore.listeners.*;
import me.thecatisbest.radiantcore.utils.ItemUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
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

        loadAllHooks();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerStorage.saveConfig();
        LoadConfigs.loadConfigs();
    }

    private void loadAllHooks() {
        // Load dependencies
        CMIUtil.initCMI();
        CoreProtectUtil.initCoreProtect();
        PAPIUtil.initPAPI();
        ResidenceUtil.initResidence();
    }

    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}
