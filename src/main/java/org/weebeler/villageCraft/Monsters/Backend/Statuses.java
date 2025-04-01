package org.weebeler.villageCraft.Monsters.Backend;

import org.bukkit.scheduler.BukkitRunnable;
import org.weebeler.villageCraft.Main;

import java.util.ArrayList;

public class Statuses {
    public ArrayList<Status> list;

    public GenericMonster monster;
    public BukkitRunnable statusHandler;

    public Statuses(GenericMonster m) {
        list = new ArrayList<>();
        monster = m;
        handle();
    }

    public void handle() {
        statusHandler = new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<Status> toRemove = new ArrayList<>();
                for (Status s : list) {
                    s.ticksLeft -= 10;
                    System.out.println(s.type + ": " + s.ticksLeft);
                    if (s.ticksLeft <= 0) {
                        toRemove.add(s);
                    }
                }
                for (Status s : toRemove) {
                    list.remove(s);
                }
            }
        };
        statusHandler.runTaskTimer(Main.getPlugin(Main.class), 0, 10);
    }

    public int getStacks(StatusType type) {
        int stacks = 0;
        for (Status s : list) {
            if (s.type == type) {
                stacks++;
            }
        }
        return stacks;
    }

    public ArrayList<StatusType> getActive() {
        ArrayList<StatusType> active = new ArrayList<>();
        for (Status s : list) {
            if (!active.contains(s.type)) {
                active.add(s.type);
            }
        }
        return active;
    }

    public boolean removeAll(StatusType type) {
        ArrayList<Status> toRemove = new ArrayList<>();
        for (Status s : list) {
            if (s.type == type) {
                toRemove.add(s);
            }
        }
        list.removeAll(toRemove);
        return toRemove.isEmpty();
    }
}
