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
import org.vcteam.villageCraft.VCPlayer.VCPlayer;

/**
 * Toggles debug mode, which enables a bunch of debugging stuff.
 *
 * @author VCTeam
 */
public class ToggleDebugModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Main.debug = !Main.debug;
        if (commandSender instanceof Player) {
            ((Player) commandSender).sendMessage(Main.debug ? (ChatColor.GREEN + "Enabled debug mode!") : (ChatColor.YELLOW + "Disabled debug mode!"));
        }
        if (commandSender instanceof ConsoleCommandSender) {
            Main.log.info(Main.debug ? "Enabled debug mode!" : "Disabled debug mode!");
        }

        return false;
    }
}
