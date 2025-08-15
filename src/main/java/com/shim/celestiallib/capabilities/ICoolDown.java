package com.shim.celestiallib.capabilities;

import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface ICoolDown {

    PlanetCooldown getCooldown(Planet planet);

    void decrementCooldowns();
    void resetCooldown(Planet planet);
    void setVisited(Planet planet);

    void sync(Player player);

    CompoundTag getData();
    void setData(CompoundTag nbt);

}
