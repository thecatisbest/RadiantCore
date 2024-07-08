package me.thecatisbest.radiantcore.utilis;

import me.thecatisbest.radiantcore.config.GeneralConfig;
import me.thecatisbest.radiantcore.utilis.builder.ItemBuilder;
import org.bukkit.Material;

public class ItemUtils {

    public final ItemBuilder magic_mushroom_soup;
    public final ItemBuilder super_magic_mushroom_soup;

    public final ItemBuilder[] items;

    public ItemUtils() {
        this.magic_mushroom_soup = new ItemBuilder(Material.PLAYER_HEAD);
        this.magic_mushroom_soup.setItemName(Utilis.color(GeneralConfig.MAGIC_MUSHROOM_SOUP_NAME));
        this.magic_mushroom_soup.setItemLore(Utilis.color(GeneralConfig.MAGIC_MUSHROOM_SOUP_LORE));
        this.magic_mushroom_soup.texture(GeneralConfig.MAGIC_MUSHROOM_SOUP_TEXTURE);

        this.super_magic_mushroom_soup = new ItemBuilder(Material.PLAYER_HEAD);
        this.super_magic_mushroom_soup.setItemName(Utilis.color(GeneralConfig.SUPER_MAGIC_MUSHROOM_SOUP_NAME));
        this.super_magic_mushroom_soup.setItemLore(Utilis.color(GeneralConfig.SUPER_MAGIC_MUSHROOM_SOUP_LORE));
        this.super_magic_mushroom_soup.texture(GeneralConfig.SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE);

        this.items = new ItemBuilder[] {
                this.magic_mushroom_soup, this.super_magic_mushroom_soup
        };
    }
}
