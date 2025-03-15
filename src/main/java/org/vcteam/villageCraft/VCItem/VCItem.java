package org.vcteam.villageCraft.VCItem;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.vcteam.villageCraft.Enums.Rarity;
import org.vcteam.villageCraft.Enums.Type;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Highest level of item class. Represents all items in the game.
 *
 * @author VCTeam
 */
public class VCItem {
    protected boolean built;
    public static final NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(Main.class), "ID");

    protected ItemStack item;
    protected String name;
    protected String id;
    protected List<String> lore;
    protected Rarity rarity;
    protected Type type;

    /**
     * Constructor sets everything to default values. Use setters to assign.
     */
    public VCItem() {
        built = false;

        item = null;
        name = "No Name";
        id = "No ID";
        lore = new ArrayList<>();
        rarity = Rarity.NORARITY;
        type = Type.NOTYPE;
    }

    /**
     * Initializes this item. Call after assigning everything. Overriden by things with stats, abilities, etc.
     */
    public void build() {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, id);
        if (!lore.isEmpty()) {
            lore.add(""); // line break between lore and rarity
        }
        lore.add(rarity.getColor() + ChatColor.BOLD.toString() + rarity.getTitle() + " " + type.getTitle());
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
        if (Main.debug) Main.log.info("Init was called on a VCItem!");
    }

    /**
     * Finds a VCItem tied to an ItemStack via persistentdata. May not work. IDK
     * @param i ItemStack to check
     * @return VCItem tied to i
     * @throws FailedToFindException thrown if no item is found
     */
    public static VCItem find(ItemStack i) throws FailedToFindException {
        try {
            String iid = i.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);

            for (VCItem vc : Main.getItemTemplates()) {
                if (vc.id.equals(iid)) {
                    return vc;
                }
            }
        } catch (NullPointerException e) {
            if (Main.debug) e.printStackTrace();
        }
        throw new FailedToFindException("VCItem");
    }

    /**
     * Add this item to the list of templates
     */
    public void addTemplate() {
        if (!built) init();
        Main.addItemTemplate(this);
    }

    public void give(Player player) {
        player.getInventory().addItem(item);
    }

    /**
     * @param i ItemStack that this VCItem will be based on
     */
    public void setItem(ItemStack i) {
        item = i;
    }

    /**
     * @param n new display name of item (don't use colors)
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * @param i new id of item (used for give commands, etc)
     */
    public void setId(String i) {
        id = i;
    }

    /**
     * @param l new lore of item
     */
    public void setLore(ArrayList<String> l) {
        lore = l;
    }

    /**
     * @param r rarity of item
     */
    public void setRarity(Rarity r) {
        rarity = r;
    }

    /**
     * @param t type of item (set to NOTYPE by default)
     */
    public void setType(Type t) {
        type = t;
    }

    /**
     * @return ItemStack this VCItem is based on
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @return Display name of item
     */
    public String getName() {
        return name;
    }

    /**
     * @return Id of item used for give commands, etc
     */
    public String getId() {
        return id;
    }

    /**
     * @return lore of this item
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * @return rarity of item
     */
    public Rarity getRarity() {
        return rarity;
    }

    /**
     * @return type of item
     */
    public Type getType() {
        return type;
    }
}
