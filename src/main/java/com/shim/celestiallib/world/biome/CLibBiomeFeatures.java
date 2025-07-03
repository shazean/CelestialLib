package com.shim.celestiallib.world.biome;

import net.minecraft.data.worldgen.Carvers;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

import javax.annotation.Nullable;

public class CLibBiomeFeatures {

    @Nullable
    protected static final Music NORMAL_MUSIC = null;

    protected static int calculateSkyColor(float p_194844_) {
        float $$1 = p_194844_ / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

    protected static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, int waterColor, int waterFogColor, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder, @Nullable Music music) {
        return (new Biome.BiomeBuilder()).precipitation(precipitation).biomeCategory(category).temperature(temperature).downfall(downfall)
                .specialEffects((new BiomeSpecialEffects.Builder()).waterColor(waterColor).waterFogColor(waterFogColor).fogColor(12638463).skyColor(calculateSkyColor(temperature))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build()).mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }

    protected static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, int skyColor, int fogColor, int waterColor, int waterFogColor, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder, @Nullable Music music) {
        return (new Biome.BiomeBuilder()).precipitation(precipitation).biomeCategory(category).temperature(temperature).downfall(downfall)
                .specialEffects((new BiomeSpecialEffects.Builder()).waterColor(waterColor).waterFogColor(waterFogColor).fogColor(fogColor).skyColor(skyColor)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build()).mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }

    protected static Biome biome(Biome.Precipitation precipitation, Biome.BiomeCategory category, float temperature, float downfall, int skyColor, int fogColor, int waterColor, int waterFogColor, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder, @Nullable Music music, AmbientParticleSettings particle) {
        return (new Biome.BiomeBuilder()).precipitation(precipitation).biomeCategory(category).temperature(temperature).downfall(downfall)
                .specialEffects((new BiomeSpecialEffects.Builder()).waterColor(waterColor).waterFogColor(waterFogColor).fogColor(fogColor).skyColor(skyColor).ambientParticle(particle)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).backgroundMusic(music).build()).mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }

    public static void addCarversAndLakes(BiomeGenerationSettings.Builder biomeBuilder) {
        biomeBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        biomeBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
    }
}