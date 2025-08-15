package com.shim.celestiallib.api.world.renderer;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ISkyRenderHandler;
import net.minecraftforge.client.IWeatherParticleRenderHandler;
import net.minecraftforge.client.IWeatherRenderHandler;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class DimensionRenderers {

    /**
     * Override to add custom effects to {@link DimensionSpecialEffects}.EFFECTS
     */
    public static void setDimensionEffects() {

    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class PlanetEffects extends DimensionSpecialEffects {

        public PlanetEffects(@Nullable ISkyRenderHandler skyHandler, @Nullable IWeatherRenderHandler weatherHandler, @Nullable IWeatherParticleRenderHandler particleHandler) {
            this(Float.NaN, true, SkyType.NORMAL, false, false, skyHandler, weatherHandler, particleHandler);
        }

        public PlanetEffects(float cloudLevel, boolean hasGround, SkyType sky, boolean forceBrightLightmap, boolean constantAmbientLight, @Nullable ISkyRenderHandler skyHandler, @Nullable IWeatherRenderHandler weatherHandler, @Nullable IWeatherParticleRenderHandler particleHandler) {
            super(cloudLevel, hasGround, sky, forceBrightLightmap, constantAmbientLight);

            if (skyHandler != null)
                setSkyRenderHandler(skyHandler);
            if (weatherHandler != null)
                setWeatherRenderHandler(weatherHandler);
            if (particleHandler != null)
                setWeatherParticleRenderHandler(particleHandler);
        }

        @Override
        public Vec3 getBrightnessDependentFogColor(Vec3 color, float brightness) {
            return color.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
        }

        @Override
        public boolean isFoggyAt(int x, int z) {
            return false;
        }

        @Override
        public float[] getSunriseColor(float p_230492_1_, float p_230492_2_) {
            return null;
        }
    }

    //---- DATAPACK PLANETS -------------------------------------------------------------------------------
    @OnlyIn(Dist.CLIENT)
    public static class DatapackSkyEffects extends PlanetEffects {
        boolean hasSunrise;
        private final float[] sunriseCol = new float[4];

        public DatapackSkyEffects(float cloudLevel, boolean hasGround, SkyType sky, boolean forceBrightLightmap, boolean constantAmbientLight, boolean hasSunrise, @Nullable ISkyRenderHandler skyHandler, @Nullable IWeatherRenderHandler weatherHandler, @Nullable IWeatherParticleRenderHandler particleHandler) {
            super(cloudLevel, hasGround, sky, forceBrightLightmap, constantAmbientLight, skyHandler, weatherHandler, particleHandler);
            this.hasSunrise = hasSunrise;
        }

        @Override
        public float[] getSunriseColor(float p_230492_1_, float p_230492_2_) {
            if (this.hasSunrise) {
                float f = 0.4F;
                float f1 = Mth.cos(p_230492_1_ * ((float)Math.PI * 2F)) - 0.0F;
                float f2 = -0.0F;
                if (f1 >= -0.4F && f1 <= 0.4F) {
                    float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
                    float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float)Math.PI)) * 0.99F;
                    f4 *= f4;
                    this.sunriseCol[0] = f3 * 0.3F + 0.7F;
                    this.sunriseCol[1] = f3 * f3 * 0.7F + 0.2F;
                    this.sunriseCol[2] = f3 * f3 * 0.0F + 0.2F;
                    this.sunriseCol[3] = f4;
                    return this.sunriseCol;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }
}