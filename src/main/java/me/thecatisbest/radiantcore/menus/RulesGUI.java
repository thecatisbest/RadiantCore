package me.thecatisbest.radiantcore.menus;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class RulesGUI {

    private RadiantCore plugin;
    private Inventory gui;
    private final Player player;


    public RulesGUI(Player player, RadiantCore plugin) {
        this.plugin = plugin;
        this.player = player;
        this.gui = Bukkit.createInventory(null, 45, Utils.color("&8歡迎！請閲讀以下規範"));
        addItemsToInventory();
    }

    public void openGUI() {
        this.player.openInventory(this.gui);
        this.player.setMetadata("RulesGUI", new FixedMetadataValue(plugin, this.gui));
    }

    private void addItemsToInventory() {
        // 將空的槽位設置為泥土
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null || gui.getItem(i).getType() == Material.AIR) {
                gui.setItem(i, item);
            }
        }

        ItemStack rules_1 = new ItemStack(Material.AXOLOTL_BUCKET, 1);
        ItemMeta target_rules_1 = rules_1.getItemMeta();
        target_rules_1.setDisplayName(Utils.color("&6伺服器規則"));
        target_rules_1.setLore(Utils.color(Arrays.asList(
                "",
                " &f1. 不要利用信任，進行偷竊、詐騙等",
                " &f2. 不要以任何方式騷擾玩家造成困擾",
                " &f*. 更多規則請前往 &9Discord &f中查看",
                "",
                "&fDiscord: &9https://discord.twstgaming.xyz",
                "")));
        rules_1.setItemMeta(target_rules_1);
        gui.setItem(12, rules_1);

        ItemStack rules_2 = new ItemStack(Material.GOLDEN_AXE, 1);
        ItemMeta target_rules_2 = rules_2.getItemMeta();
        target_rules_2.setDisplayName(Utils.color("&6我該如何開始?"));
        target_rules_2.setLore(Utils.color(Arrays.asList(
                "",
                " &f不妨先看看我們的官方文檔，你很快就能上手的!",
                "",
                " &7暉長官方文檔 Website",
                " &9https://docs.twstgaming.xyz",
                "")));
        rules_2.setItemMeta(target_rules_2);
        gui.setItem(13, rules_2);

        ItemStack rules_3 = new ItemStack(Material.COMPASS, 1);
        ItemMeta target_rules_3 = rules_3.getItemMeta();
        target_rules_3.setDisplayName(Utils.color("&6我在哪裡?"));
        target_rules_3.setLore(Utils.color(Arrays.asList(
                "",
                " &f你在大廳出生點",
                "",
                " &f如果不喜歡這個地點",
                " &f輸入 &c/rtp &f可以換一個新的地點",
                "")));
        rules_3.setItemMeta(target_rules_3);
        gui.setItem(14, rules_3);

        ItemStack accept_rules = new ItemStack(Material.EMERALD, 1);
        ItemMeta target_accept_rules = accept_rules.getItemMeta();
        target_accept_rules.setDisplayName(Utils.color("&a我同意規範"));
        target_accept_rules.setLore(Utils.color(Arrays.asList(
                "",
                " &c同意此規範即代表你同意了以上敘述，",
                " &c伺服器有權拒絕任何上訴。",
                "",
                " &f→ &b點擊後同意，並開始你的旅程 &f←",
                "")));
        accept_rules.setItemMeta(target_accept_rules);
        gui.setItem(31, accept_rules);
    }
}
