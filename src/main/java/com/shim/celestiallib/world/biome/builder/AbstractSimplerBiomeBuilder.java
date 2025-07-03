package com.shim.celestiallib.world.biome.builder;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

public abstract class AbstractSimplerBiomeBuilder extends AbstractBiomeBuilder {

    @Override
    public ResourceKey<Biome> getMushroomIslandBiome() {
        return getPlateauBiomes()[2][2];
    }

    @Override
    public ResourceKey<Biome> getStonyShoreBiome() {
        return getPlateauBiomes()[2][0];
    }

    @Override
    public ResourceKey<Biome> getSwampBiome() {
        return getMiddleBiomes()[2][4];
    }

    @Override
    public ResourceKey<Biome> pickPeakBiome(int temp, int humidity, Climate.Parameter weirdness) {
        return getPlateauBiomes()[temp][humidity];
    }

    @Override
    public ResourceKey<Biome> pickSlopeBiome(int temp, int humidity, Climate.Parameter weirdness) {
        return getMiddleBiomes()[temp][humidity];
    }
}