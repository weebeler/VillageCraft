package org.weebeler.villageCraft.Worlds;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.weebeler.villageCraft.NMS.NPC;
import org.weebeler.villageCraft.WorldGeneration.CreateGamerules;
import org.weebeler.villageCraft.WorldGeneration.EmptyChunkGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Server {
    public String name;
    public World world;
    public ArrayList<NPC> npcs;

    public Server(World w, String n) {
        name = n;
        world = w;
        npcs = new ArrayList<>();
    }

    public NPC getNPC(int id) {
        System.out.println("Passed id: " + id);
        for (NPC npc : npcs) {
            System.out.println("Checking NPC: " + npc.name);
            for (Map.Entry<UUID, Integer> e : npc.versions.entrySet()) {
                System.out.println("Comparing to id " + e.getValue());
                if (e.getValue() == id) {
                    return npc;
                }
            }
        }
        throw new RuntimeException();
    }
    public static <T extends Server> T create(Class<T> clazz, Object... arg) {
        World w;

        String title = (String) arg[0];

        if (Bukkit.getWorld(title) == null) {
            WorldCreator genSpawn = new WorldCreator(title);
            genSpawn.generator(new EmptyChunkGenerator());
            genSpawn.type(WorldType.FLAT);
            genSpawn.environment(World.Environment.NORMAL);

            w = genSpawn.createWorld();

            Location spawnOrigin = new Location(w, 0, 64, 0);
            w.setSpawnLocation(spawnOrigin);
            w.loadChunk(w.getChunkAt(spawnOrigin));

            for (Entity e : w.getEntities()) {
                e.remove();
            }

            CreateGamerules.generate(w);
        } else {
            w = Bukkit.getWorld(title);
            CreateGamerules.generate(w);
        }

        try {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];

            Object[] finalParams = new Object[constructor.getParameterCount()];
            finalParams[0] = w;

            for (int i = 1, j = 0; j < arg.length; i++, j++) {
                finalParams[i] = arg[j];
            }

            constructor.setAccessible(true);
            T instance = (T) constructor.newInstance(finalParams);
            instance.pasteSchematic();
            return instance;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
        }
    }
    public void pasteSchematic() {

    }
}
