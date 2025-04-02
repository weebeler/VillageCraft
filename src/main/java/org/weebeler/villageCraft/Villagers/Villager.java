package org.weebeler.villageCraft.Villagers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;
import org.weebeler.villageCraft.Worlds.Home;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Villager {
    public Player player;
    public UUID uuid;
    public Home home;
    public StatProfile statProfile;
    public FlagProfile flags;

    public Villager(Player p) {
        player = p;
        uuid = null;
        home = Home.create(Home.class, p.getUniqueId().toString());
        Main.servers.add(home);
        statProfile = new StatProfile();
        flags = new FlagProfile();
    }
    public Villager(UUID u, StatProfile sp) {
        uuid = u;
        home = Home.create(Home.class, uuid.toString());
        Main.servers.add(home);
        statProfile = sp;
        flags = new FlagProfile();
    }
    public void kill() {
        ((LivingEntity) player).damage(Math.pow(1, -500));
        player.teleport(player.getWorld().getSpawnLocation());
        statProfile.tempModifiers.remove(Stat.HEALTH);
    }
    public String serialize() {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(new VillagerJSON(this));
    }
    public static Villager deserialize(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<VillagerJSON>() {}.getType();
        VillagerJSON vjs = gson.fromJson(json, type);
        return vjs.create();
    }
}
class VillagerJSON {
    String uuid;
    HashMap<String, Double> stats;

    public VillagerJSON(Villager v) {
        uuid = v.player.getUniqueId().toString();
        stats = new HashMap<>();
        for (Map.Entry<Stat, Double> e : v.statProfile.baseStats.entrySet()) {
            stats.put(e.getKey().name(), e.getValue());
        }
    }
    public Villager create() {
        StatProfile sp = new StatProfile();
        sp.baseStats.clear();
        for (Map.Entry<String, Double> e : stats.entrySet()) {
            sp.baseStats.put(Stat.valueOf(e.getKey()), e.getValue());
        }
        return new Villager(UUID.fromString(uuid), sp);
    }
}
