package com.shim.celestiallib.world.structures;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.structure.PlanetStructure;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CLibStructures {

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, CelestialLib.MODID);

    public static final RegistryObject<StructureFeature<?>> PLANET = DEFERRED_REGISTRY_STRUCTURE.register("planet", PlanetStructure::new);

}
