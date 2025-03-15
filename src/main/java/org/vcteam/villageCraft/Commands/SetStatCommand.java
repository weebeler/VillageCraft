package org.vcteam.villageCraft.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.MessageSender;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;
import org.vcteam.villageCraft.VCPlayer.VCStatProfile;

/**
 * Sets a stat.
 *
 * @author VCTeam
 */
public class SetStatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            if (strings.length == 2) {
                Stat stat;
                double val;
                try {
                    stat = Stat.valueOf(strings[0]);
                    val = Double.parseDouble(strings[1]);
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage(ChatColor.RED + "Invalid input! Must be /setstat StatName Value!");
                    return false;
                }
                try {
                    VCPlayer player = VCPlayer.find((Player) commandSender);
                    VCStatProfile vsp = player.getStatProfile();
                    vsp.addNewStat(stat, val);
                    commandSender.sendMessage(ChatColor.GREEN + "Done! Your " + stat.getName() + " stat is now: " + stat.getColor() + vsp.getStatVal(stat));
                } catch (FailedToFindException e) {
                    if (Main.debug) e.printStackTrace();
                    Main.log.info("Failed to find a player!");
                }

            } else {
                commandSender.sendMessage(ChatColor.RED + "Incorrect arguments! Must be /setstat StatName Value!");
            }
        } else {
            MessageSender.gameCommand();
        }

        return false;
    }
}
