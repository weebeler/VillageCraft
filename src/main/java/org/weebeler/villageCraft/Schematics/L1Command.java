package org.weebeler.villageCraft.Schematics;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Admin;

public class L1Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Admin a = Main.getAdmin(((Player) commandSender).getUniqueId());
        if (a != null) {
            Location l;
            if (strings.length == 0) {
                l = ((Player) commandSender).getLocation();
            } else {
                l = new Location(((Player) commandSender).getWorld(), Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
            }
            a.l1 = l;
            ((Player) commandSender).sendMessage(ChatColor.GREEN + "Set your L1 to " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());

        }

        return false;
    }
}
