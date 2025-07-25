package com.shim.celestiallib.world.galaxy;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.util.CelestialUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;
import java.util.function.Function;

public class Galaxy extends ForgeRegistryEntry<Galaxy> {
    Function<ResourceKey<Level>, Integer> galaxyRatioConfig;
    private final ResourceKey<Level> dimension;
    boolean isLocked = false;
    boolean isHidden = false;
    private ResourceLocation icon = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/galaxy_icons/default_galaxy.png"); //DEFAULT
    private ResourceLocation backgroundImage;
    private int backgroundImageSize;
    private ItemStack lightSpeedCost;
    private int guiScale = 2;

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

    public static List<Galaxy> getAlphabetizedList() {
        List<Galaxy> list = new ArrayList<>(DIMENSIONS.values().stream().toList());
        list.sort(Comparator.comparing(galaxy -> CelestialUtil.getDisplayName(galaxy.getDimension()).getString()));
        return list;
    }

    public int getGalaxyRatio() {
        return this.galaxyRatioConfig.apply(dimension);
    }

    public static boolean isGalaxyDimension(ResourceKey<Level> dimension) {
        return DIMENSIONS.containsKey(dimension);
    }

    public Galaxy lockedAndMaybeHidden(boolean isHidden) {
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

    public int getBackgroundImageSize() {
        return this.backgroundImageSize;
    }

    public Galaxy lightSpeedCost(ItemStack itemStack) {
        this.lightSpeedCost = itemStack;
        return this;
    }

    public ItemStack getLightSpeedCost() {
        return this.lightSpeedCost;
    }

    public int getGuiScale() {
        return this.guiScale;
    }

    public Galaxy guiScale(int scale) {
        this.guiScale = Mth.clamp(scale, 1, 6);
        return this;
    }

    public static List<Galaxy> getVisibleGalaxies() {
        ArrayList<Galaxy> unlockedGalaxies = new ArrayList<>();
        for (Galaxy galaxy : DIMENSIONS.values()) {
            if (!galaxy.isHidden)
                unlockedGalaxies.add(galaxy);
        }
        return unlockedGalaxies;
    }
}