package org.weebeler.villageCraft.Items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Stat;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GenericUUIDItem extends GenericItem {
    public static final NamespacedKey uuidKey = new NamespacedKey(Main.getPlugin(Main.class), "UUID");

    public String uuid;

    public GenericUUIDItem(ItemStack m, String n, String i, List<String> l, Rarity r, Type t, HashMap<Stat, Double> s) {
        super(m, n, i, l, r, t, s);
        uuid = null;
    }

    @Override
    public void extra() {
        uuid = UUID.randomUUID().toString();
        model.getItemMeta().getPersistentDataContainer().set(uuidKey, PersistentDataType.STRING, uuid);
    }
}
