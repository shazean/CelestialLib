package com.shim.celestiallib.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CLibCapabilities {

    public static final Capability<ISpaceFlight> SPACE_FLIGHT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilities(RegisterCapabilitiesEvent eventIn) {
        eventIn.register(ISpaceFlight.class);
    }

}