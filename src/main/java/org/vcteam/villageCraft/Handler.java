package org.vcteam.villageCraft;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.VCItem.VCItem;
import org.vcteam.villageCraft.VCItem.VCStatsItem;
import org.vcteam.villageCraft.VCMonster.VCMonster;
import org.vcteam.villageCraft.VCPlayer.EquipmentInfo;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;
import org.vcteam.villageCraft.VCPlayer.VCStatProfile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Handles all hits between players and entities, as well as damage by arrows. Also handles player stats, mob health, etc.
 *
 * @author VCTeam
 */
public class Handler {

    /**
     * Handles regular hits from entities onto players.
     */
    public void entHitPlayer(EntityDamageByEntityEvent e) throws FailedToFindException {
        e.setDamage(0);

        VCMonster attacker = VCMonster.find(e.getDamager());
        VCPlayer target = VCPlayer.find((Player) e.getEntity());

        if (Main.debug) Main.log.info("Hit detected! Damage: " + attacker.getDamage() + " | Target's health: " + target.getStatProfile().getStatVal(Stat.HEALTH));

        target.getStatProfile().tempSubtractFromStat(Stat.HEALTH, attacker.getDamage());
    }

    /**
     * Handles regular hits from players onto entities.
     */
    public void playerHitEnt(EntityDamageByEntityEvent e) throws FailedToFindException {
        e.setDamage(0);

        VCPlayer attacker = VCPlayer.find((Player) e.getDamager());
        VCMonster target = VCMonster.find(e.getEntity());

        if (Main.debug) Main.log.info("Hit detected! Damage: " + attacker.getStatProfile().getStatVal(Stat.STRENGTH) + " | Target's health: " + target.getHealth());

        target.damage(attacker.getStatProfile().getStatVal(Stat.STRENGTH));
        target.checkAlive();
    }

    /**
     * Handles hits from arrows shot by players onto entities.
     */
    public void arrowHitEnt(EntityDamageByEntityEvent e) throws FailedToFindException {
        e.setDamage(0);

        VCPlayer attacker = VCPlayer.find((Player) ((Arrow) e.getDamager()).getShooter());
        VCMonster target = VCMonster.find(e.getEntity());

        HashMap<Stat, Double> frozen = attacker.getProjHandler().get(e.getDamager().getUniqueId().toString());

        if (Main.debug) Main.log.info("Hit detected! Damage: " + frozen.get(Stat.STRENGTH) + " | Target's health: " + target.getHealth());

        target.damage(frozen.get(Stat.STRENGTH));
        target.checkAlive();

        try {
            attacker.getProjHandler().remove(e.getDamager().getUniqueId().toString());
        } catch (FailedToFindException ex) {
            if (Main.debug) ex.printStackTrace();
            Main.log.info("Failed to find a projectile with uuid " + e.getDamager().getUniqueId().toString());
        }
    }

    /**
     * Handles hits from arrows shot by entities onto players.
     */
    public void arrowHitPlayer(EntityDamageByEntityEvent e) throws FailedToFindException {
        e.setDamage(0);

        VCMonster attacker = VCMonster.find((Entity) ((Arrow) e.getDamager()).getShooter());
        VCPlayer target = VCPlayer.find((Player) e.getEntity());

        if (Main.debug) Main.log.info("Hit detected! Damage: " + attacker.getDamage() + " | Target's health: " + target.getStatProfile().getStatVal(Stat.HEALTH));

        target.getStatProfile().tempSubtractFromStat(Stat.HEALTH, attacker.getDamage());
    }

    /**
     * Runnable which updates every tick. Handles all connected players' stats.
     */
    public void handleStats() {
        ArrayList<VCPlayer> players = Main.getConnectedPlayers();

        final int REGEN_RATE = 40; // ticks between regen procs
        final double HEALTH_REGEN_PERCENT = 0.05;
        final int[] ticks = {0};

        final HashMap<VCPlayer, ArrayList<VCItem>> saved = new HashMap<>();
        final HashMap<VCPlayer, ArrayList<VCItem>> current = new HashMap<>();

        /*
         * 0 = held
         * 1 = helmet
         * 2 = chestplate
         * 3 = leggings
         * 4 = boots
         * more to be added later ?
         */
        final HashMap<VCPlayer, ArrayList<Integer>> updatedIndexes = new HashMap<>();

        BukkitRunnable handleStats = new BukkitRunnable() {
            @Override
            public void run() {
                for (VCPlayer vcp : players) {
                    if (saved.get(vcp) == null) {
                        saved.put(vcp, new ArrayList<>());
                    }
                    if (current.get(vcp) == null) {
                        current.put(vcp, new ArrayList<>());
                    }
                    if (updatedIndexes.get(vcp) == null) {
                        updatedIndexes.put(vcp, new ArrayList<>());
                    }

                    Player player = vcp.getPlayer();
                    VCStatProfile sp = vcp.getStatProfile();
                    EquipmentInfo eq = vcp.getEquipment();

                    /*
                     * Register held item
                     */
                    VCItem held = null;
                    if (player.getInventory().getItemInMainHand().hasItemMeta()) {
                        try {
                            held = VCItem.find(player.getInventory().getItemInMainHand());
                        } catch (FailedToFindException ignored) {}
                    }
                    current.get(vcp).add(held);

                    /*
                     * Register armor
                     */
                    ItemStack[] armor = player.getEquipment().getArmorContents();
                    for (int i = 0; i < eq.getLength(); i++) {
                        if (armor[i] != null) {
                            try {
                                current.get(vcp).add(VCItem.find(armor[i]));
                            } catch (FailedToFindException ignored) {
                            }
                        } else {
                            current.get(vcp).add(null);
                        }
                    }


                    /*
                     * check for different items
                     */
                    for (int i = 0; i < current.size(); i++) {
                        if (!saved.get(vcp).isEmpty()) {
                            if (!Objects.equals(current.get(vcp).get(i), saved.get(vcp).get(i))) { // allows nulls
                                updatedIndexes.get(vcp).add(i);
                            }
                        }
                    }

                    /*
                     * add new stats, remove old stats
                     */
                    for (int i : updatedIndexes.get(vcp)) {
                        if (current.get(vcp).get(i) instanceof VCStatsItem vcsi) { // new stats
                            for (Stat st : vcsi.getStats().keySet()) {
                                vcp.getStatProfile().addToStat(st, vcsi.getStatValue(st));
                            }
                        }
                        if (saved.get(vcp).get(i) instanceof VCStatsItem vcsi) { // old stats
                            for (Stat st : vcsi.getStats().keySet()) {
                                vcp.getStatProfile().subtractFromStat(st, vcsi.getStatValue(st));
                            }
                        }
                    }

                    /*
                     * update health
                     */
                    if (sp.isModified(Stat.HEALTH)) {
                        if (sp.getStatVal(Stat.HEALTH) < sp.getUnmodifiedStatVal(Stat.HEALTH)) {
                            if (ticks[0] > REGEN_RATE) {
                                double diff = sp.getUnmodifiedStatVal(Stat.HEALTH) - sp.getStatVal(Stat.HEALTH);
                                sp.addToStat(Stat.HEALTH, Math.min(diff, sp.getUnmodifiedStatVal(Stat.HEALTH) * HEALTH_REGEN_PERCENT)); // adds either the missing health or 0.05 of max health
                            }
                        }
                    }

                    /*
                     * check if player is dead
                     */
                    if (sp.getStatVal(Stat.HEALTH) <= 0) {
                        vcp.kill();
                    }

                    /*
                     * display health
                     */
                    String health = ChatColor.RED + "Health: " + ChatColor.RED + new DecimalFormat("#").format(sp.getStatVal(Stat.HEALTH)) + "/" + ChatColor.RED + new DecimalFormat("#").format(sp.getUnmodifiedStatVal(Stat.HEALTH));
                    player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(health));

                    /*
                     * update saved as reference
                     */
                    saved.get(vcp).clear();
                    for (VCItem i : current.get(vcp)) {
                        saved.get(vcp).add(i);
                    }
                    current.get(vcp).clear();
                    updatedIndexes.get(vcp).clear();
                }

                ticks[0]++;
            }
        };
        handleStats.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
    }
}
