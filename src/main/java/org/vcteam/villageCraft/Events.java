package org.vcteam.villageCraft;

import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.vcteam.villageCraft.Enums.Permission;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Extensions.IgniteEvent;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;
import org.vcteam.villageCraft.VCPlayer.VCPlayerJSON;
import org.vcteam.villageCraft.VCPlayer.VCStatProfile;

import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * Main eventlistener. Very rarely handles things here, usually passes events to different classes.
 *
 * @author VCTeam
 */
public class Events implements Listener {

    /**
     * Adds a player to the connected players list on join.
     */
    @EventHandler
    public void registerNewPlayer(PlayerJoinEvent e) {
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(Main.getPlayerJson());

            VCPlayerJSON[] data = gson.fromJson(reader, VCPlayerJSON[].class);
            VCPlayerJSON playerJson = VCPlayerJSON.blank();

            for (VCPlayerJSON vcpj : data) {
                if (vcpj.getUuid().equals(e.getPlayer().getUniqueId().toString())) {
                    playerJson = vcpj;
                }
            }

            VCPlayer vcp = VCPlayer.construct(e.getPlayer(), playerJson);
            vcp.setStatProfile(new VCStatProfile()); // <---- change this when you add saving VCStatProfiles
            Main.addConnectedPlayer(vcp);
            vcp.addPermission(Permission.DEFAULT);

        } catch (Exception exc) {
            Main.log.info("Something went wrong loading a player! Creating new player...");
            if (Main.debug) exc.printStackTrace();
            VCPlayer vcp = new VCPlayer(e.getPlayer());
            Main.addConnectedPlayer(vcp);
        }
    }

    /**
     * Removes a player from the connected players list on leave and saves their data.
     */
    @EventHandler
    public void removeOldPlayer(PlayerQuitEvent e) {
        try {
            VCPlayer vcp = VCPlayer.find(e.getPlayer());
            Main.addPlayerJson(new VCPlayerJSON(vcp));
            Main.savePlayerJsonData();
            Main.removeConnectedPlayer(vcp);
        } catch (FailedToFindException exc) {
            if (Main.debug) exc.printStackTrace();
        }
    }

    /**
     * Ensure player has the correct permissions before executing a command.
     */
    @EventHandler
    public void validateCommandPermissions(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().split(" ")[0];
        command = command.replaceAll("/", ""); // idk if the string contains the slash

        Permission required = Main.getCommandPerms().get(command);

        try {
            VCPlayer player = VCPlayer.find(e.getPlayer());

            if (player.hasPermission(Permission.OP) || player.hasPermission(required)) {
                if (Main.debug) Main.log.info(e.getPlayer().getDisplayName() + " sent command /" + command + "!");
            } else {
                MessageSender.permissionDenied(e.getPlayer());
                e.setCancelled(true);

            }
        } catch (FailedToFindException ex) {
            e.getPlayer().sendMessage(ChatColor.RED + "Something went wrong while executing this command.");
            e.setCancelled(true);
            if (Main.debug) ex.printStackTrace();
        }
    }

    /**
     * Handles hits by players, entities, and arrows
     */
    @EventHandler
    public void handleHits(EntityDamageByEntityEvent e) {
        if (((LivingEntity) e.getEntity()).getNoDamageTicks() <= 20) {
            try {
                if (e.getDamager() instanceof Player) {
                    Main.handler.playerHitEnt(e);
                }
                if (e.getDamager() instanceof Arrow && !(e.getEntity() instanceof Player)) {
                    Main.handler.arrowHitEnt(e);
                }
                if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) {
                    Main.handler.entHitPlayer(e);
                }
                if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
                    Main.handler.arrowHitPlayer(e);
                }
            } catch (FailedToFindException ex) {
                if (Main.debug) ex.printStackTrace();
                Main.log.info("Failed to find something during a HandleHits!");
            }
        } else {
            if (Main.debug) Main.log.info("Prevented a spam hit!");
            e.setCancelled(true);
        }
    }

    /**
     * Generates a frozen StatProfile and connects it to the UUID of a player's fired projectile.
     */
    @EventHandler
    public void freezeOnProjectile(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            try {
                VCPlayer kingvon = VCPlayer.find((Player) e.getEntity().getShooter());
                kingvon.getProjHandler().add(e.getEntity().getUniqueId().toString(), kingvon.getStatProfile().freeze());
            } catch (FailedToFindException ex) {
                if (Main.debug) ex.printStackTrace();
                Main.log.info("Failed to find a VCPlayer when firing projectile!");
            }
        }
    }

    /**
     * Cancels all combust events to prevent mobs burning. Use IgniteEvent to bypass.
     */
    @EventHandler
    public void preventZombieBurning(EntityCombustEvent e) {
        if (!(e instanceof IgniteEvent)) {
            e.setCancelled(true);
        }
    }
}
