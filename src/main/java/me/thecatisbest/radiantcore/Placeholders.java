package me.thecatisbest.radiantcore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "radiantcore";
    }

    @Override
    public @NotNull String getAuthor() {
        return "tcib_cat";
    }

    @Override
    public @NotNull String getVersion() {
        return RadiantCore.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }
        if (params.equalsIgnoreCase("mushroom-time")) {
            int timeLeft = RadiantCore.getInstance().getPlayerStorage().getFlyTime(player.getUniqueId());
            return String.valueOf(timeLeft);
        }
        return "";
    }
}
