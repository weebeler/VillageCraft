package org.weebeler.villageCraft.MiscCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Stat;
import org.weebeler.villageCraft.Villagers.Villager;

import java.text.DecimalFormat;

public class GetStatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Villager v = Main.getVillager(((Player) commandSender).getUniqueId());

        Stat stat = Stat.valueOf(strings[0]);

        commandSender.sendMessage(ChatColor.GRAY + "Your " + stat.displayName + " stat is currently: " + stat.color + new DecimalFormat("#.##").format(v.statProfile.getVal(stat)));

        return false;
    }
}
