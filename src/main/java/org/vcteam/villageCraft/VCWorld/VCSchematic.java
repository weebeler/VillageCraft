package org.vcteam.villageCraft.VCWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.vcteam.villageCraft.Exceptions.InvalidSchematicException;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Saves a block-by-block schematic of a bounding box to paste later. Need to make it saveable with json data.
 * Used for things pasted in the exact same spot every time.
 */
public class VCSchematic {
    private HashMap<VCLocation, VCBlock> blocks;
    private World world;

    /**
     * Create a schematic out of a bounding box.
     * @param world world where the schematic is
     * @param box bounding box which encompasses the schematic
     */
    private VCSchematic(World world, BoundingBox box) {
        this.world = world;
        this.blocks = new HashMap<>();
        for (double x = box.getMinX(); x < box.getMaxX(); x++) {
            for (double y = box.getMinY(); y < box.getMaxY(); y++) {
                for (double z = box.getMinZ(); z < box.getMaxZ(); z++) {
                    Location location = new Location(world, x, y, z);
                    blocks.put(new VCLocation(location), new VCBlock(location.getBlock()));
                }
            }
        }
    }

    /**
     * Pastes the saved schematic in the exact location where it was earlier.
     */
    public void paste() {
        for (Map.Entry<VCLocation, VCBlock> entry : blocks.entrySet()) {
            Location loc = entry.getKey().convert();
            world.getBlockAt(loc).setType(entry.getValue().getMaterial());
            world.getBlockAt(loc).setBlockData(Bukkit.createBlockData(entry.getValue().data));
        }
    }

    /**
     * Creates a schematic using the two saved locations. Sets locOne and locTwo to null after use.
     * @return VCSchematic created from two saved locations.
     */
    public static VCSchematic create(VCPlayer caster) throws InvalidSchematicException {
        if (caster.savedLocOne == null || caster.savedLocTwo == null) {
            throw new InvalidSchematicException();
        }
        Location s1 = caster.savedLocOne;
        Location s2 = caster.savedLocTwo;
        BoundingBox box = new BoundingBox(s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ());
        caster.savedLocOne = null;
        caster.savedLocTwo = null;
        return new VCSchematic(caster.getPlayer().getWorld(), box);
    }

    /**
     * @return map of locations and blocks
     */
    public HashMap<VCLocation, VCBlock> getMap() {
        return blocks;
    }

    /**
     * Sets the map of locations and blocks. Used when building from JSON data.
     * @param map new map
     */
    public void setMap(HashMap<VCLocation, VCBlock> map) {
        this.blocks = map;
    }

    /**
     * Builds a VCSchematic from a VCSchematic JSON
     * @param json
     * @return
     */
    public static VCSchematic build(VCSchematicJSON json) {
        Location s1 = json.getLocations().getFirst().convert();
        Location s2 = json.getLocations().getLast().convert();
        BoundingBox box = new BoundingBox(s1.getX(), s1.getY(), s1.getZ(), s2.getX(), s2.getY(), s2.getZ());
        VCSchematic schem = new VCSchematic(Bukkit.getWorld(json.getWorldName()), box);
        HashMap<VCLocation, VCBlock> map = new HashMap<>();
        for (int i = 0; i < json.getBlocks().size(); i++) {
            map.put(json.getLocations().get(i), json.getBlocks().get(i));
        }
        schem.setMap(map);
        return schem;
    }

    /**
     * @return world of this schematic
     */
    public World getWorld() {
        return world;
    }
}
