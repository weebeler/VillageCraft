package org.vcteam.villageCraft.Extensions;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityCombustEvent;

public class IgniteEvent extends EntityCombustEvent {
    public IgniteEvent(Entity combustee, int duration) {
        super(combustee, duration);
    }
}
