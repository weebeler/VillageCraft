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
import java.util.HashMap;

public class WoodenSword extends GenericUUIDItem {
    public WoodenSword() {
        super(
                new ItemStack(Material.WOODEN_SWORD),
                "Wooden Sword",
                "WOODEN_SWORD",
                Arrays.asList(
                        ChatColor.DARK_GRAY + "The most basic of weaponry."
                ),
                Rarity.TOKEN,
                Type.SWORD,
                ActiveSlot.HAND
        );
        stats.put(Stat.POWER, 10.0);
    }
}
