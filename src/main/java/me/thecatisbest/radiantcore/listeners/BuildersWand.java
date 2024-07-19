package me.thecatisbest.radiantcore.listeners;

import me.thecatisbest.radiantcore.RadiantCore;
import me.thecatisbest.radiantcore.utilis.ItemUtils;
import me.thecatisbest.radiantcore.utilis.Utilis;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class BuildersWand implements Listener {

    private final ItemUtils itemUtils = RadiantCore.getInstance().getItemUtils();
    private static final Map<Player, List<BlockState>> wandOops = new HashMap<>();

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();

        if (player.getGameMode() == GameMode.ADVENTURE && item != null && item.isSimilar(this.itemUtils.builders_wand.toItemStack())) {
            player.sendMessage(Utilis.color("&c你無法在這裡使用建造者魔杖！"));
            return;
        }

        if (item != null && item.isSimilar(this.itemUtils.builders_wand.toItemStack()) &&
                (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(RadiantCore.getInstance(),
                    () -> fillConnectedFaces(player, block, event.getBlockFace(), this.itemUtils.builders_wand.toItemStack())
                    , 1);
        }

    }

    // main logic for builder's wand
    public void fillConnectedFaces(Player player, Block origin, BlockFace face, ItemStack item) {
        Material fillMaterial = origin.getType();
        int blocksInInventory = countBlocks(player.getInventory(), origin.getType());
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
            if (needBlocks) removeBlocks(player.getInventory(), origin.getType(), blocksPlaced);

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

    // counts amount of blocks of type m in inventory inv
    public int countBlocks(Inventory inv, Material m) {
        int blockAmount = 0;

        for (ItemStack item : inv){
            if (item != null){
                if (item.getType() == m){
                    blockAmount += item.getAmount();
                }
            }
        }
        return blockAmount;
    }

    // remove blockAmount blocks of type m from inv
    public void removeBlocks(Inventory inv, Material m, int blockAmount){
        inv.removeItem(new ItemStack (m, blockAmount));
    }

    // save a builder's wand action for the player
    // places the first element of the list as a blockstate from before the wand usage
    public static void putWandOops(Player player, List<BlockState> blocks) {
        wandOops.put(player, blocks);
    }

    public boolean canPlaceBlock(Player player, Location l) {
        return true;
        //BlockBreakEvent e = new BlockBreakEvent(l.getWorld().getBlockAt(l), player);
        //Bukkit.getServer().getPluginManager().callEvent(e);
        //return !e.isCancelled();
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
            // Bukkit.getLogger().info(blocks.get(counter).getType().toString());
        }


        wandOops.remove(player);
        player.sendMessage(Utilis.color("&e已撤銷 &6" + (blocks.size() - 1) + " &e個方塊！"));

        if (player.getGameMode() == GameMode.SURVIVAL)
            givePlayerItemSafely(player, new ItemStack(returnPoint.getType(), blocks.size() - 1));
    }

    // adds item to the player's inventory. Drops the item on the ground if inventory is full
    public static void givePlayerItemSafely(Player player, ItemStack item) {
        final Map<Integer, ItemStack> items = player.getInventory().addItem(item);
        for (final ItemStack i : items.values()) {
            if (i == null || i.getType() == Material.AIR) continue;
            Entity e = player.getWorld().dropItemNaturally(player.getLocation(), i);
            e.setVelocity(player.getLocation().getDirection().multiply(0.1f));
        }
    }
}
