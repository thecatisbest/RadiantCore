package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utilis.YamlConfigurationDescriptor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

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

        if (!file.exists()) {
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
        ConfigValue.COMMANDS = config.getString("commands");
        ConfigValue.SWAP = config.getBoolean("cancel_swap");

        ConfigValue.MAGIC_MUSHROOM_SOUP_NAME = config.getString("Magic-Mushroom-Soup.name");
        if (config.contains("Magic-Mushroom-Soup.lore"))
            ConfigValue.MAGIC_MUSHROOM_SOUP_LORE = config.getStringList("Magic-Mushroom-Soup.lore");
        ConfigValue.MAGIC_MUSHROOM_SOUP_DURATION = config.getInt("Magic-Mushroom-Soup.duration");
        ConfigValue.MAGIC_MUSHROOM_SOUP_TEXTURE = config.getString("Magic-Mushroom-Soup.texture");

        ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_NAME = config.getString("Super-Magic-Mushroom-Soup.name");
        if (config.contains("Super-Magic-Mushroom-Soup.lore"))
            ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_LORE = config.getStringList("Super-Magic-Mushroom-Soup.lore");
        ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_DURATION = config.getInt("Super-Magic-Mushroom-Soup.duration");
        ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE = config.getString("Super-Magic-Mushroom-Soup.texture");

        ConfigValue.BUILDERS_WAND_NAME = config.getString("Builders-Wand.name");
        if (config.contains("Builders-Wand.lore"))
            ConfigValue.BUILDERS_WAND_LORE = config.getStringList("Builders-Wand.lore");

        ConfigValue.GRAPPLING_HOOK_NAME = config.getString("Grappling-Hook.name");
        if (config.contains("Grappling-Hook.lore"))
            ConfigValue.GRAPPLING_HOOK_LORE = config.getStringList("Grappling-Hook.lore");

        // auto update file if newer version
        {
            CURRENT_CONFIG_VERSION = config.getString("file-version");

            if (CURRENT_CONFIG_VERSION == null || !CURRENT_CONFIG_VERSION.equals(PLUGIN_VERSION)) {
                loadOldConfigs(config);
                save();
            }
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
}
