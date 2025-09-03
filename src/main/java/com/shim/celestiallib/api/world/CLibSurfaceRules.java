package com.shim.celestiallib.api.world;

import com.google.common.collect.ImmutableList;
import com.shim.celestiallib.world.biome.PresetBiomeKeys;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public class CLibSurfaceRules {
    public static final SurfaceRules.ConditionSource EXTRA_DEEP_UNDER_FLOOR = SurfaceRules.stoneDepthCheck(0, true, 50, CaveSurface.FLOOR);
    private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
    private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
    private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);
    private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
    private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
    private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
    private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
    private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
    private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
    private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
    private static final SurfaceRules.RuleSource MYCELIUM = makeStateRule(Blocks.MYCELIUM);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);
    private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
    private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
    private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
    private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
    private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
    private static final SurfaceRules.RuleSource ICE = makeStateRule(Blocks.ICE);

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }


    public static SurfaceRules.RuleSource space() {
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("air", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), AIR));

        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }

    public static SurfaceRules.RuleSource datapackPresets(boolean isDesertPreset) {
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();

        if (isDesertPreset) {
            SurfaceRules.ConditionSource isDunes = SurfaceRules.isBiome(PresetBiomeKeys.DUNES);
            SurfaceRules.ConditionSource needsSand = SurfaceRules.isBiome(PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.MONSOON_DESERT);
            SurfaceRules.ConditionSource needsSnow = SurfaceRules.isBiome(PresetBiomeKeys.ICY_DESERT);
            SurfaceRules.ConditionSource needsSandstone = SurfaceRules.isBiome(PresetBiomeKeys.ICY_DESERT, PresetBiomeKeys.HIGH_DESERT, PresetBiomeKeys.LOW_DESERT, PresetBiomeKeys.MONSOON_DESERT);

            builder.add(SurfaceRules.sequence(SurfaceRules.ifTrue(isDunes, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SAND))));
            builder.add(SurfaceRules.sequence(SurfaceRules.ifTrue(needsSand, SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SAND))));
            builder.add(SurfaceRules.sequence(SurfaceRules.ifTrue(needsSandstone, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE))));
            builder.add(SurfaceRules.sequence(SurfaceRules.ifTrue(isDunes, SurfaceRules.ifTrue(EXTRA_DEEP_UNDER_FLOOR, SANDSTONE))));
            builder.add(SurfaceRules.sequence(SurfaceRules.ifTrue(needsSnow, SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SNOW_BLOCK))));
        }

        SurfaceRules.ConditionSource isY97 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
        SurfaceRules.ConditionSource isY256 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
        SurfaceRules.ConditionSource isYStart63 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
        SurfaceRules.ConditionSource isYStart74 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
        SurfaceRules.ConditionSource isY62 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource isY63 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        SurfaceRules.ConditionSource water1Deep = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource noWaterAboveIsSurface = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource waterSixDeepWithVariety = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.ConditionSource hole = SurfaceRules.hole();
        SurfaceRules.ConditionSource isFrozenOcean = SurfaceRules.isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
        SurfaceRules.ConditionSource isSteep = SurfaceRules.steep();
        SurfaceRules.RuleSource surfacerules$rulesource = SurfaceRules.sequence(SurfaceRules.ifTrue(noWaterAboveIsSurface, GRASS_BLOCK), DIRT);
        SurfaceRules.RuleSource surfacerules$rulesource1 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
        SurfaceRules.RuleSource surfacerules$rulesource2 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
        SurfaceRules.ConditionSource isWarmOceanBeachOrSnowyBeach = SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH);
        SurfaceRules.ConditionSource isDesert = SurfaceRules.isBiome(Biomes.DESERT);

        SurfaceRules.ConditionSource isStonyPeaks = SurfaceRules.isBiome(Biomes.STONY_PEAKS);
        SurfaceRules.ConditionSource isStonyShore = SurfaceRules.isBiome(Biomes.STONY_SHORE);
        SurfaceRules.ConditionSource isWindsweptHills = SurfaceRules.isBiome(Biomes.WINDSWEPT_HILLS);
        SurfaceRules.ConditionSource isDripstoneCave = SurfaceRules.isBiome(Biomes.DRIPSTONE_CAVES);
        SurfaceRules.ConditionSource isSnowySlopes = SurfaceRules.isBiome(Biomes.SNOWY_SLOPES);
        SurfaceRules.ConditionSource isJaggedPeaks = SurfaceRules.isBiome(Biomes.JAGGED_PEAKS);
        SurfaceRules.ConditionSource isGrove = SurfaceRules.isBiome(Biomes.GROVE);
        SurfaceRules.ConditionSource isWindsweptSavanna = SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA);
        SurfaceRules.ConditionSource isWindsweptGravelHill = SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS);


        SurfaceRules.RuleSource surfacerules$rulesource3 = SurfaceRules.sequence(SurfaceRules.ifTrue(isStonyPeaks,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125D, 0.0125D), CALCITE), STONE)),
                SurfaceRules.ifTrue(isStonyShore,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05D, 0.05D), surfacerules$rulesource2), STONE)),
                SurfaceRules.ifTrue(isWindsweptHills, SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)),
                SurfaceRules.ifTrue(isWarmOceanBeachOrSnowyBeach, surfacerules$rulesource1), SurfaceRules.ifTrue(isDesert, surfacerules$rulesource1),
                SurfaceRules.ifTrue(isDripstoneCave, STONE));

        SurfaceRules.RuleSource powderedSnowOnSurface = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D),
                SurfaceRules.ifTrue(noWaterAboveIsSurface, POWDER_SNOW));

        SurfaceRules.RuleSource powderedSnowOnSurfaceAlt = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35D, 0.6D),
                SurfaceRules.ifTrue(noWaterAboveIsSurface, POWDER_SNOW));

        SurfaceRules.RuleSource surfacerules$rulesource6 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, PACKED_ICE),
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5D, 0.2D), PACKED_ICE),
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625D, 0.025D), ICE),
                                SurfaceRules.ifTrue(noWaterAboveIsSurface, SNOW_BLOCK))),
                SurfaceRules.ifTrue(isSnowySlopes,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, STONE), powderedSnowOnSurface,
                                SurfaceRules.ifTrue(noWaterAboveIsSurface, SNOW_BLOCK))),
                SurfaceRules.ifTrue(isJaggedPeaks, STONE),
                SurfaceRules.ifTrue(isGrove,
                        SurfaceRules.sequence(powderedSnowOnSurface, DIRT)), surfacerules$rulesource3,
                SurfaceRules.ifTrue(isWindsweptSavanna,
                        SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)),
                SurfaceRules.ifTrue(isWindsweptGravelHill,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), surfacerules$rulesource2),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT), surfacerules$rulesource2)), DIRT);

        SurfaceRules.RuleSource surfacerules$rulesource7 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0D, 0.2D), PACKED_ICE),
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0D, 0.025D), ICE), SurfaceRules.ifTrue(noWaterAboveIsSurface, SNOW_BLOCK))),
                SurfaceRules.ifTrue(isSnowySlopes, SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, STONE), powderedSnowOnSurfaceAlt,
                        SurfaceRules.ifTrue(noWaterAboveIsSurface, SNOW_BLOCK))),
                SurfaceRules.ifTrue(isJaggedPeaks,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, STONE),
                                SurfaceRules.ifTrue(noWaterAboveIsSurface, SNOW_BLOCK))),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), SurfaceRules.sequence(powderedSnowOnSurfaceAlt,
                        SurfaceRules.ifTrue(noWaterAboveIsSurface, SNOW_BLOCK))), surfacerules$rulesource3,
                SurfaceRules.ifTrue(isWindsweptSavanna,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT))),
                SurfaceRules.ifTrue(isWindsweptGravelHill,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), surfacerules$rulesource2),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), surfacerules$rulesource), surfacerules$rulesource2)),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT),
                                SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL))),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.ICE_SPIKES),
                        SurfaceRules.ifTrue(noWaterAboveIsSurface, SNOW_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM), surfacerules$rulesource);

        SurfaceRules.ConditionSource surfacerules$conditionsource14 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909D, -0.5454D);

        SurfaceRules.ConditionSource surfacerules$conditionsource15 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818D, 0.1818D);

        SurfaceRules.ConditionSource surfacerules$conditionsource16 = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454D, 0.909D);

        SurfaceRules.RuleSource surfacerules$rulesource8 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WOODED_BADLANDS),
                                        SurfaceRules.ifTrue(isY97,
                                                SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource14, COARSE_DIRT),
                                                        SurfaceRules.ifTrue(surfacerules$conditionsource15, COARSE_DIRT),
                                                        SurfaceRules.ifTrue(surfacerules$conditionsource16, COARSE_DIRT), surfacerules$rulesource))),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SWAMP), SurfaceRules.ifTrue(isY62,
                                        SurfaceRules.ifTrue(SurfaceRules.not(isY63),
                                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)))))),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS),
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                        SurfaceRules.sequence(SurfaceRules.ifTrue(isY256, ORANGE_TERRACOTTA),
                                                SurfaceRules.ifTrue(isYStart74,
                                                        SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource14, TERRACOTTA),
                                                                SurfaceRules.ifTrue(surfacerules$conditionsource15, TERRACOTTA),
                                                                SurfaceRules.ifTrue(surfacerules$conditionsource16, TERRACOTTA),
                                                                SurfaceRules.bandlands())), SurfaceRules.ifTrue(water1Deep,
                                                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND)),
                                                SurfaceRules.ifTrue(SurfaceRules.not(hole), ORANGE_TERRACOTTA),
                                                SurfaceRules.ifTrue(waterSixDeepWithVariety, WHITE_TERRACOTTA), surfacerules$rulesource2)),
                                SurfaceRules.ifTrue(isYStart63, SurfaceRules.sequence(SurfaceRules.ifTrue(isY63,
                                        SurfaceRules.ifTrue(SurfaceRules.not(isYStart74), ORANGE_TERRACOTTA)), SurfaceRules.bandlands())),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(waterSixDeepWithVariety, WHITE_TERRACOTTA)))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(water1Deep,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(isFrozenOcean, SurfaceRules.ifTrue(hole,
                                SurfaceRules.sequence(SurfaceRules.ifTrue(noWaterAboveIsSurface, AIR),
                                        SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE), WATER))), surfacerules$rulesource7))),
                SurfaceRules.ifTrue(waterSixDeepWithVariety, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.ifTrue(isFrozenOcean, SurfaceRules.ifTrue(hole, WATER))),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, surfacerules$rulesource6),
                        SurfaceRules.ifTrue(isWarmOceanBeachOrSnowyBeach,
                                SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)),
                        SurfaceRules.ifTrue(isDesert,
                                SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE)))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE),
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN), surfacerules$rulesource1), surfacerules$rulesource2)));


        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));

        SurfaceRules.RuleSource surfacerules$rulesource9 = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), surfacerules$rulesource8);
        builder.add(surfacerules$rulesource9);
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double p_194809_) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, p_194809_ / 8.25D, Double.MAX_VALUE);
    }
}
