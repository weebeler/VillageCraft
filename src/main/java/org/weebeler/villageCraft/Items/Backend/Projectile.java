package org.weebeler.villageCraft.Items.Backend;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;

import java.util.Collection;

public class Projectile {
    public GenericItem from;
    public Player shooter;
    public Entity projectile;
    public Slime moving;
    public Vector direction;
    public Location location;
    public boolean gravity;
    World world;

    public Projectile(GenericItem f, Player sh, Entity p, Vector d, double s, Location l, boolean g) {
        from = f;
        shooter = sh;
        projectile = p;
        direction = d.clone().normalize().multiply(s / 4);
        location = l;
        gravity = g;

        world = l.getWorld();
    }

    public void shoot() {
        projectile.getPersistentDataContainer().set(GenericMonster.nonmonster, PersistentDataType.BOOLEAN, true);

        moving = (Slime) world.spawnEntity(new Location(world, 0, 1000, 0), EntityType.SLIME);
        moving.setSize(1);
        moving.setGravity(gravity);
        moving.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 255, false, false, false));
        moving.setCollidable(false);
        moving.getPersistentDataContainer().set(GenericMonster.nonmonster, PersistentDataType.BOOLEAN, true);

        moving.teleport(location);
        moving.addPassenger(projectile);

        int[] ticksAlive = {0};

        BukkitRunnable move = new BukkitRunnable() {
            @Override
            public void run() {
                if (moving.isDead() || projectile.isDead() || ticksAlive[0] > 100 || moving.getWorld().getBlockAt(moving.getLocation().add(direction.clone().multiply(0.2))).getType() != Material.AIR) {
                    moving.remove();
                    projectile.remove();
                    cancel();
                }
                moving.setVelocity(direction);
                Collection<Entity> nearby = projectile.getWorld().getNearbyEntities(projectile.getLocation(), 0.8, 0.8, 0.8);
                nearby.removeIf(n -> n instanceof Player);
                nearby.removeIf(n -> n.getPersistentDataContainer().has(GenericMonster.nonmonster));
                if (!nearby.isEmpty()) {
                    moving.remove();
                    projectile.remove();
                    for (Entity e : nearby) {
                        from.onProjectileHit(shooter, e);
                    }
                    cancel();
                }
                ticksAlive[0]++;
            }
        };
        move.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
    }
}
