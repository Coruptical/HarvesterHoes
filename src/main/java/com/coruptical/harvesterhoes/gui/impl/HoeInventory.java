package com.coruptical.harvesterhoes.gui.impl;

import com.coruptical.harvesterhoes.HarvesterHoes;
import com.coruptical.harvesterhoes.gui.InventoryButton;
import com.coruptical.harvesterhoes.gui.InventoryGUI;
import com.coruptical.harvesterhoes.models.Enchant;
import com.coruptical.harvesterhoes.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.OptionalLong;

public class HoeInventory extends InventoryGUI {
    private final HarvesterHoes plugin = HarvesterHoes.getInstance();

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9,
                Utils.cmsg("&7[&bHarvester Hoe Upgrades&7]"));

    }

    @Override
    public void decorate(Player p){
        HashMap<Integer, InventoryButton> buttonHashMap;

        buttonHashMap = createButtons(p);

        for (int slot : buttonHashMap.keySet()){
            this.addButton(slot, buttonHashMap.get(slot));
        }

        super.decorate(p);
    }

    private HashMap<Integer, InventoryButton> createButtons(Player p){
        HashMap<Integer, InventoryButton> buttonHashMap = new HashMap<>();

        ItemStack hoe = p.getInventory().getItemInMainHand();
        ItemMeta hoeMeta = hoe.getItemMeta();
        PersistentDataContainer container = hoeMeta.getPersistentDataContainer();

        int count = 0;
        for (Enchant enchant : Enchant.values()){
            NamespacedKey enchantKey = new NamespacedKey(plugin,
                    enchant.hoeEnchant.getKeyName());

            int currentLevel = container.get(enchantKey, PersistentDataType.INTEGER);

            int nextLevelCost = (int) (enchant.hoeEnchant.getStartingPrice() * Math.pow(1+enchant.hoeEnchant.getPriceMultiplier(), currentLevel));

            InventoryButton button = new InventoryButton()
                    .creator(player -> enchant.hoeEnchant.createGuiItem(currentLevel))
                    .consumer(event -> {
                        Player player = (Player) event.getWhoClicked();

                        OptionalLong tokensOptionalLong = plugin.getTokenManager().getTokens(player);
                        long playerTokens = tokensOptionalLong.getAsLong();

                        if (currentLevel == enchant.hoeEnchant.getMaxLevel()){
                            player.sendMessage(Utils.cmsg(
                                    plugin.getPrefix()+"&7You already have the max level of this. &8("+
                                    enchant.hoeEnchant.getMaxLevel()+")"));
                            return;
                        }

                        if (playerTokens >= nextLevelCost){
                            plugin.getTokenManager().setTokens(player, playerTokens-nextLevelCost);
                            container.set(enchantKey, PersistentDataType.INTEGER, currentLevel+1);
                            player.sendMessage(Utils.cmsg(plugin.getPrefix()+"&aSuccessfully upgraded "+
                                    enchant.hoeEnchant.getName()));

                            hoeMeta.setLore(Utils.getLoreFromHoe(plugin, container));

                            hoe.setItemMeta(hoeMeta);

                            decorate(player);
                        }else{
                            player.sendMessage(Utils.cmsg(plugin.getPrefix()+"&7You do not have enough tokens for this."));
                        }
                    });

            buttonHashMap.put(count, button);
            count++;
        }

        return buttonHashMap;
    }

    private ItemStack createItem(Material material, String name, String[] lore){
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(Utils.cmsg(name));

        ArrayList<String> coloredLore = new ArrayList<>();

        for (String string : lore){
            coloredLore.add(Utils.cmsg(string));
        }

        meta.setLore(coloredLore);

        stack.setItemMeta(meta);

        return stack;
    }



}
