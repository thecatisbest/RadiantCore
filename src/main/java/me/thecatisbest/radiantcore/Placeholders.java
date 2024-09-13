package me.thecatisbest.radiantcore;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "radiant";
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
        if (params.equalsIgnoreCase("flight_time")) {
            int timeLeft = RadiantCore.getInstance().getPlayerStorage().getFlyTime(player.getUniqueId());
            return String.valueOf(timeLeft);
        }
        if (params.equalsIgnoreCase("flight_status")) {
            boolean currentMode = PlayerStorage.getFlightMode(player.getUniqueId());
            return currentMode ? Utils.color( "&a啟用") : Utils.color("&c禁用");
        }
        if (params.equalsIgnoreCase("residence_user_maxx")) {
            ResidencePlayer resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(player.getName());
            return String.valueOf(resPlayer.getMaxX());
        }
        if (params.equalsIgnoreCase("residence_user_maxz")) {
            ResidencePlayer resPlayer = ResidenceApi.getPlayerManager().getResidencePlayer(player.getName());
            return String.valueOf(resPlayer.getMaxZ());
        }
        return "";
    }
}
