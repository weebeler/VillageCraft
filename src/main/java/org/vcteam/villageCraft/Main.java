package org.vcteam.villageCraft;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;
import org.vcteam.villageCraft.Commands.*;
import org.vcteam.villageCraft.Enums.Permission;
import org.vcteam.villageCraft.Exceptions.FailedToFindException;
import org.vcteam.villageCraft.VCItem.Helmet;
import org.vcteam.villageCraft.VCItem.Sword;
import org.vcteam.villageCraft.VCItem.VCItem;
import org.vcteam.villageCraft.VCMonster.VCMonster;
import org.vcteam.villageCraft.VCMonster.Zombie;
import org.vcteam.villageCraft.VCPlayer.VCPlayer;
import org.vcteam.villageCraft.VCPlayer.VCPlayerJSON;
import org.vcteam.villageCraft.VCWorld.VCSchematic;
import org.vcteam.villageCraft.VCWorld.VCSchematicJSON;
import org.vcteam.villageCraft.VCWorld.VCWorld;
import org.vcteam.villageCraft.VCWorld.VCWorldJSON;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Main class. Starts and stops the program, handles temporary storage (i.e. online players)
 *
 * @author VCTeam
 * @version 0.1
 */
public final class Main extends JavaPlugin {
    public static final Logger log = Logger.getLogger(Main.class.getName());
    public static final Handler handler = new Handler();
    private static ArrayList<VCMonster> monsterTemplates = new ArrayList<>();
    private static ArrayList<VCItem> itemTemplates = new ArrayList<>();
    private static ArrayList<VCPlayer> connectedPlayers = new ArrayList<>();
    private static HashMap<String, Permission> commandPerms = new HashMap<>();
    private static ArrayList<VCWorld> worlds = new ArrayList<>();
    private static HashMap<String, VCSchematic> schematics = new HashMap<>();
    private static ArrayList<VCMonster> aliveMonsters = new ArrayList<>();
    private static File playerJson;
    private static File worldJson;
    private static File schemJson;
    public static boolean debug;
    private static ArrayList<VCPlayerJSON> playerJsons = new ArrayList<>();
    private static ArrayList<VCWorldJSON> worldJsons = new ArrayList<>();
    private static ArrayList<VCSchematicJSON> schematicJsons = new ArrayList<>();

    /**
     * Registers commands & event listeners. Loads saved worlds. Initializes templates of all monsters and items.
     */
    @Override
    public void onEnable() {
        debug = true;

        if (debug) log.info("Successfully launched!");

        try {
            playerJson = new File("C:\\Users\\Natha\\Downloads\\1.21 Server\\json\\villagecraft_players.json");
            if (!playerJson.exists()) {
                playerJson.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong loading playerJson!");
            if (debug) e.printStackTrace();
        }

        try {
            worldJson = new File("C:\\Users\\Natha\\Downloads\\1.21 Server\\json\\villagecraft_world.json");
            if (!worldJson.exists()) {
                worldJson.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong loading worldJson!");
            if (debug) e.printStackTrace();
        }

        try {
            schemJson = new File("C:\\Users\\Natha\\Downloads\\1.21 Server\\json\\villagecraft_schem.json");
            if (!schemJson.exists()) {
                schemJson.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong loading worldJson!");
            if (debug) e.printStackTrace();
        }

        loadPlayerJsonData();
        loadSchematics();


        Bukkit.getPluginManager().registerEvents(new Events(), this);

        regCom("debug", new DebugCommand(), Permission.DEBUG);
        regCom("addperm", new AddPermissionCommand(), Permission.MANAGER);
        regCom("remperm", new RemovePermissionCommand(), Permission.MANAGER);
        regCom("toggledebug", new ToggleDebugModeCommand(), Permission.DEBUG);
        regCom("home", new HomeCommand(), Permission.DEFAULT);
        regCom("locone", new LocOneCommand(), Permission.WORLD);
        regCom("loctwo", new LocTwoCommand(), Permission.WORLD);
        regCom("saveschem", new SaveSchematicCommand(), Permission.WORLD);
        regCom("loadschem", new LoadSchematicCommand(), Permission.WORLD);
        regCom("gnh", new GenerateNewHomeCommand(), Permission.WORLD);
        regCom("setstat", new SetStatCommand(), Permission.OP);
        regCom("getstat", new GetStatCommand(), Permission.DEFAULT);
        regCom("addstat", new AddStatCommand(), Permission.OP);
        regCom("substat", new DecreaseStatCommand(), Permission.OP);
        regCom("vcsummon", new VCSummonCommand(), Permission.ENTITY);
        regCom("vcgive", new VCGiveCommand(), Permission.INVENTORY);

        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(Main.getWorldJson());

            VCWorldJSON[] data = gson.fromJson(reader, VCWorldJSON[].class);

            worldJsons.addAll(Arrays.asList(data));

        } catch (Exception exc) {
            Main.log.info("Something went wrong loading json data!");
            if (Main.debug) exc.printStackTrace();
        }

        new Zombie().addTemplate();
        new Sword().addTemplate();
        new Helmet().addTemplate();

        handler.handleStats();
    }

    /**
     * Registers a command and its permission.
     * @param command exact phrasing of command's in-game reference
     * @param exec code to execute
     * @param perm permission required to used command
     */
    private void regCom(String command, CommandExecutor exec, Permission perm) {
        getCommand(command).setExecutor(exec);
        commandPerms.put(command, perm);
    }

    /**
     * Saves worlds.
     */
    @Override
    public void onDisable() {
        for (VCWorld world : worlds) {
            world.getWorld().save();
            if (!containsUnloadedWorld(world.getName())) {
                worldJsons.add(new VCWorldJSON(world));
            }
        }
        Main.saveWorldJsonData();
        try {
            Main.saveSchematicJsonData();
        } catch (FailedToFindException e) {
            if (debug) e.printStackTrace();
        }
    }

    /**
     * @return list of connected VCPlayers
     */
    public static ArrayList<VCPlayer> getConnectedPlayers() {return connectedPlayers;}

    /**
     * Adds a VCPlayer to the list of connected players
     * @param player VCPlayer to add
     */
    public static void addConnectedPlayer(VCPlayer player) {connectedPlayers.add(player);}

    /**
     * Removes a VCPlayer from the list of connected players
     * @param player VCPlayer to remove
     */
    public static void removeConnectedPlayer(VCPlayer player) {
        if (connectedPlayers.contains(player)) {
            connectedPlayers.remove(player);
        } else {
            log.info("connectedPlayers does not contain " + player.getPlayer().getName());
        }
    }

    /**
     * @return json file where VCPlayer data is stored
     */
    public static File getPlayerJson() {
        return playerJson;
    }

    /**
     * @return list of all playerJSONs saved saved
     */
    public static ArrayList<VCPlayerJSON> getPlayerJsons() {
        return playerJsons;
    }

    /**
     * @return json file where VCWorld data is stored
     */
    public static File getWorldJson() {
        return worldJson;
    }

    /**
     * Loads all JSON data from the players json file.
     */
    public static void loadPlayerJsonData() {
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(Main.getPlayerJson());

            VCPlayerJSON[] data = gson.fromJson(reader, VCPlayerJSON[].class);

            playerJsons.addAll(Arrays.asList(data));

        } catch (Exception exc) {
            Main.log.info("Something went wrong loading json data!");
            if (Main.debug) exc.printStackTrace();
        }
    }

    /**
     * Loads all schematics from the json file.
     */
    public static void loadSchematics() {
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(Main.getSchemJson());

            VCSchematicJSON[] data = gson.fromJson(reader, VCSchematicJSON[].class);

            schematicJsons.addAll(Arrays.asList(data));

        } catch (Exception exc) {
            Main.log.info("Something went wrong loading json data!");
            if (Main.debug) exc.printStackTrace();
        }

        for (VCSchematicJSON js : schematicJsons) {
            schematics.put(js.getName(), VCSchematic.build(js));
        }
    }

    /**
     * Culls duplicates, then saves all JSON data to the json file.
     */
    public static void savePlayerJsonData() {
        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(Main.getPlayerJson(), false);

            gson.toJson(playerJsons, writer);
            if (Main.debug) Main.log.info("Saved data to the players json file!");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            if (Main.debug) ex.printStackTrace();
        }
    }

    /**
     * Saves every VCWorldJSON in the worldJsons file.
     */
    public static void saveWorldJsonData() {
        for (World world : Bukkit.getWorlds()) {
            world.save();
        }

        for (VCWorld world : worlds) {
            if (!containsUnloadedWorld(world.getName())) {
                worldJsons.add(new VCWorldJSON(world));
            }
        }

        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(Main.getWorldJson(), false);

            gson.toJson(worldJsons, writer);
            if (Main.debug) Main.log.info("Saved data to the worlds json file!");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            if (Main.debug) ex.printStackTrace();
        }
    }

    /**
     * Saves all schematic jsons.
     */
    public static void saveSchematicJsonData() throws FailedToFindException {
        for (Map.Entry<String, VCSchematic> entry : schematics.entrySet()) {
            if (!containsSchematicJson(entry.getValue())) {
                schematicJsons.add(new VCSchematicJSON(entry.getValue()));
            }
        }
        try {
            Gson gson = new Gson();
            Writer writer = new FileWriter(Main.getSchemJson(), false);

            gson.toJson(schematicJsons, writer);
            if (Main.debug) Main.log.info("Saved data to the schematics json file!");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            if (Main.debug) ex.printStackTrace();
        }
    }

    /**
     * @return map of all commandname, permissionrequired keysets
     */
    public static HashMap<String, Permission> getCommandPerms() {
        return commandPerms;
    }

    /**
     * @return list of VCWorlds
     */
    public static ArrayList<VCWorld> getWorlds() {return worlds;}

    /**
     * @return list of VCWorldJSONs
     */
    public static ArrayList<VCWorldJSON> getWorldJsons() {
        return worldJsons;
    }

    /**
     * @return schematics Json file
     */
    public static File getSchemJson() {
        return schemJson;
    }

    /**
     * Adds a world to the list of worlds.
     * @param world VCWorld to add
     */
    public static void addWorld(VCWorld world) {
        worlds.add(world);
    }

    /**
     * Removes a world from the list of worlds.
     * @param world VCWorld to remove
     */
    public static void removeWorld(VCWorld world) {
        if (worlds.contains(world)) worlds.remove(world);
        else if (debug) log.info("Worlds does not contain a world with the name " + world.getName());
    }

    /**
     * Returns whether or not the server has an unloaded world with a given name.
     * @param name name of world
     * @return true if there is a world in the worldJsons list with a given name
     */
    public static boolean containsUnloadedWorld(String name) {
        for (VCWorldJSON json : worldJsons) {
            if (json.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsLoadedWorld(String name) {
        for (VCWorld world : worlds) {
            if (world.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a VCWorld by a name as long as that world is loaded.
     * @param name name of world
     * @return world of given name.
     * @throws FailedToFindException if no world is found with that name
     */
    public static VCWorld getWorldFromName(String name) throws FailedToFindException {
        for (VCWorld world : worlds) {
            if (world.getName().equals(name)) {
                return world;
            }
        }
        throw new FailedToFindException("VCWorld");
    }

    /**
     * Gets a VCWorldJSON by a name as long as that world is loaded.
     * @param name name of world
     * @return worldjson of given name.
     * @throws FailedToFindException if no worldjson is found with that name
     */
    public static VCWorldJSON getWorldJSONFromName(String name) throws FailedToFindException {
        for (VCWorldJSON world : worldJsons) {
            if (world.getName().equals(name)) {
                return world;
            }
        }
        throw new FailedToFindException("VCWorldJSON");
    }

    /**
     * Add a schematic to the list of schematics.
     * @param name name of schematic (used for pasting)
     * @param schematic schematic object
     */
    public static void putSchematic(String name, VCSchematic schematic) {
        schematics.put(name, schematic);
    }

    /**
     * Returns a schematic with a given name
     * @param name name of the schematic
     * @return VCSchematic object
     * @throws FailedToFindException thrown if there's no schematic with the given name
     */
    public static VCSchematic getSchematic(String name) throws FailedToFindException {
        if (schematics.containsKey(name)) {
            return schematics.get(name);
        } else {
            throw new FailedToFindException("VCSchematic");
        }
    }

    /**
     * @return HashMap of schematic names and vcschematics
     */
    public static HashMap<String, VCSchematic> getSchematics() {
        return schematics;
    }

    /**
     * Add a playerjson to the list.
     * @param js VCPlayerJSON to add
     */
    public static void addPlayerJson(VCPlayerJSON js) {
        playerJsons.add(js);
    }

    /**
     * Returns true if the schematic jsons list contains a json with the same name as a given schematic.
     * @param schematic schematic to check for
     * @return true if schematic jsons list contains a match
     */
    public static boolean containsSchematicJson(VCSchematic schematic) {
        String name = "NONAME";
        for (Map.Entry<String, VCSchematic> entry : schematics.entrySet()) {
            if (entry.getValue().equals(schematic)) {
                name = entry.getKey();
            }
        }
        for (VCSchematicJSON js : schematicJsons) {
            if (js.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param template VCMonster to add as a template (Don't use a monster actually alive in game)
     */
    public static void addMonsterTemplate(VCMonster template) {
        monsterTemplates.add(template);
    }

    /**
     * @return list of monster templates
     */
    public static ArrayList<VCMonster> getMonsterTemplates() {
        return monsterTemplates;
    }

    /**
     * @param alive Living VCMonster to add as a living monster
     */
    public static void addAliveMonster(VCMonster alive) {
        aliveMonsters.add(alive);
    }

    /**
     * @return list of alive monsters
     */
    public static ArrayList<VCMonster> getAliveMonsters() {
        return aliveMonsters;
    }

    /**
     * Searches for an alive monster with a matching UUID.
     * @param uuid UUID to look for
     * @return mob with given UUID
     * @throws FailedToFindException if no mob exists with that UUID
     */
    public static VCMonster getAliveMonster(String uuid) throws FailedToFindException {
        for (VCMonster v : aliveMonsters) {
            if (v.getUuid().equals(uuid)) {
                return v;
            }
        }
        throw new FailedToFindException("VCMonster");
    }

    /**
     * @param dead VCMonster to remove
     */
    public static void removeAliveMonster(VCMonster dead) {
        if (aliveMonsters.contains(dead)) {
            aliveMonsters.remove(dead);
        } else {
            if (debug) Main.log.info("Monster with name " + dead.getName() + " does not exist!");
        }
    }

    /**
     * @param item VCItem to add as a reference
     */
    public static void addItemTemplate(VCItem item) {
        itemTemplates.add(item);
    }

    /**
     * @return list of VCItem templates
     */
    public static ArrayList<VCItem> getItemTemplates() {
        return itemTemplates;
    }
}
