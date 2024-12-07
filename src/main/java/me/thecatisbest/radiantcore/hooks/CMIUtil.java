package me.thecatisbest.radiantcore.hooks;

import me.thecatisbest.radiantcore.utils.Log;
import org.bukkit.Bukkit;

public class CMIUtil {
    private static boolean isCMILoaded = false;

    public static boolean initCMI() {
        if (Bukkit.getPluginManager().getPlugin("CMI") != null) {
            isCMILoaded = true;
            Log.info("CMI found!");
            return true;
        } else {
            isCMILoaded = false;
            Log.warning("CMI was not found! Some features won't work!");
            return false;
        }
    }
}
