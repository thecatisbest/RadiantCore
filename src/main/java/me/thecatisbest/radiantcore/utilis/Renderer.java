package me.thecatisbest.radiantcore.utilis;

import me.thecatisbest.radiantcore.listeners.SlimeMap;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.Random;

public class Renderer extends MapRenderer {

    private MapView.Scale oncePerScale = null;

    public void render(MapView map, MapCanvas canvas, Player player) {
        if (map.getScale().equals(this.oncePerScale))
            return;
        if (!SlimeMap.slimemaps.contains(map.getId()))
            return;
        this.oncePerScale = map.getScale();
        player.sendMessage(Utilis.color("&a已加載史萊姆地圖！"));
        int bpp = scaleToBlocksPerChunk(map.getScale());
        for (int x = 0; x < 128; x++) {
            for (int z = 0; z < 128; z++) {
                int xPos = map.getCenterX() + 16 / bpp * (x - 64);
                int zPos = map.getCenterZ() + 16 / bpp * (z - 64);
                if (isSlimeChunk(player.getWorld().getSeed(), xPos, zPos)) {
                    canvas.setPixel(x, z, (byte)-124);
                } else if (bpp < 5) {
                    canvas.setPixel(x, z, (byte)99);
                }
            }
        }
    }

    public int scaleToBlocksPerChunk(MapView.Scale scale) {
        switch (scale) {
            case CLOSEST:
                return 16;
            case CLOSE:
                return 8;
            case NORMAL:
                return 4;
            case FAR:
                return 2;
            case FARTHEST:
                return 1;
        }
        return 0;
    }

    public static boolean isSlimeChunk(long seed, int xPos, int zPos) {
        int xPosition = xPos >> 4;
        int zPosition = zPos >> 4;
        Random rnd = new Random(seed + (xPosition * xPosition * 4987142) + (xPosition * 5947611) + (zPosition * zPosition) * 4392871L + (zPosition * 389711) ^ 0x3AD8025FL);
        return (rnd.nextInt(10) == 0);
    }
}
