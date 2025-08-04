package com.shim.celestiallib.world.planet;

import com.shim.celestiallib.world.galaxy.Galaxy;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class Planet extends ForgeRegistryEntry<Planet> {
    private MobEffect gravity = null;
    private final ResourceKey<Level> dimension;
    private final Galaxy galaxy;
    boolean isLocked = false;
    boolean isHidden = false;
    ResourceLocation texture;
    int textureSize = 16; //default size
    private ItemStack lightSpeedCost = new ItemStack(Blocks.AIR);
    float costMultiplier;

    public static final Map<ResourceKey<Level>, Planet> DIMENSIONS = new HashMap<>();

    public Planet(ResourceKey<Level> dimension, Galaxy galaxy) {
        this.dimension = dimension;
        this.galaxy = galaxy;

        if (DIMENSIONS.containsKey(dimension)) {
            throw new IllegalStateException("dimension: " + dimension.toString() + " already has an associated planet");
        }
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
        if (gravity != null)
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
        return lightSpeedCost(itemStack, 1.0f);
    }

//    public ItemStack getLightSpeedCost() {
//      return this.lightSpeedCost;
//    }

    //note: multiplier is later clamped to be within the values of 1 and 2048, even if value passed in is higher than that
    public Planet lightSpeedCost(ItemStack cost, float costMultiplier) {
        this.lightSpeedCost = cost;
        this.costMultiplier = costMultiplier;
        return this;
    }

    public ItemStack getLightSpeedCost(float distance) {
        return new ItemStack(this.lightSpeedCost.getItem(),
                (int) (this.lightSpeedCost.getCount() * Mth.clamp(this.costMultiplier,
                        1, 2048) * distance));
    }
}