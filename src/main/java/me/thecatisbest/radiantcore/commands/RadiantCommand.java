package me.thecatisbest.radiantcore.commands;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.config.LoadConfigs;
import me.thecatisbest.radiantcore.listeners.BuildersWand;
import me.thecatisbest.radiantcore.listeners.SlimeMap;
import me.thecatisbest.radiantcore.utilis.Utilis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class RadiantCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(Utilis.color("&6RadiantCore &7- &e指令列表"));
            sender.sendMessage(" ");
            sender.sendMessage(Utilis.color(" &6| &e/radiant slimemap &7- &6繪製史萊姆區塊在地圖裡"));
            sender.sendMessage(Utilis.color(" &6| &e/radiant getcustomitem <ID> &7- &6獲取自製物品"));
            sender.sendMessage(Utilis.color(" &6| &e/radiant pluginslist &7- &6查看所有插件列表"));
            sender.sendMessage(Utilis.color(" &6| &e/radiant reload &7- &6重新加載配置"));
            sender.sendMessage(" ");
            sender.sendMessage(Utilis.color(" &d插件開發者: &b" + RadiantCore.getInstance().getDescription().getAuthors()));
            sender.sendMessage(" ");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("restorewand")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("你不能在控制台裡輸入這個指令");
                    return true;
                }
                Player player = (Player) sender;

                BuildersWand.restoreWandOops(player);
            }
            if (args[0].equalsIgnoreCase("slimemap")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("你不能在控制台裡輸入這個指令");
                    return true;
                }
                Player player = (Player) sender;

                if (!player.hasPermission("radiant.command.slimemap")) {
                    player.sendMessage(Utilis.color("&c你沒有權限使用這個指令！"));
                    return true;
                }
                if (getMap(player) == -1) {
                    player.sendMessage(Utilis.color("&c你必須握著地圖才能使用！"));
                    return true;
                }
                ItemStack newItem;
                ItemStack item = player.getInventory().getItemInMainHand();
                newItem = SlimeMap.toggle(item);
                MapMeta meta = (MapMeta) newItem.getItemMeta();

                if (SlimeMap.slimemaps.contains(meta.getMapId())) {
                    player.sendMessage(Utilis.color("&a現在你的地圖會顯示 &e&lSlime Chunk &a了"));
                } else {
                    player.sendMessage(Utilis.color("&c現在你的地圖不會顯示 &e&lSlime Chunk &c了"));
                }

                player.getInventory().setItemInMainHand(newItem);
                return true;
            }
            if (args[0].equalsIgnoreCase("pluginslist")) {
                if (!sender.hasPermission("radiant.command.pluginslist")) {
                    sender.sendMessage(Utilis.color("&c你沒有權限使用這個指令！"));
                    return true;
                }
                sender.sendMessage(Utilis.color("&6Bukkit 插件列表:"));
                sender.sendMessage(listPlugins(sender));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("radiant.command.reload")) {
                    sender.sendMessage(Utilis.color("&c你沒有權限使用這個指令！"));
                    return true;
                }
                LoadConfigs.loadConfigs();
                sender.sendMessage(Utilis.color("&a已重新加載配置文件!"));
                return true;
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("getcustomitem")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("你不能在控制台裡輸入這個指令");
                return true;
            }

            Player player = (Player) sender;
            String itemType = args[1];

            if (!player.hasPermission("radiant.command.getcustomitem")) {
                player.sendMessage(Utilis.color("&c你沒有權限使用這個指令！"));
                return true;
            }

            switch (itemType) {
                case "MAGIC_MUSHROOM_SOUP":
                    player.getInventory().addItem(RadiantCore.getInstance().getItemUtils().magic_mushroom_soup.toItemStack());
                    player.sendMessage(Utilis.color("&6你獲得了一個 &e" + itemType));
                    break;
                case "SUPER_MAGIC_MUSHROOM_SOUP":
                    player.getInventory().addItem(RadiantCore.getInstance().getItemUtils().super_magic_mushroom_soup.toItemStack());
                    player.sendMessage(Utilis.color("&6你獲得了一個 &e" + itemType));
                    break;
                case "BUILDERS_WAND":
                    player.getInventory().addItem(RadiantCore.getInstance().getItemUtils().builders_wand.toItemStack());
                    player.sendMessage(Utilis.color("&6你獲得了一個 &e" + itemType));
                    break;
                case "GRAPPLING_HOOK":
                    player.getInventory().addItem(RadiantCore.getInstance().getItemUtils().grappling_hook.toItemStack());
                    player.sendMessage(Utilis.color("&6你獲得了一個 &e" + itemType));
                    break;
                default:
                    player.sendMessage(Utilis.color("&c無效的物品: &e" + itemType));
                    break;
            }
            return true;
        }
        return true;
    }

    private int getMap(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().equals(Material.FILLED_MAP))
            return -1;
        MapMeta mapMeta = (MapMeta) item.getItemMeta();
        return mapMeta.getMapId();
    }

    private String listPlugins(CommandSender sender) {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        StringBuilder pluginList = new StringBuilder();

        pluginList.append("\n");

        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            String name = plugin.getDescription().getName();
            String version = plugin.getDescription().getVersion();
            boolean enabled = plugin.isEnabled();

            String color = enabled ? ChatColor.GREEN.toString() : ChatColor.RED.toString();
            pluginList
                    .append(ChatColor.YELLOW).append(i + 1).append(". ")
                    .append(color).append(name)
                    .append(ChatColor.GRAY).append(" - ")
                    .append(ChatColor.GOLD).append(version)
                    .append("\n");
        }

        return pluginList.toString();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> toComplete = new ArrayList<>();

        if (args.length == 1) {
            toComplete.add("pluginslist");
            toComplete.add("reload");
            toComplete.add("getcustomitem");
            toComplete.add("slimemap");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("getcustomitem")) {
            toComplete.add("MAGIC_MUSHROOM_SOUP");
            toComplete.add("SUPER_MAGIC_MUSHROOM_SOUP");
            toComplete.add("BUILDERS_WAND");
            toComplete.add("GRAPPLING_HOOK");
        }

        /*
        switch (args.length){
            case 1:
                toComplete.add("reload");
                toComplete.add("migrate");
                break;
            case 2:
                Bukkit.getOnlinePlayers().forEach(player -> toComplete.add(player.getDisplayName()));
                break;
        }
         */

        return toComplete;
    }
}