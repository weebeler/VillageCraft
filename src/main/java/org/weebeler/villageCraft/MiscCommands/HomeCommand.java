package org.weebeler.villageCraft.Villagers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Villager v = Main.getVillager(((Player) commandSender).getUniqueId());

        ((Player) commandSender).teleport(v.home.world.getSpawnLocation());

        return false;
    }
}
