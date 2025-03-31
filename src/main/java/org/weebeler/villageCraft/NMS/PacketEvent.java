package org.weebeler.villageCraft.NMS;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.weebeler.villageCraft.Main;

public class PacketEvent {
    public final Class<? extends Packet> type;

    public PacketEvent(Class<? extends Packet> type) {
        this.type = type;
        Main.listener.addListener(this);
    }

    public void receive(Packet packet, ServerCommonPacketListenerImpl listener) {
    }


    public void kill() {
        Main.listener.removeListener(this);
    }
}
