package com.shim.celestiallib.world.biome.builder.presets;

import com.shim.celestiallib.world.biome.builder.AbstractBiomeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

public class ForestPlanetBiomeBuilder extends AbstractBiomeBuilder {

    private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][]{
            {Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN},
            {Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN}};
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
            {Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.TAIGA},
            {Biomes.FLOWER_FOREST, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
            {Biomes.FLOWER_FOREST, Biomes.MEADOW, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST},
            {Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE},
            {Biomes.SPARSE_JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.JUNGLE}};
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
            {null, null, Biomes.SNOWY_TAIGA, null, null},
            {null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA},
            {Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null},
            {null, null, null, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE},
            {null, null, null, Biomes.BAMBOO_JUNGLE, null}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][]{
            {Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA},
            {Biomes.MEADOW, Biomes.FOREST, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
            {Biomes.FOREST, Biomes.MEADOW, Biomes.FOREST, Biomes.FOREST, Biomes.DARK_FOREST},
            {Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE},
            {Biomes.SPARSE_JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
            {Biomes.ICE_SPIKES, null, null, null, null},
            {null, null, Biomes.DARK_FOREST, Biomes.DARK_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA},
            {null, null, Biomes.FOREST, Biomes.BIRCH_FOREST, null},
            {null, null, null, null, null},
            {Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS, null, null, null}};
    private final ResourceKey<Biome>[][] SHATTERED_BIOMES = new ResourceKey[][]{
            {Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
            {Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
            {Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST},
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
        return new ResourceKey[]{Biomes.SNOWY_TAIGA, Biomes.FOREST, Biomes.FOREST, Biomes.FOREST, Biomes.FOREST};
    }

    @Override
    public ResourceKey<Biome> getMushroomIslandBiome() {
        return Biomes.OLD_GROWTH_SPRUCE_TAIGA;
    }

    @Override
    public ResourceKey<Biome> getStonyShoreBiome() {
        return Biomes.FOREST;
    }

    @Override
    public ResourceKey<Biome> getSwampBiome() {
        return Biomes.SWAMP;
    }

    @Override
    public ResourceKey<Biome> getRiverBiome() {
        return Biomes.RIVER;
    }

    @Override
    public ResourceKey<Biome> getFrozenRiverBiome() {
        return Biomes.FROZEN_RIVER;
    }

    @Override
    public ResourceKey<Biome> pickPeakBiome(int temp, int humidity, Climate.Parameter weirdness) {
        if (temp <= 2) {
            return weirdness.max() < 0L ? Biomes.FOREST : Biomes.SNOWY_TAIGA;
        } else {
            return temp == 3 ? Biomes.FOREST : this.pickBadlandsBiome(humidity, weirdness);
        }
    }

    @Override
    public ResourceKey<Biome> pickSlopeBiome(int temp, int humidity, Climate.Parameter weirdness) {
        if (temp >= 3) {
            return this.pickPlateauBiome(temp, humidity, weirdness);
        } else {
            return Biomes.SNOWY_TAIGA;
        }
    }

    @Override
    public ResourceKey<Biome> pickBadlandsBiome(int humidity, Climate.Parameter weirdness) {
        return Biomes.WOODED_BADLANDS;
    }
}