package org.weebeler.villageCraft.Worlds;

import org.bukkit.*;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Schematics.Schematic;

public class Umbralith extends Server{
    public Umbralith(World w, String t) {
        super(w, t);
    }

    @Override
    public void pasteSchematic() {
        Schematic island = Main.getSchematic("UMBRALITH");
        island.paste(new Location(world, -31, 56, -36));
    }
}
