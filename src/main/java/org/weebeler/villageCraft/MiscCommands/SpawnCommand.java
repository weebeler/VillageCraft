package org.weebeler.villageCraft.MiscCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ((Player) commandSender).teleport(Main.getServer(Main.TITLE_SPAWN).world.getSpawnLocation());

        return false;
    }
}
