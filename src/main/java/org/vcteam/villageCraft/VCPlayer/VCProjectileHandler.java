package org.vcteam.villageCraft.VCPlayer;

import org.vcteam.villageCraft.Enums.Stat;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;

import java.util.HashMap;

/**
 * Used to handle projectile damage. Saves the player's stats at time of firing against the UUID of the projectile fired.
 *
 * @author VCTeam
 */
public class VCProjectileHandler {
    private HashMap<String, HashMap<Stat, Double>> projStatsMap;

    /**
     * Constructor just initializes the map.
     */
    public VCProjectileHandler() {
        projStatsMap = new HashMap<>();
    }

    /**
     * Add a pair of UUID/Stat profile to map
     * @param uuid UUID of projectile
     * @param freeze frozen stat profile hashmap (use StatProfile.freeze) of player who fired projectile
     */
    public void add(String uuid, HashMap<Stat, Double> freeze) {
        projStatsMap.put(uuid, freeze);
    }

    /**
     * Returns the VCStatProfile tied to a projectile's UUID
     * @param uuid UUID to search for
     * @return Hashmap version of statprofile frozen at time of projectile's firing
     * @throws FailedToFindException if no pair is present with that UUID
     */
    public HashMap<Stat, Double> get(String uuid) throws FailedToFindException {
        if (projStatsMap.containsKey(uuid)) {
            return projStatsMap.get(uuid);
        }
        throw new FailedToFindException("Projectile");
    }

    public void remove(String uuid) throws FailedToFindException {
        if (projStatsMap.containsKey(uuid)) {
            projStatsMap.remove(uuid);
        } else {
            throw new FailedToFindException("Projectile");
        }
    }
}
