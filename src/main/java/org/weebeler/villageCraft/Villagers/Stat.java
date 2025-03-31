package org.weebeler.villageCraft.Villagers;

import org.bukkit.ChatColor;

public enum Stat {
    HEALTH(ChatColor.GREEN, "Health"),
    POWER(ChatColor.RED, "Power"),
    MANA(ChatColor.LIGHT_PURPLE, "Mana");

    public ChatColor color;
    public String displayName;

    private Stat(ChatColor c, String dn) {
        this.color = c;
        this.displayName = dn;
    }
}
