package org.weebeler.villageCraft.Villagers;

import java.util.ArrayList;
import java.util.HashMap;

public class StatProfile {
    public HashMap<Stat, Double> baseStats;
    public HashMap<Stat, Double> bonusStats;
    public HashMap<Stat, Double> tempModifiers;
    public ArrayList<Stat> minimum; // used when a stat goes under min value
    public ArrayList<Stat> maximum; // used when a stat goes over max value

    public StatProfile() {
        baseStats = new HashMap<>();
        bonusStats = new HashMap<>();
        tempModifiers = new HashMap<>();
        minimum = new ArrayList<>();
        maximum = new ArrayList<>();

        baseStats.putAll(generateBaseStats());
    }
    public HashMap<Stat, Double> generateBaseStats() {
        HashMap<Stat, Double> generated = new HashMap<>();

        generated.put(Stat.HEALTH, 100.0);
        generated.put(Stat.POWER, 1.0);
        generated.put(Stat.MANA, 20.0);
        generated.put(Stat.CRITICALCHANCE, 20.0);
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
    public double getRawVal(Stat stat) {
        double bonus = 0;
        double base = 0;
        double modified = 0;
        if (bonusStats.containsKey(stat)) {
            bonus = bonusStats.get(stat);
        }
        if (baseStats.containsKey(stat)) {
            base = baseStats.get(stat);
        }
        if (tempModifiers.containsKey(stat)) {
            modified = tempModifiers.get(stat);
        }
        return base + bonus + modified;
    }
    public double getVal(Stat stat) {
        if (minimum.contains(stat)) {
            return stat.minVal;
        }
        if (maximum.contains(stat)) {
            return stat.maxVal;
        }
        double bonus = 0;
        double base = 0;
        double modified = 0;
        if (bonusStats.containsKey(stat)) {
            bonus = bonusStats.get(stat);
        }
        if (baseStats.containsKey(stat)) {
            base = baseStats.get(stat);
        }
        if (tempModifiers.containsKey(stat)) {
            modified = tempModifiers.get(stat);
        }
        return base + bonus + modified;
    }
    public double getUnmodifiedVal(Stat stat) {
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
    public boolean isCrit() {
        int rand = (int) (Math.random() * 100);
        return getVal(Stat.CRITICALCHANCE) >= rand;

    }
    public double calcDamage(boolean isCrit) {
        double base = getVal(Stat.POWER);
        if (isCrit) {
            base = base * 2;
        }

        return base;
    }
    public double calcMagicDamage() {
        double base = getVal(Stat.ARCANE);

        return base;
    }
    public ArrayList<Stat> getAllStats() {
        ArrayList<Stat> allStats = new ArrayList<>();

        for (Stat s : bonusStats.keySet()) {
            allStats.add(s);
        }
        for (Stat s : baseStats.keySet()) {
            if (!allStats.contains(s)) {
                allStats.add(s);
            }
        }
        for (Stat s : tempModifiers.keySet()) {
            if (!allStats.contains(s)) {
                allStats.add(s);
            }
        }

        return allStats;
    }
}
