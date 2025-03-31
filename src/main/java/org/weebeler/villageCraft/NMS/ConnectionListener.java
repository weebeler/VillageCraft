package org.weebeler.villageCraft.NMS;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private PacketListener lis;
    public ConnectionListener(PacketListener lis) {
        this.lis = lis;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        lis.inject(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        lis.stop(e.getPlayer());
    }
}
