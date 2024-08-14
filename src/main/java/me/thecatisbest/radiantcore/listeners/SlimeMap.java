package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.utilis.Renderer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.List;

public class SlimeMap implements Listener {

    public static List<Integer> slimemaps = new ArrayList<>();

    @EventHandler
    public void onMapInitialise(MapInitializeEvent e) {
        MapView mapView = e.getMap();
        if (!slimemaps.contains(mapView.getId()))
            return;
        mapView.setUnlimitedTracking(true);
        mapView.addRenderer(new Renderer());
    }

    private static ItemStack addMap(ItemStack itemStack) {
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        slimemaps.add(meta.getMapId());
        MapView map = meta.getMapView();
        if (map != null) {
            map.addRenderer(new Renderer());
        }
        meta.setMapView(map);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static ItemStack removeMap(ItemStack itemStack) {
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        slimemaps.remove(Integer.valueOf(meta.getMapId()));
        MapView map = meta.getMapView();
        if (map != null) {
            for (MapRenderer renderer : map.getRenderers()) {
                if (renderer instanceof Renderer)
                    map.removeRenderer(renderer);
            }
        }
        meta.setMapView(map);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack toggle(ItemStack itemStack) {
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        if (slimemaps.contains(meta.getMapId()))
            return removeMap(itemStack);
        return addMap(itemStack);
    }
}