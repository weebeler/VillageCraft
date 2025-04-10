package org.weebeler.villageCraft.Worlds;

import org.bukkit.*;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.NPCs.SpawnNPC;
import org.weebeler.villageCraft.Schematics.Schematic;


public class Spawn extends Server {

    public Spawn(World w, String t) {
        super(w, t);
        npcs.add(new SpawnNPC());
        System.out.println("Added dummy SpawnNPC to npcs!");
    }

    @Override
    public void pasteSchematic() {
        Schematic island = Main.getSchematic("SPAWN");
        island.paste(new Location(world, -1, 63, -1));
    }
}
