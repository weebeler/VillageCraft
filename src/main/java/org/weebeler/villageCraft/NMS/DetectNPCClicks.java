package org.weebeler.villageCraft.NMS;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.weebeler.villageCraft.Main;

import java.lang.reflect.Field;

public class DetectNPCClicks extends PacketEvent{
    public DetectNPCClicks() {
        super(ServerboundInteractPacket.class);
    }

    @Override
    public void receive(Packet packet, ServerCommonPacketListenerImpl listener) {
        try {
            Field id = packet.getClass().getDeclaredField("b");
            id.setAccessible(true);
            int entityId = id.getInt(packet);

            Field click = packet.getClass().getDeclaredField("c");
            click.setAccessible(true);
            Object clickType = click.get(packet);

            if (clickType.toString().split("\\$")[1].charAt(0) == 'e' || clickType.toString().split("\\$")[1].charAt(0) == '1') {
                return;
            }

            Field hand = clickType.getClass().getDeclaredField("a");
            hand.setAccessible(true);
            if (!hand.get(clickType).toString().equals("MAIN_HAND")) {
                return;
            }

            NPC clicked = Main.getServer(Main.TITLE_SPAWN).getNPC(entityId);
            if (clicked != null) {
                clicked.onClick(listener.getCraftPlayer().getHandle());
            }

        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void kill() {
        Main.listener.removeListener(this);
    }
}
