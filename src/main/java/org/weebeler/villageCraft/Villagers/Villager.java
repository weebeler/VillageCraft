package org.weebeler.villageCraft.Villagers;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Worlds.Home;

public class Villager {
    public Player player;
    public Home home;
    public StatProfile statProfile;

    public Villager(Player p) {
        player = p;
        System.out.println("UUID: " + p.getUniqueId().toString());
        home = Home.create(Home.class, p.getUniqueId().toString());
        Main.servers.add(home);
        statProfile = new StatProfile();
    }
    public void kill() {
        ((LivingEntity) player).damage(Math.pow(1, -500));
        player.teleport(player.getWorld().getSpawnLocation());
        statProfile.tempModifiers.remove(Stat.HEALTH);
    }
}
