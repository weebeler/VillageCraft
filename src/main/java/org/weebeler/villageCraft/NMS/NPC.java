package org.weebeler.villageCraft.NMS;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
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

import java.util.Arrays;
import java.util.UUID;

public class NPC {
    public ServerPlayer npc;
    public String name;
    public UUID uuid;
    public GameProfile profile;

    private ServerPlayer holder;

    public NPC(Player p, String n, String s1, String s2) {
        holder = ((CraftPlayer) p).getHandle();
        name = n;
        uuid = UUID.randomUUID();
        profile = new GameProfile(uuid, ChatColor.GOLD + name);
        profile.getProperties().put("textures", new Property("textures", s1, s2));
    }
    public void spawn(Location location) {
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        npc = new ServerPlayer(holder.getServer(), world, profile, new ClientInformation("en_us", 2, ChatVisiblity.HIDDEN, true, 0, net.minecraft.world.entity.player.Player.DEFAULT_MAIN_HAND, false, false));

        npc.connection = new ServerGamePacketListenerImpl(
                ((CraftServer) Bukkit.getServer()).getServer(),
                new EmptyConnection(null),
                npc,
                CommonListenerCookie.createInitial(profile, true)
        );

        update(location);
        npc.setGameMode(GameType.CREATIVE);

        npc.setPos(location.getX(), location.getY(), location.getZ());
    }
    public void hideDisplayName() {
        Team teamScore = Bukkit.getScoreboardManager().getNewScoreboard().registerNewTeam(UUID.randomUUID().toString());
        teamScore.addEntry(npc.getDisplayName().toString());
    }
    public void update(Location location) {
        SynchedEntityData data = npc.getEntityData();
        byte bitmask = (byte) (0x01 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40);
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), bitmask);

        float pitch = 180f;

        for (Player p : Bukkit.getOnlinePlayers()) {
            ServerPlayer inst = ((CraftPlayer) p).getHandle();
            inst.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
            inst.connection.send(new ClientboundAddEntityPacket(npc, new ServerEntity(((CraftWorld) location.getWorld()).getHandle(), npc, 1, true, null, null)));
            inst.connection.send(new ClientboundSetEntityDataPacket(npc.getId(), data.packDirty()));
            inst.connection.send(new ClientboundRotateHeadPacket(npc, (byte) ((pitch % 360) * 256 / 360)));
            inst.connection.send(new ClientboundMoveEntityPacket.Rot(npc.getBukkitEntity().getEntityId(), (byte) ((pitch % 360) * 256 / 360), (byte) (0), true));
        }

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            for (Player p : location.getWorld().getPlayers()) {
                ServerPlayer inst = ((CraftPlayer) p).getHandle();
                inst.connection.send(new ClientboundPlayerInfoRemovePacket(Arrays.asList(npc.getUUID())));
            }
        }, 20);

        hideDisplayName();
    }
    public void onClick(ServerPlayer player) {

    }
}
