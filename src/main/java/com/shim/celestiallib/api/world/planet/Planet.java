package com.shim.celestiallib.api.world.planet;

import com.shim.celestiallib.api.effects.GravityEffect;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.world.celestials.ICelestial;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Planet extends ForgeRegistryEntry<Planet> implements ICelestial {
    private Supplier<GravityEffect> gravity = null;
    private final ResourceKey<Level> dimension;
    private final Galaxy galaxy;
    boolean isLocked = false;
    boolean isLightSpeedLocked = false;
    boolean isHidden = false;
    ResourceLocation texture;
    int textureSize = 16; //default size
    private ItemStack lightSpeedCost = new ItemStack(Blocks.AIR);
    Supplier<Integer> costMultiplier;

    public static final Map<ResourceKey<Level>, Planet> DIMENSIONS = new HashMap<>();
    private boolean cooldownsEnabled = true;

    public static ResourceLocation advancementUnlock = new ResourceLocation("story/upgrade_tools");

    public Planet(ResourceKey<Level> dimension, Galaxy galaxy) {
        this.dimension = dimension;
        this.galaxy = galaxy;

        //FIXME add exception for overworld…?
        if (DIMENSIONS.containsKey(dimension)) {
            throw new IllegalStateException("dimension: " + dimension.toString() + " already has an associated planet");
        }
        DIMENSIONS.put(dimension, this);
    }

    public void setGravity(Supplier<GravityEffect> gravity) {
        this.gravity = gravity;
    }

    public void setLocked() {
        this.isLocked = true;
    }

    public void setLightSpeedLockedAndMaybeHidden(boolean isHidden) {
        this.isLightSpeedLocked = true;
        this.isHidden = isHidden;
    }

    public void disableCooldowns() {
        this.cooldownsEnabled = false;
    }

    public void setLightSpeedCost(ItemStack cost, Supplier<Integer> costMultiplier) {
        this.lightSpeedCost = cost;
        this.costMultiplier = costMultiplier;
    }

    public void setLightSpeedCost(ItemStack itemStack) {
        setLightSpeedCost(itemStack, () -> 0);
    }

    public static Planet getPlanet(ResourceKey<Level> dimension) {
        return DIMENSIONS.getOrDefault(dimension, null);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    @Nullable
    public GravityEffect getGravity() {
        if (gravity != null)
            return this.gravity.get();
        else return null;
    }

    public Galaxy getGalaxy() {
        return this.galaxy;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public boolean isLightSpeedLocked() {
        return this.isLightSpeedLocked;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public void setTexture(ResourceLocation texture, int size) {
        this.texture = texture;
        this.textureSize = size;
    }

    public boolean areCooldownsEnabled() {
        return this.cooldownsEnabled;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public int getTextureSize() {
        return this.textureSize;
    }

    public ItemStack getLightSpeedCost(float distance) {
        int costMultiplier = Mth.clamp(this.costMultiplier.get(), 0, 2048);
        float multiplier = costMultiplier == 0 ? 0 : distance / (float) costMultiplier; //check to avoid dividing by 0… oops
        float f1 = this.lightSpeedCost.getCount() * multiplier;
        int cost = this.lightSpeedCost.getCount() + (int) f1;

        return new ItemStack(this.lightSpeedCost.getItem(), cost);
    }

    public boolean isMoon() {
        return false;
    }

    @Override
    public boolean isGalaxy() {
        return false;
    }
}