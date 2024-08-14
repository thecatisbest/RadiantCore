package me.thecatisbest.radiantcore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.utilis.Utilis;
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
        if (params.equalsIgnoreCase("flight-status")) {
            boolean currentMode = PlayerStorage.getFlightMode(player.getUniqueId());
            return currentMode ? Utilis.color( "&a啟用") : Utilis.color("&c禁用");
        }
        return "";
    }
}
