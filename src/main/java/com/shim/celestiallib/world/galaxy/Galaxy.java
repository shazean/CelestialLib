package com.shim.celestiallib.world.galaxy;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Galaxy extends ForgeRegistryEntry<Galaxy> {
    Function<ResourceKey<Level>, Integer> galaxyRatioConfig;
    private final ResourceKey<Level> dimension;
    boolean isLocked = false;
    boolean isHidden = false;
    private ResourceLocation icon;
    private ResourceLocation backgroundImage;
    private int backgroundImageSize;

    public static final Map<ResourceKey<Level>, Galaxy> DIMENSIONS = new HashMap<>();

    public Galaxy(ResourceKey<Level> galaxyDimension, Function<ResourceKey<Level>, Integer> galaxyRatioConfig) {
        this.dimension = galaxyDimension;
        this.galaxyRatioConfig = galaxyRatioConfig;

        //TODO check dimension coming in is unique
        DIMENSIONS.put(dimension, this);
    }

    public static Galaxy getGalaxy(ResourceKey<Level> dimension) {
        return DIMENSIONS.getOrDefault(dimension, null);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    public static ResourceKey<Level> getFirstDimension() {
        return DIMENSIONS.keySet().stream().toList().get(0);
    }

    public int getGalaxyRatio() {
        return this.galaxyRatioConfig.apply(dimension);
    }

    public static boolean isGalaxyDimension(ResourceKey<Level> dimension) {
        return DIMENSIONS.containsKey(dimension);
    }

    public Galaxy setLocked(boolean isHidden) {
        //TODO accept a criteria for unlocking
        this.isLocked = true;
        this.isHidden = isHidden;
        return this;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public void setIcon(ResourceLocation texture) {
        this.icon = texture;
    }

    public ResourceLocation getIcon() {
        return this.icon;
    }

    public void setBackgroundImage(ResourceLocation texture, int size) {
        this.backgroundImage = texture;
        this.backgroundImageSize = size;
    }

    public ResourceLocation getBackgroundImage() {
        return this.backgroundImage;
    }


}