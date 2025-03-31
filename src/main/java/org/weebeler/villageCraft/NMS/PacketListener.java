package com.seealio.hsb.NMS;

import io.netty.channel.*;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

public class PacketListener {
    public ArrayList<PacketEvent> listeners = new ArrayList<>();
    private ServerCommonPacketListenerImpl listener;

    public void inject(Player player) {
        ChannelDuplexHandler channelHandler = new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
                super.write(ctx, packet, promise);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                System.out.println("Packet: " + ((Packet) packet).type().id().toString().substring(10).toUpperCase(Locale.ROOT));
                broadcast((Packet<?>) packet);

                super.channelRead(ctx, packet);
            }
        };

        listener = ((CraftPlayer) player).getHandle().connection;
        Connection connection;

        try {
            Field field = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
            field.setAccessible(true);
            connection = (Connection) field.get(listener);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        ChannelPipeline pipeline = connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelHandler);
    }

    public void stop(Player player) {
        ServerCommonPacketListenerImpl packetListenerImpl = ((CraftPlayer) player).getHandle().connection;
        Connection connection;

        try {
            Field field = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
            field.setAccessible(true);
            connection = (Connection) field.get(packetListenerImpl);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Channel channel = connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    public void addListener(PacketEvent e) {
        listeners.add(e);
    }
    public void broadcast(Packet packet) {
        for (PacketEvent e : listeners) {
            if (e.type.isInstance(packet)) {
                e.receive(packet, listener);
            }
        }
    }
    public void removeListener(PacketEvent e) {
        listeners.remove(e);
    }
    public void removeAllListeners() {
        listeners.clear();
    }
}
