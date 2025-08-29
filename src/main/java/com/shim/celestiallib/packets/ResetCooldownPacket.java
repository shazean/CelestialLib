package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.ICoolDown;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetCooldownPacket {

    private final int playerId;
    private final ResourceKey<Level> dimension;

    public ResetCooldownPacket(int playerId, ResourceLocation dimensionLoc) {
        this(playerId, ResourceKey.create(Registry.DIMENSION_REGISTRY, dimensionLoc));
    }

    public ResetCooldownPacket(int playerId, ResourceKey<Level> dimension) {
        this.playerId = playerId;
        this.dimension = dimension;
    }

    public static void encoder(ResetCooldownPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.playerId);
        buffer.writeResourceLocation(packet.dimension.location());
    }

    public static ResetCooldownPacket decoder(FriendlyByteBuf buffer) {
        return new ResetCooldownPacket(buffer.readInt(), buffer.readResourceLocation());
    }

    public static void handle(ResetCooldownPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();

            if (serverPlayer != null) {

                Entity player = serverPlayer.level.getEntity(message.playerId);

                ICoolDown travelCap = CelestialLib.getCapability(player, CLibCapabilities.COOLDOWN_CAPABILITY);

                if (travelCap != null) {

                    travelCap.resetCooldown(Planet.getPlanet(message.dimension));
                    travelCap.sync((Player) player);

                }
            }
        });
        context.setPacketHandled(true);
    }
}