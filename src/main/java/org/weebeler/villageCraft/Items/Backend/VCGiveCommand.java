package org.weebeler.villageCraft.Items.Backend;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Admin;

public class VCGiveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Admin a = Main.getAdmin(((Player) commandSender).getUniqueId());
        if (a != null) {
            ((GenericItem) Main.getItem(strings[0]).clone()).build().give(((Player) commandSender));
        }
        return false;
    }
}
