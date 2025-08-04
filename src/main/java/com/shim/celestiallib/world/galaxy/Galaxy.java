package com.shim.celestiallib.world.galaxy;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.util.CelestialUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;
import java.util.function.Supplier;

public class Galaxy extends ForgeRegistryEntry<Galaxy> {
    int galaxyRatio = 1;
    private final ResourceKey<Level> dimension;
    boolean isLocked = false;
    boolean isHidden = false;
    private ResourceLocation icon = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/galaxy_icons/default_galaxy.png"); //DEFAULT
    private ResourceLocation backgroundImage;
    private int backgroundImageSize;
    private ItemStack lightSpeedCost;
    private int guiScale = 2;
    private Supplier<Integer> yHeight;

    public static final Map<ResourceKey<Level>, Galaxy> DIMENSIONS = new HashMap<>();

    public Galaxy(ResourceKey<Level> galaxyDimension) {
        this.dimension = galaxyDimension;

        if (DIMENSIONS.containsKey(dimension)) {
            throw new IllegalStateException("dimension: " + dimension.toString() + " already has an associated galaxy");
        }
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

    public Galaxy lockedAndMaybeHidden(boolean isHidden) {
        //TODO accept a criteria for unlocking
        this.isLocked = true;
        this.isHidden = isHidden;
        return this;
    }

    public static void unlockGalaxies(Advancement advancement) {

    }

    public void unlock() {
        this.isLocked = false;
        this.isHidden = false;
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

//    public ItemStack getLightSpeedCost() {
//        return this.lightSpeedCost;
//    }

    public ItemStack getLightSpeedCost(Galaxy originatingGalaxy) {
        if (this.equals(originatingGalaxy))
            return new ItemStack(Blocks.AIR);
        else
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

    public void yHeight(Supplier<Integer> yHeight) {
        this.yHeight = yHeight;
    }

    public int getYHeight() {
        if (yHeight != null)
            return yHeight.get();
        else return 135; //default value
    }
}