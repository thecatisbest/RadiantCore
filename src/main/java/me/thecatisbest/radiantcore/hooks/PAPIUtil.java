package me.thecatisbest.radiantcore.hooks;

import me.thecatisbest.radiantcore.Placeholders;
import me.thecatisbest.radiantcore.utils.Log;
import org.bukkit.Bukkit;

public class PAPIUtil {
    private static boolean isPAPILoaded = false;

    public static boolean initPAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            isPAPILoaded = true;
            new Placeholders().register();
            Log.info("PlaceholderAPI found! PAPI placeholders will work!");
            return true;
        } else {
            isPAPILoaded = false;
            Log.warning("PlaceholderAPI was not Found! PAPI placeholders won't work!");
            return false;
        }
    }
}
