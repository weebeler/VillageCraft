package org.vcteam.villageCraft.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.MessageSender;

public class LoadSchematicCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length == 1) {
                try {

                    Main.getSchematic(strings[0]).paste();
                    commandSender.sendMessage(ChatColor.GREEN + "Pasted the " + strings[0] + " schematic!");
                } catch (FailedToFindException e) {
                    commandSender.sendMessage(ChatColor.RED + "No schematic with that name!");
                    if (Main.debug) e.printStackTrace();
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Invalid usage! Must be /loadSchematic name!");
            }
        } else {
            MessageSender.gameCommand();
        }

        return false;
    }
}
