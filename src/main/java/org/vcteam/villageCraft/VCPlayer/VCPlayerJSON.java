package org.vcteam.villageCraft.VCPlayer;

import org.vcteam.villageCraft.Enums.Permission;

import java.util.ArrayList;

/**
 * Used to convert a VCPlayer to JSON-friendly primatives.
 *
 * @author VCTeam
 */
public class VCPlayerJSON {
    private String uuid;
    private ArrayList<String> perms = new ArrayList<>();
    private String home;

    /**
     * Constructor using a VCPlayer. Everything is broken down into strings here.
     * @param player VCPlayer to convert
     */
    public VCPlayerJSON(VCPlayer player) {
        uuid = player.getPlayer().getUniqueId().toString();
        for (Permission perm : player.getPerms()) {
            perms.add(perm.name());
        }
        home = player.getHome().getName();
    }

    private VCPlayerJSON() {
        uuid = "No UUID Assigned!";
    }

    /**
     * @return string state of the UUID of this JSON file
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return list of all perms in this JSON file as strings
     */
    public ArrayList<String> getPerms() {
        return perms;
    }

    /**
     * Generates an empty VCPlayerJSON.
     * @return empty VCPlayerJSON object
     */
    public static VCPlayerJSON blank() {
        return new VCPlayerJSON();
    }

    /**
     * @return name of this player's home world.
     */
    public String getHome() {return this.home;}
}
