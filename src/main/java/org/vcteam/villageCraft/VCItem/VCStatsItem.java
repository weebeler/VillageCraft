package org.vcteam.villageCraft.VCItem;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a VCItem which has stats. These stats are granted while the item is held.
 */
public class VCStatsItem extends VCItem {
    protected HashMap<Stat, Double> stats;

    /**
     * Constructor sets everything to default values. Use setters.
     */
    public VCStatsItem() {
        super();
        stats = new HashMap<>();
    }

    /**
     * @return HashMap of stats and stat values
     */
    public HashMap<Stat, Double> getStats() {
        return stats;
    }

    /**
     * @param stat type of stat to add
     * @param val value of stat to add
     */
    public void addStat(Stat stat, double val) {
        stats.put(stat, val);
    }

    /**
     * Gets the value of a stat
     * @param stat stat to get value of
     * @return value of that stat, 0 if this item does not have that stat
     */
    public double getStatValue(Stat stat) {
        if (stats.containsKey(stat)) {
            return stats.get(stat);
        } else {
            if (Main.debug) Main.log.info("Attempted to get a stat which doesn't exist on this item!");
            return 0;
        }
    }

    /**
     * Initializes this item. Use after setting everything. Does everything the VCItem#build() does, but also adds stats to the lore.
     * Precondition: Stats is not empty
     */
    public void build() {
        assert !stats.isEmpty() : "Stats cannot be empty!";

        ItemMeta meta = getItem().getItemMeta();
        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, id);

        ArrayList<String> lore1 = new ArrayList<>();
        for (Map.Entry<Stat, Double> entry : stats.entrySet()) {
            lore1.add(ChatColor.GRAY + entry.getKey().getName() + ": " + entry.getKey().getColor() + entry.getValue());
        }
        lore1.add("");

        lore1.addAll(lore);

        lore1.add("");

        lore1.add(rarity.getColor() + ChatColor.BOLD.toString() + rarity.getTitle() + " " + type.getTitle());

        lore = lore1;
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.setDisplayName(rarity.getColor() + name);
        item.setItemMeta(meta);

        built = true;
    }

    /**
     * Generic superclass method. Overriden by all actual items. Needed for polymorphism.
     */
    public void init() {
        if (Main.debug) Main.log.info("Init was called on a VCStatsItem!");
    }
}
