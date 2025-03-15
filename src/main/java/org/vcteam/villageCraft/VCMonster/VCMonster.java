package org.vcteam.villageCraft.VCMonster;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Main;

import java.text.DecimalFormat;

public class VCMonster {
    protected boolean built;

    protected EntityType type;
    protected Entity entity;
    protected String name;
    protected double health;
    protected double maxHealth;
    protected double damage;
    protected String backendName;
    protected String uuid;

    /**
     * Constructor for a VCMonster sets everything to default values. Set them later on.
     */
    public VCMonster() {
        built = false;

        type = null;
        entity = null;
        name = "No Name";
        health = 0;
        maxHealth = 0;
        damage = 0;
        backendName = "No Backend Name";
        uuid = "No UUID";
    }

    /**
     * @param e EntityType to use when spawning this mob.
     */
    public void setType(EntityType e) {
        type = e;
    }

    /**
     * Also sets UUID.
     * @param e Entity to set as this VCMonster's entity.
     */
    public void setEntity(Entity e) {
        entity = e;
        uuid = e.getUniqueId().toString();
    }

    /**
     * @param n Name of this mob (don't use chatcolors)
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * @param h Health of this mob
     */
    public void setHealth(double h) {
        health = h;
        maxHealth = h;
    }

    /**
     * Used to damage the mob without reducing its maxhealth.
     * @param d damage to deal
     */
    public void damage(double d) {
        health -= d;
    }

    /**
     * @param d Damage of this mob
     */
    public void setDamage(double d) {
        damage = d;
    }

    /**
     * @param b backend name of this mob, used for summon commands
     */
    public void setBackendName(String b) {
        backendName = b;
    }

    /**
     * @return entitytype tied to this mob
     */
    public EntityType getType() {
        return type;
    }

    /**
     * @return entity tied to this mob
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @return UUID of this mob's entity
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return name of this mob
     */
    public String getName() {
        return name;
    }

    /**
     * @return health of this mob
     */
    public double getHealth() {
        return health;
    }

    /**
     * @return damage of this mob
     */
    public double getDamage() {
        return damage;
    }

    /**
     * @return backend name of this mob
     */
    public String getBackendName() {
        return backendName;
    }

    /**
     * Overriden by subclasses.
     */
    public void init() {
        Main.log.info("Called init on a VCMonster!");
    }

    /**
     * Add an un-spawned VCMonster to use as a template.
     */
    public void addTemplate() {
        if (!built) init();
        Main.addMonsterTemplate(this);
    }

    /**
     * Overriden by subclasses.
     */
    public void spawn(World world, Location location) {
        Main.log.info("Called spawn on a VCMonster!");
    }

    /**
     * Initializes nameplate on the head entity.
     */
    public void checkAlive() {
        if (health <= 0) {
            Main.removeAliveMonster(this);
            ((LivingEntity) entity).damage(Integer.MAX_VALUE);
        }

        entity.setCustomName(ChatColor.GOLD + name + ChatColor.RED + (" ♥" + new DecimalFormat("#").format(Math.max(0, health)) + "/" + new DecimalFormat("#").format(maxHealth)));
        entity.setCustomNameVisible(true);
    }

    /**
     * Finds a VCMonster from an Entity.
     * @param e entity to look for a match for
     * @return null if no entity is found; otherwise returns VCMonster which contains that entity
     * @throws FailedToFindException if no mob match is found
     */
    public static VCMonster find(Entity e) throws FailedToFindException {
        return Main.getAliveMonster(e.getUniqueId().toString());
    }
}
