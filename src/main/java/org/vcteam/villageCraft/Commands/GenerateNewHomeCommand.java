package org.vcteam.villageCraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.MessageSender;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;
import org.vcteam.villageCraft.VCWorld.VCWorld;

public class GenerateNewHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            try {
                VCPlayer vcp = VCPlayer.find((Player) commandSender);
                String uuid = vcp.getPlayer().getUniqueId().toString();

                VCWorld world = new VCWorld(uuid);

                world.build();
                world.load();
                world.teleport(vcp);
            } catch (FailedToFindException e) {
                if (Main.debug) e.printStackTrace();
                Main.log.info("Failed to find a player!");
            }
        }
        if (commandSender instanceof ConsoleCommandSender) {
            MessageSender.gameCommand();
        }


        return false;
    }
}
