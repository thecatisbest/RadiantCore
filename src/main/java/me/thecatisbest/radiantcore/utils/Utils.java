package me.thecatisbest.radiantcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * @param msg The message to format
     * @return The formatted message
     */
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> color(List<String> msg){
        final List<String> colored = new ArrayList<>();
        for (String s : msg) {
            colored.add(color(s));
        }
        return colored;
    }

    /**
     * @param message The message to send to the console
     */
    public static void consoleMessage(String message){
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }
}
