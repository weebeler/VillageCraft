package org.vcteam.villageCraft.VCPlayer;

import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.Main;

import java.util.HashMap;
import java.util.Map;

/**
 * Class which handles the stats of this player. Only contains stats that the player has.
 */
public class VCStatProfile {
    private HashMap<Stat, Double> stats;
    private HashMap<Stat, Double> modifiedStats; // i.e. health that's less than max health; subtracts, so adding a positive lowers total amt

    /**
     * Constructor for a new stat profile. Uses default stat values.
     */
    public VCStatProfile() {
        stats = new HashMap<>();
        modifiedStats = new HashMap<>();
        addDefaultValues();
    }

    /**
     * Constructor for a VCStatProfile which already has predefined stats. Used for returning players.
     * @param presets
     */
    public VCStatProfile(HashMap<Stat, Double> presets) {
        stats = (HashMap<Stat, Double>) presets.clone();
    }

    /**
     * Checks if a stat is currently modified.
     * @param stat stat to check
     * @return whether or not the stat is currently modified
     */
    public boolean isModified(Stat stat) {
        return modifiedStats.containsKey(stat);
    }

    /**
     * Manages the base stats a new player gets upon joining. Only called in the constructor.
     */
    private void addDefaultValues() {
        stats.put(Stat.HEALTH, 100.0);
        stats.put(Stat.STRENGTH, 25.0);
    }

    /**
     * Add a stat to the stats map. Use when the stat is not currently present.
     * @param stat stat to add
     * @param amt value of stat
     */
    public void addNewStat(Stat stat, double amt) {
        stats.put(stat, amt);
    }

    /**
     * Add to a stat already present in the stats map.
     * @param stat stat to add to
     * @param amt amount to add
     */
    public void addToStat(Stat stat, double amt) {
        stats.put(stat, stats.get(stat) + amt);
    }

    /**
     * Subtact from a stat already present in the stats map.
     * @param stat stat to subtract from
     * @param amt amount to remove
     */
    public void subtractFromStat(Stat stat, double amt) {
        stats.put(stat, stats.get(stat) - amt);
    }

    /**
     * Subtract temporarily from a stat (i.e. health when taking damage)
     * @param stat stat to temporarily reduce
     * @param amt amount to reduce
     */
    public void tempSubtractFromStat(Stat stat, double amt) {
        if (modifiedStats.containsKey(stat)) {
            modifiedStats.put(stat, stats.get(stat) - modifiedStats.get(stat) - amt);
        } else {
            modifiedStats.put(stat, stats.get(stat) - amt);
        }
        if (modifiedStats.get(stat).equals(stats.get(stat))) { // remove if modified stats is the same as base stat
            modifiedStats.remove(stat);
        }
    }

    /**
     * Add temporarily to a stat (i.e. timed buff)
     * @param stat stat to temporarily increase
     * @param amt amount to increase
     */
    public void tempAddToStat(Stat stat, double amt) {
        if (modifiedStats.containsKey(stat)) {
            modifiedStats.put(stat, stats.get(stat) - modifiedStats.get(stat) + amt);
        } else {
            modifiedStats.put(stat, stats.get(stat) + amt);
        }
        if (modifiedStats.get(stat).equals(stats.get(stat))) { // remove if modified stats is the same as base stat
            modifiedStats.remove(stat);
        }
    }

    /**
     * Saves a player's VCStatprofile at a time.
     * @return HashMap of frozen stats.
     */
    public HashMap<Stat, Double> freeze() {
        HashMap<Stat, Double> frozen = new HashMap<>();
        frozen.putAll(stats);
        for (Map.Entry<Stat, Double> entry : modifiedStats.entrySet()) {
            frozen.put(entry.getKey(), frozen.get(entry.getKey()) - entry.getValue());
        }

        return frozen;
    }

    /**
     * @return HashMap of all stats
     */
    public HashMap<Stat, Double> getMap() {
        return stats;
    }

    /**
     * Get the active value from a stat.
     * @param stat stat to find value of
     * @return value of the given stat
     */
    public double getStatVal(Stat stat) {
        if (stats.containsKey(stat)) {
            if (modifiedStats.containsKey(stat)) {
                return stats.get(stat) - modifiedStats.get(stat);
            }
            return stats.get(stat);
        } else {
            if (Main.debug) Main.log.info("Attempted to get a stat that isn't in this statprofile!");
            return 0;
        }
    }

    /**
     * Gets the unmodified value from a stat.
     * @param stat stat to find unmodified value of
     * @return value of the given stat without modifiers
     */
    public double getUnmodifiedStatVal(Stat stat) {
        if (stats.containsKey(stat)) {
            return stats.get(stat);
        } else {
            if (Main.debug) Main.log.info("Attempted to get a stat that isn't in this statprofile!");
            return 0;
        }
    }

    /**
     * Gets if this stat profile contains a stat
     * @param stat stat to search for
     * @return true if this stat profile contains that stat
     */
    public boolean containsStat(Stat stat) {
        return stats.containsKey(stat);
    }

}
