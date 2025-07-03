package com.shim.celestiallib.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SpaceFlightProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    /**
     * @return LazyOptional of your implementation of ISpaceFlight
     */
    public abstract LazyOptional<ISpaceFlight> getLazySpaceshipFlight();

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CLibCapabilities.SPACE_FLIGHT_CAPABILITY.orEmpty(cap, getLazySpaceshipFlight());
    }

    @Override
    public CompoundTag serializeNBT() {
        return getLazySpaceshipFlight().orElseThrow(NullPointerException::new).getData();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getLazySpaceshipFlight().orElseThrow(NullPointerException::new).setData(nbt);
    }
}