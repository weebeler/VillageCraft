package org.weebeler.villageCraft.Worlds;

import org.bukkit.*;
import org.bukkit.util.BoundingBox;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Schematics.Schematic;

public class Home extends Server {
    public Server portalTarget;
    public BoundingBox portalLocation;

    public Home(World world, String uuid) {
        super(world, uuid);
        portalLocation = new BoundingBox(1, 64, -12, -1, 68, -14);
    }

    @Override
    public void pasteSchematic() {
        Schematic island = Main.getSchematic("ISLAND");
        island.paste(new Location(world, -16, 62, -14));
        portalTarget = Main.getServer(Main.TITLE_UMBRALITH);
    }
}
