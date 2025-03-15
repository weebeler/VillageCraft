package org.vcteam.villageCraft.VCWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.Extensions.EmptyChunkGenerator;
import org.vcteam.villageCraft.Main;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;

import java.io.File;

public class VCWorld {
    private String name; // set to the player's uuid if it's a home world
    private World world;
    private World.Environment environment;
    private Location spawnLoc;

    /**
     * Constructor for only the name of the world. Typically used on home worlds.
     * @param name name of the world
     */
    public VCWorld(String name) {
        this.name = name;
        this.environment = World.Environment.NORMAL;
    }

    /**
     * Constructor only takes name and environment, because spawn location and world are done later.
     * @param name name of the world
     * @param env environment (i.e. Nether, End) of the world
     */
    public VCWorld(String name, World.Environment env) {
        this.name = name;
        this.environment = env;
    }

    /**
     * Constructor used when reloading an already-built VCWorld
     * @param name name of world
     * @param world world of... world
     */
    public VCWorld(String name, World world) {
        this.name = name;
        this.world = world;
    }

    /**
     * Sets the spawn location.
     * @param loc location to set as spawn location.
     */
    public void setSpawnLoc(Location loc) {
        spawnLoc = loc;
    }

    /**
     * Builds the world and sets this.world to the result.
     */
    public void build() {
        WorldCreator wc = new WorldCreator(name);
        wc.generator(new EmptyChunkGenerator());
        wc.environment(environment);
        world = wc.createWorld();
        if (spawnLoc == null) {
            spawnLoc = new Location(world, 0, 100, 0);
        }
        world.setSpawnLocation(spawnLoc);

    }

    /**
     * Teleports the player to this world's spawn location.
     * @param player VCPlayer to teleport
     */
    public void teleport(VCPlayer player) {
        player.getPlayer().teleport(spawnLoc);
    }

    /**
     * Getter for the home's name.
     * @return home name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the home tied to a VCPlayer
     * @param player VCPlayer to find home of
     * @return home VCWorld of the player
     */
    public static VCWorld getFromPlayer(VCPlayer player) throws FailedToFindException {
        String uuid = player.getPlayer().getUniqueId().toString();

        VCWorldJSON data = null;

        for (VCWorldJSON vcwj: Main.getWorldJsons()) {
            if (vcwj.getName().equals(uuid)) {
                data = vcwj;
            }
        }

        if (data != null) {
            return construct(data);
        }
        throw new FailedToFindException("VCWorld");
    }

    /**
     * Constructs a VCWorld from a VCWorldJSON.
     * @param json JSON to convert into world.
     * @return VCWorld VCWorld assembled from JSON data
     * @throws FailedToFindException thrown if no match is found in bukkit worlds
     */
    public static VCWorld construct(VCWorldJSON json) throws FailedToFindException {
        File worldFolder = new File(Bukkit.getWorldContainer(), json.getName());

        if (!worldFolder.exists()) {
            throw new FailedToFindException("world");
        }
        Main.log.info("Loading world with name " + json.getName());
        World saved = Bukkit.createWorld(new WorldCreator(json.getName()));

        VCWorld constructed = new VCWorld(json.getName(), saved);
        constructed.setSpawnLoc(new Location(saved,0, 100, 0));

        return constructed;
    }

    /**
     * Loads a VCWorld. Used when sending a player to a world that is currently unloaded.
     * @param name name of world to load
     * @return VCWorld
     * @throws FailedToFindException thrown if no world in list has the same name as the parameter
     */
    public static VCWorld load(String name) throws FailedToFindException {
        for (VCWorldJSON js : Main.getWorldJsons()) {
            if (js.getName().equals(name)) {
                VCWorld world = construct(js);
                Main.addWorld(world);
                return world;
            }
        }
        throw new FailedToFindException("VCWorld");
    }

    /**
     * Adds this world to main.
     */
    public void load() {
        Main.addWorld(this);
    }

    /**
     * @return world of this dimension
     */
    public World getWorld() {
        return world;
    }
}
