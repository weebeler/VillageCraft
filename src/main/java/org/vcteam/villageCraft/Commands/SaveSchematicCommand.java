package org.vcteam.villageCraft.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Exceptions.InvalidSchematicException;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.MessageSender;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;
import org.vcteam.villageCraft.VCWorld.VCSchematic;

public class SaveSchematicCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length == 1) {
                try {
                    Main.putSchematic(strings[0], VCSchematic.create(VCPlayer.find((Player) commandSender)));
                    commandSender.sendMessage(ChatColor.GREEN + "Saved the schematic as " + strings[0]);

                } catch (InvalidSchematicException | FailedToFindException e) {
                    if (Main.debug) e.printStackTrace();
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Invalid usage! Must be /saveSchematic name!");
            }
        } else {
            MessageSender.gameCommand();
        }

        return false;
    }
}
