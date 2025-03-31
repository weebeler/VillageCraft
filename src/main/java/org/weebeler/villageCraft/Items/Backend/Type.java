package org.weebeler.villageCraft.Items;

public enum Type {
    NOTYPE(""),
    HELMET("Helmet"),
    CHESTPLATE("Chestplate"),
    LEGGINGS("Leggings"),
    BOOTS("Boots"),
    SWORD("Sword");

    public String displayName;

    private Type(String dn) {
        displayName = dn;
    }
}
