package com.shim.celestiallib.world.biome.builder.presets;

import com.mojang.datafixers.util.Pair;
import com.shim.celestiallib.world.biome.PresetBiomeKeys;
import net.minecraft.SharedConstants;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.List;
import java.util.function.Consumer;

public class OceanPlanetBiomeBuilder {

    private static final float VALLEY_SIZE = 0.05F;
    private static final float LOW_START = 0.26666668F;
    public static final float HIGH_START = 0.4F;
    private static final float HIGH_END = 0.93333334F;
    private static final float PEAK_SIZE = 0.1F;
    public static final float PEAK_START = 0.56666666F;
    private static final float PEAK_END = 0.7666667F;
    public static final float NEAR_INLAND_START = -0.11F;
    public static final float MID_INLAND_START = 0.03F;
    public static final float FAR_INLAND_START = 0.3F;
    public static final float EROSION_INDEX_1_START = -0.78F;
    public static final float EROSION_INDEX_2_START = -0.375F;

    private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][]{
            {Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN},
            {Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN}};
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{
            {Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN},
            {Biomes.COLD_OCEAN, Biomes.COLD_OCEAN, Biomes.COLD_OCEAN, Biomes.COLD_OCEAN, Biomes.COLD_OCEAN},
            {Biomes.OCEAN, Biomes.OCEAN, Biomes.OCEAN, Biomes.OCEAN, Biomes.OCEAN},
            {Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN},
            {Biomes.WARM_OCEAN, Biomes.WARM_OCEAN, Biomes.WARM_OCEAN, Biomes.WARM_OCEAN, Biomes.WARM_OCEAN}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][]{
            {Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN},
            {Biomes.COLD_OCEAN, Biomes.COLD_OCEAN, Biomes.COLD_OCEAN, Biomes.COLD_OCEAN, Biomes.COLD_OCEAN},
            {Biomes.OCEAN, Biomes.OCEAN, Biomes.OCEAN, Biomes.OCEAN, Biomes.OCEAN},
            {Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.LUKEWARM_OCEAN},
            {Biomes.WARM_OCEAN, Biomes.WARM_OCEAN, Biomes.WARM_OCEAN, Biomes.WARM_OCEAN, Biomes.WARM_OCEAN}};

    protected final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
    protected final Climate.Parameter[] temperatures = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.45F), Climate.Parameter.span(-0.45F, -0.15F), Climate.Parameter.span(-0.15F, 0.2F), Climate.Parameter.span(0.2F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    protected final Climate.Parameter[] humidities = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.35F), Climate.Parameter.span(-0.35F, -PEAK_SIZE), Climate.Parameter.span(-PEAK_SIZE, PEAK_SIZE), Climate.Parameter.span(PEAK_SIZE, 0.3F), Climate.Parameter.span(0.3F, 1.0F)};
    protected final Climate.Parameter[] erosions = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, EROSION_INDEX_1_START), Climate.Parameter.span(EROSION_INDEX_1_START, EROSION_INDEX_2_START), Climate.Parameter.span(EROSION_INDEX_2_START, -0.2225F), Climate.Parameter.span(-0.2225F, VALLEY_SIZE), Climate.Parameter.span(VALLEY_SIZE, 0.45F), Climate.Parameter.span(0.45F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    protected final Climate.Parameter FROZEN_RANGE = this.temperatures[0];
    protected final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);
    protected final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
    protected final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
    protected final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, NEAR_INLAND_START);
    protected final Climate.Parameter inlandContinentalness = Climate.Parameter.span(NEAR_INLAND_START, 0.55F);
    protected final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(NEAR_INLAND_START, MID_INLAND_START);
    protected final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(MID_INLAND_START, FAR_INLAND_START);
    protected final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(FAR_INLAND_START, 1.0F);

    public List<Climate.ParameterPoint> spawnTarget() {
        Climate.Parameter climate$parameter = Climate.Parameter.point(0.0F);
        float f = 0.16F;
        return List.of(new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, climate$parameter, Climate.Parameter.span(-1.0F, -0.16F), 0L), new Climate.ParameterPoint(this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.FULL_RANGE), this.FULL_RANGE, climate$parameter, Climate.Parameter.span(0.16F, 1.0F), 0L));
    }

    protected void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        if (SharedConstants.debugGenerateSquareTerrainWithoutNoise) {
            TerrainProvider.overworld(false).addDebugBiomesToVisualizeSplinePoints(consumer);
        } else {
            this.addOffCoastBiomes(consumer);
            this.addInlandBiomes(consumer);
            this.addUndergroundBiomes(consumer);
        }
    }

    protected void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        for(int i = 0; i < this.temperatures.length; ++i) {
            Climate.Parameter tempParameters = this.temperatures[i];
            this.addSurfaceBiome(consumer, tempParameters, this.FULL_RANGE, this.deepOceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[0][i]);
            this.addSurfaceBiome(consumer, tempParameters, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[1][i]);
        }
    }

    protected void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        this.addMidSlice(consumer, Climate.Parameter.span(-1.0F, -HIGH_END));
        this.addHighSlice(consumer, Climate.Parameter.span(-HIGH_END, -PEAK_END));
        this.addPeaks(consumer, Climate.Parameter.span(-PEAK_END, -PEAK_START));
        this.addHighSlice(consumer, Climate.Parameter.span(-PEAK_START, -HIGH_START));
        this.addMidSlice(consumer, Climate.Parameter.span(-HIGH_START, -LOW_START));
        this.addLowSlice(consumer, Climate.Parameter.span(-LOW_START, -VALLEY_SIZE));
        this.addValleys(consumer, Climate.Parameter.span(-VALLEY_SIZE, VALLEY_SIZE));
        this.addLowSlice(consumer, Climate.Parameter.span(VALLEY_SIZE, LOW_START));
        this.addMidSlice(consumer, Climate.Parameter.span(LOW_START, HIGH_START));
        this.addHighSlice(consumer, Climate.Parameter.span(HIGH_START, PEAK_START));
        this.addPeaks(consumer, Climate.Parameter.span(PEAK_START, PEAK_END));
        this.addHighSlice(consumer, Climate.Parameter.span(PEAK_END, HIGH_END));
        this.addMidSlice(consumer, Climate.Parameter.span(HIGH_END, 1.0F));
    }

    protected void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        for(int i = 0; i < this.temperatures.length; ++i) {
            Climate.Parameter tempParameters = this.temperatures[i];

            for(int j = 0; j < this.humidities.length; ++j) {
                Climate.Parameter humidityParameters = this.humidities[j];
                ResourceKey<Biome> middleBiome = this.pickMiddleBiome(i, j, weirdness);
                ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(i, j, weirdness);
//                ResourceKey<Biome> shatteredBiome = this.pickShatteredBiome(i, j, weirdness);
                ResourceKey<Biome> peakBiome = this.pickPeakBiome(i, j, weirdness);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, peakBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[1], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, peakBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, plateauBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.midInlandContinentalness, this.erosions[3], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, plateauBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
//                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
            }
        }

    }

    protected void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        for(int i = 0; i < this.temperatures.length; ++i) {
            Climate.Parameter tempParameters = this.temperatures[i];

            for(int j = 0; j < this.humidities.length; ++j) {
                Climate.Parameter humidityParameters = this.humidities[j];
                ResourceKey<Biome> middleBiome = this.pickMiddleBiome(i, j, weirdness);
                ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(i, j, weirdness);
                ResourceKey<Biome> slopeBiome = this.pickSlopeBiome(i, j, weirdness);
                ResourceKey<Biome> peakBiome = this.pickPeakBiome(i, j, weirdness);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.nearInlandContinentalness, this.erosions[0], weirdness, 0.0F, slopeBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, peakBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.nearInlandContinentalness, this.erosions[1], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdness, 0.0F, slopeBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdness, 0.0F, plateauBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.midInlandContinentalness, this.erosions[3], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.farInlandContinentalness, this.erosions[3], weirdness, 0.0F, plateauBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
            }
        }

    }

    protected void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
//        this.addSurfaceBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdness, 0.0F, this.getStonyShoreBiome());
//        this.addSurfaceBiome(consumer, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, this.getSwampBiome());

        for(int i = 0; i < this.temperatures.length; ++i) {
            Climate.Parameter tempParameters = this.temperatures[i];

            for(int j = 0; j < this.humidities.length; ++j) {
                Climate.Parameter humidityParameters = this.humidities[j];
                ResourceKey<Biome> middleBiome = this.pickMiddleBiome(i, j, weirdness);
                ResourceKey<Biome> plateauBiome = this.pickPlateauBiome(i, j, weirdness);
                ResourceKey<Biome> slopeBiome = this.pickSlopeBiome(i, j, weirdness);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdness, 0.0F, slopeBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.nearInlandContinentalness, this.midInlandContinentalness), this.erosions[1], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.farInlandContinentalness, this.erosions[1], weirdness, 0.0F, i == 0 ? slopeBiome : plateauBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.nearInlandContinentalness, this.erosions[2], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.midInlandContinentalness, this.erosions[2], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.farInlandContinentalness, this.erosions[2], weirdness, 0.0F, plateauBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[3], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[3], weirdness, 0.0F, middleBiome);
                if (weirdness.max() < 0L) {
                    this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.coastContinentalness, this.erosions[4], weirdness, 0.0F, middleBiome);
                    this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
                } else {
                    this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
                }

//                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.coastContinentalness, this.erosions[5], weirdness, 0.0F, shatteredCoastBiome);
//                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, shatteredBiome);
                if (weirdness.max() < 0L) {
                    this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, middleBiome);
                } else {
                    this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, middleBiome);
                }

                if (i == 0) {
                    this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
                }
            }
        }

    }

    protected void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {
        for(int i = 0; i < this.temperatures.length; ++i) {
            Climate.Parameter tempParameters = this.temperatures[i];

            for(int j = 0; j < this.humidities.length; ++j) {
                Climate.Parameter humidityParameters = this.humidities[j];
                ResourceKey<Biome> middleBiome = this.pickMiddleBiome(i, j, weirdness);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.coastContinentalness, Climate.Parameter.span(this.erosions[3], this.erosions[4]), weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdness, 0.0F, middleBiome);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, this.coastContinentalness, this.erosions[6], weirdness, 0.0F, middleBiome);
                if (i == 0) {
                    this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdness, 0.0F, middleBiome);
                }
            }
        }

    }

    protected void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter weirdness) {

        for(int i = 0; i < this.temperatures.length; ++i) {
            Climate.Parameter tempParameters = this.temperatures[i];

            for(int j = 0; j < this.humidities.length; ++j) {
                Climate.Parameter humidityParameters = this.humidities[j];
                ResourceKey<Biome> middleOrBadlandsBiome = this.pickMiddleBiome(i, j, weirdness);
                this.addSurfaceBiome(consumer, tempParameters, humidityParameters, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdness, 0.0F, middleOrBadlandsBiome);
            }
        }
    }

    protected ResourceKey<Biome> pickMiddleBiome(int temp, int humidity, Climate.Parameter weirdness) {
        return this.MIDDLE_BIOMES[temp][humidity];
    }

    private ResourceKey<Biome> pickPeakBiome(int temp, int humidity, Climate.Parameter weirdness) {
        return PresetBiomeKeys.ISLANDS;
    }

    private ResourceKey<Biome> pickSlopeBiome(int temp, int humidity, Climate.Parameter weirdness) {
        return PresetBiomeKeys.SHALLOW_OCEAN;
    }

    protected ResourceKey<Biome> pickPlateauBiome(int temp, int humidity, Climate.Parameter weirdness) {
        return PLATEAU_BIOMES[temp][humidity];
    }

    protected void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temp, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        consumer.accept(Pair.of(Climate.parameters(temp, humidity, continentalness, erosion, Climate.Parameter.point(0.0F), weirdness, offset), biome));
        consumer.accept(Pair.of(Climate.parameters(temp, humidity, continentalness, erosion, Climate.Parameter.point(1.0F), weirdness, offset), biome));
    }

    private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        this.addUndergroundBiome(consumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
        this.addUndergroundBiome(consumer, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
    }


    protected void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temp, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        consumer.accept(Pair.of(Climate.parameters(temp, humidity, continentalness, erosion, Climate.Parameter.span(0.2F, 0.9F), weirdness, offset), biome));
    }
}
