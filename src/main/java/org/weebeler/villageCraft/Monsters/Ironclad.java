package org.weebeler.villageCraft.Monsters;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;

public class Ironclad extends GenericMonster {
    public Ironclad() {
        super(
                EntityType.ZOMBIE,
                "Ironclad",
                "IRONCLAD",
                100,
                15
        );
    }

    @Override
    public void onSpawn() {
        living.getEquipment().setArmorContents(new ItemStack[] {new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_BLOCK)});
        living.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
    }
}
