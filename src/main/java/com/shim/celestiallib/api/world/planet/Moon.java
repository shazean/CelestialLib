package com.shim.celestiallib.api.world.planet;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.util.TeleportUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class Moon extends Planet {
    Planet planetOrbited;

    public Moon(ResourceKey<Level> dimension, Planet planetOrbited) {
        super(dimension, planetOrbited.getGalaxy());
        this.planetOrbited = planetOrbited;
        TeleportUtil.addMoon(planetOrbited.getDimension(), dimension);
    }

    @Override
    public boolean isMoon() {
        return true;
    }

    public Planet getPlanetOrbited() {
        return this.planetOrbited;
    }

    @Override
    public String toString() {
        return "[" + CelestialLib.MODID + ":moon / " + this.location() + "]";
    }
}