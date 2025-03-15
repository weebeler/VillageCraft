package org.vcteam.villageCraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.vcteam.villageCraft.Enums.DeathReason;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;

/**
 * Used for messages that will be repeated a lot, i.e. permission denied messages
 *
 * @author VCTeam
 */
public class MessageSender {
    public static void permissionDenied(Player player) {
        player.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
    }
    public static void consoleCommand(Player player) {
        player.sendMessage(ChatColor.RED + "This is a console-only command!");
    }
    public static void gameCommand() {
        Main.log.info("This is a game-only command!");
    }
    public static void died(VCPlayer player, DeathReason reason) {
        String death = switch (reason) {
            case DeathReason.COMMAND -> "You died!";
            case DeathReason.MONSTER -> "You were killed by a monster!";
            default -> "You died!";
        };
        player.getPlayer().sendMessage(ChatColor.RED + death);
    }
}
