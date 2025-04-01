package org.weebeler.villageCraft.Items;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.weebeler.villageCraft.Items.Backend.*;
import org.weebeler.villageCraft.Items.Backend.Projectile;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;
import org.weebeler.villageCraft.Monsters.Backend.Status;
import org.weebeler.villageCraft.Monsters.Backend.StatusType;
import org.weebeler.villageCraft.Villagers.Stat;
import org.weebeler.villageCraft.Villagers.Villager;

import java.util.*;

public class IceWand extends GenericUUIDItem {
    public IceWand() {
        super(
                new ItemStack(Material.BIRCH_SAPLING),
                "Ice Wand",
                "ICE_WAND",
                Arrays.asList(
                        ChatColor.BLUE + "Primary Ability: Icicle Beam",
                        ChatColor.GRAY + "Fire an icicle forwards. Mobs hit by the icicle are marked",
                        ChatColor.GRAY + "with a stack of " + ChatColor.AQUA + "Frosted" + ChatColor.GRAY + ".",
                        "",
                        ChatColor.BLUE + "Secondary Ability: Shatter",
                        ChatColor.GRAY + "Removes all " + ChatColor.AQUA + "Frosted" + ChatColor.GRAY + " tags from nearby enemies. For",
                        ChatColor.GRAY + "each "+ ChatColor.AQUA + "Frosted" + ChatColor.GRAY + " removed from a mob, it",
                        ChatColor.GRAY + "is stunned for 0.25s."
                ),
                Rarity.EPIC,
                Type.WAND,
                ActiveSlot.HAND
        );
        stats.put(Stat.ARCANE, 20.0);
        stats.put(Stat.MANA, 50.0);
    }

    @Override
    public void primary(Player player) {
        Villager v = Main.getVillager(player.getUniqueId());
        if (v.statProfile.getVal(Stat.MANA) < 3) {
            player.sendMessage(ChatColor.RED + "You don't have enough mana to use this!");
            return;
        }
        Location loc = player.getEyeLocation().clone();

        ItemDisplay visible = (ItemDisplay) player.getWorld().spawnEntity(new Location(player.getWorld(), 0, 1000, 0), EntityType.ITEM_DISPLAY);
        visible.setItemStack(new ItemStack(Material.BLUE_ICE));
        visible.setVisibleByDefault(true);
        Transformation transform = visible.getTransformation();
        transform.getScale().set(0.25);
        transform.getLeftRotation().x = 0.444f;
        transform.getLeftRotation().y = 0.117f;
        transform.getLeftRotation().z = 0.444f;
        transform.getLeftRotation().w = 0.770f;
        visible.setTransformation(transform);

        Vector dir = loc.clone().getDirection();

        Projectile fire = new Projectile(this, player, visible, dir, 5, loc, false);

        fire.shoot();
        Main.getVillager(player.getUniqueId()).statProfile.subtractTempStat(Stat.MANA, 3);
    }

    @Override
    public void secondary(Player player) {
        Villager v = Main.getVillager(player.getUniqueId());
        if (v.statProfile.getVal(Stat.MANA) < 15) {
            player.sendMessage(ChatColor.RED + "You don't have enough mana to use this!");
            return;
        }
        boolean hitOne = false;
        Collection<Entity> nearby = player.getNearbyEntities(20, 20, 20);
        nearby.removeIf(n -> n instanceof Player);
        nearby.removeIf(n -> n.getPersistentDataContainer().has(GenericMonster.nonmonster));
        HashMap<GenericMonster, Integer> stacks = new HashMap<>();
        for (Entity e : nearby) {
            System.out.println(e.getType());
            GenericMonster mob = Main.getAliveMonster(e.getUniqueId());
            if (mob != null) {
                stacks.put(mob, mob.statuses.getStacks(StatusType.ICEWAND));
            }
        }
        for (Map.Entry<GenericMonster, Integer> e : stacks.entrySet()) {
            e.getKey().living.setAI(false);
            boolean procced = e.getKey().statuses.removeAll(StatusType.ICEWAND);
            if (procced) {
                hitOne = true;
                e.getKey().living.getWorld().playSound(e.getKey().living.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.PLAYERS, 1f, 3f);
            }
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> e.getKey().living.setAI(true), 5 * e.getValue());
        }
        if (hitOne) v.statProfile.subtractTempStat(Stat.MANA, 15);
    }

    @Override
    public void onProjectileHit(Player player, Entity e) {
        boolean damaged = Main.damageHandler.magicOnEntity(e, player);
        if (damaged) {
            GenericMonster monster = Main.getAliveMonster(e.getUniqueId());
            if (monster.statuses.getStacks(StatusType.ICEWAND) < 10) {
                monster.statuses.list.add(new Status(StatusType.ICEWAND, 60));
            }
        }
    }
}
