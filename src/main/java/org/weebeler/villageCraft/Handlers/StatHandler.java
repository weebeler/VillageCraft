package org.weebeler.villageCraft.Handlers;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.weebeler.villageCraft.Items.Backend.ActiveSlot;
import org.weebeler.villageCraft.Items.Backend.GenericItem;
import org.weebeler.villageCraft.Items.Backend.GenericUUIDItem;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Villagers.Stat;
import org.weebeler.villageCraft.Villagers.StatProfile;
import org.weebeler.villageCraft.Villagers.Villager;

import java.text.DecimalFormat;
import java.util.*;

public class StatHandler {
    public void handleStats() {
        ArrayList<Villager> players = Main.villagers;

        final int REGEN_RATE = 40;
        final double HEALTH_REGEN_PERCENT = 0.05; // 1 = 100%

        HashMap<Villager, ArrayList<GenericItem>> saved = new HashMap<>();
        HashMap<Villager, ArrayList<GenericItem>> current = new HashMap<>();
        HashMap<Villager, Integer> regenTicks = new HashMap<>();

        HashMap<Villager, ArrayList<Integer>> updated = new HashMap<>();


        BukkitRunnable handleStats = new BukkitRunnable() {
            @Override
            public void run() {
                for (Villager v : players) {
                    if (saved.get(v) == null) {
                        saved.put(v, new ArrayList<>(Arrays.asList(null, null, null, null, null)));
                    }
                    if (current.get(v) == null) {
                        current.put(v, new ArrayList<>());
                    }
                    if (updated.get(v) == null) {
                        updated.put(v, new ArrayList<>());
                    }
                    if (regenTicks.get(v) == null) {
                        regenTicks.put(v, 0);
                    }
                    int ticks = regenTicks.get(v);
                    Player p = v.player;
                    StatProfile sp = v.statProfile;

                    // register held item
                    GenericItem held = null;
                    if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                        held = Main.getItem(p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(GenericItem.idKey, PersistentDataType.STRING));
                        if (held instanceof GenericUUIDItem) {
                            held = Main.getActiveItem(p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(GenericUUIDItem.uuidKey, PersistentDataType.STRING));
                        }
                    }
                    current.get(v).add(held);

                    // register armor
                    ItemStack[] armorRaw = p.getInventory().getArmorContents();
                    for (int i = 0; i < armorRaw.length; i++) {
                        GenericItem gen = null;
                        if (armorRaw[i] != null) {
                            gen = Main.getItem(armorRaw[i].getItemMeta().getPersistentDataContainer().get(GenericItem.idKey, PersistentDataType.STRING));
                            if (gen instanceof GenericUUIDItem) {
                                gen = Main.getActiveItem(armorRaw[i].getItemMeta().getPersistentDataContainer().get(GenericUUIDItem.uuidKey, PersistentDataType.STRING));
                            }
                        }
                        current.get(v).add(gen);
                    }


                    // check for differences
                    for (int i = 0; i < current.get(v).size(); i++) {
                        if (!Objects.equals(saved.get(v).get(i), current.get(v).get(i))) {
                            updated.get(v).add(i);
                        }
                    }

                    // add new stats
                    for (int i = 0; i < updated.get(v).size(); i++) {
                        int indx = updated.get(v).get(i);
                        GenericItem cur = current.get(v).get(indx);
                        GenericItem sav = saved.get(v).get(indx);
                        if (cur != null) {
                            if (indx == 0 && cur.slot != ActiveSlot.HAND) {
                                continue;
                            }
                            for (Map.Entry<Stat, Double> e : cur.stats.entrySet()) {
                                sp.addBonusStat(e.getKey(), e.getValue());
                                System.out.println("Stat value after addition: " + sp.getVal(e.getKey()));
                            }
                        }
                        if (sav != null) {
                            if (indx == 0 && sav.slot != ActiveSlot.HAND) {
                                continue;
                            }
                            for (Map.Entry<Stat, Double> e : sav.stats.entrySet()) {
                                sp.addBonusStat(e.getKey(), -1 * e.getValue());
                                System.out.println("Stat value after subtraction: " + sp.getVal(e.getKey()));
                            }
                        }
                    }

                    // apply flags
                    for (int i = 0; i < updated.get(v).size(); i++) {
                        int indx = updated.get(v).get(i);
                        GenericItem cur = current.get(v).get(indx);
                        GenericItem sav = saved.get(v).get(indx);

                        if (cur != null) {
                            v.flags.add(cur);
                        }
                        if (sav != null) {
                            v.flags.remove(sav);
                        }
                    }

                    // apply modifiers, remove modifiers that are 0
                    ArrayList<Stat> unmodified = new ArrayList<>();
                    for (Map.Entry<Stat, Double> e: sp.tempModifiers.entrySet()) {
                        if (e.getValue() == 0) {
                            unmodified.add(e.getKey());
                        }
                        sp.addBonusStat(e.getKey(), e.getValue());
                    }
                    for (Stat s : unmodified) {
                        sp.tempModifiers.remove(s);
                    }

                    // regen health
                    if (sp.getTempValue(Stat.HEALTH) < 0) {
                        regenTicks.put(v, (ticks + 1) % REGEN_RATE);
                        if (regenTicks.get(v) == 0) {
                            sp.addTempStat(Stat.HEALTH, Math.min(sp.tempModifiers.get(Stat.HEALTH), sp.getVal(Stat.HEALTH) * HEALTH_REGEN_PERCENT));
                        }
                    }

                    // check for death
                    if (sp.tempModifiers.containsKey(Stat.HEALTH) && -1 * sp.tempModifiers.get(Stat.HEALTH) > sp.getVal(Stat.HEALTH)) {
                        v.kill();
                    }

                    // display health & mana
                    String health = ChatColor.RED + "HP: " + new DecimalFormat("#").format(sp.getVal(Stat.HEALTH) - sp.getTempValue(Stat.HEALTH)) + "/" + new DecimalFormat("#").format(sp.getVal(Stat.HEALTH));
                    String mana = ChatColor.LIGHT_PURPLE + "Mana: " + new DecimalFormat("#").format(sp.getVal(Stat.MANA) - sp.getTempValue(Stat.MANA)) + "/" + new DecimalFormat("#").format(sp.getVal(Stat.MANA));
                    String combo = health + ChatColor.WHITE + "  |  " + mana;
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(combo));

                    // update
                    saved.get(v).clear();
                    saved.get(v).addAll(current.get(v));
                    updated.get(v).clear();
                    current.get(v).clear();
                }
            }
        };
        handleStats.runTaskTimer(Main.getPlugin(Main.class), 0, 1);
    }
}
