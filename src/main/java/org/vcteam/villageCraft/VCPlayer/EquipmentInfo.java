package org.vcteam.villageCraft.VCPlayer;

import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.VCItem.VCItem;
import org.vcteam.villageCraft.VCItem.VCStatsItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used for managing all active equipment pieces of a player.
 */
public class EquipmentInfo {
    private List<VCItem> all;
    private VCItem helmet;
    private VCItem chestplate;
    private VCItem leggings;
    private VCItem boots;

    /**
     * Constructor sets all equipment pieces to null.
     */
    public EquipmentInfo() {
        all = Arrays.asList(helmet, chestplate, leggings, boots);
        helmet = null;
        chestplate = null;
        leggings = null;
        boots = null;
    }

    /**
     * Gets the equipment piece at an index. Useful for
     * 0 = helmet | 1 = chestplate | 2 = leggings | 3 = boots
     * @param index index of equipment piece to get
     * @return equipment piece at index
     * @throws IndexOutOfBoundsException if index is too large
     */
    public VCItem getFromIndex(int index) {
        if (index < all.size() && index > 0) {
            return all.get(index);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * @return List of all equipment pieces
     */
    public List<VCItem> getEquipment() {
        return all;
    }

    /**
     * @param h new VCItem helmet
     */
    public void setHelmet(VCItem h) {
        helmet = h;
    }

    /**
     * @param c new VCItem chestplate
     */
    public void setChestplate(VCItem c) {
        chestplate = c;
    }

    /**
     * @param l new VCItem leggings
     */
    public void setLeggings(VCItem l) {
        leggings = l;
    }

    /**
     * @param b new VCItem boots
     */
    public void setBoots(VCItem b) {
        boots = b;
    }

    /**
     * @return helmet VCItem
     */
    public VCItem getHelmet() {
        return helmet;
    }

    /**
     * @return chestplate VCItem
     */
    public VCItem getChestplate() {
        return chestplate;
    }

    /**
     * @return leggings VCItem
     */
    public VCItem getLeggings() {
        return leggings;
    }

    /**
     * @return boots VCItem
     */
    public VCItem getBoots() {
        return boots;
    }

    /**
     * @return number of items in EquipmentInfo
     */
    public int getLength() {
        return all.size();
    }
}
