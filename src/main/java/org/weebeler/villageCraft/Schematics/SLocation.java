package org.weebeler.villageCraft.Schematics;

import org.bukkit.Location;
import org.bukkit.World;

public class SLocation {
    public final int x, y, z;
    public SLocation(Location loc) {
        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();
    }
    public Location convert(World w) {
        return new Location(w, x, y, z);
    }
}
