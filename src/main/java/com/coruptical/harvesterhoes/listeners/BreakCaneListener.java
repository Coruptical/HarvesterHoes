package com.coruptical.harvesterhoes.listeners;

import com.coruptical.harvesterhoes.HarvesterHoes;
import com.coruptical.harvesterhoes.models.Enchant;
import com.coruptical.harvesterhoes.models.Keys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class BreakCaneListener implements Listener {
    private final HarvesterHoes plugin = HarvesterHoes.getInstance();

    public Block recurseToTop(Block b){
        Block blockAbove = b.getRelative(BlockFace.UP);

        if (blockAbove.getType().equals(Material.SUGAR_CANE)){
            return recurseToTop(blockAbove);
        }else{
            return b;
        }
    }

    public void processStack(Block topBlock, Player p, PersistentDataContainer container){
        Block selectedBlock = topBlock.getRelative(0, -1, 0);
        int caneBroke = 0;
        ArrayDeque<Block> selectedBlocks = new ArrayDeque<>();

        if (selectedBlock.getType().equals(Material.SUGAR_CANE)){
            selectedBlocks.addFirst(topBlock);
            caneBroke++;
        }

        NamespacedKey tokenMasterKey = new NamespacedKey(plugin, Enchant.TOKENMASTER.hoeEnchant.getKeyName());
        int levelTokenMaster = container.get(tokenMasterKey, PersistentDataType.INTEGER);

        double tokenMasterMultipler = 1;

        if(levelTokenMaster > 0){
            tokenMasterMultipler = 1 + (Enchant.TOKENMASTER.hoeEnchant.getLevelMultipler()*levelTokenMaster);
        }

        while (selectedBlock.getType().equals(Material.SUGAR_CANE)){
            if (!selectedBlock.getRelative(0, -1, 0).
                    getType().equals(Material.SUGAR_CANE)) break;

            caneBroke++;
            selectedBlocks.addFirst(selectedBlock);
            selectedBlock = selectedBlock.getRelative(0, -1, 0);
        }

        for (Block b : selectedBlocks){
            b.setType(Material.AIR);
        }

        p.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, caneBroke));

        plugin.getTokenManager().addTokens(p, (long) (caneBroke*tokenMasterMultipler));


        // Try implement key finder

        // TODO: Do chance of rolling PER CANE, then individual chances for each key type (rarity-wise)
        while(caneBroke > 0){
            caneBroke--;
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    "excellentcrates give " + p.getDisplayName() + " vote 1");
        }
    }


    @EventHandler
    public void onBreak(BlockBreakEvent e){


        Block brokenBlock = e.getBlock();
        if (!brokenBlock.getType().equals(Material.SUGAR_CANE)) return;

        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_HOE)){

            // check if is harvester hoe

            ItemStack stack = p.getInventory().getItemInMainHand();
            ItemMeta meta = stack.getItemMeta();

            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (!container.has(plugin.getHoe_key(), PersistentDataType.STRING)) return;

            e.setCancelled(true);

            // behaviour
            Block topBlock = recurseToTop(brokenBlock);
            processStack(topBlock, p, container);
        }
    }
}
