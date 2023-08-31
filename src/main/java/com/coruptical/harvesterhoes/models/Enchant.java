package com.coruptical.harvesterhoes.models;

import com.coruptical.harvesterhoes.utils.Utils;
import org.bukkit.Material;

public enum Enchant {
    TOKENMASTER(new HoeEnchant(
            Utils.cmsg("&eToken Master"),
            Utils.cmsg("&7Increases the number of tokens acquired per sugar cane."),
            0,
            Material.SUNFLOWER,
            0,
            0,
            0)),
    SPAWNERFINDER(new HoeEnchant(
            Utils.cmsg("&5Spawner Finder"),
            Utils.cmsg("&7Increases the chance of finding spawners by breaking sugar cane."),
            0,
            Material.SPAWNER,
            0,
            0,
            0
    ))
    ;

    public final HoeEnchant hoeEnchant;

    Enchant(HoeEnchant hoeEnchant) {
        this.hoeEnchant = hoeEnchant;
    }
}
