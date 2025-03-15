package org.vcteam.villageCraft.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.VCMonster.VCMonster;

/**
 * Used to summon a mob. Works both in-game and through a console.
 *
 * @author VCTeam
 */
public class VCSummonCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 1) {
                // mob
                for (VCMonster m : Main.getMonsterTemplates()) {
                    if (m.getBackendName().equals(strings[0])) {
                        m.spawn(player.getWorld(), player.getLocation());
                    }
                }
            } else if (strings.length == 4) {
                // mob x y z
                for (VCMonster m : Main.getMonsterTemplates()) {
                    if (m.getBackendName().equals(strings[0])) {
                        try {
                            double x = Double.parseDouble(strings[1]);
                            double y = Double.parseDouble(strings[2]);
                            double z = Double.parseDouble(strings[3]);

                            m.spawn(player.getWorld(), new Location(player.getWorld(), x, y, z));
                        } catch (NumberFormatException ex) {
                            if (Main.debug) ex.printStackTrace();
                            player.sendMessage(ChatColor.RED + "Something went wrong! Make sure your coordinates are correct.");
                        }
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Improper usage! Correct usage: /vcsummon MOB_NAME");
            }
        } else {
            if (strings.length == 2) {
                // mob player
                Player target = Bukkit.getPlayer(strings[1]);

                for (VCMonster m : Main.getMonsterTemplates()) {
                    if (m.getBackendName().equals(strings[0])) {
                        m.spawn(target.getWorld(), target.getLocation());
                    }
                }

            } else if (strings.length == 5) {
                // mob world x y z
                World world = Bukkit.getWorld(strings[1]);

                double x = Double.parseDouble(strings[2]);
                double y = Double.parseDouble(strings[3]);
                double z = Double.parseDouble(strings[4]);

                Location loc = new Location(world, x, y, z);

                for (VCMonster m : Main.getMonsterTemplates()) {
                    if (m.getBackendName().equals(strings[0])) {
                        m.spawn(world, loc);
                    }
                }
            } else {
                Main.log.info("Improper usage! Correct usage: /vcsummon MOB_NAME PLAYER_NAME or /vcsummon MOB_NAME WORLD_NAME x y z");
            }
        }

        return false;
    }
}
