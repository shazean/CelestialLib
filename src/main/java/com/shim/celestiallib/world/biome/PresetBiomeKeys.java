package com.shim.celestiallib.world.biome;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class PresetBiomeKeys {

    //---- FOR DATAPACK/PRESETS -------------------------------------------------------------------------------
    public static final ResourceKey<Biome> HIGH_DESERT = register("high_desert");
    public static final ResourceKey<Biome> LOW_DESERT = register("low_desert");
    public static final ResourceKey<Biome> ICY_DESERT = register("icy_desert");
    public static final ResourceKey<Biome> MONSOON_DESERT = register("monsoon_desert");
    public static final ResourceKey<Biome> DUNES = register("dunes");
    public static final ResourceKey<Biome> SHALLOW_OCEAN = register("shallow_ocean");
    public static final ResourceKey<Biome> ISLANDS = register("islands");


    private static ResourceKey<Biome> register(String key) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(CelestialLib.MODID, key));
    }

}
