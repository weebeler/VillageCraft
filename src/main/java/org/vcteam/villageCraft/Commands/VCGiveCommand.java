package org.vcteam.villageCraft.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.VCItem.VCItem;

public class VCGiveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 1) {
                // item
                for (VCItem i : Main.getItemTemplates()) {
                    if (i.getId().equals(strings[0])) {
                        i.give(player);
                    }
                }
            } else if (strings.length == 2) {
                // item player
                Player target = Bukkit.getPlayer(strings[1]);

                for (VCItem i : Main.getItemTemplates()) {
                    if (i.getId().equals(strings[0])) {
                        i.give(target);
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Improper usage! Correct usage: /vcgive ITEM_NAME or /vcgive ITEM_NAME PLAYER_NAME");
            }
        } else {
            if (strings.length == 2) {
                // item player
                Player target = Bukkit.getPlayer(strings[1]);

                for (VCItem i : Main.getItemTemplates()) {
                    if (i.getId().equals(strings[0])) {
                        i.give(target);
                    }
                }
            } else {
                Main.log.info("Improper usage! Correct usage: /vcgive ITEM_NAME PLAYER_NAME");
            }
        }

        return false;
    }
}
