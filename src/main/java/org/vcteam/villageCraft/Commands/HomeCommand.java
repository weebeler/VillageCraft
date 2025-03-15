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

/**
 * Finds the home of a VCPlayer and then teleports them to it. If no home is found, throw FailedToFindException and create new home.
 */
public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            try {
                VCPlayer vcp = VCPlayer.find((Player) commandSender);
                String uuid = vcp.getPlayer().getUniqueId().toString();

                VCWorld world = new VCWorld(uuid);

                if (Main.containsLoadedWorld(uuid)) {
                    Main.log.info("Setting world to loaded world!");
                    world = Main.getWorldFromName(uuid);
                } else {
                    if (Main.containsUnloadedWorld(uuid)) {
                        Main.log.info("Constructing unloaded world!");
                        world = VCWorld.construct(Main.getWorldJSONFromName(uuid));
                    }  else {
                        Main.log.info("Main does not contain a world with name " + uuid + "! Creating new world...");
                        world.build();
                    }
                }
                world.load();
                world.teleport(vcp);
            } catch (FailedToFindException e) {
                if (Main.debug) e.printStackTrace();
                Main.log.info("Error probably caused by VCWorld.construct()!");
            }
        }
        if (commandSender instanceof ConsoleCommandSender) {
            MessageSender.gameCommand();
        }

        return false;
    }
}
