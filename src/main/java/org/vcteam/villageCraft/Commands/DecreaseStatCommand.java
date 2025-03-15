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
 * Decreases a stat.
 *
 * @author VCTeam
 */
public class DecreaseStatCommand implements CommandExecutor {
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
                    commandSender.sendMessage(ChatColor.RED + "Invalid input! Must be /substat StatName Decrease!");
                    return false;
                }
                try {
                    VCPlayer player = VCPlayer.find((Player) commandSender);
                    VCStatProfile vsp = player.getStatProfile();
                    if (vsp.getMap().containsKey(stat)) {
                        vsp.subtractFromStat(stat, val);
                    } else {
                        vsp.subtractFromStat(stat, val);
                    }
                    commandSender.sendMessage(ChatColor.GREEN + "Done! Your " + stat.getName() + " stat is now: " + stat.getColor() + vsp.getStatVal(stat));
                } catch (FailedToFindException e) {
                    if (Main.debug) e.printStackTrace();
                    Main.log.info("Failed to find a player!");
                }

            } else {
                commandSender.sendMessage(ChatColor.RED + "Incorrect arguments! Must be /substat StatName Decrease!");
            }
        } else {
            MessageSender.gameCommand();
        }

        return false;
    }
}