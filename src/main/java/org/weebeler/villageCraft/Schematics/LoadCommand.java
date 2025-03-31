package org.weebeler.villageCraft.Schematics;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Admin;

public class LoadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Admin a = Main.getAdmin(((Player) commandSender).getUniqueId());
        if (a != null) {
            Schematic sc = Main.getSchematic(strings[0]);
            if (sc != null) {
                Location l = ((Player) commandSender).getLocation();
                sc.paste(l);
                commandSender.sendMessage(ChatColor.GOLD + "Pasted schematic " + sc + " at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
            }
        }
        return false;
    }
}
