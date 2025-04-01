package org.weebeler.villageCraft.Monsters.Backend;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.weebeler.villageCraft.Main;

import java.text.DecimalFormat;

public class GenericMonster implements Cloneable {
    public static final NamespacedKey nonmonster = new NamespacedKey(Main.getPlugin(Main.class), "nonmob");

    public EntityType model;
    public ArmorStand nameplate;
    public LivingEntity living;
    public String name;
    public String id;
    public double health;
    public double maxHealth;
    public double damage;
    public Statuses statuses;

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
        nameplate = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND, false);
        update();
        nameplate.setCustomNameVisible(true);
        nameplate.setInvisible(true);
        nameplate.setSmall(true);
        nameplate.setInvulnerable(true);
        nameplate.getPersistentDataContainer().set(nonmonster, PersistentDataType.BOOLEAN, true);

        Main.aliveMonsters.add(this);
        statuses = new Statuses(this);
        onSpawn();
    }
    public void onSpawn() {

    }
    public void update() {
        String fullN = ChatColor.WHITE + name + ChatColor.RED + (" ♥" + new DecimalFormat("#").format(Math.max(0, health)) + "/" + new DecimalFormat("#").format(maxHealth));
        if (statuses != null) {
            for (StatusType s : statuses.getActive()) {
                fullN += " " + s.addToName;
            }
        }
        nameplate.setCustomName(fullN);
        if (health < 1) {
            kill();
        }
    }
    public void kill() {
        statuses.statusHandler.cancel();
        living.damage(Integer.MAX_VALUE);
        nameplate.remove();
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
