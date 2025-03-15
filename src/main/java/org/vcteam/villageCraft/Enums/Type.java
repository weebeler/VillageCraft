package org.vcteam.villageCraft.Enums;

/**
 * Dictates the type of an item. Contains a title.
 *
 * @author VCTeam
 */
public enum Type {
    NOTYPE(""),
    WEAPON("WEAPON"),
    ARMOR("ARMOR");

    private String title;

    /**
     * Private constructor assigns a title
     * @param title title of this type used in descriptions
     */
    private Type(String title) {
        this.title = title;
    }

    /**
     * @return string to use in descriptions of items of this type
     */
    public String getTitle() {
        return title;
    }
}
