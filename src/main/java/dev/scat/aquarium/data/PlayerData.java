package dev.scat.aquarium.data;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.scat.aquarium.Aquarium;
import dev.scat.aquarium.check.Check;
import dev.scat.aquarium.data.processor.impl.*;
import dev.scat.aquarium.data.processor.Processor;
import dev.scat.aquarium.util.PacketUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerData {

    private final Player player;
    private final ClientVersion version;

    private final List<Processor> processors = new ArrayList<>();
    private final PositionProcessor positionProcessor = new PositionProcessor(this);
    private final RotationProcessor rotationProcessor = new RotationProcessor(this);
    private final WorldProcessor worldProcessor = new WorldProcessor(this);
    private final TransactionProcessor pledgeProcessor = new TransactionProcessor(this);
    private final EntityProcessor entityProcessor = new EntityProcessor(this);
    private final CollisionProcessor collisionProcessor = new CollisionProcessor(this);
    
    private final List<Check> checks = Aquarium.getInstance().getCheckManager().loadChecks(this);

    private int tick;

    @Setter
    private boolean alerting, punishing;

    public PlayerData(Player player) {
        this.player = player;
        version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);

        processors.add(positionProcessor);
        processors.add(rotationProcessor);
        processors.add(worldProcessor);
        processors.add(pledgeProcessor);
        processors.add(entityProcessor);
        processors.add(collisionProcessor);

        if (player.hasPermission("aquarium.alerts")) {
            alerting = true;
        }
    }

    public void handle(PacketReceiveEvent event) {
        if (PacketUtil.isFlying(event.getPacketType())) ++tick;

        processors.forEach(processor -> processor.handlePre(event));

        checks.stream().filter(Check::isEnabled).forEach(check -> check.handle(event));

        processors.forEach(processor -> processor.handlePost(event));
    }

    public void handle(PacketSendEvent event) {
        processors.forEach(processor -> processor.handlePre(event));

        checks.stream().filter(Check::isEnabled).forEach(check -> check.handle(event));

        processors.forEach(processor -> processor.handlePost(event));
    }
}
