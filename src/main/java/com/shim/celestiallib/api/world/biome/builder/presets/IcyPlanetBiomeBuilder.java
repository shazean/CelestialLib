package com.shim.celestiallib.api.world.biome.builder.presets;

import com.shim.celestiallib.api.world.biome.builder.AbstractBiomeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

public class IcyPlanetBiomeBuilder extends AbstractBiomeBuilder {

    private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][]{
            {Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN},
            {Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN}};

    private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA},
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {Biomes.FLOWER_FOREST, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS},
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS}};
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
            {Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null},
            {null, null, null, null, Biomes.SNOWY_TAIGA},
            {Biomes.SUNFLOWER_PLAINS, null, null, Biomes.SNOWY_TAIGA, null},
            {null, null, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {null, null, null, null, null}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][]{
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
            {Biomes.ICE_SPIKES, null, null, null, null},
            {null, null, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA},
            {null, null, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, null},
            {null, null, null, null, null},
            {Biomes.ICE_SPIKES, Biomes.ICE_SPIKES, null, null, null}};
    private final ResourceKey<Biome>[][] SHATTERED_BIOMES = new ResourceKey[][]{
            {Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES},
            {Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES},
            {Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES, Biomes.SNOWY_SLOPES},
            {null, null, null, null, null},
            {null, null, null, null, null}};


    @Override
    public ResourceKey<Biome>[][] getOceans() {
        return this.OCEANS;
    }

    @Override
    public ResourceKey<Biome>[][] getMiddleBiomes() {
        return this.MIDDLE_BIOMES;
    }

    @Override
    public ResourceKey<Biome>[][] getMiddleBiomeVariants() {
        return this.MIDDLE_BIOMES_VARIANT;
    }

    @Override
    public ResourceKey<Biome>[][] getPlateauBiomes() {
        return this.PLATEAU_BIOMES;
    }

    @Override
    public ResourceKey<Biome>[][] getPlateauBiomeVariants() {
        return this.PLATEAU_BIOMES_VARIANT;
    }

    @Override
    public ResourceKey<Biome>[][] getShatteredBiomes() {
        return this.SHATTERED_BIOMES;
    }

    @Override
    public ResourceKey<Biome>[] getBeachBiomes() {
        return new ResourceKey[]{Biomes.SNOWY_BEACH, Biomes.SNOWY_BEACH, Biomes.SNOWY_BEACH, Biomes.SNOWY_BEACH, Biomes.SNOWY_BEACH};
    }

    @Override
    public ResourceKey<Biome> getMushroomIslandBiome() {
        return Biomes.ICE_SPIKES;
    }

    @Override
    public ResourceKey<Biome> getStonyShoreBiome() {
        return Biomes.SNOWY_BEACH;
    }

    @Override
    public ResourceKey<Biome> getSwampBiome() {
        return Biomes.SNOWY_PLAINS;
    }

    @Override
    public ResourceKey<Biome> getRiverBiome() {
        return Biomes.FROZEN_RIVER;
    }

    @Override
    public ResourceKey<Biome> getFrozenRiverBiome() {
        return Biomes.FROZEN_RIVER;
    }

    @Override
    public ResourceKey<Biome> pickPeakBiome(int temp, int humidity, Climate.Parameter weirdness) {
        if (temp <= 2) {
            return Biomes.FROZEN_PEAKS;
        } else {
            return temp == 3 ? Biomes.FROZEN_PEAKS : this.pickBadlandsBiome(humidity, weirdness);
        }
    }

    @Override
    public ResourceKey<Biome> pickSlopeBiome(int temp, int humidity, Climate.Parameter weirdness) {
        if (temp >= 3) {
            return this.pickPlateauBiome(temp, humidity, weirdness);
        } else {
            return humidity <= 1 ? Biomes.SNOWY_SLOPES : Biomes.SNOWY_PLAINS;
        }
    }

    @Override
    public ResourceKey<Biome> pickBadlandsBiome(int humidity, Climate.Parameter weirdness) {
        if (humidity < 2) {
            return weirdness.max() < 0L ? Biomes.ICE_SPIKES : Biomes.SNOWY_PLAINS;
        } else {
            return humidity < 3 ? Biomes.SNOWY_PLAINS : Biomes.SNOWY_TAIGA;
        }
    }
}