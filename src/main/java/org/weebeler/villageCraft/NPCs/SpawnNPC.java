package org.weebeler.villageCraft.NPCs;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.NMS.NPC;
import org.weebeler.villageCraft.Villagers.Villager;

public class SpawnNPC extends NPC {
    public SpawnNPC() {
        super("Click To Play!",  "ewogICJ0aW1lc3RhbXAiIDogMTY0MjMwMjA3NzIzNywKICAicHJvZmlsZUlkIiA6ICI0MzE4YzBjMzY2ZGU0Y2NlOTIwMzlhZTJiODQ5NWNmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJCbGFja1RoZUN1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2EzMTA1N2RjYzMzNTY5MzRhOTgxNDYyOGFmOTk4NGEzZTdmMWU0MTA5NzA3OTVkYWFmMzhjZTkwNzZjMDM3NjAiCiAgICB9CiAgfQp9", "vd/gfg6lNzPbeUydXPQAItR5oBGqubPY/HmL8uVz4kaSuQ0G3Ryka4e+8pY3fVLZGtOu6sPdr2dLlaz4Zi1QAQQetn88lILTeuvpNcU/xhpzkyXHw0GcLtymUIOXOFuKi5HK/zXGYOO4Tbry5fhhKvs+Pn2pvIJQ71zUNlR8NJdRQ2vWUzxnKxcMpxeqJCD2scicYFWTsa4Z6Mz6zpu66i3GXIQ2AeMg2yFYcpCKtpwsdrkri23cITyQ+NRrMvZxfkj9ifpsBq8oVM45DQHepbC4q9HNSntyTBlLUhfoSc7FltacpGQ5sJv414a7y8ki5w4gKQ86XdXzY9KQS8wRrOlVyLJL3wi8ufdmficJbsvR0m9KJslQ/92Zz6xVc46PdTo4ovbfYRvgBCdC83roqYyjhfgEB3sO1zZtGQoAnM4Rdn6ynzRtnfAulBNKh50gESO08IA6LKwiBc8dqy3gFhvO3C5iK0epbcK9YZQS3xWk1EQ+gAMu5Qsng3/fZeyr8AfokDd08W/WTsYaSaUB6/wqH5jzVsJ3vwUQw7Kud9+QXMsxTJ298cvJP5BvOrv1waqmq6Y7ZDTmwvDgpGWMyUdEq5RCB5+dX3+y/1nS0qzaMETAoSPoDKKwuJO+iOmkwAWbkzuyi1849MsIRsjWYJ4EPGnL4nrCYnmXn3V7FHg=");
    }

    public SpawnNPC(String n, GameProfile gp, Location l, Player p) { // used by NPC.create
        super(n, gp, l, p);
    }

    @Override
    public void createLocation() {
        location = new Location(Main.getServer(Main.TITLE_SPAWN).world, 0.5, 64, 17.5);
    }

    @Override
    public void onClick(ServerPlayer player) {
        System.out.println("Correct onClick was called!");
        Villager villager = Main.getVillager(player.getUUID());
        Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> {
            villager.player.teleport(villager.home.world.getSpawnLocation());
        });
    }
}
