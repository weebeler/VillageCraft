package org.weebeler.villageCraft.Items;

import org.bukkit.ChatColor;

public enum Rarity {
    NORARITY(ChatColor.BLACK, "NO RARITY"),
    TOKEN(ChatColor.WHITE, "TOKEN"),
    UNCOMMON(ChatColor.YELLOW, "UNCOMMON"),
    RARE(ChatColor.GREEN, "RARE"),
    EPIC(ChatColor.BLUE, "EPIC"),
    LEGENDARY(ChatColor.RED, "LEGENDARY"),
    FABLED(ChatColor.AQUA, "FABLED");

    public ChatColor color;
    public String title;

    private Rarity(ChatColor color, String title) {
        this.color = color;
        this.title = title;
    }
}
