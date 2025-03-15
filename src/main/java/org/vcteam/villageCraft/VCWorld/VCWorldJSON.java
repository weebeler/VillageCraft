package org.vcteam.villageCraft.VCWorld;

public class VCWorldJSON {
    private String name;

    /**
     * Constructor takes a VCWorld and extracts important things
     * @param world world to save as JSON data
     */
    public VCWorldJSON(VCWorld world) {
        this.name = world.getName();
    }

    /**
     * @return Name of VCWorld
     */
    public String getName() {
        return name;
    }

}
