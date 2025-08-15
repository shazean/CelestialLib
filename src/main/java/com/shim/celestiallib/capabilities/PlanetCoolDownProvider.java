package com.shim.celestiallib.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlanetCoolDownProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    private final LazyOptional<ICoolDown> lazyCooldown = LazyOptional.of(PlanetCoolDownHandler::new);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CLibCapabilities.COOLDOWN_CAPABILITY.orEmpty(cap, lazyCooldown);
    }

    @Override
    public CompoundTag serializeNBT() {
        return lazyCooldown.orElseThrow(NullPointerException::new).getData();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        lazyCooldown.orElseThrow(NullPointerException::new).setData(nbt);
    }
}