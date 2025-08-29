package com.shim.celestiallib.world.celestials;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface ICelestial {

    ICelestial locked();
    ICelestial lightSpeedLockedAndMaybeHidden(boolean isHidden);
    boolean isLocked();
    boolean isLightSpeedLocked();
    ResourceKey<Level> getDimension();
    boolean isGalaxy();


}