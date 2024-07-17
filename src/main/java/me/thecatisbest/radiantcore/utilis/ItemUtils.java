package me.thecatisbest.radiantcore.utilis;

import me.thecatisbest.radiantcore.config.ConfigValue;
import me.thecatisbest.radiantcore.utilis.builder.ItemBuilder;
import org.bukkit.Material;

public class ItemUtils {

    public final ItemBuilder magic_mushroom_soup;
    public final ItemBuilder super_magic_mushroom_soup;
    public final ItemBuilder builders_wand;
    public final ItemBuilder grappling_hook;

    public final ItemBuilder[] items;

    public ItemUtils() {
        this.magic_mushroom_soup = new ItemBuilder(Material.PLAYER_HEAD);
        this.magic_mushroom_soup.setItemName(Utilis.color(ConfigValue.MAGIC_MUSHROOM_SOUP_NAME));
        this.magic_mushroom_soup.setItemLore(Utilis.color(ConfigValue.MAGIC_MUSHROOM_SOUP_LORE));
        this.magic_mushroom_soup.texture(ConfigValue.MAGIC_MUSHROOM_SOUP_TEXTURE);

        this.super_magic_mushroom_soup = new ItemBuilder(Material.PLAYER_HEAD);
        this.super_magic_mushroom_soup.setItemName(Utilis.color(ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_NAME));
        this.super_magic_mushroom_soup.setItemLore(Utilis.color(ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_LORE));
        this.super_magic_mushroom_soup.texture(ConfigValue.SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE);

        this.builders_wand = new ItemBuilder(Material.BLAZE_ROD);
        this.builders_wand.setItemName(Utilis.color(ConfigValue.BUILDERS_WAND_NAME));
        this.builders_wand.setItemLore(Utilis.color(ConfigValue.BUILDERS_WAND_LORE));

        this.grappling_hook = new ItemBuilder(Material.FISHING_ROD);
        this.builders_wand.setItemName(Utilis.color(ConfigValue.GRAPPLING_HOOK_NAME));
        this.builders_wand.setItemLore(Utilis.color(ConfigValue.GRAPPLING_HOOK_LORE));

        this.items = new ItemBuilder[] {
                this.magic_mushroom_soup, this.super_magic_mushroom_soup, this.builders_wand, this.grappling_hook
        };
    }
}
