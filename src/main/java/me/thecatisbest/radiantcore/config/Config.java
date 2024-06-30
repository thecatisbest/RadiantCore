package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.RadiantCore;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static String COMMANDS;
    public static Boolean SWAP;

    public static void load() {
        RadiantCore.getInstance().saveDefaultConfig();
        loadVars();
    }

    public static void reload() {
        RadiantCore.getInstance().reloadConfig();
        loadVars();
    }

    private static FileConfiguration getConfig() {
        return RadiantCore.getInstance().getConfig();
    }

    private static void loadVars() {
        COMMANDS = getConfig().getString("commands");
        SWAP = getConfig().getBoolean("cancel_swap");
    }
}
