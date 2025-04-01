package org.weebeler.villageCraft.Monsters;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;

public class Goop extends GenericMonster {
    public Goop() {
        super(
                EntityType.SLIME,
                "Goop",
                "GOOP",
                2500,
                45
        );
    }
    @Override
    public void onSpawn() {
        ((Slime) living).setSize(7);
    }
}
