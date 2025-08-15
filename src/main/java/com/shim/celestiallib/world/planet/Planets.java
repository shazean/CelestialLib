package com.shim.celestiallib.world.planet;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class Planets {

    public static final DeferredRegister<Planet> PLANET_REGISTRY = DeferredRegister.create(new ResourceLocation(CelestialLib.MODID, "planet_registry"), CelestialLib.MODID);
    public static final DeferredRegister<Planet> PLANETS = DeferredRegister.create(PLANET_REGISTRY.getRegistryName(), CelestialLib.MODID);
    public static final Supplier<IForgeRegistry<Planet>> PLANETS_SUPPLIER = PLANETS.makeRegistry(Planet.class, RegistryBuilder::new);

}