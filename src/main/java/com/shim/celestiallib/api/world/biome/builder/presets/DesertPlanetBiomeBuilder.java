package com.shim.celestiallib.api.world.biome.builder.presets;

import com.shim.celestiallib.api.world.biome.builder.AbstractBiomeBuilder;
import com.shim.celestiallib.world.biome.PresetBiomeKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

public class DesertPlanetBiomeBuilder extends AbstractBiomeBuilder {

    private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][]{
            {PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.LOW_DESERT},
            {PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.LOW_DESERT}};
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
            {PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.ICY_DESERT},
            {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT},
            {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.HIGH_DESERT},
            {PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.MONSOON_DESERT, PresetBiomeKeys.MONSOON_DESERT},
            {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT}};
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{
            {PresetBiomeKeys.HIGH_DESERT, null, PresetBiomeKeys.HIGH_DESERT, null, null},
            {null, null, null, null, PresetBiomeKeys.HIGH_DESERT},
            {PresetBiomeKeys.HIGH_DESERT, null, null, PresetBiomeKeys.HIGH_DESERT, null},
            {null, null, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.MONSOON_DESERT, PresetBiomeKeys.MONSOON_DESERT},
            {null, null, null, null, null}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][]{
            {PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.ICY_DESERT},
            {Biomes.DESERT, Biomes.DESERT, PresetBiomeKeys.HIGH_DESERT, Biomes.DESERT, PresetBiomeKeys.HIGH_DESERT},
            {PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.HIGH_DESERT, Biomes.DESERT, Biomes.DESERT},
            {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, PresetBiomeKeys.MONSOON_DESERT},
            {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{
            {Biomes.ICE_SPIKES, null, null, null, null},
            {null, null, Biomes.DESERT, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.HIGH_DESERT},
            {null, null, Biomes.BADLANDS, Biomes.DESERT, null},
            {null, null, null, null, null},
            {Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null}};
    private final ResourceKey<Biome>[][] SHATTERED_BIOMES = new ResourceKey[][]{
            {null, null, null, null, null},
            {PresetBiomeKeys.DUNES, PresetBiomeKeys.DUNES, PresetBiomeKeys.DUNES, PresetBiomeKeys.DUNES, PresetBiomeKeys.HIGH_DESERT},
            {PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.DUNES, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.DUNES},
            {null, PresetBiomeKeys.DUNES, null, null, PresetBiomeKeys.HIGH_DESERT},
            {PresetBiomeKeys.DUNES, PresetBiomeKeys.DUNES, null, null, null}};

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
        return new ResourceKey[]{Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, PresetBiomeKeys.LOW_DESERT};
    }

    @Override
    public ResourceKey<Biome> getMushroomIslandBiome() {
        return Biomes.DESERT;
    }

    @Override
    public ResourceKey<Biome> getStonyShoreBiome() {
        return Biomes.DESERT;
    }

    @Override
    public ResourceKey<Biome> getSwampBiome() {
        return PresetBiomeKeys.MONSOON_DESERT;
    }

    @Override
    public ResourceKey<Biome> getRiverBiome() {
        return PresetBiomeKeys.LOW_DESERT;
    }

    @Override
    public ResourceKey<Biome> getFrozenRiverBiome() {
        return PresetBiomeKeys.ICY_DESERT;
    }

    @Override
    public ResourceKey<Biome> pickPeakBiome(int temp, int humidity, Climate.Parameter weirdness) {
        if (temp <= 2) {
            return weirdness.max() < 0L ? PresetBiomeKeys.HIGH_DESERT : PresetBiomeKeys.ICY_DESERT;
        } else {
            return temp == 3 ? PresetBiomeKeys.HIGH_DESERT : this.pickBadlandsBiome(humidity, weirdness);
        }
    }

    @Override
    public ResourceKey<Biome> pickSlopeBiome(int temp, int humidity, Climate.Parameter weirdness) {
        if (temp >= 3) {
            return this.pickPlateauBiome(temp, humidity, weirdness);
        } else {
            return humidity <= 1 ? PresetBiomeKeys.ICY_DESERT : PresetBiomeKeys.HIGH_DESERT;
        }
    }

    @Override
    public ResourceKey<Biome> pickBadlandsBiome(int humidity, Climate.Parameter weirdness) {
        if (humidity < 2) {
            return weirdness.max() < 0L ? Biomes.ERODED_BADLANDS : Biomes.BADLANDS;
        } else {
            return humidity < 3 ? Biomes.BADLANDS : Biomes.WOODED_BADLANDS;
        }
    }
}