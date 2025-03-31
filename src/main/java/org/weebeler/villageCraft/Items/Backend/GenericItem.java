package org.weebeler.villageCraft.Items.Backend;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericItem implements Cloneable {
    public static final NamespacedKey idKey = new NamespacedKey(Main.getPlugin(Main.class), "ID");

    public ItemStack model;
    public String name;
    public String id;
    public List<String> lore;
    public Rarity rarity;
    public Type type;
    public HashMap<Stat, Double> stats;
    public ActiveSlot slot;

    public GenericItem(ItemStack m, String n, String i, List<String> l, Rarity r, Type t, ActiveSlot s) {
        model = m;
        name = n;
        id = i;
        lore = l;
        rarity = r;
        type = t;
        stats = new HashMap<>();
        slot = s;
    }

    public void give(Player p) {
        p.getInventory().addItem(model);
        onGive();
    }

    public GenericItem build() {
        ItemMeta meta = model.getItemMeta();
        meta.setDisplayName(rarity.color + name);

        ArrayList<String> newLore = new ArrayList<>();
        for (Map.Entry<Stat, Double> e : stats.entrySet()) {
            Stat s = e.getKey();
            double v = e.getValue();
            newLore.add(ChatColor.GRAY + "" + s + ": " + s.color + v);
        }
        newLore.addAll(lore);
        if (!newLore.isEmpty()) {
            newLore.add("");
        }
        newLore.add(rarity.color + ChatColor.BOLD.toString() + rarity.title + " " + type.displayName);

        meta.setLore(newLore);

        meta.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, id);

        model.setItemMeta(meta);

        return this;
    }

    public void onGive() {
    }

    @Override
    public Object clone() {
        try {
            return (GenericItem) super.clone();
        } catch (CloneNotSupportedException ignored) {
        }
        return null;
    }
}
