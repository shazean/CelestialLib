package com.shim.celestiallib.api.world;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CLibNoiseSettings extends NoiseRouterData  {
    public static final DeferredRegister<NoiseGeneratorSettings> NOISES = DeferredRegister.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, CelestialLib.MODID);

    public static final RegistryObject<NoiseGeneratorSettings> SPACE_NOISE = NOISES.register("space", () -> new NoiseGeneratorSettings(spaceNoiseSettings(), Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(),
            NoiseRouterData.overworld(spaceNoiseSettings(), false), CLibSurfaceRules.space(), -64, false, false, false, false));
    
    //---- FOR DATAPACK/PRESETS -------------------------------------------------------------------------------
    public static final RegistryObject<NoiseGeneratorSettings> DESERT_NOISE = NOISES.register("desert", () -> new NoiseGeneratorSettings(datapackPlanetNoiseSettings(), Blocks.SANDSTONE.defaultBlockState(), Blocks.SAND.defaultBlockState(),
            NoiseRouterData.overworld(datapackPlanetNoiseSettings(), false), CLibSurfaceRules.datapackPresets(true), 32, false, true , true, false));


    public static final RegistryObject<NoiseGeneratorSettings> FOREST_NOISE = NOISES.register("forest", () -> new NoiseGeneratorSettings(datapackPlanetNoiseSettings(), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
            NoiseRouterData.overworld(datapackPlanetNoiseSettings(), false), CLibSurfaceRules.datapackPresets(false), 63, false, true , true, false));


    public static final RegistryObject<NoiseGeneratorSettings> OCEAN_NOISE = NOISES.register("ocean", () -> new NoiseGeneratorSettings(datapackPlanetNoiseSettings(), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
            NoiseRouterData.overworld(datapackPlanetNoiseSettings(), false), CLibSurfaceRules.datapackPresets(false), 128, false, true , true, false));


    public static final RegistryObject<NoiseGeneratorSettings> ICE_NOISE = NOISES.register("icy", () -> new NoiseGeneratorSettings(datapackPlanetNoiseSettings(), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
            NoiseRouterData.overworld(datapackPlanetNoiseSettings(), false), CLibSurfaceRules.datapackPresets(false), 63, false, true , true, false));


    static NoiseSettings spaceNoiseSettings() {
        return NoiseSettings.create(-32, 32, new NoiseSamplingSettings(1.0D, 1.0D, 1.0D, 1.0D), //1.0D, 1.0D, 80.0D, 160.0D
                new NoiseSlider(0.0D, 0, 0),
                new NoiseSlider(0.0D, 0, 0),
                1, 1,
                new TerrainShaper(CubicSpline.constant(0.0F), CubicSpline.constant(0.0F), CubicSpline.constant(0.0F)));
    }

    static NoiseSettings datapackPlanetNoiseSettings() {
        return NoiseSettings.create(-64, 384, new NoiseSamplingSettings(1.0D, 1.0D, 80.0D, 160.0D),
                new NoiseSlider(-0.078125D, 2, 8), new NoiseSlider(0.1171875D, 3, 0), 1, 2, TerrainProvider.overworld(false));
    }
}
