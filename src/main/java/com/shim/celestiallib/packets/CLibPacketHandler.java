package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class CLibPacketHandler {

    int id = 0;

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CelestialLib.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int i = 0;

        INSTANCE.registerMessage(i++, SpaceFlightPacket.class, SpaceFlightPacket::encoder,
                SpaceFlightPacket::decoder, SpaceFlightPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        INSTANCE.registerMessage(i++, LightSpeedMenuPacket.class, LightSpeedMenuPacket::encoder,
                LightSpeedMenuPacket::decoder, LightSpeedMenuPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        INSTANCE.registerMessage(i++, DoLightTravelPacket.class, DoLightTravelPacket::encoder,
                DoLightTravelPacket::decoder, DoLightTravelPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        INSTANCE.registerMessage(i++, ServerDidLightTravelPacket.class, ServerDidLightTravelPacket::encoder,
                ServerDidLightTravelPacket::decoder, ServerDidLightTravelPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

//        INSTANCE.registerMessage(i++, ServerResetLightTravelPacket.class, ServerResetLightTravelPacket::encoder,
//                ServerResetLightTravelPacket::decoder, ServerResetLightTravelPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));


    }
}