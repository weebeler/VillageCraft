package org.weebeler.villageCraft.Items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.weebeler.villageCraft.Items.Backend.ActiveSlot;
import org.weebeler.villageCraft.Items.Backend.GenericUUIDItem;
import org.weebeler.villageCraft.Items.Backend.Rarity;
import org.weebeler.villageCraft.Items.Backend.Type;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Stat;

import java.util.Arrays;
import java.util.List;

public class RainbowPants extends GenericUUIDItem {
    public RainbowPants() {
        super(
                new ItemStack(Material.LEATHER_LEGGINGS),
                "Rainbow Pants",
                "RAINBOW_PANTS",
                Arrays.asList(
                        ChatColor.DARK_GRAY + "It's beautiful..."
                ),
                Rarity.LEGENDARY,
                Type.LEGGINGS,
                ActiveSlot.ARMOR
        );
        stats.put(Stat.HEALTH, 100.0);
        stats.put(Stat.POWER, 5.0);
    }
    @Override
    public void onGive() {
        System.out.println("RainbowPants onGive called!");
        LeatherArmorMeta meta = (LeatherArmorMeta) model.getItemMeta();
        int[] r = {1};
        int[] g = {1};
        int[] b = {1};
        BukkitRunnable changeColor = new BukkitRunnable() {
            @Override
            public void run() {
                r[0] = (r[0] + (int) (Math.random() * 20)) % 255;
                g[0] = (g[0] + (int) (Math.random() * 20)) % 255;;
                b[0] = (b[0] + (int) (Math.random() * 20)) % 255;;
                System.out.println("burp");

                meta.setColor(Color.fromRGB(r[0], g[0], b[0]));
                model.setItemMeta(meta);

            }
        };
        changeColor.runTaskTimer(Main.getPlugin(Main.class), 0, 2);
    }
}
