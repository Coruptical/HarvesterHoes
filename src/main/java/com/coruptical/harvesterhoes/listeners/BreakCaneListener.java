package com.coruptical.harvesterhoes.listeners;

import com.coruptical.harvesterhoes.HarvesterHoes;
import com.coruptical.harvesterhoes.models.Keys;
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

public class BreakCaneListener implements Listener {
    private final HarvesterHoes plugin = HarvesterHoes.getInstance();

    @EventHandler
    public void onBreak(BlockBreakEvent e){


        Block b = e.getBlock();
        if (!b.getType().equals(Material.SUGAR_CANE)) return;

        Player p = e.getPlayer();
        if(p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_HOE)){

            // check if is harvester hoe

            ItemStack stack = p.getInventory().getItemInMainHand();
            ItemMeta meta = stack.getItemMeta();

            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (!container.has(plugin.getHoe_key(), PersistentDataType.STRING)) return;

            e.setCancelled(true);

            // behaviour
            int caneBroke = 0;


            Block one_above = b.getRelative(0, 1, 0);

            if (!b.getRelative(0, -1, 0).getType().equals(Material.SUGAR_CANE)){
                Block two_above = b.getRelative(0, 2, 0);
                if (two_above.getType().equals(Material.SUGAR_CANE)){
                    caneBroke = 2;
                }else if(one_above.getType().equals(Material.SUGAR_CANE)){
                    caneBroke = 1;
                }

                if (caneBroke == 2){
                    two_above.setType(Material.AIR);
                    one_above.setType(Material.AIR);
                    p.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, 2));
                    plugin.getTokenManager().addTokens(p, 2);
                }else if(caneBroke == 1){
                    one_above.setType(Material.AIR);
                    p.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, 2));
                    plugin.getTokenManager().addTokens(p, 1);
                }

            }else if(b.getRelative(0, -1, 0).getType().equals(Material.SUGAR_CANE)){
                Block two_below = b.getRelative(0, -2, 0);
                if (two_below.getType().equals(Material.SUGAR_CANE)){
                    caneBroke = 1;
                }else if(one_above.getType().equals(Material.SUGAR_CANE)){
                    caneBroke = 2;
                }else if(!one_above.getType().equals(Material.SUGAR_CANE)){
                    caneBroke = 1;
                }

                if (caneBroke == 2){
                    one_above.setType(Material.AIR);
                    b.setType(Material.AIR);
                    p.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, 2));
                    plugin.getTokenManager().addTokens(p, 2);
                }else if(caneBroke == 1){
                    b.setType(Material.AIR);
                    p.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, 1));
                    plugin.getTokenManager().addTokens(p, 1);
                }
            }
        }
    }
}
