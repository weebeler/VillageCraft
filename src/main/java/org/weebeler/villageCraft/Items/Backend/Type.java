package org.weebeler.villageCraft.Items.Backend;

public enum Type {
    NOTYPE(""),
    HELMET("Helmet"),
    CHESTPLATE("Chestplate"),
    LEGGINGS("Leggings"),
    BOOTS("Boots"),
    WAND("Wand"),
    SWORD("Sword");

    public String displayName;

    private Type(String dn) {
        displayName = dn;
    }
}
