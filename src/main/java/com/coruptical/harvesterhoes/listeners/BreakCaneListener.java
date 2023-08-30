package com.coruptical.harvesterhoes.listeners;

import com.coruptical.harvesterhoes.HarvesterHoes;
import com.coruptical.harvesterhoes.models.Enchant;
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

    // TODO: Implement new recursive cane breaking system.
    // TODO: Create key finder/spawner finder
    // TODO: Create auto-sell enchantment (Sells every 64 cane)
    // TODO: Create quake enchantment (Need to discuss how behaviour works)

    public Block recurseToTop(Block b ){
        Block blockAbove = b.getRelative(BlockFace.UP);

        if (blockAbove.getType().equals(Material.SUGAR_CANE)){
            return recurseToTop(blockAbove);
        }else{
            return b;
        }
    }

    public void processStack(Block b, Player p, PersistentDataContainer container){
        Block blockBelow = b.getRelative(BlockFace.DOWN);
        int caneBroke = 0;

        NamespacedKey tokenMasterKey = new NamespacedKey(plugin, Enchant.TOKENMASTER.hoeEnchant.getKeyName());
        int levelTokenMaster = container.get(tokenMasterKey, PersistentDataType.INTEGER);

        double tokenMasterMultipler = 1;

        if(levelTokenMaster > 0){
            tokenMasterMultipler = 1 + (Enchant.TOKENMASTER.hoeEnchant.getLevelMultipler()*levelTokenMaster);
        }

        b.setType(Material.AIR);
        while (blockBelow.getType().equals(Material.SUGAR_CANE)){
            caneBroke++;
            blockBelow.setType(Material.AIR);
            blockBelow = blockBelow.getRelative(BlockFace.DOWN);
        }

        plugin.getTokenManager().addTokens(p, (long) (caneBroke*tokenMasterMultipler));
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
