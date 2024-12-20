package me.thecatisbest.radiantcore.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.thecatisbest.radiantcore.config.ConfigValue;
import me.thecatisbest.radiantcore.utils.builder.ItemBuilder;
import org.bukkit.Material;

import java.util.Arrays;

public class ItemUtils {
    
    public ItemBuilder magic_mushroom_soup() {
        ItemBuilder magic_mushroom_soup = new ItemBuilder(Material.PLAYER_HEAD);
        magic_mushroom_soup.setItemName(Utils.color(ConfigValue.MAGIC_MUSHROOM_SOUP_NAME));
        magic_mushroom_soup.setItemLore(Utils.color(ConfigValue.MAGIC_MUSHROOM_SOUP_LORE));
        magic_mushroom_soup.texture(ConfigValue.MAGIC_MUSHROOM_SOUP_TEXTURE);
        magic_mushroom_soup.addUniqueId(Key.MAGIC_MUSHROOM_SOUP);
        
        return magic_mushroom_soup;
    }
    
    public ItemBuilder super_magic_mushroom_soup() {
        ItemBuilder super_magic_mushroom_soup = new ItemBuilder(Material.PLAYER_HEAD);
        super_magic_mushroom_soup.setItemName(Utils.color(ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_NAME));
        super_magic_mushroom_soup.setItemLore(Utils.color(ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_LORE));
        super_magic_mushroom_soup.texture(ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE);
        super_magic_mushroom_soup.addUniqueId(Key.SUPER_MAGIC_MUSHROOM_SOUP);
        
        return super_magic_mushroom_soup;
    }
    
    public ItemBuilder builders_wand() {
        ItemBuilder builders_wand = new ItemBuilder(Material.BLAZE_ROD);
        builders_wand.setItemName(Utils.color(ConfigValue.BUILDERS_WAND_NAME));
        builders_wand.setItemLore(Utils.color(ConfigValue.BUILDERS_WAND_LORE));
        builders_wand.addUniqueId(Key.BUILDERS_WAND);
        
        return builders_wand;
    }

    public ItemBuilder grappling_hook() {
        ItemBuilder grappling_hook = new ItemBuilder(Material.FISHING_ROD);
        grappling_hook.setItemName(Utils.color(ConfigValue.GRAPPLING_HOOK_NAME));
        grappling_hook.setItemLore(Utils.color(ConfigValue.GRAPPLING_HOOK_LORE));
        grappling_hook.addUniqueId(Key.GRAPPLING_HOOK);

        return grappling_hook;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Key {

        MAGIC_MUSHROOM_SOUP("magic_mushroom_soup"),
        SUPER_MAGIC_MUSHROOM_SOUP("super_magic_mushroom_soup"),
        BUILDERS_WAND("builders_wand"),
        GRAPPLING_HOOK("grappling_hook"),
        SURVIVAL_DEBUG_STICK("survival_debug_stick");
        
        private final String name;
    }
}
