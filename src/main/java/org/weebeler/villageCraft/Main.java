package org.weebeler.villageCraft;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.weebeler.villageCraft.Handlers.DamageHandler;
import org.weebeler.villageCraft.Handlers.MonsterHandler;
import org.weebeler.villageCraft.Handlers.StatHandler;
import org.weebeler.villageCraft.Items.*;
import org.weebeler.villageCraft.Items.Backend.GenericItem;
import org.weebeler.villageCraft.Items.Backend.GenericUUIDItem;
import org.weebeler.villageCraft.Items.Backend.VCGiveCommand;
import org.weebeler.villageCraft.MiscCommands.GetStatCommand;
import org.weebeler.villageCraft.MiscCommands.SpawnCommand;
import org.weebeler.villageCraft.Monsters.Backend.GenericMonster;
import org.weebeler.villageCraft.Monsters.Backend.VCSummonCommand;
import org.weebeler.villageCraft.Monsters.Goop;
import org.weebeler.villageCraft.Monsters.Ironclad;
import org.weebeler.villageCraft.NMS.ConnectionListener;
import org.weebeler.villageCraft.NMS.DetectNPCClicks;
import org.weebeler.villageCraft.NMS.PacketListener;
import org.weebeler.villageCraft.Schematics.*;
import org.weebeler.villageCraft.Villagers.Admin;
import org.weebeler.villageCraft.MiscCommands.HomeCommand;
import org.weebeler.villageCraft.Villagers.Villager;
import org.weebeler.villageCraft.Worlds.Server;
import org.weebeler.villageCraft.Worlds.Spawn;
import org.weebeler.villageCraft.Worlds.Umbralith;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static PacketListener listener;

    public static DamageHandler damageHandler;

    public static final String TITLE_SPAWN = "SPAWN";
    public static final String TITLE_UMBRALITH = "UMBRALITH";

    public static ArrayList<Server> servers = new ArrayList<>();
    public static ArrayList<Villager> villagers = new ArrayList<>();
    public static ArrayList<GenericUUIDItem> activeItems = new ArrayList<>();
    public static ArrayList<GenericMonster> aliveMonsters = new ArrayList<>();

    public static ArrayList<GenericItem> itemTemplates = new ArrayList<>();
    public static ArrayList<GenericMonster> monsterTemplates = new ArrayList<>();

    public static ArrayList<Admin> admins = new ArrayList<>();
    public static ArrayList<Schematic> schematics = new ArrayList<>();
    public static ArrayList<String> schematicJSONs = new ArrayList<>();

    public static File schemFile;

    @Override
    public void onEnable() {
        listener = new PacketListener();

        damageHandler = new DamageHandler();

        MonsterHandler monsterHandler = new MonsterHandler();
        monsterHandler.handle();
        StatHandler statHandler = new StatHandler();
        statHandler.handleStats();

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(listener), this);

        try {
            schemFile = new File("C:\\Users\\Natha\\Desktop\\VillageCraft\\json\\schemjsons.json");
            if (!schemFile.exists()) {
                schemFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadJson();

        servers.add(Spawn.create(Spawn.class, TITLE_SPAWN));
        servers.add(Umbralith.create(Umbralith.class, TITLE_UMBRALITH));

        DetectNPCClicks dnpcc = new DetectNPCClicks();

        itemTemplates.addAll(Arrays.asList(
                new WoodenSword(),
                new LeatherHelmet(),
                new CrystalChestplate(),
                new RainbowPants(),
                new FeatherBoots(),
                new Kunai(),
                new IceWand()
        ));
        monsterTemplates.addAll(Arrays.asList(
                new Ironclad(),
                new Goop()
        ));

        getCommand("l1").setExecutor(new L1Command());
        getCommand("l1").setTabCompleter(new CoordinateTabCompleter());
        getCommand("l2").setExecutor(new L2Command());
        getCommand("l2").setTabCompleter(new CoordinateTabCompleter());
        getCommand("save").setExecutor(new SaveCommand());
        getCommand("load").setExecutor(new LoadCommand());

        getCommand("vcgive").setExecutor(new VCGiveCommand());
        getCommand("vcsummon").setExecutor(new VCSummonCommand());

        getCommand("home").setExecutor(new HomeCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("getstat").setExecutor(new GetStatCommand());
    }

    @Override
    public void onDisable() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity e : world.getEntities()) {
                if (e.getType() != EntityType.PLAYER) {
                    GenericMonster m = getAliveMonster(e.getUniqueId());
                    if (m != null) {
                        m.kill();
                    } else {
                        e.remove();
                    }
                }
            }
        }
        saveJson();
    }

    public void saveJson() {
        if (!schematics.isEmpty()) {
            for (Schematic entry : schematics) {
                String str = entry.serialize();

                if (!schematicJSONs.contains(str)) {
                    schematicJSONs.add(str);
                }
            }

            try {
                Gson gson = new GsonBuilder().create();
                Writer writer = new FileWriter(schemFile, false);

                gson.toJson(schematicJSONs, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadJson() {
        Gson gson = new Gson();

        try {
            Reader reader = new FileReader(schemFile);

            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> data = gson.fromJson(reader, type);

            for (String str : data) {
                Schematic s = Schematic.deserialize(str);
                schematics.add(s);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Server getServer(String n) {
        for (Server s : servers) {
            if (Objects.equals(s.name, n)) {
                return s;
            }
        }
        throw new RuntimeException();
    }

    public static Villager getVillager(UUID uuid) {
        for (Villager v : villagers) {
            if (v.player.getUniqueId().equals(uuid)) {
                return v;
            }
        }
        throw new RuntimeException();
    }

    public static Admin getAdmin(UUID uuid) {
        for (Admin a : admins) {
            if (a.player.getUniqueId().equals(uuid)) {
                return a;
            }
        }
        return null;
    }

    public static void addSchematic(Schematic sc) {
        Schematic toRemove = null;
        for (Schematic s : schematics) {
            if (s.name.equals(sc.name)) {
                toRemove = s;
            }
        }
        if (toRemove != null) {
            schematics.remove(toRemove);
        }
        schematics.add(sc);
    }

    public static Schematic getSchematic(String name) {
        for (Schematic s : schematics) {
            if (s.name.equals(name)) {
                return s;
            }
        }
        return null;
    }

    public static GenericItem getItem(String id) {
        for (GenericItem i : itemTemplates) {
            if (i.id.equals(id)) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    public static GenericItem getItem(ItemStack item) {
        String id = item.getItemMeta().getPersistentDataContainer().get(GenericItem.idKey, PersistentDataType.STRING);
        for (GenericItem i : itemTemplates) {
            if (i.id.equals(id)) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    public static GenericUUIDItem getActiveItem(ItemStack item) {
        String uuid = item.getItemMeta().getPersistentDataContainer().get(GenericUUIDItem.uuidKey, PersistentDataType.STRING);
        for (GenericUUIDItem i : activeItems) {
            if (i.uuid.equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    public static void addUUIDItem(GenericUUIDItem add) {
        if (activeItems.contains(add)) {
            System.out.println("Attempted to add a GenericUUIDItem which is already present in activeItems!! Potential dupe?");
        } else {
            activeItems.add(add);
        }
    }

    public static GenericMonster getAliveMonster(UUID uuid) {
        for (GenericMonster m : aliveMonsters) {
            if (m.living.getUniqueId().equals(uuid)) {
                return m;
            }
        }
        return null;
    }

    public static GenericMonster getMonsterTemplate(String id) {
        for (GenericMonster m : monsterTemplates) {
            if (m.id.equals(id)) {
                return m;
            }
        }
        throw new RuntimeException();
    }
}
