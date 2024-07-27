package me.thecatisbest.radiantcore.utilis.builder;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utilis.ItemUtils;
import me.thecatisbest.radiantcore.utilis.Utilis;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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

    public ItemStack texture(String url) {
        return SkullBuilder.itemWithBase64(this.itemStack, url);
    }

    public ItemBuilder addUniqueId(String uniqueId) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(RadiantCore.getInstance(), "unique_id");
            data.set(key, PersistentDataType.STRING, uniqueId);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addUniqueId(ItemUtils.Key Key) {
        return addUniqueId(Key.getName());
    }

    public static boolean hasUniqueId(ItemStack item, String uniqueId) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(RadiantCore.getInstance(), "unique_id");
            return uniqueId.equals(data.get(key, PersistentDataType.STRING));
        }
        return false;
    }

    public static boolean hasUniqueId(ItemStack item, ItemUtils.Key Key) {
        return hasUniqueId(item, Key.getName());
    }

    public ItemStack toItemStack() {
        return this.itemStack;
    }
}
