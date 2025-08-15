package com.shim.celestiallib.util.teleportation;

import com.shim.celestiallib.api.world.galaxy.Galaxy;
import net.minecraft.world.phys.Vec3;

public class CelestialCoordinateTeleport extends AbstractCelestialTeleportData {
    Galaxy galaxy;
    Vec3 coordinates;

    public CelestialCoordinateTeleport(Galaxy galaxy, int x, int z) {
        this.galaxy = galaxy;
        this.coordinates = new Vec3(x, 0, z);
    }

    @Override
    public Vec3 getOutputCoordinates(int x, int z) {
        return new Vec3(this.coordinates.x * this.galaxy.getGalaxyRatio(),
                this.coordinates.y * this.galaxy.getGalaxyRatio(),
                this.coordinates.z * this.galaxy.getGalaxyRatio());
    }
}
