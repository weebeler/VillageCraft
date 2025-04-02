package org.weebeler.villageCraft.NMS;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.level.GameType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.weebeler.villageCraft.Main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class NPC {
    public ServerPlayer npc;
    public String name;
    public UUID uuid;
    public GameProfile profile;
    public Location location;
    public boolean registered;

    public HashMap<UUID, Integer> versions;

    public NPC(String n, String s1, String s2) {
        name = n;
        uuid = UUID.randomUUID();
        profile = new GameProfile(uuid, ChatColor.GOLD + name);
        profile.getProperties().put("textures", new Property("textures", s1, s2));
        versions = new HashMap<>();

        registered = false;
    }

    public NPC create(Player player) {
        try {
            Constructor<? extends NPC> constructor = this.getClass().getDeclaredConstructor(String.class, GameProfile.class, Location.class, Player.class);
            return constructor.newInstance(name, profile, location, player);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public NPC(String n, GameProfile pr, Location l, Player p) {
        versions = new HashMap<>();

        name = n;
        profile = pr;
        uuid = pr.getId();
        location = l;

        registered = true;
    }

    public void createLocation() {

    }

    public void spawn(Player player) {
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        npc = new ServerPlayer(world.getServer(), world, profile, new ClientInformation("en_us", 2, ChatVisiblity.HIDDEN, true, 0, net.minecraft.world.entity.player.Player.DEFAULT_MAIN_HAND, false, false));

        npc.connection = new ServerGamePacketListenerImpl(
                ((CraftServer) Bukkit.getServer()).getServer(),
                new EmptyConnection(null),
                npc,
                CommonListenerCookie.createInitial(profile, true)
        );

        npc.setGameMode(GameType.CREATIVE);

        npc.setPos(location.getX(), location.getY(), location.getZ());

        SynchedEntityData data = npc.getEntityData();
        byte bitmask = (byte) (0x01 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40);
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), bitmask);

        float pitch = 180f;

        ServerPlayer inst = ((CraftPlayer) player).getHandle();
        inst.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        inst.connection.send(new ClientboundAddEntityPacket(npc, new ServerEntity(world, npc, 1, true, null, null)));
        inst.connection.send(new ClientboundSetEntityDataPacket(npc.getId(), data.packDirty()));
        inst.connection.send(new ClientboundRotateHeadPacket(npc, (byte) ((pitch % 360) * 256 / 360)));
        inst.connection.send(new ClientboundMoveEntityPacket.Rot(npc.getBukkitEntity().getEntityId(), (byte) ((pitch % 360) * 256 / 360), (byte) (0), true));

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> inst.connection.send(new ClientboundPlayerInfoRemovePacket(Arrays.asList(npc.getUUID()))), 20);

        versions.put(player.getUniqueId(), npc.getId());

        hideDisplayName();
    }
    public void hideDisplayName() {
        Team teamScore;
        try {
            teamScore = Bukkit.getScoreboardManager().getNewScoreboard().registerNewTeam(uuid.toString());
        } catch (IllegalArgumentException e) {
            teamScore = Bukkit.getScoreboardManager().getNewScoreboard().getTeam(uuid.toString());
        }
        teamScore.addEntry(npc.getDisplayName().toString());
    }
    public void onClick(ServerPlayer player) {
        System.out.println("buuuuuuurp");
    }
}
