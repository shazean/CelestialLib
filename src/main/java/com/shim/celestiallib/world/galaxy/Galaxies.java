package com.shim.celestiallib.world.galaxy;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class Galaxies {

//    public static final ResourceKey<Registry<Galaxy>> GALAXIES_KEY  = ResourceKey.createRegistryKey(new ResourceLocation(CelestialLib.MODID, "galaxy"));
    public static final DeferredRegister<Galaxy> GALAXY_REGISTRY = DeferredRegister.create(new ResourceLocation(CelestialLib.MODID, "galaxy_registry"), CelestialLib.MODID);
    public static final DeferredRegister<Galaxy> GALAXIES = DeferredRegister.create(GALAXY_REGISTRY.getRegistryName(), CelestialLib.MODID);
    public static final Supplier<IForgeRegistry<Galaxy>> GALAXIES_SUPPLIER = GALAXIES.makeRegistry(Galaxy.class, RegistryBuilder::new);


//    public static final ResourceKey<Level> TEST_DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(CelestialLib.MODID, "test"));
//
//    public static final RegistryObject<Galaxy> TEST_GALAXY = GALAXIES.register("galaxy", () ->
//            new Galaxy(Level.END, (dimension) -> 10));

}