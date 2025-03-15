package org.vcteam.villageCraft.VCItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.vcteam.villageCraft.Enums.Rarity;
import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.Enums.Type;

import java.util.Arrays;

/**
 * Generic sword used to test stats.
 */
public class Sword extends VCStatsItem {
    /**
     * Initializes the item.
     */
    public void init() {
        item = new ItemStack(Material.STICK);
        name = "Sword";
        id = "SWORD";
        lore = Arrays.asList(ChatColor.DARK_GRAY + "It's clearly not a sword.");
        rarity = Rarity.FABLED;
        type = Type.WEAPON;

        addStat(Stat.STRENGTH, 50.0);

        build();
    }
}
