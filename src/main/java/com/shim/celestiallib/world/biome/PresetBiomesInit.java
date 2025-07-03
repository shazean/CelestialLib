package com.shim.celestiallib.world.biome;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PresetBiomesInit {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registry.BIOME_REGISTRY, CelestialLib.MODID);

    //---- FOR DATAPACK/PRESETS -------------------------------------------------------------------------------
    public static final RegistryObject<Biome> HIGH_DESERT = BIOMES.register("high_desert", () -> PresetBiomeFeatures.desert(false, false));
    public static final RegistryObject<Biome> LOW_DESERT = BIOMES.register("low_desert",  () -> PresetBiomeFeatures.desert(false, false));
    public static final RegistryObject<Biome> ICY_DESERT = BIOMES.register("icy_desert", () -> PresetBiomeFeatures.desert(false, true));
    public static final RegistryObject<Biome> MONSOON_DESERT = BIOMES.register("monsoon_desert",  () -> PresetBiomeFeatures.desert(true, false));
    public static final RegistryObject<Biome> DUNES = BIOMES.register("dunes",  () -> PresetBiomeFeatures.desert(false, false));
    public static final RegistryObject<Biome> SHALLOW_OCEAN = BIOMES.register("shallow_ocean", PresetBiomeFeatures::ocean);
    public static final RegistryObject<Biome> ISLANDS = BIOMES.register("islands", PresetBiomeFeatures::island);

}
