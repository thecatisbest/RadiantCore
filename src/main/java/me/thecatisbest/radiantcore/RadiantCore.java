package me.thecatisbest.radiantcore;

import lombok.Getter;
import me.thecatisbest.radiantcore.commands.RadiantCommand;
import me.thecatisbest.radiantcore.config.Config;
import me.thecatisbest.radiantcore.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RadiantCore extends JavaPlugin {

    // Instance
    @Getter private static RadiantCore instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Config.load();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getCommand("radiant").setExecutor(new RadiantCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
