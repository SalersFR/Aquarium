package dev.scat.aquarium.listener;

import dev.scat.aquarium.Aquarium;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Aquarium.getInstance().getPlayerDataManager().add(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Aquarium.getInstance().getPlayerDataManager().remove(event.getPlayer().getUniqueId());
    }
}
