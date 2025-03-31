package org.weebeler.villageCraft.Schematics;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SBlock {
    public final String material;
    public final int x, y, z;
    public final String data;

    public SBlock(Block b) {
        material = b.getType().name();
        x = b.getX();
        y = b.getY();
        z = b.getZ();
        data = b.getBlockData().getAsString();
    }
    public Material mat() {
        return Material.getMaterial(material);
    }
}
