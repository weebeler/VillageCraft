package org.vcteam.villageCraft.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Enums.Permission;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.MessageSender;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;

/**
 * Console command used to grant perms to players.
 */
public class AddPermissionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            if (strings.length == 2) {
                try {
                    VCPlayer.findFromName(strings[0]).addPermission(Permission.valueOf(strings[1]));
                    Main.log.info("Gave " + strings[0] + " the " + strings[1] + " permission!");
                } catch (FailedToFindException e) {
                    if (Main.debug) e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    Main.log.info(strings[1] + "is not a valid permission!");
                }
            }
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            MessageSender.consoleCommand(player);
        }
        return false;
    }
}
