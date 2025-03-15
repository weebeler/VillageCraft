package org.vcteam.villageCraft.VCWorld;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Used to make saving block json data easier.
 */
public class VCBlock {
    public final String material;
    public final int x, y, z;
    public final String data;

    /**
     * Constructor converts a block to material, coordinates, and rotation if applicable
     * @param block block to convert
     */
    public VCBlock(Block block) {
        material = block.getType().name();
        x = block.getX();
        y = block.getY();
        z = block.getZ();
        data = block.getBlockData().getAsString();

    }

    /**
     * @return Material type
     */
    public Material getMaterial() {
        return Material.getMaterial(material);
    }
}
