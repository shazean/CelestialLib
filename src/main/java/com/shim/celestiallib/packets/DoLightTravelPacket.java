package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.ISpaceFlight;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.util.TeleportUtil;
import com.shim.celestiallib.world.galaxy.Galaxy;
import com.shim.celestiallib.world.planet.Planet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
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
    private final ResourceKey<Level> planet;
    private final float distance;

    public DoLightTravelPacket(int spaceVehicleId, @Nullable ArrayList<Integer> additionalEntitiesToTeleport, ResourceLocation galaxy, ResourceLocation planet, float distance) {
        this(spaceVehicleId, additionalEntitiesToTeleport, ResourceKey.create(Registry.DIMENSION_REGISTRY, galaxy), ResourceKey.create(Registry.DIMENSION_REGISTRY, planet), distance);
    }

    public DoLightTravelPacket(int spaceVehicleId, @Nullable ArrayList<Integer> additionalEntitiesToTeleport, ResourceKey<Level> galaxy, ResourceKey<Level> planet, float distance) {

        this.spaceVehicleId = spaceVehicleId;
        this.additionalEntities = additionalEntitiesToTeleport;
        this.galaxy = galaxy;
        this.planet = planet;
        this.distance = distance;

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
        buffer.writeResourceLocation(packet.planet.location());
        buffer.writeFloat(packet.distance);
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
        ResourceLocation planet = buffer.readResourceLocation();

        float distance = buffer.readFloat();

        return new DoLightTravelPacket(spaceVehicleId, additionalEntities, galaxy, planet, distance);
    }

    public static void handle(DoLightTravelPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        CelestialLib.LOGGER.debug("handling doLightTravel packet?");

        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();


            if (serverPlayer != null) {

                Inventory inv = serverPlayer.getInventory();

                Galaxy galaxy = Galaxy.getGalaxy(message.galaxy);
                Planet planet = Planet.getPlanet(message.planet);
                int cost = galaxy.getLightSpeedCost(Galaxy.getGalaxy(serverPlayer.level.dimension())).getCount();
//            int planetCost = Planet.getPlanet(message.planet).getLightSpeedCost().getCount();


                while (cost > 0) {
                    for (ItemStack item : inv.items) {
                        CelestialLib.LOGGER.debug("running remove galaxy cost items… i: " + cost);
                        if (item.is(galaxy.getLightSpeedCost(Galaxy.getGalaxy(serverPlayer.level.dimension())).getItem())) {
                            int i = Math.min(item.getCount(), cost);
                            cost -= i;
                            item.shrink(i);
                            if (cost == 0) break;
                        }
                    }
                }

                cost = planet.getLightSpeedCost(message.distance).getCount();

                while (cost > 0) {
                    CelestialLib.LOGGER.debug("running remove planet cost items… i: " + cost);
                    for (ItemStack item : inv.items) {
                        if (item.is(planet.getLightSpeedCost(message.distance).getItem())) {
                            int i = Math.min(item.getCount(), cost);
                            cost -= i;
                            item.shrink(i);
                            if (cost == 0) break;
                        }
                    }
                }

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

                    TeleportUtil.handleLightSpeedTravel(serverPlayer, entity, passengers, message.galaxy, CelestialUtil.getPlanetBlockCoordinates(planet.getDimension()));

//                    TeleportUtil.teleport(entity, passengers, message.galaxy, message.planetPos);
//
//                    CLibPacketHandler.INSTANCE.sendTo(new ServerDidLightTravelPacket(message.spaceVehicleId, message.additionalEntities, message.planetPos), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
                }
            }
        });
        context.setPacketHandled(true);
    }
}