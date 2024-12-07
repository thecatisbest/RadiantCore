package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.utils.Log;

public class LoadConfigs {

    public static void loadConfigs() {
        final long start = System.currentTimeMillis();

        MainConfig.load();
        ItemsConfig.load();

        final long end = System.currentTimeMillis();
        Log.info("Configs loaded in " + (end - start) + "ms.");
    }
}
