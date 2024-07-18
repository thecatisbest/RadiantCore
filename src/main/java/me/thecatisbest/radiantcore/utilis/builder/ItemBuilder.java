package me.thecatisbest.radiantcore.utilis.builder;

import me.thecatisbest.radiantcore.utilis.Utilis;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, int byteId) {
        this.itemStack = new ItemStack(material, amount, (short) byteId);
    }

    public ItemBuilder setItemName(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null)
            meta.setDisplayName(Utilis.color(name));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setItemLore(List<String> lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            meta.setLore(lore);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStack toItemStack() {
        return this.itemStack;
    }

    public ItemStack texture(String url) {
        return SkullBuilder.itemWithBase64(this.itemStack, url);
    }
}
