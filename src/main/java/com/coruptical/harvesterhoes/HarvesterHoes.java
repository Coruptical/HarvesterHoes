package com.coruptical.harvesterhoes;

import com.coruptical.harvesterhoes.commands.HoeCommand;
import com.coruptical.harvesterhoes.commands.ReloadCommand;
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

// TODO: Create key finder
// TODO: Create spawner finder
// TODO: Create auto-sell enchantment (Sells every 64 cane)
// TODO: Create quake enchantment (Need to discuss how behaviour works)

public final class HarvesterHoes extends JavaPlugin {
    private static HarvesterHoes instance;

    private TokenManager tokenManager;

    private NamespacedKey hoe_key;

    private GUIManager guiManager;

    private String[] keys;

    private String[] spawners;

    public static HarvesterHoes getInstance(){
        return instance;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public NamespacedKey getHoe_key() {
        return hoe_key;
    }

    public String[] getKeys() {
        return keys;
    }

    public String[] getSpawners() {
        return spawners;
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

        int spawnerFinderMaxLevel = getSpawnerFinderMaxLevel();
        if (spawnerFinderMaxLevel < 1 || spawnerFinderMaxLevel > 1500){
            getServer().getPluginManager().disablePlugin(this);
        }

        int keyFinderMaxLevel = getKeyFinderMaxLevel();
        if (keyFinderMaxLevel < 1 || keyFinderMaxLevel > 1500){
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public void setupEnchants(){
        // Token master
        Enchant tokenMaster = Enchant.TOKENMASTER;
        tokenMaster.hoeEnchant.setMaxLevel(getTokenMasterMaxLevel());
        tokenMaster.hoeEnchant.setStartingPrice(getTokenMasterStartingPrice());
        tokenMaster.hoeEnchant.setLevelMultiplier(getTokenMasterTokenMultiplier());
        tokenMaster.hoeEnchant.setPriceMultiplier(getTokenMasterPriceMultiplier());

        // Spawner finder
        Enchant spawnerFinder = Enchant.SPAWNERFINDER;
        spawnerFinder.hoeEnchant.setMaxLevel(getSpawnerFinderMaxLevel());
        spawnerFinder.hoeEnchant.setStartingPrice(getSpawnerFinderStartingPrice());
        spawnerFinder.hoeEnchant.setLevelMultiplier(getSpawnerFinderChanceMultiplier());
        spawnerFinder.hoeEnchant.setPriceMultiplier(getSpawnerFinderPriceMultiplier());

        spawners = getSpawnerStringList().toLowerCase().split(",");

        // Key finder
        Enchant keyFinder = Enchant.KEYFINDER;
        keyFinder.hoeEnchant.setMaxLevel(getKeyFinderMaxLevel());
        keyFinder.hoeEnchant.setStartingPrice(getKeyFinderStartingPrice());
        keyFinder.hoeEnchant.setLevelMultiplier(getKeyFinderChanceMultiplier());
        keyFinder.hoeEnchant.setPriceMultiplier(getKeyFinderPriceMultiplier());

        keys = getKeyStringList().toLowerCase().split(",");


        // Auto sell


        // Quake

    }

    public void register(){
        // Commands
        getCommand("harvesterhoes").setExecutor(new HoeCommand());
        getCommand("hoesreload").setExecutor(new ReloadCommand());

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

    public int getSpawnerFinderMaxLevel(){
        return getConfig().getInt("SpawnerFinderMaxLevel");
    }

    public int getSpawnerFinderStartingPrice(){
        return getConfig().getInt("SpawnerFinderStartingPrice");
    }

    public double getSpawnerFinderPriceMultiplier(){
        return getConfig().getDouble("SpawnerFinderPriceMultiplier");
    }

    public double getSpawnerFinderChanceMultiplier(){
        return getConfig().getDouble("SpawnerFinderChanceMultiplier");
    }
    public int getKeyFinderMaxLevel(){
        return getConfig().getInt("KeyFinderMaxLevel");
    }

    public int getKeyFinderStartingPrice(){
        return getConfig().getInt("KeyFinderStartingPrice");
    }

    public double getKeyFinderPriceMultiplier(){
        return getConfig().getDouble("KeyFinderPriceMultiplier");
    }

    public double getKeyFinderChanceMultiplier(){
        return getConfig().getDouble("KeyFinderChanceMultiplier");
    }

    public String getKeyStringList(){
        return getConfig().getString("KeysList");
    }

    public String getSpawnerStringList(){
        return getConfig().getString("SpawnerList");
    }
}
