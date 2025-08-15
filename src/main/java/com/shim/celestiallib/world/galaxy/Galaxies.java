package com.shim.celestiallib.world.galaxy;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class Galaxies {

    public static final DeferredRegister<Galaxy> GALAXY_REGISTRY = DeferredRegister.create(new ResourceLocation(CelestialLib.MODID, "galaxy_registry"), CelestialLib.MODID);
    public static final DeferredRegister<Galaxy> GALAXIES = DeferredRegister.create(GALAXY_REGISTRY.getRegistryName(), CelestialLib.MODID);
    public static final Supplier<IForgeRegistry<Galaxy>> GALAXIES_SUPPLIER = GALAXIES.makeRegistry(Galaxy.class, RegistryBuilder::new);

}