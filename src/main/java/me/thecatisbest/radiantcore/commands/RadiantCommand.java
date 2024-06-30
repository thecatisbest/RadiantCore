package me.thecatisbest.radiantcore.commands;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.config.Config;
import me.thecatisbest.radiantcore.utilis.Utilis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
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
            sender.sendMessage(Utilis.color(" &6| &e/radiant pluginslist &7- &6查看所有插件列表"));
            sender.sendMessage(Utilis.color(" &6| &e/radiant reload &7- &6重新加載配置"));
            sender.sendMessage(" ");
            sender.sendMessage(Utilis.color(" &d插件開發者: &b" + RadiantCore.getInstance().getDescription().getAuthors()));
            sender.sendMessage(" ");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("pluginslist")) {
                if (!sender.hasPermission("radiant.command.pluginslist")) {
                    sender.sendMessage(Utilis.color("&c你沒有權限使用這個指令！"));
                    return false;
                }
                sender.sendMessage(Utilis.color("&6Bukkit 插件列表:"));
                sender.sendMessage(listPlugins(sender));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("radiant.command.reload")) {
                    sender.sendMessage(Utilis.color("&c你沒有權限使用這個指令！"));
                    return false;
                }
                Config.reload();
                sender.sendMessage(Utilis.color("&a已重新加載配置文件!"));
                return true;
            }
        }
        return true;
    }

    private String listPlugins(CommandSender sender) {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        StringBuilder pluginList = new StringBuilder();

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

        if (sender instanceof ConsoleCommandSender) {
            // Remove color codes for console output
            return ChatColor.stripColor(pluginList.toString());
        } else {
            return pluginList.toString();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> toComplete = new ArrayList<>();

        if (args.length == 1) {
            toComplete.add("pluginslist");
            toComplete.add("reload");
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