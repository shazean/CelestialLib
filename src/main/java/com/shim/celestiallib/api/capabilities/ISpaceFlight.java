package com.shim.celestiallib.api.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;

public interface ISpaceFlight {
    /**
     * Checks if entity is allowed/capable of space travel.
     * Can be used to check for certain equipment, etc.
     * @param entity The entity to do the check on
     **/
    boolean canSpaceTravel(Entity entity);

    /**
     * Similar to canSpaceTravel, but this check is called when light speed travel is attempted
     * Can be overridden to allow for unique checks
     * @param entity The entity to do the check on
     */
    default boolean canLightSpeedTravel(Entity entity) {
        return this.canSpaceTravel(entity);
    }

    int getTeleportationCooldown();
    void setTeleportationCooldown(int cooldown);
    void decrementTeleportationCooldown();
    void resetTeleportationCooldown();

    /**
     * Check if entity should be bringing additional entities with, i.e. passengers
     * @param entity The entity to do the check on
     * @return ArrayList of all additional entities, excluding self
     */
    ArrayList<Entity> getAdditionalEntitiesToTeleport(Entity entity);

    default boolean isTeleportHeight(Entity entity) {
        return entity.position().y >= entity.level.getMaxBuildHeight() + 10;
    }

    default CompoundTag getData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("cooldown", this.getTeleportationCooldown());

        return nbt;
    }

    default void setData(CompoundTag nbt) {
        if (nbt.contains("cooldown")) this.setTeleportationCooldown(nbt.getInt("cooldown"));
    }

    default double pickDistance(Entity entity) {
        return 18.0D;
    }
}