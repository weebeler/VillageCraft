package org.vcteam.villageCraft.VCPlayer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.vcteam.villageCraft.Enums.Permission;
import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.VCWorld.VCWorld;
import org.vcteam.villageCraft.VCWorld.VCWorldJSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class which contains everything related to the player. Profiles, permissions, achievements, etc.
 *
 * @author VCTeam
 */
public class VCPlayer {
    private Player player;
    private ArrayList<Permission> perms;
    private VCStatProfile statProfile;
    private VCProjectileHandler projHandler;
    private EquipmentInfo equipment;
    public Location savedLocOne;
    public Location savedLocTwo;
    private VCWorld home;
    private boolean cancel = false;

    /**
     * Constructor for rapidly assembled VCPlayers. Usually when a new player joins.
     * @param player Player to tie to this object
     */
    public VCPlayer(Player player) {
        this.player = player;
        this.perms = new ArrayList<>();
        this.statProfile = new VCStatProfile();
        this.projHandler = new VCProjectileHandler();
        this.equipment = new EquipmentInfo();
    }

    /**
     * Used to find the VCPlayer tied to a player via cross-checking UUIDs in the list of connected players in Main.
     * @param player player to find the match for
     * @return VCPlayer which contains the player
     * @throws FailedToFindException thrown if no match is found
     */
    public static VCPlayer find(Player player) throws FailedToFindException {
        for (VCPlayer vcp : Main.getConnectedPlayers()) {
            if (player.getUniqueId().equals(vcp.player.getUniqueId())) {
                return vcp;
            }
        }
        throw new FailedToFindException("VCPlayer");
    }

    /**
     * Used to find the VCPlayer tied to a player via cross-checking usernames in the list of connected players in Main.
     * @param name name to search for
     * @return VCPlayer which contains the player with the name given
     * @throws FailedToFindException thrown if no match is found
     */
    public static VCPlayer findFromName(String name) throws FailedToFindException {
        for (VCPlayer vcp : Main.getConnectedPlayers()) {
            if (name.equals(vcp.player.getName())) {
                return vcp;
            }
        }
        throw new FailedToFindException("VCPlayer");
    }

    /**
     * Removes a permission from a VCPlayer.
     * @param perm permission to remove
     */
    public void removePermission(Permission perm) {
        if (perms.contains(perm)) {
            perms.remove(perm);
        } else {
            if (Main.debug) Main.log.info(player.getName() + " does not have permission " + perm.name());
        }
    }

    /**
     * Add a permission to a player's permissions list
     * @param perm
     */
    public void addPermission(Permission perm) {
        if (!perms.contains(perm)) {
            perms.add(perm);
        } else {
            if (Main.debug) Main.log.info("Attempted to add a permission that a player already has!");
        }
    }

    /**
     * @return list of all permissions of this player
     */
    public ArrayList<Permission> getPerms() {
        return perms;
    }

    /**
     * Quick way to check if a VCPlayer has a certain permission.
     * @param perm permission to check for
     * @return true if the player has that permission
     */
    public boolean hasPermission(Permission perm) {
        return perms.contains(perm);
    }

    /**
     * @return spigot player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Creates a VCPlayer from a VCPlayerJSON object.
     * @param player Player to build on
     * @param json VCPlayerJSON to build the VCPlayer from
     * @return constructed VCPlayer object
     */
    public static VCPlayer construct(Player player, VCPlayerJSON json) {
        VCPlayer build = new VCPlayer(player);
        for (String perm : json.getPerms()) {
            build.addPermission(Permission.valueOf(perm));
        }
        for (VCWorldJSON j : Main.getWorldJsons()) {
            if (j.getName().equals(player.getUniqueId().toString())) {
                Main.log.info("Found a match! Attempting to construct...");
                try {
                    build.setHome(VCWorld.construct(j));
                } catch (FailedToFindException e) {
                    if (Main.debug) e.printStackTrace();
                    Main.log.info("Failed to construct a home for player " + player.getName() + "! Creating new home...");
                    build.setHome(new VCWorld(player.getUniqueId().toString()));
                }
            }
        }

        // add future json stuff here

        return build;
    }

    /**
     * @return this player's home VCWorld object
     */
    public VCWorld getHome() {
        return home;
    }

    /**
     * @return this player's projectile handler
     */
    public VCProjectileHandler getProjHandler() {
        return projHandler;
    }

    /**
     * @param world world to set as this VCPlayer's home
     */
    public void setHome(VCWorld world) {
        home = world;
    }

    /**
     * Sets the stat profile of this player.
     * @param vsp VCStatProfile to tie to this player
     */
    public void setStatProfile(VCStatProfile vsp) {
        statProfile = vsp;
    }

    /**
     * @return VCStatProfile owned by this player
     */
    public VCStatProfile getStatProfile() {
        return statProfile;
    }

    /**
     * @return EquipmentInfo tied to this player (contains armor pieces)
     */
    public EquipmentInfo getEquipment() {
        return equipment;
    }

    /**
     * Kills the player.
     */
    public void kill() {
        ((LivingEntity) player).damage(Math.pow(1, -500));
        player.teleport(player.getWorld().getSpawnLocation());
    }

    /**
     * @param ei new EquipmentInfo to tie to this player
     */
    public void setEquipment(EquipmentInfo ei) {
        equipment = ei;
    }
}
