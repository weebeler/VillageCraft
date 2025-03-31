package org.weebeler.villageCraft.NMS;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

import java.net.SocketAddress;

public class EmptyConnection extends Connection {
    public EmptyConnection(PacketFlow flag) {
        super(flag);
        channel = new EmptyChannel(null);
        address = new SocketAddress() {
            private static final long serialVersionUID = 8207338859896320185L;
        };
    }

    @Override
    public void flushChannel() {
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void send(Packet packet) {
    }

    @Override
    public void send(Packet packet, PacketSendListener genericfuturelistener) {
    }

    @Override
    public void send(Packet packet, PacketSendListener genericfuturelistener, boolean flag) {
    }
}
