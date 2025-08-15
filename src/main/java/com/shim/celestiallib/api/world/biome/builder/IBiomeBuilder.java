package com.shim.celestiallib.api.world.biome.builder;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.function.Consumer;

public interface IBiomeBuilder {

    /**
     * Sorted by depth and then by temperature.
     * @return 2D array of ocean biomes; should be of size [2][5]
     */
    ResourceKey<Biome>[][] getOceans();

    /**
     * Sorted by temperature, and then humidity.
     * @return 2D array of biomes; should be of size [5][5]
     */
    ResourceKey<Biome>[][] getMiddleBiomes();

    /**
     * Sorted by temperature, and then humidity.
     * Individual values can be null, in which case they default to values in {@link #getMiddleBiomes()}
     * @return 2D array of biomes; should be of size [5][5]
     */
    ResourceKey<Biome>[][] getMiddleBiomeVariants();

    /**
     * Sorted by temperature, and then humidity.
     * @return 2D array of biomes; should be of size [5][5]
     */
    ResourceKey<Biome>[][] getPlateauBiomes();

    /**
     * Sorted by temperature, and then humidity.
     * Individual values can be null, in which case they default to values in {@link #getPlateauBiomes()}
     * @return 2D array of biomes; should be of size [5][5]
     */
    ResourceKey<Biome>[][] getPlateauBiomeVariants();

    /**
     * Sorted by temperature, and then humidity.
     * Individual values can be null, in which case they default to values in {@link #getMiddleBiomes()}
     * @return 2D array of biomes; should be of size [5][5]
     */
    ResourceKey<Biome>[][] getShatteredBiomes();

    /**
     * Sorted by temperature
     * @return array of biomes; should be of size [5]
     */
    ResourceKey<Biome>[] getBeachBiomes();

    /**
     * Specifically a biome of low continentalness, i.e. island-y
     * This is where the Overworld puts the Mushroom Island biome
     */
    ResourceKey<Biome> getMushroomIslandBiome();

    /**
     * Specifically a biome of coast continentalness and low erosion
     * This is where the Overworld puts the Stony Shore biome
     */
    ResourceKey<Biome> getStonyShoreBiome();

    /**
     * Specifically a biome of inland continentalness and high erosion
     * This is where the Overworld puts the Swamp biome
     */
    ResourceKey<Biome> getSwampBiome();

    /**
     * River biome, alternatively can return a valley biome for a dry planet/dimension
     */
    ResourceKey<Biome> getRiverBiome();

    /**
     * Frozen river biome, alternatively can return a valley biome for a dry planet/dimension
     */
    ResourceKey<Biome> getFrozenRiverBiome();

    /**
     * Return specific peak biomes based off temp, humidity, and/or weirdness
     */
    ResourceKey<Biome> pickPeakBiome(int temp, int humidity, Climate.Parameter weirdness);

    /**
     * Return specific slope biomes based off temp, humidity, and/or weirdness
     */
    ResourceKey<Biome> pickSlopeBiome(int temp, int humidity, Climate.Parameter weirdness);

    /**
     * Return specific badlands biomes based off humidity and/or weirdness
     */
    ResourceKey<Biome> pickBadlandsBiome(int humidity, Climate.Parameter weirdness);


    /**
     * Override and use {@link AbstractBiomeBuilder#addUndergroundBiome(Consumer, Climate.Parameter, Climate.Parameter, Climate.Parameter, Climate.Parameter, Climate.Parameter, float, ResourceKey) addUndergroundBiome} to add underground biomes
     * @param weirdness
     */
    default void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> weirdness) {}
}