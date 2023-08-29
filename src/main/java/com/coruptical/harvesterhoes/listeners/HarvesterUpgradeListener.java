package com.coruptical.harvesterhoes.listeners;

import com.coruptical.harvesterhoes.HarvesterHoes;
import com.coruptical.harvesterhoes.gui.GUIManager;
import com.coruptical.harvesterhoes.gui.impl.HoeInventory;
import com.coruptical.harvesterhoes.gui.impl.PianoInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class HarvesterUpgradeListener implements Listener {

    private final GUIManager guiManager;

    public HarvesterUpgradeListener(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;

        if (!p.isSneaking()) return;

        if (!p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_HOE)) return;

        ItemStack hoeItem = p.getInventory().getItemInMainHand();
        ItemMeta hoeMeta = hoeItem.getItemMeta();

        PersistentDataContainer container = hoeMeta.getPersistentDataContainer();

        if (!container.has(HarvesterHoes.getInstance().getHoe_key(),
                PersistentDataType.STRING)) return;

        open(p);
    }

    public void open(Player player) {
        this.guiManager.openGUI(new HoeInventory(), player);
    }


}
