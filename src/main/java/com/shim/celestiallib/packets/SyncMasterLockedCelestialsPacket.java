package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.world.celestials.ICelestial;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncMasterLockedCelestialsPacket {

    final Map<ResourceLocation, List<ICelestial>> travelLockedData;
    final Map<ResourceLocation, List<ICelestial>> lightSpeedLockedData;
    final Map<ICelestial, Boolean> lightSpeedHidden;

    public SyncMasterLockedCelestialsPacket() {
        this(CelestialUtil.getTravelLockedCelestialData(), CelestialUtil.getLightSpeedLockedCelestialData(), CelestialUtil.getLightSpeedHiddenData());
    }

    public SyncMasterLockedCelestialsPacket(Map<ResourceLocation, List<ICelestial>> travelLockedData, Map<ResourceLocation, List<ICelestial>> lightSpeedLockedData, Map<ICelestial, Boolean> lightSpeedHidden) {
        this.travelLockedData = travelLockedData;
        this.lightSpeedLockedData = lightSpeedLockedData;
        this.lightSpeedHidden = lightSpeedHidden;
    }

    public static void encoder(SyncMasterLockedCelestialsPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.travelLockedData, FriendlyByteBuf::writeResourceLocation,
                (buf, loc) -> {
                    buf.writeInt(loc.size());
                    CelestialLib.LOGGER.debug("encoder: size: " + loc.size());
                    for (ICelestial celestial : loc) {
                        CelestialLib.LOGGER.debug("encoder: celestial: " + celestial.location());
                        buf.writeResourceLocation(celestial.location());
                        CelestialLib.LOGGER.debug("encoder: isGalaxy: " + celestial.isGalaxy());
                        buf.writeBoolean(celestial.isGalaxy());
                    }
                });
        buffer.writeMap(packet.lightSpeedLockedData, FriendlyByteBuf::writeResourceLocation,
                (buf, loc) -> {
                    buf.writeInt(loc.size());
                    CelestialLib.LOGGER.debug("encoder ls: size: " + loc.size());
                    for (ICelestial celestial : loc) {
                        CelestialLib.LOGGER.debug("encoder ls: celestial: " + celestial.location());
                        buf.writeResourceLocation(celestial.location());
                        CelestialLib.LOGGER.debug("encoder ls: isGalaxy: " + celestial.isGalaxy());
                        buf.writeBoolean(celestial.isGalaxy());
                    }
                });
        buffer.writeMap(packet.lightSpeedHidden, (buf, celestial) -> {
            buf.writeResourceLocation(celestial.location());
            buf.writeBoolean(celestial.isGalaxy());
        }, FriendlyByteBuf::writeBoolean);
    }

    public static SyncMasterLockedCelestialsPacket decoder(FriendlyByteBuf buffer) {
        return new SyncMasterLockedCelestialsPacket(buffer.readMap(
               FriendlyByteBuf::readResourceLocation,
                (buf) -> {
                   int size = buf.readInt();
                   CelestialLib.LOGGER.debug("decoder: size: " + size);

                   List<ICelestial> list = new ArrayList<>();
                   for (int i = 0; i < size; i++) {
                       ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
                       CelestialLib.LOGGER.debug("decoder: dimension: " + dimension);

                       boolean isGalaxy = buf.readBoolean();
                       CelestialLib.LOGGER.debug("decoder: isGalaxy: " + isGalaxy);

                       ICelestial celestial = isGalaxy ? Galaxy.getGalaxy(dimension) : Planet.getPlanet(dimension);
                       CelestialLib.LOGGER.debug("decoder: celestial: " + celestial);

                       list.add(celestial);
                       CelestialLib.LOGGER.debug("decoder: list: " + list);


                   }
                   return list;
                }),
                buffer.readMap(
                        FriendlyByteBuf::readResourceLocation,
                        (buf) -> {
                            int size = buf.readInt();
                            CelestialLib.LOGGER.debug("decoder ls: size: " + size);
                            List<ICelestial> list = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
                                CelestialLib.LOGGER.debug("decoder ls: dimension: " + dimension);

                                boolean isGalaxy = buf.readBoolean();
                                CelestialLib.LOGGER.debug("decoder ls: isGalaxy: " + isGalaxy);

                                ICelestial celestial = isGalaxy ? Galaxy.getGalaxy(dimension) : Planet.getPlanet(dimension);
                                CelestialLib.LOGGER.debug("decoder ls: celestial: " + celestial);

                                list.add(celestial);
                                CelestialLib.LOGGER.debug("decoder ls: list: " + list);

                            }
                            return list;
                        }),
                buffer.readMap((buf) -> {
                    ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation());
                    boolean isGalaxy = buf.readBoolean();
                    return isGalaxy ? Galaxy.getGalaxy(dimension) : Planet.getPlanet(dimension);
                }, FriendlyByteBuf::readBoolean));
    }

    public static void handle(SyncMasterLockedCelestialsPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> CelestialUtil.syncLockedCelestials(message.travelLockedData, message.lightSpeedLockedData, message.lightSpeedHidden));
        context.setPacketHandled(true);
    }
}