package org.weebeler.villageCraft.Monsters;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class GenericMonster implements Cloneable {
    public EntityType model;
    public LivingEntity alive;
    public String name;
    public String id;
    public double health;
    public double damage;

    public GenericMonster(EntityType m, String n, String i, double h, double d) {
        model = m;
        name = n;
        id = i;
        health = h;
        damage = d;
    }

    public void spawn(Location loc) {
        
    }
}
