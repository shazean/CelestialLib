package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.ISpaceFlight;
import com.shim.celestiallib.util.TeleportUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Supplier;

public class DoLightTravelPacket {

    private final int spaceVehicleId;
    @Nullable private final ArrayList<Integer> additionalEntities;
    private final ResourceKey<Level> galaxy;
    private final BlockPos planetPos;


    public DoLightTravelPacket(int spaceVehicleId, @Nullable ArrayList<Integer> additionalEntitiesToTeleport, ResourceLocation galaxy, BlockPos planetPos) {
        this(spaceVehicleId, additionalEntitiesToTeleport, ResourceKey.create(Registry.DIMENSION_REGISTRY, galaxy), planetPos);
    }

    public DoLightTravelPacket(int spaceVehicleId, @Nullable ArrayList<Integer> additionalEntitiesToTeleport, ResourceKey<Level> galaxy, BlockPos planetPos) {

        this.spaceVehicleId = spaceVehicleId;
        this.additionalEntities = additionalEntitiesToTeleport;
        this.galaxy = galaxy;
        this.planetPos = planetPos;

    }

    public static void encoder(DoLightTravelPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.spaceVehicleId);

        if (packet.additionalEntities != null) {
            buffer.writeInt(packet.additionalEntities.size());

            for (int entityId : packet.additionalEntities) {
                buffer.writeInt(entityId);
            }
        } else {
            buffer.writeInt(0);
        }

        buffer.writeResourceLocation(packet.galaxy.location());
        buffer.writeDouble(packet.planetPos.getX());
        buffer.writeDouble(packet.planetPos.getY());
        buffer.writeDouble(packet.planetPos.getZ());
    }

    public static DoLightTravelPacket decoder(FriendlyByteBuf buffer) {
        int spaceVehicleId = buffer.readInt();
        int size = buffer.readInt();

        ArrayList<Integer> additionalEntities = new ArrayList<>();

        if (size != 0) {
            for (int i = 0; i < size; i++) {
                additionalEntities.add(buffer.readInt());
            }
        } else {
            additionalEntities = null;
        }

        ResourceLocation galaxy = buffer.readResourceLocation();

        BlockPos planetPos = new BlockPos(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());

        return new DoLightTravelPacket(spaceVehicleId, additionalEntities, galaxy, planetPos);
    }

    public static void handle(DoLightTravelPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        CelestialLib.LOGGER.debug("handling doLightTravel packet?");

        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();

            if (serverPlayer != null) {

                Entity entity = serverPlayer.level.getEntity(message.spaceVehicleId);

                ISpaceFlight flightCap = CelestialLib.getCapability(entity, CLibCapabilities.SPACE_FLIGHT_CAPABILITY);

                if (flightCap != null) {

                    ArrayList<Entity> passengers = null;

                    if (message.additionalEntities != null) {
                        passengers = new ArrayList<>();
                        for (int entityId : message.additionalEntities) {
                            passengers.add(serverPlayer.level.getEntity(entityId));
                        }
                    }

                    TeleportUtil.handleLightSpeedTravel(serverPlayer, entity, passengers, message.galaxy, message.planetPos);

//                    TeleportUtil.teleport(entity, passengers, message.galaxy, message.planetPos);
//
//                    CLibPacketHandler.INSTANCE.sendTo(new ServerDidLightTravelPacket(message.spaceVehicleId, message.additionalEntities, message.planetPos), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
                }
            }
        });
        context.setPacketHandled(true);
    }
}