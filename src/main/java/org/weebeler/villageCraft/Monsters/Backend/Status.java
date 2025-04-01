package org.weebeler.villageCraft.Monsters.Backend;

public class Status {
    public StatusType type;
    public int ticksLeft;
    public String addToName;

    public Status(StatusType t, int tl) {
        type = t;
        ticksLeft = tl;
    }
}
