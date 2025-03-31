package org.weebeler.villageCraft.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.weebeler.villageCraft.Items.Backend.ActiveSlot;
import org.weebeler.villageCraft.Items.Backend.GenericUUIDItem;
import org.weebeler.villageCraft.Items.Backend.Rarity;
import org.weebeler.villageCraft.Items.Backend.Type;
import org.weebeler.villageCraft.Villagers.Stat;

import java.util.Arrays;

public class LeatherHelmet extends GenericUUIDItem {
    public LeatherHelmet() {
        super(
                new ItemStack(Material.LEATHER_HELMET),
                "Leather Helmet",
                "LEATHER_HELMET",
                Arrays.asList(
                        ChatColor.DARK_GRAY + "More uncomfortable than protective."
                ),
                Rarity.TOKEN,
                Type.HELMET,
                ActiveSlot.ARMOR
        );
        stats.put(Stat.HEALTH, 10.0);
    }
}
