package org.weebeler.villageCraft;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.weebeler.villageCraft.Items.Backend.GenericItem;
import org.weebeler.villageCraft.Items.Backend.GenericUUIDItem;
import org.weebeler.villageCraft.Items.FeatherBoots;
import org.weebeler.villageCraft.NPCs.SpawnNPC;
import org.weebeler.villageCraft.Villagers.Admin;
import org.weebeler.villageCraft.Villagers.Villager;
import org.weebeler.villageCraft.Worlds.Home;
import org.weebeler.villageCraft.Worlds.Server;

public class Events implements Listener {

    @EventHandler
    public void registerUUIDItemsOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (ItemStack i : p.getInventory().getContents()) {
            if (i != null) {
                GenericItem nouuid = Main.getItem(i.getItemMeta().getPersistentDataContainer().get(GenericItem.idKey, PersistentDataType.STRING));
                if (nouuid instanceof GenericUUIDItem) {
                    GenericUUIDItem built = GenericUUIDItem.rebuild(nouuid, i.getItemMeta().getPersistentDataContainer().get(GenericUUIDItem.uuidKey, PersistentDataType.STRING));
                    Main.addUUIDItem(built);
                }
            }
        }
    }

    @EventHandler
    public void teleportOnJoin(PlayerJoinEvent e) {
        e.getPlayer().teleport(Main.getServer(Main.TITLE_SPAWN).world.getSpawnLocation().add(0.5, 0, 0.5));

        String name = "Click To Play!";
        Server s = Main.getServer(Main.TITLE_SPAWN);
        Location spawnLoc = new Location(s.world, 0.5, 64, 17.5);
        if (!s.containsNPC(name)) {
            SpawnNPC npc = new SpawnNPC(e.getPlayer());
            npc.spawn(spawnLoc);
            s.npcs.add(npc);
        } else {
            s.getNPC(name).update(spawnLoc);
        }

        Villager v = new Villager(e.getPlayer());
        Main.villagers.add(v);

        if (e.getPlayer().isOp()) {
            Admin a = new Admin(e.getPlayer());
            Main.admins.add(a);
        }
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
    public void featherBoots(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity() instanceof Player p) {
            System.out.println("Player took fall damage!");
            if (p.getInventory().getBoots() != null) {
                System.out.println("Player wearing boots!");
                try {
                    GenericItem boots = Main.getItem(p.getInventory().getBoots().getItemMeta().getPersistentDataContainer().get(GenericItem.idKey, PersistentDataType.STRING));
                    System.out.println(boots.name);
                    if (boots instanceof FeatherBoots) {
                        e.setCancelled(true);
                    }
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
