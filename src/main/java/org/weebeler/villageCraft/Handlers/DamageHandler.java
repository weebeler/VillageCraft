package org.weebeler.villageCraft.Handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;
import org.weebeler.villageCraft.Villagers.Flag;
import org.weebeler.villageCraft.Villagers.Stat;
import org.weebeler.villageCraft.Villagers.Villager;

public class DamageHandler {
    public void playerOnEntity(EntityDamageByEntityEvent e) {
        System.out.println("PlayerOnEntity!");
        GenericMonster target = Main.getAliveMonster(e.getEntity().getUniqueId());
        Villager attacker = Main.getVillager(((Player) e.getDamager()).getUniqueId());

        target.health = target.health - attacker.statProfile.getVal(Stat.POWER);
        target.update();
    }
    public void entityOnPlayer(EntityDamageByEntityEvent e) {
        System.out.println("EntityOnPlayer!");
        GenericMonster attacker = Main.getAliveMonster(e.getDamager().getUniqueId());
        Villager target = Main.getVillager(((Player) e.getEntity()).getUniqueId());

        target.statProfile.subtractTempStat(Stat.HEALTH, attacker.damage);
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
}
