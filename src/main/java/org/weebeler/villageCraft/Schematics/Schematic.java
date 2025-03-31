package org.weebeler.villageCraft.Schematics;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Schematic {
    public String name;
    public BoundingBox box;
    public HashMap<SLocation, SBlock> map;

    public Schematic(String n, int x1, int y1, int z1, int x2, int y2, int z2, World dummy) {
        map = new HashMap<>();
        name = n;
        box = new BoundingBox(x1, y1, z1, x2, y2, z2);

        for (int x = (int) box.getMinX(); x <= box.getMaxX(); x++) {
            for (int y = (int) box.getMinY(); y <= box.getMaxY(); y++) {
                for (int z = (int) box.getMinZ(); z <= box.getMaxZ(); z++) {
                    Location bl = new Location(dummy, x, y, z);
                    map.put(new SLocation(bl), new SBlock(dummy.getBlockAt(bl)));
                }
            }
        }
    }

    public void paste(Location location) {
        World world = location.getWorld();
        for (Map.Entry<SLocation, SBlock> e : map.entrySet()) {

            int diffx = (int) (location.getBlockX() - box.getMinX());
            int diffy = (int) (location.getBlockY() - box.getMinY());
            int diffz = (int) (location.getBlockZ() - box.getMinZ());

            Location loc = e.getKey().convert(world).add(diffx, diffy, diffz);
            world.getBlockAt(loc).setType(e.getValue().mat());
            world.getBlockAt(loc).setBlockData(Bukkit.createBlockData(e.getValue().data));
        }
    }

    public String serialize() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(this);
    }

    public static Schematic deserialize(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Schematic>() {}.getType();
        return gson.fromJson(json, type);
    }
}
