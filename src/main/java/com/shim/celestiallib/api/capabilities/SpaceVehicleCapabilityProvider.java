package com.shim.celestiallib.api.capabilities;

import net.minecraftforge.common.util.LazyOptional;

public class SpaceVehicleCapabilityProvider extends SpaceFlightProvider {
    private final LazyOptional<ISpaceFlight> lazySpaceshipFlight = LazyOptional.of(SpaceVehicleFlightHandler::new);
    @Override
    public LazyOptional<ISpaceFlight> getLazySpaceshipFlight() {
        return lazySpaceshipFlight;
    }
}