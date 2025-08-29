package com.shim.celestiallib.capabilities;

import com.shim.celestiallib.world.celestials.ICelestial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IUnlock {

    boolean isCelestialLocked(ICelestial celestial);
    void unlockCelestial(ICelestial celestial);
    boolean isCelestialLightSpeedLocked(ICelestial celestial);
    void unlockCelestialLightSpeed(ICelestial celestial);

    void sync(Player player);

    CompoundTag getData();
    void setData(CompoundTag nbt);

}