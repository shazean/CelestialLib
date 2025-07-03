package com.shim.celestiallib.world.planet;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.effects.CelestialLibEffects;
import com.shim.celestiallib.world.galaxy.Galaxies;
import com.shim.celestiallib.world.galaxy.Galaxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class Planets {

//    public static final ResourceKey<Registry<Planet>> PLANETS_KEY  = ResourceKey.createRegistryKey(new ResourceLocation(CelestialLib.MODID, "planet"));
//    public static final IForgeRegistry<Planet> PLANET_REGISTRY = RegistryManager.ACTIVE.getRegistry(PLANETS_KEY);
    public static final DeferredRegister<Planet> PLANET_REGISTRY = DeferredRegister.create(new ResourceLocation(CelestialLib.MODID, "planet_registry"), CelestialLib.MODID);
    public static final DeferredRegister<Galaxy> PLANETS = DeferredRegister.create(PLANET_REGISTRY.getRegistryName(), CelestialLib.MODID);
    public static final Supplier<IForgeRegistry<Planet>> PLANETS_SUPPLIER = PLANETS.makeRegistry(Planet.class, RegistryBuilder::new);


//    public static final ResourceKey<Level> TEST_DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(CelestialLib.MODID, "test"));
//
//    public static final RegistryObject<Planet> TEST_PLANET = PLANETS.register("test_planet", () ->
//            new Planet(TEST_DIMENSION, (dimension) -> false, Galaxies.TEST_GALAXY.get()).setGravity(CelestialLibEffects.LOW_GRAVITY.get()));

}