package com.shim.celestiallib.world.celestials;

import com.shim.celestiallib.api.effects.GravityEffect;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public interface ICelestial {

    void setTravelLocked();
    void setLightSpeedLockedAndMaybeHidden(boolean isHidden);
    boolean isTravelLocked();
    boolean isLightSpeedLocked();
    ResourceKey<Level> getDimension();
    boolean isGalaxy();
    GravityEffect getGravity();

    default ResourceLocation location() {
        return this.getDimension().location();
    }
}