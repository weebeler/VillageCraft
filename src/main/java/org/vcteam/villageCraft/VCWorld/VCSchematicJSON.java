package org.vcteam.villageCraft.VCWorld;

import org.bukkit.Location;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;

import java.util.ArrayList;
import java.util.Map;

public class VCSchematicJSON {
    private String worldName;
    private String name;
    private ArrayList<VCLocation> locations;
    private ArrayList<VCBlock> blocks;

    /**
     * Constructor takes apart a schematic and saves the name, locations, and blocks.
     * @param schem schematic to save
     * @throws FailedToFindException thrown if no schematic match is found in main
     */
    public VCSchematicJSON(VCSchematic schem) throws FailedToFindException {
        locations = new ArrayList<>();
        blocks = new ArrayList<>();
        name = "NONAME";
        worldName = "NONAME";
        for (Map.Entry<String, VCSchematic> entry : Main.getSchematics().entrySet()) {
            if (entry.getValue().equals(schem)) {
                name = entry.getKey();
            }
            worldName = entry.getValue().getWorld().getName();
        }
        if (name.equals("NONAME")) {
            throw new FailedToFindException("VCSchematic match");
        }
        if (worldName.equals("NONAME")) {
            throw new FailedToFindException("World match");
        }

        for (Map.Entry<VCLocation, VCBlock> entry : schem.getMap().entrySet()) {
            locations.add(entry.getKey());
            blocks.add(entry.getValue());
        }
    }

    /**
     * @return name of the VCWorld
     */
    public String getName() {
        return name;
    }

    /**
     * @return list of locations of this JSON
     */
    public ArrayList<VCLocation> getLocations() {
        return locations;
    }

    /**
     * @return list of blocks of this JSON
     */
    public ArrayList<VCBlock> getBlocks() {
        return blocks;
    }

    /**
     * @return name of world
     */
    public String getWorldName() {
        return worldName;
    }
}
