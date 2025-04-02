package org.weebeler.villageCraft.NMS;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.bukkit.entity.Player;
import org.weebeler.villageCraft.Main;

public class PacketEvent {
    public final Class<? extends Packet> type;

    public PacketEvent(Class<? extends Packet> type) {
        this.type = type;
    }

    public void receive(Packet packet, ServerCommonPacketListenerImpl listener, Player player) {
    }
}
