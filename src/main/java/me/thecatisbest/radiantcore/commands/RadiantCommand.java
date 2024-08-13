package me.thecatisbest.radiantcore.commands;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.config.LoadConfigs;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.listeners.BuildersWand;
import me.thecatisbest.radiantcore.listeners.MushroomSoup;
import me.thecatisbest.radiantcore.listeners.SlimeMap;
import me.thecatisbest.radiantcore.utilis.Utilis;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class RadiantCommand implements TabExecutor {

    public static final Map<UUID, Boolean> isWandUsingCMD = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(Utilis.color("&6RadiantCore &7- &e指令列表"));
            sender.sendMessage(" ");
            sender.sendMessage(Utilis.color(" &6| &e/radiant slimemap &7- &6繪製史萊姆區塊在地圖裡"));
            sender.sendMessage(Utilis.color(" &6| &e/radiant restorewand &7- &6撤銷上一次的記錄"));
            sender.sendMessage(Utilis.color(" &6| &e/radiant toggleflight <true/false> &7- &6設置飛行狀態"));
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
                return true;
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
        if (args.length == 2 && args[0].equalsIgnoreCase("openwandinv")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("你不能在控制台裡輸入這個指令");
                return true;
            }

            Player player = (Player) sender;
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (!player.hasPermission("radiant.command.openwandinv")) {
                player.sendMessage(Utilis.color("&c你沒有權限使用這個指令！"));
                return true;
            }

            if (!target.hasPlayedBefore() && !target.isOnline()) {
                player.sendMessage(Utilis.color("&c找不到該玩家!"));
                return true;
            }

            if (target.isOnline()) {
                player.sendMessage(Utilis.color("&e你正在打開 &6" + target.getName() + " &e的魔杖庫存 (&6Online&e)"));
            } else {
                player.sendMessage(Utilis.color("&e你正在打開 &6" + target.getName() + " &e的魔杖庫存 (&cOffline&e)"));
            }

            openWandInventory(target, player);
            return true;

        }

        if (args[0].equalsIgnoreCase("toggleflight")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("你不能在控制台裡輸入這個指令");
                return true;
            }
            Player player = (Player) sender;

            if (args.length == 1) {
                boolean currentMode = PlayerStorage.getFlightMode(player.getUniqueId());
                PlayerStorage.setFlightMode(player.getUniqueId(), !currentMode);
                if (!currentMode) {
                    int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(player.getUniqueId()); // 从文件中加载
                    if (savedTime > 0 && !MushroomSoup.tasks.containsKey(player.getUniqueId()) && !MushroomSoup.isAFK.containsKey(player.getUniqueId())) {
                        MushroomSoup.flyTimes.put(player.getUniqueId(), savedTime);
                        if (MushroomSoup.isWorldAllowed(player.getWorld())) {
                            MushroomSoup.startFlying(player);
                            Bukkit.getLogger().info("Successful");
                        }
                    }
                }
                player.sendMessage(Utilis.color("&6你的飛行模式已 " + (!currentMode ? "&a啟用" : "&c禁用")));
                return true;
            }

            if (args.length == 2) {
                boolean newModeStatus = Boolean.parseBoolean(args[1]);
                boolean currentMode = PlayerStorage.getFlightMode(player.getUniqueId());

                if (currentMode == newModeStatus) {
                    player.sendMessage(Utilis.color("&c你的飛行模式已經是 " + (newModeStatus ?  "&a啟用" : "&c禁用") + " &c狀態了！"));
                } else {
                    PlayerStorage.setFlightMode(player.getUniqueId(), newModeStatus);

                    if (!currentMode) {
                        int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(player.getUniqueId()); // 从文件中加载
                        if (savedTime > 0 && !MushroomSoup.tasks.containsKey(player.getUniqueId()) && !MushroomSoup.isAFK.containsKey(player.getUniqueId())) {
                            MushroomSoup.flyTimes.put(player.getUniqueId(), savedTime);
                            if (MushroomSoup.isWorldAllowed(player.getWorld())) {
                                MushroomSoup.startFlying(player);
                                Bukkit.getLogger().info("Successful");
                            }
                        }
                    }

                    player.sendMessage(Utilis.color("&6你的飛行模式已 " + (newModeStatus ?  "&a啟用" : "&c禁用")));
                }
                return true;
            }

            if (args.length == 3) {
                if (!sender.hasPermission("radiant.command.toggleflight.setplayer")) {
                    sender.sendMessage(Utilis.color("&c你沒有權限使用這個指令！"));
                    return true;
                }

                boolean newModeStatus = Boolean.parseBoolean(args[1]);
                Player targetPlayer = Bukkit.getPlayer(args[2]);

                if (targetPlayer != null) {
                    boolean currentMode = PlayerStorage.getFlightMode(targetPlayer.getUniqueId());
                    if (currentMode == newModeStatus) {
                        sender.sendMessage(Utilis.color("&6" + targetPlayer.getName() + " &c的飛行模式已經是 " + (newModeStatus ?  "&a啟用" : "&c禁用") + " &c狀態了！"));
                    } else {
                        PlayerStorage.setFlightMode(targetPlayer.getUniqueId(), newModeStatus);

                        if (!currentMode) {
                            int savedTime = RadiantCore.getInstance().getPlayerStorage().getFlyTime(targetPlayer.getUniqueId()); // 从文件中加载
                            if (savedTime > 0 && !MushroomSoup.tasks.containsKey(targetPlayer.getUniqueId()) && !MushroomSoup.isAFK.containsKey(targetPlayer.getUniqueId())) {
                                MushroomSoup.flyTimes.put(targetPlayer.getUniqueId(), savedTime);
                                if (MushroomSoup.isWorldAllowed(targetPlayer.getWorld())) {
                                    MushroomSoup.startFlying(targetPlayer);
                                }
                            }
                        }

                        sender.sendMessage(Utilis.color("&e" + targetPlayer.getName() + " &6的飛行模式設置為 " + (newModeStatus ?  "&a啟用" : "&c禁用")));
                    }
                } else {
                    player.sendMessage(Utilis.color("&c找不到該玩家！"));
                }
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
                    player.getInventory().addItem(RadiantCore.getInstance().getItemUtils().magic_mushroom_soup().toItemStack());
                    player.sendMessage(Utilis.color("&6你獲得了一個 &e" + itemType));
                    break;
                case "SUPER_MAGIC_MUSHROOM_SOUP":
                    player.getInventory().addItem(RadiantCore.getInstance().getItemUtils().super_magic_mushroom_soup().toItemStack());
                    player.sendMessage(Utilis.color("&6你獲得了一個 &e" + itemType));
                    break;
                case "BUILDERS_WAND":
                    player.getInventory().addItem(RadiantCore.getInstance().getItemUtils().builders_wand().toItemStack());
                    player.sendMessage(Utilis.color("&6你獲得了一個 &e" + itemType));
                    break;
                case "GRAPPLING_HOOK":
                    player.getInventory().addItem(RadiantCore.getInstance().getItemUtils().grappling_hook().toItemStack());
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
        List<String> result = new ArrayList<>();

        if (sender instanceof Player player) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 80.0F, 1.0F);
        }

        switch (args.length) {
            case 1:
                toComplete.add("toggleflight");
                toComplete.add("pluginslist");
                toComplete.add("reload");
                toComplete.add("getcustomitem");
                toComplete.add("slimemap");
                toComplete.add("openwandinv");
                toComplete.add("restorewand");
                for (String a : toComplete) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                        result.add(a);
                }
                return result;
            case 2:
                if (args[0].equalsIgnoreCase("toggleflight")) {
                    toComplete.add("true");
                    toComplete.add("false");
                    for (String a : toComplete) {
                        if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(a);
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("openwandinv")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        toComplete.add(player.getName());
                    }
                    for (String a : toComplete) {
                        if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(a);
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("getcustomitem")) {
                    toComplete.add("MAGIC_MUSHROOM_SOUP");
                    toComplete.add("SUPER_MAGIC_MUSHROOM_SOUP");
                    toComplete.add("BUILDERS_WAND");
                    toComplete.add("GRAPPLING_HOOK");
                    for (String a : toComplete) {
                        if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                            result.add(a);
                    }
                    return result;
                }
            case 3:
                if (args[0].equalsIgnoreCase("toggleflight")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        toComplete.add(player.getName());
                    }
                    for (String a : toComplete) {
                        if (a.toLowerCase().startsWith(args[2].toLowerCase()))
                            result.add(a);
                    }
                    return result;
                }
        }

        return new ArrayList<>(); // null = online players
    }

    private void openWandInventory(OfflinePlayer target, Player viewer) {
        Inventory inv = BuildersWand.wandInventories.computeIfAbsent(target.getUniqueId(), k -> {
            Inventory newInv = Bukkit.createInventory(null, 27, Utilis.color("&8" + target.getName() + " 的魔杖庫存"));
            PlayerStorage.loadWandInventory(target.getUniqueId(), newInv);
            return newInv;
        });
        viewer.openInventory(inv);
        isWandUsingCMD.put(viewer.getUniqueId(), true);
    }
}