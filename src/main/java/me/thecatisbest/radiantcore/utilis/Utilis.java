package me.thecatisbest.radiantcore.utilis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Utilis {

    /**
     * @param msg The message to format
     * @return The formatted message
     */
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * @param message The message to send to the console
     */
    public static void consoleMessage(String message){
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }
}
