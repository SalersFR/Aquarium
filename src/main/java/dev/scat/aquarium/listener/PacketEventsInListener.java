package dev.scat.aquarium.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dev.scat.aquarium.Aquarium;
import dev.scat.aquarium.data.PlayerData;
import org.bukkit.entity.Player;

public class PacketEventsInListener extends PacketListenerAbstract {

    public PacketEventsInListener() {
        super(PacketListenerPriority.HIGHEST);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = (Player) event.getPlayer();

        if (player == null) return;

        PlayerData data = Aquarium.getInstance().getPlayerDataManager().get(player);

        if (data == null) return;

        data.handle(event);

    }
}
