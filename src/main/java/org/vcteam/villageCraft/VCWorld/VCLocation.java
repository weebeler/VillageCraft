package org.vcteam.villageCraft.VCWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Used to make saving locations easier. Does not save rotation data.
 */
public class VCLocation {
    public final String worldName;
    public final int x, y, z;

    /**
     * Converts a location into a json-friendly VCLocation
     * @param loc location to convert
     */
    public VCLocation(Location loc) {
        worldName = loc.getWorld().getName();
        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();
    }

    /**
     * Converts a VCLocation to a location
     * @return location assembled with this VCLocation's information
     */
    public Location convert() {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }
}
