package org.weebeler.villageCraft.Villagers;

import java.util.HashMap;

public class StatProfile {
    public HashMap<Stat, Double> baseStats;
    public HashMap<Stat, Double> bonusStats;
    public HashMap<Stat, Double> tempModifiers;
    public StatProfile() {
        baseStats = new HashMap<>();
        bonusStats = new HashMap<>();
        tempModifiers = new HashMap<>();

        baseStats.putAll(generateBaseStats());
    }
    public HashMap<Stat, Double> generateBaseStats() {
        HashMap<Stat, Double> generated = new HashMap<>();

        generated.put(Stat.HEALTH, 100.0);
        generated.put(Stat.POWER, 1.0);
        generated.put(Stat.MANA, 20.0);
        // future base stats go here

        return generated;
    }
    public void addBaseStat(Stat stat, double val) {
        if (baseStats.containsKey(stat)) {
            baseStats.put(stat, baseStats.get(stat) + val);
        } else {
            baseStats.put(stat, val);
        }
    }
    public void addBonusStat(Stat stat, double val) {
        if (bonusStats.containsKey(stat)) {
            bonusStats.put(stat, bonusStats.get(stat) + val);
        } else {
            bonusStats.put(stat, val);
        }
    }
    public void addTempStat(Stat stat, double val) {
        if (tempModifiers.containsKey(stat)) {
            tempModifiers.put(stat, tempModifiers.get(stat) + val);
        } else {
            tempModifiers.put(stat, val);
        }
    }
    public void subtractTempStat(Stat stat, double val) {
        if (tempModifiers.containsKey(stat)) {
            tempModifiers.put(stat, tempModifiers.get(stat) - val);
        } else {
            tempModifiers.put(stat, -1 * val);
        }
    }
    public double getVal(Stat stat) {
        double bonus = 0;
        double base = 0;
        if (bonusStats.containsKey(stat)) {
            bonus = bonusStats.get(stat);
        }
        if (baseStats.containsKey(stat)) {
            base = baseStats.get(stat);
        }
        return base + bonus;
    }
    public double getTempValue(Stat stat) {
        if (tempModifiers.containsKey(stat)) {
            return tempModifiers.get(stat);
        } else {
            return 0;
        }
    }
}
