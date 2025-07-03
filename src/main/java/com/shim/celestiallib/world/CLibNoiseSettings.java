package com.shim.celestiallib.world;

import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.levelgen.NoiseSamplingSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.NoiseSlider;

public class CLibNoiseSettings {


    static NoiseSettings spaceNoiseSettings() {
        return NoiseSettings.create(-32, 32, new NoiseSamplingSettings(1.0D, 1.0D, 1.0D, 1.0D), //1.0D, 1.0D, 80.0D, 160.0D
                new NoiseSlider(0.0D, 0, 0), //-0.078125D, 2, 8
                new NoiseSlider(0.0D, 0, 0), //0.1171875D, 3, 0
                1, 1, //1, 2
                new TerrainShaper(CubicSpline.constant(0.0F), CubicSpline.constant(0.0F), CubicSpline.constant(0.0F)));
    }
}
