package org.weebeler.villageCraft.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;
import org.weebeler.villageCraft.Villagers.Flag;
import org.weebeler.villageCraft.Villagers.Stat;
import org.weebeler.villageCraft.Villagers.Villager;

public class DamageHandler {
    public boolean playerOnEntity(EntityDamageByEntityEvent e) {
        GenericMonster target = Main.getAliveMonster(e.getEntity().getUniqueId());
        Villager attacker = Main.getVillager(e.getDamager().getUniqueId());

        int ticks = 10; // half-second
        if (attacker.flags.map.containsKey(Flag.NOATTACKCOOLDOWN)) {
            ticks = 20;
        }
        if (((LivingEntity) e.getEntity()).getNoDamageTicks() <= ticks) {
            boolean isCrit = attacker.statProfile.isCrit();
            double dmg = attacker.statProfile.calcDamage(isCrit);
            target.health = target.health - dmg;

            ChatColor damageColor = ChatColor.GRAY;
            if (isCrit) {
                damageColor = ChatColor.YELLOW;
            }
            displayDamage((Player) e.getDamager(), e.getEntity(), damageColor, dmg);
            return true;
        } else {
            e.setCancelled(true);
            return false;
        }
    }
    public void entityOnPlayer(EntityDamageByEntityEvent e) {
        GenericMonster attacker = Main.getAliveMonster(e.getDamager().getUniqueId());
        Villager target = Main.getVillager(e.getEntity().getUniqueId());

        target.statProfile.subtractTempStat(Stat.HEALTH, attacker.damage);
    }
    public boolean magicOnEntity(Entity target, Player attacker) {
        Villager v = Main.getVillager(attacker.getUniqueId());
        GenericMonster m = Main.getAliveMonster(target.getUniqueId());

        int ticks = 10; // half-second
        if (v.flags.map.containsKey(Flag.NOATTACKCOOLDOWN)) {
            ticks = 20;
        }
        if (((LivingEntity) target).getNoDamageTicks() <= ticks) {
            double dmg = v.statProfile.calcMagicDamage();
            m.health = m.health - dmg;
            displayDamage(attacker, target, ChatColor.AQUA, dmg);

            ((LivingEntity) target).damage(Math.pow(1, -500));
            return true;
        } else {
            return false;
        }
    }
    public void miscPlayer(EntityDamageEvent e) {
        Villager v = Main.getVillager(e.getEntity().getUniqueId());

        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && v.flags.map.containsKey(Flag.NOFALLDAMAGE)) {
            e.setCancelled(true);
            return;
        }
        // add more flags here

        v.statProfile.subtractTempStat(Stat.HEALTH, e.getDamage() * 5);
    }
    public void displayDamage(Player hitter, Entity target, ChatColor color, double dmg) {
        double hdev = Math.random() - 0.5 * 2;
        double vdev = Math.random() * 1.4;

        Location display = target.getLocation().clone().add(new Vector(hdev, vdev + 1, hdev));

        TextDisplay temp = (TextDisplay) display.getWorld().spawnEntity(display, EntityType.TEXT_DISPLAY);
        temp.setText(color + "" + dmg);
        temp.setVisibleByDefault(true);
        temp.getPersistentDataContainer().set(GenericMonster.nonmonster, PersistentDataType.BOOLEAN, true);

        BukkitRunnable rotate = new BukkitRunnable() {
            @Override
            public void run() {
                facePlayer(temp, hitter);
            }
        };
        rotate.runTaskTimer(Main.getPlugin(Main.class), 0, 1);

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            rotate.cancel();
            temp.remove();
        }, 20L);
    }
    public void facePlayer(TextDisplay display, Player e) {
        Location loc = display.getLocation();
        Location eloc = e.getEyeLocation();

        double dx = eloc.getX() - loc.getX();
        double dy = eloc.getY() - loc.getY();
        double dz = eloc.getZ() - loc.getZ();

        double yaw = Math.toDegrees(Math.atan2(-dx, dz));

        double pitch = Math.toDegrees(Math.atan2(-dy, Math.sqrt(dx * dx + dz * dz)));

        loc.setYaw((float) yaw);
        loc.setPitch((float) pitch);

        display.setRotation(loc.getYaw(), loc.getPitch());
    }
}
