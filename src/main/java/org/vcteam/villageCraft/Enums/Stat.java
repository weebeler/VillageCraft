package org.vcteam.villageCraft.Enums;

import org.bukkit.ChatColor;

/**
 * One enum tied to each stat. Contains a chatcolor (used for item lore) and a string (used for displaying)
 *
 * @author VCTeam
 */
public enum Stat {
    HEALTH(ChatColor.GREEN, "Health"),
    STRENGTH(ChatColor.RED, "Strength");

    ChatColor color;
    String name;

    /**
     * Private constructor assigns a ChatColor and a name to each stat
     * @param color
     * @param name
     */
    private Stat(ChatColor color, String name) {
        this.color = color;
        this.name = name;
    }

    /**
     * @return color of the stat
     */
    public ChatColor getColor() {
        return this.color;
    }

    /**
     * @return name of the stat
     */
    public String  getName() {
        return this.name;
    }

}
