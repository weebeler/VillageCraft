package org.vcteam.villageCraft.Enums;

import org.bukkit.ChatColor;

/**
 * Dictates the rarity of an item. Contains a color and a name.
 *
 * @author VCTeam
 */
public enum Rarity {
    NORARITY(ChatColor.BLACK, "NO RARITY"),
    TOKEN(ChatColor.WHITE, "TOKEN"),
    UNCOMMON(ChatColor.YELLOW, "UNCOMMON"),
    RARE(ChatColor.GREEN, "RARE"),
    EPIC(ChatColor.BLUE, "EPIC"),
    LEGENDARY(ChatColor.RED, "LEGENDARY"),
    FABLED(ChatColor.AQUA, "FABLED");

    private ChatColor color;
    private String title;

    /**
     * Private constructor assigns a color and a title
     * @param color color used when generating items of this rarity
     * @param title title used when generating lore of items of this rarity
     */
    private Rarity(ChatColor color, String title) {
        this.color = color;
        this.title = title;
    }

    /**
     * @return color to use when generating items of this rarity
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * @return what to call the rarity in descriptions
     */
    public String getTitle() {
        return title;
    }
}
