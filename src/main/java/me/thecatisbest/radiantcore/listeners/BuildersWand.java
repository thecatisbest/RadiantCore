package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.config.PlayerStorage;
import me.thecatisbest.radiantcore.utilis.ItemUtils;
import me.thecatisbest.radiantcore.utilis.Utilis;
import me.thecatisbest.radiantcore.utilis.builder.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class BuildersWand implements Listener {

    private final ItemUtils itemUtils = RadiantCore.getInstance().getItemUtils();
    private static final Map<Player, List<BlockState>> wandOops = new HashMap<>();
    private static final Map<UUID, Inventory> wandInventories = new HashMap<>();
    private static final String inventoryName = "魔杖儲存庫";

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();

        if (item != null && ItemBuilder.hasUniqueId(itemUtils.builders_wand().toItemStack(), ItemUtils.Key.BUILDERS_WAND)) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(true);
                openWandInventory(player);
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (player.getGameMode() == GameMode.ADVENTURE) {
                    player.sendMessage(Utilis.color("&c你無法在這裡使用建造者魔杖！"));
                    return;
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(RadiantCore.getInstance(),
                        () -> fillConnectedFaces(player, block, event.getBlockFace(), this.itemUtils.builders_wand().toItemStack())
                        , 1);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = getWandInventory(player);
            if (inventory != null) {
                PlayerStorage.saveWandInventory(player.getUniqueId(), inventory);
            }
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = getWandInventory(player);
        if (inventory != null) {
            if (event.getCurrentItem().isSimilar(itemUtils.builders_wand().toItemStack())) {
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(Utilis.color("&c你不能把建造者魔杖放進儲存庫裡！"));
            }
        }
    }

    // main logic for builder's wand
    public void fillConnectedFaces(Player player, Block origin, BlockFace face, ItemStack item) {
        Material fillMaterial = origin.getType();
        int blocksInInventory = countBlocks(player, new ItemStack(fillMaterial));
        boolean needBlocks = (player.getGameMode() != GameMode.CREATIVE);
        int blockLimit = 250; if (blocksInInventory < blockLimit && needBlocks) blockLimit = blocksInInventory;
        ArrayList<Block> blocks = new ArrayList<>(); blocks.add(origin);
        Location l; World w = player.getWorld(); Vector[] check = null; Vector translate = null;
        int blocksPlaced = 0;

        List<BlockState> blockStates = new ArrayList<>();
        blockStates.add(origin.getState());

        // establish which blocks to check, depending on the block face's axis
        switch (face) {
            case NORTH: //Z-
            case SOUTH: //Z+
                check = new Vector[] {
                        new Vector(-1,-1,0), new Vector(-1,0,0), new Vector(-1,1,0),
                        new Vector(0,-1,0), new Vector(0,1,0), new Vector(1,-1,0),
                        new Vector(1,0,0), new Vector(1,1,0) }; break;
            case EAST: //X+
            case WEST: //X-
                check = new Vector[] {
                        new Vector(0,-1,-1), new Vector(0,-1,0), new Vector(0,-1,1),
                        new Vector(0,0,-1), new Vector(0,0,1), new Vector(0,1,-1),
                        new Vector(0,1,0), new Vector(0,1,1) }; break;
            case UP: //Y+
            case DOWN: //Y-
                check = new Vector[] {
                        new Vector(-1,0,-1), new Vector(-1,0,0), new Vector(-1,0,1),
                        new Vector(0,0,-1), new Vector(0,0,1), new Vector(1,0,-1),
                        new Vector(1,0,0), new Vector(1,0,1) }; break;
        }
        switch (face) {
            case NORTH: translate = new Vector(0,0,-1); break;
            case SOUTH: translate = new Vector(0,0,1); break;
            case EAST: translate = new Vector(1,0,0); break;
            case WEST: translate = new Vector(-1,0,0); break;
            case UP: translate = new Vector(0,1,0); break;
            case DOWN: translate = new Vector(0,-1,0); break;
        }

        // place blocks
        while(!blocks.isEmpty() && blockLimit > 0) {
            // search surrounding matching blocks for those that are "connected" on this axis
            l = blocks.getFirst().getLocation();
            if (check != null) {
                for (Vector vector : check) {
                    if (w.getBlockAt(l.clone().add(vector)).getType() == fillMaterial &&
                            w.getBlockAt(l.clone().add(vector).clone().add(translate)).getType() == Material.AIR)
                        blocks.add(w.getBlockAt(l.clone().add(vector)));
                }
            }

            // place new material at the selected block
            Block fillBlock = w.getBlockAt(l.clone().add(translate));
            if (canPlaceBlock(player, fillBlock.getLocation())) {

                blocks.removeIf(blocks.getFirst()::equals);
                if (fillBlock.getType() != fillMaterial) {
                    blockStates.add(fillBlock.getState());
                    fillBlock.setType(fillMaterial);

                    // Apply original block's BlockData
                    BlockData blockData = origin.getBlockData().clone();
                    fillBlock.setBlockData(blockData);

                    blockLimit -= 1; blocksPlaced++;
                }

                if (needBlocks && blocksPlaced == blockLimit) break;
            } else {
                blocks.removeIf(blocks.getFirst()::equals);
                blockLimit -= 1;
            }
        }

        // finalize block place action + take blocks from inv if in survival
        if (blocksPlaced != 0) {
            if (needBlocks) removeBlocks(player, new ItemStack(origin.getType()), blocksPlaced);

            player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_BULLET_HIT, 1, 1);
            player.getWorld().playEffect(player.getEyeLocation(), Effect.SMOKE, 0);

            TextComponent textComponent = Component.text("你已放置 ")
                    .color(NamedTextColor.YELLOW)
                    .append(Component.text(blocksPlaced)
                            .color(NamedTextColor.GOLD))
                    .append(Component.text(" 個方塊! ")
                            .color(NamedTextColor.YELLOW))
                    .append(Component.text("撤銷")
                            .color(NamedTextColor.GREEN)
                            .hoverEvent(HoverEvent.showText(Component.text("點擊撤銷！")
                                    .color(NamedTextColor.YELLOW)))
                            .clickEvent(ClickEvent.runCommand("/radiant restorewand")));

            RadiantCore.getInstance().adventure().sender(player).sendMessage(textComponent);
        }

        putWandOops(player, blockStates);
    }

    // save a builder's wand action for the player
    // places the first element of the list as a blockstate from before the wand usage
    public static void putWandOops(Player player, List<BlockState> blocks) {
        wandOops.put(player, blocks);
    }

    public boolean canPlaceBlock(Player player, Location l) {
        return true;
    }

    // restore the most recent builder's wand action for the player
    // assumes that the first element of the list is the blockstate from before the wand usage
    public static void restoreWandOops(Player player) {
        if (!wandOops.containsKey(player)) {
            player.sendMessage(Utilis.color("&c找不到上一次的撤銷記錄！"));
            return;
        }

        List<BlockState> blocks = wandOops.get(player);
        BlockState returnPoint = blocks.getFirst(); World world = blocks.getFirst().getWorld();

        for (int counter = 1; counter < blocks.size(); counter++) {
            world.getBlockAt(blocks.get(counter).getLocation()).setType(blocks.get(counter).getType());
        }

        wandOops.remove(player);
        player.sendMessage(Utilis.color("&e已撤銷 &6" + (blocks.size() - 1) + " &e個方塊！"));

        if (player.getGameMode() == GameMode.SURVIVAL) {
            ItemStack returnItem = new ItemStack(returnPoint.getType(), blocks.size() - 1);
            getWandInventory(player).addItem(returnItem);
        }
    }

    // Inventory
    private static void openWandInventory(Player player) {
        Inventory inv = wandInventories.computeIfAbsent(player.getUniqueId(), k -> {
            Inventory newInv = Bukkit.createInventory(null, 27, inventoryName);
            PlayerStorage.loadWandInventory(player.getUniqueId(), newInv);
            return newInv;
        });
        player.openInventory(inv);
    }

    private static Inventory getWandInventory(Player player) {
        return wandInventories.get(player.getUniqueId());
    }

    private static void removeBlocks(Player player, ItemStack item, int amount) {
        Inventory inv = getWandInventory(player);
        if (inv != null) {
            inv.removeItem(new ItemStack(item.getType(), amount));
            PlayerStorage.saveWandInventory(player.getUniqueId(), inv);
        }
    }

    private static int countBlocks(Player player, ItemStack item) {
        Inventory inv = getWandInventory(player);
        if (inv == null) return 0;

        int count = 0;
        for (ItemStack i : inv.getContents()) {
            if (i != null && i.getType() == item.getType()) {
                count += i.getAmount();
            }
        }
        return count;
    }

    public static void saveAllInventories() {
        for (Map.Entry<UUID, Inventory> entry : wandInventories.entrySet()) {
            PlayerStorage.saveWandInventory(entry.getKey(), entry.getValue());
        }
    }
}