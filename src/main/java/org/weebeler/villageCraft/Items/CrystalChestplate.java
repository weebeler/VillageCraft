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

public class CrystalChestplate extends GenericUUIDItem {
    public CrystalChestplate() {
        super(
                new ItemStack(Material.DIAMOND_CHESTPLATE),
                "Crystal Chestplate",
                "CRYSTAL_CHESTPLATE",
                Arrays.asList(
                        ChatColor.DARK_GRAY + "\"Shine Bright Like A Diamond\" - Queen Riri"
                ),
                Rarity.RARE,
                Type.CHESTPLATE,
                ActiveSlot.ARMOR
        );
        stats.put(Stat.HEALTH, 60.0);
    }
}
