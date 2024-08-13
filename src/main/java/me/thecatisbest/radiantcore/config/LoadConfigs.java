package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.RadiantCore;

public class LoadConfigs {

    public static void loadConfigs() {
        final long start = System.currentTimeMillis();

        MainConfig.load();
        ItemsConfig.load();

        final long end = System.currentTimeMillis();
        RadiantCore.getInstance().getLogger().info("Configs loaded in " + (end - start) + "ms.");
    }
}
