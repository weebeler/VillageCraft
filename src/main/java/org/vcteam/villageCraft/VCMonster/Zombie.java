package org.vcteam.villageCraft.VCMonster;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.vcteam.villageCraft.Main;

public class Zombie extends VCMonster {

    public void init() {
        type = EntityType.ZOMBIE;
        name = "Zombie";
        health = 100;
        maxHealth = 100;
        damage = 10;
        backendName = "ZOMBIE";
        built = true;
    }

    public void spawn(World world, Location location) {
        init();
        setEntity(world.spawnEntity(location, type));
        checkAlive();
        Main.addAliveMonster(this);
    }
}
