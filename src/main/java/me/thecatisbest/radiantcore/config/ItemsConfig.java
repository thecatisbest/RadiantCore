package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utils.YamlConfigurationDescriptor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ItemsConfig {

    public static final String PLUGIN_VERSION = RadiantCore.getInstance().getDescription().getVersion();
    public static String CURRENT_CONFIG_VERSION = null;

    private static File getFile() {
        return new File(RadiantCore.getInstance().getDataFolder(), "items.yml");
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
        ConfigValue.MAGIC_MUSHROOM_SOUP_NAME = getString(config, "Magic-Mushroom-Soup.name", ConfigValue.MAGIC_MUSHROOM_SOUP_NAME);
        ConfigValue.MAGIC_MUSHROOM_SOUP_LORE = getStringList(config, "Magic-Mushroom-Soup.lore", ConfigValue.MAGIC_MUSHROOM_SOUP_LORE);
        ConfigValue.MAGIC_MUSHROOM_SOUP_DURATION = getInt(config, "Magic-Mushroom-Soup.duration", ConfigValue.MAGIC_MUSHROOM_SOUP_DURATION);
        ConfigValue.MAGIC_MUSHROOM_SOUP_TEXTURE = getString(config, "Magic-Mushroom-Soup.texture", ConfigValue.MAGIC_MUSHROOM_SOUP_TEXTURE);

        ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_NAME = getString(config, "Super-Magic-Mushroom-Soup.name", ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_NAME);
        ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_LORE = getStringList(config, "Super-Magic-Mushroom-Soup.lore", ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_LORE);
        ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_DURATION = getInt(config, "Super-Magic-Mushroom-Soup.duration", ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_DURATION);
        ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE = getString(config, "Super-Magic-Mushroom-Soup.texture", ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE);

        ConfigValue.BUILDERS_WAND_NAME = getString(config, "Builders-Wand.name", ConfigValue.BUILDERS_WAND_NAME);
        ConfigValue.BUILDERS_WAND_LORE = getStringList(config, "Builders-Wand.lore", ConfigValue.BUILDERS_WAND_LORE);

        ConfigValue.GRAPPLING_HOOK_NAME = getString(config, "Grappling-Hook.name", ConfigValue.GRAPPLING_HOOK_NAME);
        ConfigValue.GRAPPLING_HOOK_LORE = getStringList(config, "Grappling-Hook.lore", ConfigValue.GRAPPLING_HOOK_LORE);

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

        config.set("Magic-Mushroom-Soup.name", ConfigValue.MAGIC_MUSHROOM_SOUP_NAME);
        config.set("Magic-Mushroom-Soup.lore", ConfigValue.MAGIC_MUSHROOM_SOUP_LORE);
        config.set("Magic-Mushroom-Soup.duration", ConfigValue.MAGIC_MUSHROOM_SOUP_DURATION);
        config.set("Magic-Mushroom-Soup.texture", ConfigValue.MAGIC_MUSHROOM_SOUP_TEXTURE);

        config.set("Super-Magic-Mushroom-Soup.name", ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_NAME);
        config.set("Super-Magic-Mushroom-Soup.lore", ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_LORE);
        config.set("Super-Magic-Mushroom-Soup.duration", ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_DURATION);
        config.set("Super-Magic-Mushroom-Soup.texture", ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE);

        config.set("Builders-Wand.name", ConfigValue.BUILDERS_WAND_NAME);
        config.set("Builders-Wand.lore", ConfigValue.BUILDERS_WAND_LORE);

        config.set("Grappling-Hook.name", ConfigValue.GRAPPLING_HOOK_NAME);
        config.set("Grappling-Hook.lore", ConfigValue.GRAPPLING_HOOK_LORE);

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
