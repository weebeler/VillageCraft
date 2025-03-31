package org.weebeler.villageCraft.Monsters.Backend;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Admin;

public class VCSummonCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Admin a = Main.getAdmin(((Player) commandSender).getUniqueId());
        if (a != null) {
            ((GenericMonster) Main.getMonsterTemplate(strings[0]).clone()).spawn(((Player) commandSender).getLocation());
        }
        return false;
    }
}
