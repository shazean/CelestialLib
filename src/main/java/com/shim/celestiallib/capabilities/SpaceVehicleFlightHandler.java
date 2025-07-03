package com.shim.celestiallib.capabilities;

import net.minecraft.world.entity.Entity;

import java.util.ArrayList;

/**
 * Default implementation of ISpaceFlight
 */
public class SpaceVehicleFlightHandler implements ISpaceFlight {
    private int teleportationCooldown = 60;
    ArrayList<Entity> teleportingEntities;

    @Override
    public boolean canSpaceTravel(Entity entity) {
        return true;
    }

    @Override
    public int getTeleportationCooldown() {
        return teleportationCooldown;
    }

    public void setTeleportationCooldown(int cooldown) {
        teleportationCooldown = cooldown;
    }

    @Override
    public void decrementTeleportationCooldown() {
        teleportationCooldown--;
    }

    @Override
    public void resetTeleportationCooldown() {
        teleportationCooldown = 60;
    }

    @Override
    public ArrayList<Entity> getAdditionalEntitiesToTeleport(Entity vehicle) {
        teleportingEntities = new ArrayList<>();

        if (vehicle.isVehicle()) teleportingEntities.addAll(vehicle.getPassengers());
        return teleportingEntities;
    }
}