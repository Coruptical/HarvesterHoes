package com.coruptical.harvesterhoes.models;

import com.coruptical.harvesterhoes.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class HoeEnchant {
    private String name;

    private String description;

    private Material guiMaterial;

    private int startingPrice;

    private int maxLevel;

    private double priceMultiplier;

    private double levelMultiplier;

    public HoeEnchant(String name, String description, int maxLevel, Material guiMaterial, int startingPrice,
                      double priceMultiplier, double levelMultiplier){
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        this.guiMaterial = guiMaterial;
        this.startingPrice = startingPrice;
        this.priceMultiplier = priceMultiplier;
        this.levelMultiplier = levelMultiplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Material getGuiMaterial() {
        return guiMaterial;
    }

    public void setGuiMaterial(Material guiMaterial) {
        this.guiMaterial = guiMaterial;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getLevelMultiplier() {
        return levelMultiplier;
    }

    public void setLevelMultiplier(double levelMultiplier) {
        this.levelMultiplier = levelMultiplier;
    }

    // Custom methods

    public ItemStack createGuiItem(int currentLevel){
        ItemStack stack = new ItemStack(this.guiMaterial);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(Utils.cmsg("&7[" + this.name + "&7]"));

        List<String> lores = new ArrayList<>();
        lores.add(this.description);

        lores.add(Utils.cmsg("&6Current lvl: &f" + currentLevel));

        int nextLevelCost = (int) (this.startingPrice * Math.pow(1+this.priceMultiplier, currentLevel));
        lores.add(Utils.cmsg("&bNext level cost: &f" + nextLevelCost + " tokens"));

        meta.setLore(lores);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        stack.setItemMeta(meta);

        return stack;
    }

    public String getKeyName(){
        return ChatColor.stripColor(this.getName()).replace(" ", "_");
    }
}
