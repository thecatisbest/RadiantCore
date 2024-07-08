package me.thecatisbest.radiantcore.config;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utilis.YamlConfigurationDescriptor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class GeneralConfig {

    public static String COMMANDS = "say hi;say I'm %player%";
    public static Boolean SWAP = false;
    public static String MAGIC_MUSHROOM_SOUP_NAME = "&6魔菇湯";
    public static List<String> MAGIC_MUSHROOM_SOUP_LORE = Arrays.asList("&f聽說喝了可以有&b飛上天&f的感覺, 真的假的?", "&f可以讓玩家飛行&610 分鐘&f, 離開伺服器將暫停效果", "", "&e點擊右鍵使用！");
    public static Integer MAGIC_MUSHROOM_SOUP_DURATION = 600;
    public static String  MAGIC_MUSHROOM_SOUP_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjI2ODMyODZhOTEyZWVkYzIyNDk3NGIyZDRhZmI3ZmE1NDViMTNhZTYxZDE5ZjNjZGY5OTBlNTFhNTA1YWVmMSJ9fX0=";
    public static String SUPER_MAGIC_MUSHROOM_SOUP_NAME = "&6超級魔菇湯";
    public static List<String> SUPER_MAGIC_MUSHROOM_SOUP_LORE = Arrays.asList("&f聽說喝了可以有&b飛上天&f的感覺, 效果比&6魔菇湯&f還要好!", "&f可以讓玩家飛行&660 分鐘&f, 離開伺服器將暫停效果", "", "&e點擊右鍵使用！");
    public static Integer SUPER_MAGIC_MUSHROOM_SOUP_DURATION = 3600;
    public static String SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFlNWQwYzIzZWMxYTFmODEzYzBjNjNmMTEyZTU1YjdiMWM4N2ZlY2QzMjY5YzBmZGJjZTk2ZDAzYjU1OGMwOCJ9fX0=";

    public static void loadAndUpdate(RadiantCore plugin) {
        final File file = getFile(plugin);
        final YamlConfiguration root = YamlConfiguration.loadConfiguration(file);

        // Check if file doesn't exist
        if (!file.exists()) {
            save(plugin);
            return;
        }

        COMMANDS = root.getString("commands", "say hi;say I'm %player%");
        SWAP = root.getBoolean("cancel_swap", false);
        // --------------------------------------------------------------
        MAGIC_MUSHROOM_SOUP_NAME = root.getString("Magic-Mushroom-Soup.name", "&6魔菇湯");
        if (root.contains("Magic-Mushroom-Soup.lore")) {
            MAGIC_MUSHROOM_SOUP_LORE = root.getStringList("Magic-Mushroom-Soup.lore");
        } else {
            MAGIC_MUSHROOM_SOUP_LORE = Arrays.asList("&f聽說喝了可以有&b飛上天&f的感覺, 真的假的?", "&f可以讓玩家飛行&610 分鐘&f, 離開伺服器將暫停效果", "", "&e點擊右鍵使用！");
        }
        MAGIC_MUSHROOM_SOUP_DURATION = root.getInt("Magic-Mushroom-Soup.duration", 600);
        MAGIC_MUSHROOM_SOUP_TEXTURE = root.getString("Magic-Mushroom-Soup.texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZiMjkwYTEzZGY4ODI2N2VhNWY1ZmNmNzk2YjYxNTdmZjY0Y2NlZTVjZDM5ZDQ2OTcyNDU5MWJhYmVlZDFmNiJ9fX0=");
        // --------------------------------------------------------------
        SUPER_MAGIC_MUSHROOM_SOUP_NAME = root.getString("Super-Magic-Mushroom-Soup.name", "&6超級魔菇湯");
        if (root.contains("Super-Magic-Mushroom-Soup.lore")) {
            SUPER_MAGIC_MUSHROOM_SOUP_LORE = root.getStringList("Super-Magic-Mushroom-Soup.lore");
        } else {
            SUPER_MAGIC_MUSHROOM_SOUP_LORE = Arrays.asList("&f聽說喝了可以有&b飛上天&f的感覺, 效果比&6魔菇湯&f還要好!", "&f可以讓玩家飛行&660 分鐘&f, 離開伺服器將暫停效果", "", "&e點擊右鍵使用！");
        }
        SUPER_MAGIC_MUSHROOM_SOUP_DURATION = root.getInt("Super-Magic-Mushroom-Soup.duration", 3600);
        SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE = root.getString("Super-Magic-Mushroom-Soup.texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFlNWQwYzIzZWMxYTFmODEzYzBjNjNmMTEyZTU1YjdiMWM4N2ZlY2QzMjY5YzBmZGJjZTk2ZDAzYjU1OGMwOCJ9fX0=");

        // Check if versions don't match
        if (!root.getString("version", "").equals(plugin.getDescription().getVersion()))
            save(plugin);
    }

    public static void save(RadiantCore plugin) {
        final YamlConfigurationDescriptor root = new YamlConfigurationDescriptor();

        root.addComment("Radiant Configuration");

        root.set("version", plugin.getDescription().getVersion());

        root.addEmptyLine();

        root.addComment("Use Sneak(Swift) + (F) key to execute commands [ Use ; to make more commands (please don't add spaces) ]");
        root.addComment("Player ID Placeholder: %player%");

        root.set("commands", COMMANDS);
        root.set("cancel_swap", SWAP);

        root.addEmptyLine();

        root.set("Magic-Mushroom-Soup.name", MAGIC_MUSHROOM_SOUP_NAME);
        root.set("Magic-Mushroom-Soup.lore", MAGIC_MUSHROOM_SOUP_LORE);
        root.set("Magic-Mushroom-Soup.duration", MAGIC_MUSHROOM_SOUP_DURATION);
        root.set("Magic-Mushroom-Soup.texture", MAGIC_MUSHROOM_SOUP_TEXTURE);

        root.set("Super-Magic-Mushroom-Soup.name", MAGIC_MUSHROOM_SOUP_NAME);
        root.set("Super-Magic-Mushroom-Soup.lore", MAGIC_MUSHROOM_SOUP_LORE);
        root.set("Super-Magic-Mushroom-Soup.duration", MAGIC_MUSHROOM_SOUP_DURATION);
        root.set("Super-Magic-Mushroom-Soup.texture", MAGIC_MUSHROOM_SOUP_TEXTURE);

        try {
            root.save(getFile(plugin));
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save " + getFile(plugin).getName() +
                    " (Possible out of storage or missing permissions?)", e);
        }
    }

    private static File getFile(Plugin plugin) {
        return new File(plugin.getDataFolder(), "config.yml");
    }
}
