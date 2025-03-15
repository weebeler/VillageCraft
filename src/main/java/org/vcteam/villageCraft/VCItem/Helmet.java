package org.vcteam.villageCraft.VCItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.vcteam.villageCraft.Enums.Rarity;
import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.Enums.Type;

import java.util.Arrays;

/**
 * Generic armor piece used for testing.
 */
public class Helmet extends VCStatsItem {
    /**
     * Initializes the item.
     */
    public void init() {
        item = new ItemStack(Material.LEATHER_HELMET);
        name = "Helmet";
        id = "HELMET";
        lore = Arrays.asList(ChatColor.DARK_GRAY + "Will not prevent injuries in a head-on collision.");
        rarity = Rarity.LEGENDARY;
        type = Type.ARMOR;

        addStat(Stat.HEALTH, 100.0);

        build();
    }
}