package com.shim.celestiallib.api.world.planet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class Moon extends Planet {
    Planet planetOrbited;

    public Moon(ResourceKey<Level> dimension, Planet planetOrbited) {
        super(dimension, planetOrbited.getGalaxy());
        this.planetOrbited = planetOrbited;
    }

    @Override
    public boolean isMoon() {
        return true;
    }

    public Planet getPlanetOrbited() {
        return this.planetOrbited;
    }
}