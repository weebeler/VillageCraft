package org.weebeler.villageCraft.Items.Backend;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.weebeler.villageCraft.Main;

import java.util.List;
import java.util.UUID;

public class GenericUUIDItem extends GenericItem {
    public static final NamespacedKey uuidKey = new NamespacedKey(Main.getPlugin(Main.class), "UUID");

    public String uuid;

    public GenericUUIDItem(ItemStack m, String n, String i, List<String> l, Rarity r, Type t, ActiveSlot s) {
        super(m, n, i, l, r, t, s);
        uuid = null;
    }

    @Override
    public void give(Player p) {
        uuid = UUID.randomUUID().toString();
        ItemMeta meta = model.getItemMeta();
        meta.getPersistentDataContainer().set(uuidKey, PersistentDataType.STRING, uuid);
        model.setItemMeta(meta);

        p.getInventory().addItem(model);
        Main.addUUIDItem(this);
        onGive();
    }

    public static GenericUUIDItem rebuild(GenericItem from, String uuid) {
        GenericUUIDItem gen = (GenericUUIDItem) from;
        gen.uuid = uuid;
        return gen;
    }
}
