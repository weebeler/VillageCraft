package org.weebeler.villageCraft.Items.Backend;

import org.bukkit.ChatColor;

public enum Rarity {
    NORARITY(ChatColor.BLACK, "NO RARITY"),
    TOKEN(ChatColor.WHITE, "Token"),
    UNCOMMON(ChatColor.YELLOW, "Uncommon"),
    RARE(ChatColor.GREEN, "Rare"),
    EPIC(ChatColor.BLUE, "Epic"),
    LEGENDARY(ChatColor.RED, "Legendary"),
    FABLED(ChatColor.AQUA, "Fabled");

    public ChatColor color;
    public String title;

    private Rarity(ChatColor color, String title) {
        this.color = color;
        this.title = title;
    }
}
