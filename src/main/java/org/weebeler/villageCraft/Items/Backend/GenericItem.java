package org.weebeler.villageCraft.Items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericItem {
    public static final NamespacedKey idKey = new NamespacedKey(Main.getPlugin(Main.class), "ID");

    public boolean built;

    public ItemStack model;
    public String name;
    public String id;
    public List<String> lore;
    public Rarity rarity;
    public Type type;
    public HashMap<Stat, Double> stats;

    public GenericItem(ItemStack m, String n, String i, List<String> l, Rarity r, Type t, HashMap<Stat, Double> s) {
        built = false;
        model = m;
        name = n;
        id = i;
        lore = l;
        rarity = r;
        type = t;
        stats = s;
    }

    public void build() {
        ItemMeta meta = model.getItemMeta();
        meta.setDisplayName(rarity.color + name);

        ArrayList<String> newLore = new ArrayList<>();
        for (Map.Entry<Stat, Double> e : stats.entrySet()) {
            Stat s = e.getKey();
            double v = e.getValue();
            newLore.add(s + ": " + s.color + v);
        }
        newLore.addAll(lore);
        meta.setLore(newLore);
        if (newLore.isEmpty()) meta.getLore().add("");

        meta.getLore().add(rarity.color + rarity.title + " " + type.displayName);

        extra();

        built = true;
    }

    public void extra() {

    }
}
