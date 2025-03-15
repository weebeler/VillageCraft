package org.vcteam.villageCraft.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Enums.Permission;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.MessageSender;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;

/**
 * Function changes based on what has to be debugged at a time. For now, just used to test permissions.
 *
 * @author VCTeam
 */
public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            for (World world : Bukkit.getWorlds()) {
                if (!world.getName().equals(player.getUniqueId().toString()) && world.getEnvironment() == World.Environment.NORMAL) {
                    player.teleport(world.getSpawnLocation());
                }
            }
        } else {
            MessageSender.gameCommand();
        }
        return false;
    }
}
