package org.weebeler.villageCraft.Items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.weebeler.villageCraft.Items.Backend.ActiveSlot;
import org.weebeler.villageCraft.Items.Backend.GenericUUIDItem;
import org.weebeler.villageCraft.Items.Backend.Rarity;
import org.weebeler.villageCraft.Items.Backend.Type;
import org.weebeler.villageCraft.Villagers.Flag;
import org.weebeler.villageCraft.Villagers.Stat;

import java.util.Arrays;

public class FeatherBoots extends GenericUUIDItem {
    public FeatherBoots() {
        super(
                new ItemStack(Material.LEATHER_BOOTS),
                "Feather Boots",
                "FEATHER_BOOTS",
                Arrays.asList(
                        ChatColor.DARK_GRAY + "Do they really stop fall damage?",
                        ChatColor.DARK_GRAY + "Only one way to find out."
                ),
                Rarity.EPIC,
                Type.BOOTS,
                ActiveSlot.ARMOR
        );
        stats.put(Stat.HEALTH, 1.0);
        flags.add(Flag.NOFALLDAMAGE);
    }
    @Override
    public void onGive() {
        System.out.println("FeatherBoots onGive called!");
        LeatherArmorMeta lam = (LeatherArmorMeta) model.getItemMeta();
        lam.setColor(Color.WHITE);
        model.setItemMeta(lam);
    }

}
