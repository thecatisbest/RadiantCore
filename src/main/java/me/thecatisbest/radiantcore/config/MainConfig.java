package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utils.YamlConfigurationDescriptor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MainConfig {

    public static final String PLUGIN_VERSION = RadiantCore.getInstance().getDescription().getVersion();
    public static String CURRENT_CONFIG_VERSION = null;

    private static File getFile() {
        return new File(RadiantCore.getInstance().getDataFolder(), "config.yml");
    }

    public static void load() {
        synchronized (MainConfig.class) {
            try {
                loadUnchecked();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadUnchecked() throws Exception {
        final File file = getFile();

        if (!file.exists() || file.length() == 0) {
            save();
            return;
        }

        // load it
        final FileConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // read it
        ConfigValue.COMMANDS = getString(config, "commands", ConfigValue.COMMANDS);
        ConfigValue.SWAP = getBoolean(config, "cancel_swap", ConfigValue.SWAP);

        ConfigValue.SHIFT_RIGHT_INVIS_FRAMES = getBoolean(config, "shift-right-invis-frames", ConfigValue.SHIFT_RIGHT_INVIS_FRAMES);

        ConfigValue.MUSHROOM_SOUP_WORLD_TYPE_MODE = getString(config, "Mushroom-Soup.world-type-mode", ConfigValue.MUSHROOM_SOUP_WORLD_TYPE_MODE);
        ConfigValue.MUSHROOM_SOUP_WORLD_TYPE = getStringList(config, "Mushroom-Soup.world-type", ConfigValue.MUSHROOM_SOUP_WORLD_TYPE);

        ConfigValue.BUILDERS_WAND_PUT_WITHOUT_BLOCK = getBoolean(config, "Builders-Wand.putWithoutBlock", ConfigValue.BUILDERS_WAND_PUT_WITHOUT_BLOCK);
        ConfigValue.BUILDERS_WAND_WORLD_TYPE_MODE = getString(config, "Builders-Wand.world-type-mode", ConfigValue.BUILDERS_WAND_WORLD_TYPE_MODE);
        ConfigValue.BUILDERS_WAND_WORLD_TYPE = getStringList(config, "Builders-Wand.world-type", ConfigValue.BUILDERS_WAND_WORLD_TYPE);

        ConfigValue.GRAPPLING_HOOK_WORLD_TYPE_MODE = getString(config, "Grappling-Hook.world-type-mode", ConfigValue.GRAPPLING_HOOK_WORLD_TYPE_MODE);
        ConfigValue.GRAPPLING_HOOK_WORLD_TYPE = getStringList(config, "Grappling-Hook.world-type", ConfigValue.GRAPPLING_HOOK_WORLD_TYPE);

        // auto update file if newer version
        CURRENT_CONFIG_VERSION = config.getString("file-version");

        if (CURRENT_CONFIG_VERSION == null || !CURRENT_CONFIG_VERSION.equals(PLUGIN_VERSION)) {
            loadOldConfigs(config);
            save();
        }
    }

    private static void save() throws Exception {
        final YamlConfigurationDescriptor config = new YamlConfigurationDescriptor();

        config.addComment("Used for auto-updating the config file. Ignore it");
        config.set("file-version", PLUGIN_VERSION);

        config.addEmptyLine();

        config.addComment("Use Sneak(Swift) + (F) key to execute commands [ Use ; to make more commands (please don't add spaces) ]");
        config.addComment("Player ID Placeholder: %player%");

        config.set("commands", ConfigValue.COMMANDS);
        config.set("cancel_swap", ConfigValue.SWAP);

        config.addEmptyLine();

        config.addComment("Allows player to shift-right-click an item frame to turn it invisible");
        config.set("shift-right-invis-frames", ConfigValue.SHIFT_RIGHT_INVIS_FRAMES);

        config.addEmptyLine();

        config.addComment("You may choose between:");
        config.addComment("  - DISABLED: The config has no effect");
        config.addComment("  - BLACKLIST: All worlds, apart from these, will be permitted");
        config.addComment("  - WHITELIST: Only the specified worlds will be permitted");

        config.set("Mushroom-Soup.world-type-mode", ConfigValue.MUSHROOM_SOUP_WORLD_TYPE_MODE);
        config.set("Mushroom-Soup.world-type", ConfigValue.MUSHROOM_SOUP_WORLD_TYPE);

        config.addComment("You may choose between:");
        config.addComment("  - DISABLED: The config has no effect");
        config.addComment("  - BLACKLIST: All worlds, apart from these, will be permitted");
        config.addComment("  - WHITELIST: Only the specified worlds will be permitted");

        config.set("Builders-Wand.putWithoutBlock", ConfigValue.BUILDERS_WAND_PUT_WITHOUT_BLOCK);

        config.set("Builders-Wand.world-type-mode", ConfigValue.BUILDERS_WAND_WORLD_TYPE_MODE);
        config.set("Builders-Wand.world-type", ConfigValue.BUILDERS_WAND_WORLD_TYPE);

        config.addComment("You may choose between:");
        config.addComment("  - DISABLED: The config has no effect");
        config.addComment("  - BLACKLIST: All worlds, apart from these, will be permitted");
        config.addComment("  - WHITELIST: Only the specified worlds will be permitted");

        config.set("Grappling-Hook.world-type-mode", ConfigValue.GRAPPLING_HOOK_WORLD_TYPE_MODE);
        config.set("Grappling-Hook.world-type", ConfigValue.GRAPPLING_HOOK_WORLD_TYPE);

        config.save(getFile());
    }

    public static void loadOldConfigs(FileConfiguration config) {
        // Nothing here yet :)
    }

    private static String getString(FileConfiguration config, String path, String defaultValue) {
        return config.contains(path) ? config.getString(path) : defaultValue;
    }

    private static boolean getBoolean(FileConfiguration config, String path, boolean defaultValue) {
        return config.contains(path) ? config.getBoolean(path) : defaultValue;
    }

    private static int getInt(FileConfiguration config, String path, int defaultValue) {
        return config.contains(path) ? config.getInt(path) : defaultValue;
    }

    private static List<String> getStringList(FileConfiguration config, String path, List<String> defaultValue) {
        return config.contains(path) ? config.getStringList(path) : defaultValue;
    }
}
