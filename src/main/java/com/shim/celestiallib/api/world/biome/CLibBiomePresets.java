package com.shim.celestiallib.api.world.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.biome.builder.presets.DesertPlanetBiomeBuilder;
import com.shim.celestiallib.api.world.biome.builder.presets.ForestPlanetBiomeBuilder;
import com.shim.celestiallib.api.world.biome.builder.presets.IcyPlanetBiomeBuilder;
import com.shim.celestiallib.api.world.biome.builder.presets.OceanPlanetBiomeBuilder;
import com.shim.celestiallib.world.biome.CLibBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CLibBiomePresets {

    public static final MultiNoiseBiomeSource.Preset DESERT_PRESET = new MultiNoiseBiomeSource.Preset(new ResourceLocation(CelestialLib.MODID, "desert"), (biome) -> {
        ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
        new DesertPlanetBiomeBuilder().addBiomes((p) -> builder.add(p.mapSecond(biome::getOrCreateHolder)));
        return new Climate.ParameterList<>(builder.build());
    });

    public static final MultiNoiseBiomeSource.Preset ICY_PRESET = new MultiNoiseBiomeSource.Preset(new ResourceLocation(CelestialLib.MODID, "icy"), (biome) -> {
        ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
        new IcyPlanetBiomeBuilder().addBiomes((p) -> builder.add(p.mapSecond(biome::getOrCreateHolder)));
        return new Climate.ParameterList<>(builder.build());
    });

    public static final MultiNoiseBiomeSource.Preset FOREST_PRESET = new MultiNoiseBiomeSource.Preset(new ResourceLocation(CelestialLib.MODID, "forest"), (biome) -> {
        ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
        new ForestPlanetBiomeBuilder().addBiomes((p) -> builder.add(p.mapSecond(biome::getOrCreateHolder)));
        return new Climate.ParameterList<>(builder.build());
    });

    public static final MultiNoiseBiomeSource.Preset OCEAN_PRESET = new MultiNoiseBiomeSource.Preset(new ResourceLocation(CelestialLib.MODID, "ocean"), (biome) -> {
        ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
        new OceanPlanetBiomeBuilder().addBiomes((p) -> builder.add(p.mapSecond(biome::getOrCreateHolder)));
        return new Climate.ParameterList<>(builder.build());
    });

    public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCE = DeferredRegister.create(Registry.BIOME_SOURCE_REGISTRY, CelestialLib.MODID);
    public static final RegistryObject<Codec<CLibBiomeSource>> CELESTIAL_BIOMES = BIOME_SOURCE.register(CelestialLib.MODID, () -> CLibBiomeSource.CODEC);

}
