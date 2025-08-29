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
    private GravityEffect gravity = null;
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

        if (DIMENSIONS.containsKey(dimension)) {
            throw new IllegalStateException("dimension: " + dimension.toString() + " already has an associated planet");
        }
        DIMENSIONS.put(dimension, this);
    }

    //START PLANET BUILDER METHODS
    //The following methods can be called during registry of your planet to customize it.
    /**
     * Set a gravity value. If unset, planet will have standard vanilla gravity.
     * See {@link com.shim.celestiallib.effects.CelestialLibEffects} or create your own.
     */
    public Planet gravity(GravityEffect gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * Set a planet to be locked and not able to be traveled to via
     * {@link com.shim.celestiallib.api.capabilities.ISpaceFlight} until unlocked.
     * Light speed travel is also unavailable while this is locked.
     * Adding the file for unlocking this also automatically sets it as locked,
     * so this method can be skipped during registry.
     */
    public Planet locked() {
        this.isLocked = true;
        return this;
    }

    /**
     * Set a planet to be locked and possibly hidden from light speed travel.
     * If it is locked, it can not be visited by light speed travel until it is unlocked.
     * If it is hidden, it is not even visible in the light speed travel until it is unlocked.
     * This only affects light speed travel and not other means of visiting a planet/dimension.
     * Adding the file for unlocking this also automatically sets it as locked,
     * so this method can be skipped during registry.
     */
    public Planet lightSpeedLockedAndMaybeHidden(boolean isHidden) {
        this.isLightSpeedLocked = true;
        this.isHidden = isHidden;
        return this;
    }

    /**
     * This disables the cooldown feature for the planet IF cooldowns are enabled galaxy-wide.
     * Otherwise, whether a cooldown is enabled is determined by the galaxy.
     */
    public void disableCooldowns() {
        this.cooldownsEnabled = false;
    }

    /**
     * Set the light speed cost for traveling to this planet
     * @param cost base cost of light speed travel
     * @param costMultiplier number of chunks it takes for the base cost to be doubled.
     *                       This is later clamped to be within values 0 and 2048. The smaller this number is,
     *                       the greater the cost is per distance.  Set to 0 to keep light speed
     *                       travel at always exactly the base cost. Hint: pass in a config value here!
     */
    public Planet lightSpeedCost(ItemStack cost, Supplier<Integer> costMultiplier) {
        this.lightSpeedCost = cost;
        this.costMultiplier = costMultiplier;
        return this;
    }

    /**
     * Calls {@link Planet#lightSpeedCost(ItemStack, Supplier)} but with a value of 0,
     * thereby disabling light speed travel costs increasing with distance
     */
    public Planet lightSpeedCost(ItemStack itemStack) {
        return lightSpeedCost(itemStack, () -> 0);
    }
    //END PLANET BUILDER METHODS


    public static Planet getPlanet(ResourceKey<Level> dimension) {
        return DIMENSIONS.getOrDefault(dimension, null);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    @Nullable
    public GravityEffect getGravity() {
        if (gravity != null)
            return this.gravity;
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