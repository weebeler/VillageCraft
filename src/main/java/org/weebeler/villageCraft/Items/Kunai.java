package org.weebeler.villageCraft.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.weebeler.villageCraft.Items.Backend.*;
import org.weebeler.villageCraft.Villagers.Flag;
import org.weebeler.villageCraft.Villagers.Stat;

import java.util.Arrays;

public class Kunai extends GenericUUIDItem {

    public Kunai() {
        super(
                new ItemStack(Material.GHAST_TEAR),
                "Kunai",
                "KUNAI",
                Arrays.asList(
                        ChatColor.DARK_GRAY + "Made to be used very, very quickly."
                ),
                Rarity.LEGENDARY,
                Type.SWORD,
                ActiveSlot.HAND
        );
        stats.put(Stat.POWER, 8.0);
        flags.add(Flag.NOATTACKCOOLDOWN);
    }
}
