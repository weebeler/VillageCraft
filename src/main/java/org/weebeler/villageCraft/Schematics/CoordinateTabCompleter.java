package org.weebeler.villageCraft.Schematics;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CoordinateTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        Block block = player.getTargetBlock(null, 5);

        return Arrays.asList(String.valueOf(block.getX()), String.valueOf(block.getY()), String.valueOf(block.getZ()));
    }
}
