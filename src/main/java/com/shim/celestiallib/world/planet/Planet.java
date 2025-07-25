package com.shim.celestiallib.world.planet;

import com.shim.celestiallib.world.galaxy.Galaxy;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Planet extends ForgeRegistryEntry<Planet> {
    private MobEffect gravity = null;
    Supplier<Boolean> gravityConfig;
    private final ResourceKey<Level> dimension;
    private final Galaxy galaxy;
    boolean isLocked = false;
    boolean isHidden = false;
    ResourceLocation texture;
    int textureSize = 16; //default size
    private ItemStack lightSpeedCost;

    public static final Map<ResourceKey<Level>, Planet> DIMENSIONS = new HashMap<>();

    public Planet(ResourceKey<Level> dimension, Galaxy galaxy, Supplier<Boolean> gravityConfig) {
        this.dimension = dimension;
        this.galaxy = galaxy;
        this.gravityConfig = gravityConfig;

        //TODO check dimension coming in is unique
        DIMENSIONS.put(dimension, this);
    }

    public static Planet getPlanet(ResourceKey<Level> dimension) {
        return DIMENSIONS.getOrDefault(dimension, null);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    public Planet gravity(MobEffect gravity) {
        this.gravity = gravity;
        return this;
    }

    @Nullable
    public MobEffect getGravity() {
        if (this.gravityConfig != null && this.gravityConfig.get() && gravity != null)
            return this.gravity;
        else return null;
    }

    public Galaxy getGalaxy() {
        return this.galaxy;
    }

    public Planet lockedAndMaybeHidden(boolean isHidden) {
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

    public Planet lightSpeedCost(ItemStack itemStack) {
        this.lightSpeedCost = itemStack;
        return this;
    }

    public ItemStack getLightSpeedCost() {
        return this.lightSpeedCost;
    }
}