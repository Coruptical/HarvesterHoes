package com.coruptical.harvesterhoes.utils;

import com.coruptical.harvesterhoes.HarvesterHoes;
import com.coruptical.harvesterhoes.models.Enchant;
import com.coruptical.harvesterhoes.models.HoeEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public String colored(Player p, String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public String color(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String cmsg(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void giveBasicHoe(Player p, int quantity){
        final HarvesterHoes plug = HarvesterHoes.getInstance();

        ItemStack stack = new ItemStack(Material.DIAMOND_HOE, quantity);
        ItemMeta meta = stack.getItemMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey hoeKey = plug.getHoe_key();
        container.set(hoeKey, PersistentDataType.STRING, "basic");

        for (Enchant enchant : Enchant.values()){
            HoeEnchant hoeEnchant = enchant.hoeEnchant;
            NamespacedKey ench_key = new NamespacedKey(plug, hoeEnchant.getKeyName());
            container.set(ench_key, PersistentDataType.INTEGER, 0);
        }


        meta.setDisplayName(plug.getHoeName());

        meta.setLore(getHoeLore());

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);


        stack.setItemMeta(meta);
        p.getInventory().addItem(stack);
    }

    public List<String> getHoeLore(){
        List<String> lore = new ArrayList<>();

        for (Enchant ench : Enchant.values()){
            lore.add(ench.hoeEnchant.getName() + " 0");
        }

        lore.add(Utils.cmsg("&7Shift+Right-Click to open menu."));

        return lore;
    }

    public static List<String> getLoreFromHoe(JavaPlugin plugin, PersistentDataContainer container){
        List<String> lore = new LinkedList<>();

        for (Enchant enchant : Enchant.values()){
            NamespacedKey enchantKey = new NamespacedKey(plugin,
                    enchant.hoeEnchant.getKeyName());

            int currentLevel = container.get(enchantKey, PersistentDataType.INTEGER);

            lore.add(enchant.hoeEnchant.getName() + " " + currentLevel);
        }

        lore.add(Utils.cmsg("&7Shift+Right-Click to open menu."));

        return lore;
    }

}
