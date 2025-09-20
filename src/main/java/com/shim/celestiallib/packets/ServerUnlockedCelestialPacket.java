package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.IUnlock;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerUnlockedCelestialPacket {

    private final int playerId;
    private final ResourceKey<Level> dimension;
    private final boolean isLightSpeed;
    private final boolean isGalaxy;

    public ServerUnlockedCelestialPacket(int playerId, ResourceLocation dimensionLoc, boolean isLightSpeed, boolean isGalaxy) {
        this(playerId, ResourceKey.create(Registry.DIMENSION_REGISTRY, dimensionLoc), isLightSpeed, isGalaxy);
    }

    public ServerUnlockedCelestialPacket(int playerId, ResourceKey<Level> dimension, boolean isLightSpeed, boolean isGalaxy) {
        this.playerId = playerId;
        this.dimension = dimension;
        this.isLightSpeed = isLightSpeed;
        this.isGalaxy = isGalaxy;
    }

    public static void encoder(ServerUnlockedCelestialPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.playerId);
        buffer.writeResourceLocation(packet.dimension.location());
        buffer.writeBoolean(packet.isLightSpeed);
        buffer.writeBoolean(packet.isGalaxy);
    }

    public static ServerUnlockedCelestialPacket decoder(FriendlyByteBuf buffer) {
        return new ServerUnlockedCelestialPacket(buffer.readInt(), buffer.readResourceLocation(), buffer.readBoolean(), buffer.readBoolean());
    }

    public static void handle(ServerUnlockedCelestialPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            Player player = CelestialLib.PROXY.getPlayer();
            IUnlock cap = CelestialLib.getCapability(player, CLibCapabilities.UNLOCK_CAPABILITY);

                if (cap != null) {

                    if (message.isLightSpeed) {
                        if (message.isGalaxy) {
                            cap.unlockCelestialLightSpeed(Galaxy.getGalaxy(message.dimension));
                        } else {
                            cap.unlockCelestialLightSpeed(Planet.getPlanet(message.dimension));
                        }
                    } else {
                        if (message.isGalaxy)
                            cap.unlockCelestial(Galaxy.getGalaxy(message.dimension));
                        else
                            cap.unlockCelestial(Planet.getPlanet(message.dimension));
                    }
                }
        });
        context.setPacketHandled(true);
    }
}