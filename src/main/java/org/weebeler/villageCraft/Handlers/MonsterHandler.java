package org.weebeler.villageCraft.Handlers;

import org.bukkit.scheduler.BukkitRunnable;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;

import java.util.ArrayList;

public class MonsterHandler {
    public void handle() {

        BukkitRunnable handle = new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<GenericMonster> monsters = Main.aliveMonsters;
                for (GenericMonster g : monsters) {
                    if (g != null && !g.nameplate.isDead()) {
                        g.nameplate.teleport(g.living.getEyeLocation().subtract(0, 0.5, 0));
                        g.update();
                    }
                }
            }
        };
        handle.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
    }
}
