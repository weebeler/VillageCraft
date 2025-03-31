package org.weebeler.villageCraft.Schematics;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Admin;

public class SaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Admin a = Main.getAdmin(((Player) commandSender).getUniqueId());
        if (a != null) {
            Schematic sc = new Schematic(strings[0], a.l1.getBlockX(), a.l1.getBlockY(), a.l1.getBlockZ(), a.l2.getBlockX(), a.l2.getBlockY(), a.l2.getBlockZ(), ((Player) commandSender).getWorld());
            Main.addSchematic(sc);
            commandSender.sendMessage(ChatColor.GOLD + ("Saved your schematic from " + a.l1.getBlockX() + ", " + a.l1.getBlockY() + ", " + a.l1.getBlockZ() + " to " + a.l2.getBlockX() + ", " + a.l2.getBlockY() + ", " + a.l2.getBlockZ() + " as " + strings[0]));
        }
        return false;
    }
}
