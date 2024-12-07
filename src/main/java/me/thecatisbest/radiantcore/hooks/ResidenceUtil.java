package me.thecatisbest.radiantcore.hooks;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import me.thecatisbest.radiantcore.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ResidenceUtil {
    private static boolean isResidenceLoaded = false;

    public static boolean initResidence() {
        if (Bukkit.getPluginManager().getPlugin("Residence") != null) {
            isResidenceLoaded = true;
            Log.info("Residence found!");
            return true;
        } else {
            isResidenceLoaded = false;
            return false;
        }
    }

    public static boolean resCheck(Player player, Location location) {
        ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(location);

        if (isResidenceLoaded) {
            if (residence == null) {
                return true;
            }

            if (residence.getOwnerUUID().equals(player.getUniqueId()) || player.isOp()) {
                return true;
            }

            if (!residence.getPermissions().playerHas(player, Flags.valueOf("use") , true)) {
                return false;
            }
        }

        return true;
    }
}
