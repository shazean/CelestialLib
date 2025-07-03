package com.shim.celestiallib.world.planet;

import com.shim.celestiallib.world.galaxy.Galaxy;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Planet extends ForgeRegistryEntry<Planet> {
    private MobEffect gravity = null;
    Function<ResourceKey<Level>, Boolean> gravityConfig;
    private final ResourceKey<Level> dimension;
    private final Galaxy galaxy;
    boolean isLocked = false;
    boolean isHidden = false;
    ResourceLocation texture;
    int textureSize;

    public static final Map<ResourceKey<Level>, Planet> DIMENSIONS = new HashMap<>();

    public Planet(ResourceKey<Level> dimension, Function<ResourceKey<Level>, Boolean> gravityConfig, Galaxy galaxy) {
        this.dimension = dimension;
        this.gravityConfig = gravityConfig;
        this.galaxy = galaxy;

        //TODO check dimension coming in is unique
        DIMENSIONS.put(dimension, this);
    }

    public static Planet getPlanet(ResourceKey<Level> dimension) {
        return DIMENSIONS.getOrDefault(dimension, null);
    }

    public Planet setGravity(MobEffect gravity) {
        this.gravity = gravity;
        return this;
    }

    @Nullable
    public MobEffect getGravity() {
        if (this.gravityConfig != null && this.gravityConfig.apply(dimension) && gravity != null)
            return this.gravity;
        else return null;
    }

    public Galaxy getGalaxy() {
        return this.galaxy;
    }

    public static TranslatableComponent getName(ResourceKey<Level> dimension) {
        return new TranslatableComponent("dimension." + dimension.getRegistryName().getNamespace() + "." + dimension.getRegistryName().getPath());
    }

    public Planet setLocked(boolean isHidden) {
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

    public void setTexture(ResourceLocation texture, int size) {
        this.texture = texture;
        this.textureSize = size;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public int getTextureSize() {
        return this.textureSize;
    }
}