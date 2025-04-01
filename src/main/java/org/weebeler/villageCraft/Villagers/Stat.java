package org.weebeler.villageCraft.Villagers;

import org.bukkit.ChatColor;

public enum Stat {
    HEALTH(ChatColor.GREEN, "Health", 0, Integer.MAX_VALUE),
    POWER(ChatColor.RED, "Power", 0, Integer.MAX_VALUE),
    ARCANE(ChatColor.DARK_AQUA, "Arcane", 0, Integer.MAX_VALUE),
    MANA(ChatColor.LIGHT_PURPLE, "Mana", 0, Integer.MAX_VALUE),
    CRITICALCHANCE(ChatColor.DARK_BLUE, "Critical Chance", 0, 100);

    public ChatColor color;
    public String displayName;
    public int minVal;
    public int maxVal; // Integer.MAX for no

    private Stat(ChatColor c, String dn, int mi, int ma) {
        this.color = c;
        this.displayName = dn;
        minVal = mi;
        maxVal = ma;
    }
}
