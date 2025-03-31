package com.seealio.hsb.NMS;

import com.seealio.hsb.Main;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;

public class PacketEvent {
    public final Class<? extends Packet> type;

    /**
     * Creates a packetevent listener with a type to listen for.
     * @param type type to listen for
     */
    public PacketEvent(Class<? extends Packet> type) {
        this.type = type;
        Main.addListener(this);
    }

    /**
     * Called when a packet is sent. Always overriden.
     */
    public void receive(Packet packet, ServerCommonPacketListenerImpl listener) {
    }

    /**
     * Disables this listener.
     */
    public void kill() {
        Main.removeListener(this);
    }
}
