package org.weebeler.villageCraft;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.weebeler.villageCraft.Items.Backend.GenericItem;
import org.weebeler.villageCraft.Items.Backend.GenericUUIDItem;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;
import org.weebeler.villageCraft.NMS.NPC;
import org.weebeler.villageCraft.Villagers.Admin;
import org.weebeler.villageCraft.Villagers.Villager;
import org.weebeler.villageCraft.Worlds.Home;
import org.weebeler.villageCraft.Worlds.Server;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Events implements Listener {

    @EventHandler
    public void onWorldExit(PlayerTeleportEvent e) {
        World from = e.getFrom().getWorld();
        World to = e.getTo().getWorld();

        if (to.getName().equals("world") || to.getName().equals(e.getPlayer().getUniqueId().toString())) {
            return;
        }

        if (!from.equals(to)) {
            System.out.println("Teleported!");
            Server sTo = Main.getServer(to.getName());

            ArrayList<Player> players = new ArrayList<>(to.getPlayers());
            players.removeIf(n -> n.getUniqueId().equals(e.getPlayer().getUniqueId()));

            Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                ArrayList<NPC> toRemove = new ArrayList<>();
                NPC npc = null;
                for (int i = 0; i < sTo.npcs.size(); i++) {
                    npc = sTo.npcs.get(i);
                    if (!npc.registered) {
                        npc.createLocation();
                        npc = npc.create(e.getPlayer());
                    }
                    npc.spawn(e.getPlayer());
                    sTo.npcs.set(i, npc);
                }
                if (npc != null) {
                    for (NPC check : sTo.npcs) {
                        if (check.name.equals(npc.name) && check.npc.getId() != npc.npc.getId()) {
                            toRemove.add(check);
                        }
                    }
                    for (NPC r : toRemove) {
                        sTo.npcs.remove(r);
                    }
                }
            }, 1);

        }
    }

    @EventHandler
    public void registerPlayerOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        p.teleport(Bukkit.getWorld("world").getSpawnLocation());
        p.teleport(Main.getServer(Main.TITLE_SPAWN).world.getSpawnLocation().add(0.5, 0, 0.5));

        UUID uuid = p.getUniqueId();
        boolean found = false;
        for (Villager v : Main.villagers) {
            if (Objects.equals(uuid, v.uuid)) {
                v.player = p;
                v.setupListener();
                found = true;
            }
        }
        if (!found) {
            Villager v = new Villager(p);
            Main.villagers.add(v);
            v.setupListener();
        }

        if (e.getPlayer().isOp()) {
            Admin a = new Admin(p);
            Main.admins.add(a);
        }

        for (ItemStack i : p.getInventory().getContents()) {
            if (i != null) {
                GenericItem nouuid = Main.getItem(i);
                if (nouuid instanceof GenericUUIDItem) {
                    GenericUUIDItem built = GenericUUIDItem.rebuild(nouuid, i.getItemMeta().getPersistentDataContainer().get(GenericUUIDItem.uuidKey, PersistentDataType.STRING));
                    Main.addUUIDItem(built);
                }
            }
        }
    }

    @EventHandler
    public void unregisterPlayerOnQuit(PlayerQuitEvent e) {
        Villager v = Main.getVillager(e.getPlayer().getUniqueId());

        v.stopListener();

    }

    @EventHandler
    public void detectEnterPortal(PlayerMoveEvent e) {
        if (e.getPlayer().getWorld().getName().equals(e.getPlayer().getUniqueId().toString())) {
            Home h = (Home) Main.getServer(e.getPlayer().getUniqueId().toString());
            BoundingBox box = h.portalLocation;

            if (box.contains(e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ())) {
                e.getPlayer().teleport(h.portalTarget.world.getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void handleRandomDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && !(e instanceof EntityDamageByEntityEvent)) {
            Main.damageHandler.miscPlayer(e);
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            return;
        }
        e.setDamage(0);
    }

    @EventHandler
    public void handleDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            e.setCancelled(true);
        } else if (e.getEntity() instanceof Player) {
            if (e.getEntity().getPersistentDataContainer().has(GenericMonster.nonmonster)) {
                e.setCancelled(true);
            }
            Main.damageHandler.entityOnPlayer(e);
        } else if (e.getDamager() instanceof Player) {
            Main.damageHandler.playerOnEntity(e);
        }

        e.setDamage(0);
    }

    @EventHandler
    public void castAbility(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            GenericItem gen = Main.getItem(e.getItem());
            if (gen instanceof GenericUUIDItem) {
                gen = Main.getActiveItem(e.getItem());
            }
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                gen.secondary(e.getPlayer());
            }
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                gen.primary(e.getPlayer());
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!gen.placeable) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void preventSlimeSplitting(SlimeSplitEvent e) {
        e.setCancelled(true);
    }
}
