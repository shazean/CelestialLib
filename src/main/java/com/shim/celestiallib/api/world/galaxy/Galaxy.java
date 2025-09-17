package com.shim.celestiallib.api.world.galaxy;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.effects.CLibEffects;
import com.shim.celestiallib.api.effects.GravityEffect;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.world.celestials.ICelestial;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class Galaxy extends ForgeRegistryEntry<Galaxy> implements ICelestial {
    int galaxyRatio = 10; //DEFAULT
    private final ResourceKey<Level> dimension;
    boolean isLightSpeedLocked = false;
    boolean isHidden = false;
    private ResourceLocation icon = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/galaxy_icons/default_galaxy.png"); //DEFAULT
    private ResourceLocation backgroundImage;
    private int backgroundImageSize;
    private ItemStack lightSpeedCost;
    private int guiScale = 2;
    private Supplier<Integer> yHeight;
    private Supplier<GravityEffect> gravity = () -> (GravityEffect) CLibEffects.LOW_GRAVITY.get();

    public static final Map<ResourceKey<Level>, Galaxy> DIMENSIONS = new HashMap<>();
    private boolean cooldownsEnabled = false;
    private Supplier<Integer> maxCooldown;
    private Supplier<Integer> minCooldown;
    private Supplier<Integer> cooldownDecrement;

    public Galaxy(ResourceKey<Level> galaxyDimension) {
        this.dimension = galaxyDimension;

        if (DIMENSIONS.containsKey(dimension) && !dimension.equals(Level.OVERWORLD)) {
            throw new IllegalStateException("dimension: " + dimension.toString() + " already has an associated galaxy: " + DIMENSIONS.get(dimension).toString());
        }
        DIMENSIONS.put(dimension, this);
    }

    //START GALAXY BUILDER METHODS
    //The following methods can be called during registry of your galaxy to customize it.
    /**
     * Enable cooldowns for light speed travel for all planets within this galaxy.
     * Cooldowns can then be disabled for each planet individually if desired.
     * Hint: pass in config values to these!
     * @param maxCooldown The longest a cooldown can be for a given planet, in ticks.
     * @param minCooldown The shortest a cooldown can be for a given planet, in ticks.
     * @param decrement The amount of time a cooldown can decrement each time
     *                  light speed travel is used for a given planet, in ticks. A value of 0
     *                  is allowed.
     */
    public Galaxy enableCooldowns(Supplier<Integer> maxCooldown, Supplier<Integer> minCooldown, Supplier<Integer> decrement) {
        this.cooldownsEnabled = true;
        this.maxCooldown = maxCooldown;
        this.minCooldown = minCooldown;
        this.cooldownDecrement = decrement;
        return this;
    }

    /**
     * Set the visual scale of a galaxy in the light speed travel gui
     * Default value is 2, but this allows extra customization
     * if a galaxy happens to be extra expansive or extra crowded in a small area of space
     * Allowed values are between 1 and 6
     */
    public Galaxy guiScale(int scale) {
        this.guiScale = Mth.clamp(scale, 1, 6);
        return this;
    }

    /**
     * Set what y value a player should teleport to when teleporting from a planet TO a galaxy.
     * Default is 135
     */
    public Galaxy yHeight(Supplier<Integer> yHeight) {
        this.yHeight = yHeight;
        return this;
    }

    /**
     * Set a gravity value. If unset, galaxy will have {@link CLibEffects#LOW_GRAVITY LOW_GRAVITY}.
     * See {@link CLibEffects} or create your own.
     * Note that {@link com.shim.celestiallib.config.CLibCommonConfig#GRAVITY_EFFECTS GRAVITY_EFFECTS config} will need to be set to true
     */
    public Galaxy gravity(Supplier<GravityEffect> gravity) {
        this.gravity = gravity;
        return this;
    }

    //END GALAXY BUILDER METHODS

    public void setTravelLocked() {}

    public void setLightSpeedLockedAndMaybeHidden(boolean isHidden) {
        this.isLightSpeedLocked = true;
        this.isHidden = isHidden;
    }

    public void setLightSpeedCost(ItemStack itemStack) {
        this.lightSpeedCost = itemStack;
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

    public static Galaxy getFirstGalaxy() {
        return DIMENSIONS.values().stream().toList().get(0);
    }

    public static List<Galaxy> getAlphabetizedList() {
        List<Galaxy> list = new ArrayList<>(DIMENSIONS.values().stream().toList());
        list.sort(Comparator.comparing(galaxy -> CelestialUtil.getDisplayName(galaxy.getDimension()).getString()));
        return list;
    }

    public void setGalaxyRatio(int scale) {
        this.galaxyRatio = scale;
    }

    public int getGalaxyRatio() {
        return this.galaxyRatio;
    }

    public static boolean isGalaxyDimension(ResourceKey<Level> dimension) {
        return DIMENSIONS.containsKey(dimension);
    }

    public boolean isTravelLocked() {
        return this.isLightSpeedLocked;
    }

    public boolean isLightSpeedLocked() {
        return this.isLightSpeedLocked;
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

    public int getBackgroundImageSize() {
        return this.backgroundImageSize;
    }

    public ItemStack getLightSpeedCost(Galaxy originatingGalaxy) {
        if (this.equals(originatingGalaxy))
            return null;
        else
            return this.lightSpeedCost;
    }

    public int getGuiScale() {
        return this.guiScale;
    }

    public boolean areCooldownsEnabled() {
        return this.cooldownsEnabled;
    }

    public static List<Galaxy> getVisibleGalaxies() {
        ArrayList<Galaxy> visibleGalaxies = new ArrayList<>();
        for (Galaxy galaxy : DIMENSIONS.values()) {
            if (!galaxy.isHidden)
                visibleGalaxies.add(galaxy);
        }
        return visibleGalaxies;
    }


    public int getYHeight() {
        if (yHeight != null)
            return yHeight.get();
        else return 135; //default value
    }

    public Supplier<Integer> getMaxCooldown() {
        return this.maxCooldown;
    }

    public Supplier<Integer> getMinCooldown() {
        return this.minCooldown;
    }

    public Supplier<Integer> getCooldownDecrement() {
        return this.cooldownDecrement;
    }

    @Override
    public boolean isGalaxy() {
        return true;
    }

    @Nullable
    public GravityEffect getGravity() {
        if (gravity != null)
            return this.gravity.get();
        else return null;
    }
}