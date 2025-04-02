package org.weebeler.villageCraft.MiscCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Admin;
import org.weebeler.villageCraft.Villagers.Stat;
import org.weebeler.villageCraft.Villagers.Villager;

public class SetStatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Admin a = Main.getAdmin(((Player) commandSender).getUniqueId());
        if (a != null) {
            Villager v = Main.getVillager(((Player) commandSender).getUniqueId());
            Stat stat = Stat.valueOf(strings[0]);
            double amt = Double.parseDouble(strings[1]);
            v.statProfile.baseStats.put(stat, amt);
        }

        return false;
    }
}
