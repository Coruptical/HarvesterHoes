package com.coruptical.harvesterhoes;

import co.aikar.commands.BukkitCommandManager;
import com.coruptical.harvesterhoes.commands.HoeCommand;
import com.coruptical.harvesterhoes.gui.GUIListener;
import com.coruptical.harvesterhoes.gui.GUIManager;
import com.coruptical.harvesterhoes.listeners.BreakCaneListener;
import com.coruptical.harvesterhoes.listeners.HarvesterUpgradeListener;
import com.coruptical.harvesterhoes.models.Enchant;
import com.coruptical.harvesterhoes.models.Keys;
import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class HarvesterHoes extends JavaPlugin {
    private static HarvesterHoes instance;

    private TokenManager tokenManager;

    private NamespacedKey hoe_key;

    private GUIManager guiManager;

    public static HarvesterHoes getInstance(){
        return instance;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public NamespacedKey getHoe_key() {
        return hoe_key;
    }

    @Override
    public void onEnable() {
        instance = this;

        tokenManager = (TokenManager) Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
        hoe_key = new NamespacedKey(this, Keys.HARVESTER.keyName);

        saveDefaultConfig();

        doChecks();

        register();

        setupEnchants();
    }

    public void doChecks(){
        int tokenMasterMaxLevel = getTokenMasterMaxLevel();
        if (tokenMasterMaxLevel < 1 || tokenMasterMaxLevel > 1500)
            getServer().getPluginManager().disablePlugin(this);
    }

    public void setupEnchants(){
        // Token master
        Enchant tokenMaster = Enchant.TOKENMASTER;
        tokenMaster.hoeEnchant.setMaxLevel(getTokenMasterMaxLevel());
        tokenMaster.hoeEnchant.setStartingPrice(getTokenMasterStartingPrice());
        tokenMaster.hoeEnchant.setLevelMultipler(getTokenMasterTokenMultiplier());
        tokenMaster.hoeEnchant.setPriceMultiplier(getTokenMasterPriceMultiplier());

        // Spawner finder


        // Key finder

    }

    public void register(){
        // Commands
        getCommand("harvesterhoes").setExecutor(new HoeCommand());

        // Tab completers
        getCommand("harvesterhoes").setTabCompleter(new HoeCommand());

        // Listeners
        getServer().getPluginManager().registerEvents(new BreakCaneListener(), this);

        // Other features

        guiManager = new GUIManager();

        GUIListener guiListener = new GUIListener(guiManager);
        Bukkit.getPluginManager().registerEvents(guiListener, this);

        HarvesterUpgradeListener harvesterListener = new HarvesterUpgradeListener(guiManager);
        Bukkit.getPluginManager().registerEvents(harvesterListener, this);

    }

    public TokenManager getTokenManager(){
        return tokenManager;
    }

    @Override
    public void onDisable() {

    }

    public String getPermission(){
        return getConfig().getString("CommandPermission");
    }
    public String getPermissionMessage(){
        return ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("PermissionMessage"));
    }
    public String getHoeName(){
        return ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("HoeDisplayName"));
    }

    public int getTokenMasterMaxLevel(){
        return getConfig().getInt("TokenMasterMaxLevel");
    }

    public int getTokenMasterStartingPrice(){
        return getConfig().getInt("TokenMasterStartingPrice");
    }

    public double getTokenMasterPriceMultiplier(){
        return getConfig().getDouble("TokenMasterPriceMultiplier");
    }

    public double getTokenMasterTokenMultiplier(){
        return getConfig().getDouble("TokenMasterTokenMultiplier");
    }

}
