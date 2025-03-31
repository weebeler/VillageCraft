package org.weebeler.villageCraft.Monsters.Backend;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.weebeler.villageCraft.Main;

import java.text.DecimalFormat;

public class GenericMonster implements Cloneable {
    public EntityType model;
    public LivingEntity living;
    public String name;
    public String id;
    public double health;
    public double maxHealth;
    public double damage;

    public GenericMonster(EntityType m, String n, String i, double h, double d) {
        model = m;
        name = n;
        id = i;
        health = h;
        maxHealth = h;
        damage = d;
    }

    public void spawn(Location loc) {
        living = (LivingEntity) loc.getWorld().spawnEntity(loc, model, false);
        update();
        Main.aliveMonsters.add(this);
        onSpawn();
    }
    public void onSpawn() {

    }
    public void update() {
        living.setCustomName(ChatColor.WHITE + name + ChatColor.RED + (" ♥" + new DecimalFormat("#").format(Math.max(0, health)) + "/" + new DecimalFormat("#").format(maxHealth)));
        if (health <= 0) {
            kill();
        }
    }
    public void kill() {
        living.damage(Integer.MAX_VALUE);
        Main.aliveMonsters.remove(this);
    }

    @Override
    public Object clone() {
        try {
            return (GenericMonster) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
