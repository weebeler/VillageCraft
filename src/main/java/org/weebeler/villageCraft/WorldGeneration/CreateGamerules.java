package org.weebeler.villageCraft.WorldGeneration;

import org.bukkit.GameRule;
import org.bukkit.World;

public class CreateGamerules {
    public static void generate(World w) {
        w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        w.setGameRule(GameRule.DO_FIRE_TICK, false);
        w.setGameRule(GameRule.DO_TILE_DROPS, false);
        w.setGameRule(GameRule.DO_MOB_LOOT, false);
        w.setGameRule(GameRule.NATURAL_REGENERATION, false);
        w.setTime(1000);
    }
}
