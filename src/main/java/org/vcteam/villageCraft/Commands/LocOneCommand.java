package org.vcteam.villageCraft.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.MessageSender;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;
import org.vcteam.villageCraft.VCWorld.VCSchematic;

public class LocOneCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            try {
                VCPlayer vcp = VCPlayer.find(player);
                vcp.savedLocOne = player.getLocation();
            } catch (FailedToFindException e) {
                if (Main.debug) e.printStackTrace();
            }
            player.sendMessage(ChatColor.GREEN + "Saved your location as position one!");
        } else {
            MessageSender.gameCommand();
        }

        return false;
    }
}
