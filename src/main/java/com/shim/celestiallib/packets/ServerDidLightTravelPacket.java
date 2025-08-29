package com.shim.celestiallib.packets;

import com.shim.celestiallib.util.TeleportUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Supplier;

public class ServerDidLightTravelPacket {

    private final int spaceVehicleId;
    @Nullable
    private final ArrayList<Integer> additionalEntities;
    private final BlockPos planetPos;


    public ServerDidLightTravelPacket(int spaceVehicleId, @Nullable ArrayList<Integer> additionalEntitiesToTeleport, BlockPos planetPos) {

        this.spaceVehicleId = spaceVehicleId;
        this.additionalEntities = additionalEntitiesToTeleport;
        this.planetPos = planetPos;

    }

    public static void encoder(ServerDidLightTravelPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.spaceVehicleId);

        if (packet.additionalEntities != null) {
            buffer.writeInt(packet.additionalEntities.size());

            for (int entityId : packet.additionalEntities) {
                buffer.writeInt(entityId);
            }
        } else {
            buffer.writeInt(0);
        }

        buffer.writeDouble(packet.planetPos.getX());
        buffer.writeDouble(packet.planetPos.getY());
        buffer.writeDouble(packet.planetPos.getZ());
    }

    public static ServerDidLightTravelPacket decoder(FriendlyByteBuf buffer) {
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

        BlockPos planetPos = new BlockPos(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());

        return new ServerDidLightTravelPacket(spaceVehicleId, additionalEntities, planetPos);
    }

    public static void handle(ServerDidLightTravelPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
//            ServerPlayer serverPlayer = context.getSender();
//
//            if (serverPlayer != null) {
//
//                Entity entity = serverPlayer.level.getEntity(message.spaceVehicleId);
//
//                ArrayList<Entity> passengers = null;
//
//                if (message.additionalEntities != null) {
//                    passengers = new ArrayList<>();
//                    for (int entityId : message.additionalEntities) {
//                        passengers.add(serverPlayer.level.getEntity(entityId));
//                    }
//                }
//
//
//                TeleportUtil.finishLightSpeedTravel(entity, passengers, message.planetPos);
//
////                Entity entity = serverPlayer.level.getEntity(message.spaceVehicleId);
////                ISpaceFlight flightCap = CelestialLib.getCapability(entity, CLibCapabilities.SPACE_FLIGHT_CAPABILITY);
////
////                if (flightCap != null) {
////
////                    entity.moveTo(message.planetPos.getX(), message.planetPos.getY(), message.planetPos.getZ());
////
////                    if (message.additionalEntities != null) {
////                        for (int entityId : message.additionalEntities) {
////                            Entity passenger = serverPlayer.level.getEntity(entityId);
////                            if (passenger != null) {
////                                passenger.moveTo(message.planetPos.getX(), message.planetPos.getY(), message.planetPos.getZ());
////                                passenger.startRiding(entity);
////                            }
////                        }
////                    }
////                }
//            }
        });
        context.setPacketHandled(true);
    }
}