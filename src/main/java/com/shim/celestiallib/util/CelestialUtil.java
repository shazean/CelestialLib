package com.shim.celestiallib.util;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.util.teleportation.AbstractCelestialTeleportData;
import com.shim.celestiallib.util.teleportation.CelestialCoordinateTeleport;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.world.celestials.ICelestial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class CelestialUtil {

    public static TranslatableComponent getDisplayName(ResourceKey<Level> dimension) {
        return new TranslatableComponent("dimension." + dimension.location().getNamespace() + "." + dimension.location().getPath());
    }

    //Credit to: https://stackoverflow.com/questions/481144/equation-for-testing-if-a-point-is-inside-a-circle
    public static boolean isInRectangle(int centerX, int centerY, int radius, int x, int y) {
        return x >= centerX - radius && x <= centerX + radius &&
                y >= centerY - radius && y <= centerY + radius;
    }

    public static boolean isInRectangle(double centerX, double centerY, int radius, double x, double y) {
        return x >= centerX - radius && x <= centerX + radius &&
                y >= centerY - radius && y <= centerY + radius;
    }

    //Credit to: https://stackoverflow.com/questions/481144/equation-for-testing-if-a-point-is-inside-a-circle
    public static boolean isPointInCircle(int centerX, int centerY, int radius, int x, int y) {
        if (isInRectangle(centerX, centerY, radius, x, y)) {
            int dx = centerX - x;
            int dy = centerY - y;
            dx *= dx;
            dy *= dy;
            int distanceSquared = dx + dy;
            int radiusSquared = radius * radius;
            return distanceSquared <= radiusSquared;
        }
        return false;
    }

//    public static int getCalculatedCircleRadius(double radius) {
//        return (int) (radius * getSpaceRatio());
//    }

    protected static final Map<ResourceKey<Level>, AbstractCelestialTeleportData> DIMENSION_LOCATION = new HashMap<>();
    protected static final AbstractCelestialTeleportData defaultDimensionLocation = new CelestialCoordinateTeleport(null, -2, 0);

    public static AbstractCelestialTeleportData getDimensionLocation(ResourceKey<Level> dimension) {
        return DIMENSION_LOCATION.get(dimension);
    }

    public static void setDimensionLocation(ResourceKey<Level> dimension, AbstractCelestialTeleportData data) {
        DIMENSION_LOCATION.put(dimension, data);
    }

    public static void clearDimensionLocations() {
        DIMENSION_LOCATION.clear();
    }

    public static BlockPos getDimensionToSpaceCoordinates(ResourceKey<Level> dimension, ChunkPos pos) {
        Vec3 coord = getDimensionLocation(dimension).getOutputCoordinates(pos.x, pos.z); //new Vec3(CE_DIMENSION_LOCATION.get(dimension).x() * CelestialUtil.getSpaceRatio(), 0, CE_DIMENSION_LOCATION.get(dimension).z() * CelestialUtil.getSpaceRatio()); //getDimensionLocation(dimension).getOutputCoordinates(pos.x, pos.z); //FIXME
        if (coord == null) coord = defaultDimensionLocation.getOutputCoordinates(pos.x, pos.z);
        return new BlockPos(coord.x * 16, 145.0, coord.z * 16);
    }

    protected static final Map<ResourceKey<Level>, Vec3> PLANET_LOCATIONS = new HashMap<>();

    public static Vec3 getPlanetLocation(ResourceKey<Level> dimension) {
        return PLANET_LOCATIONS.get(dimension);
    }

    public static Vec3 getPlanetLocation(Planet planet) {
        return PLANET_LOCATIONS.get(planet.getDimension());
    }

    public static Map<ResourceKey<Level>, Vec3> getPlanetLocations() {
        return PLANET_LOCATIONS;
    }

    public static void setPlanetLocation(ResourceKey<Level> dimension, Vec3 data) {
        PLANET_LOCATIONS.put(dimension, data);
    }

    public static void clearPlanetLocations() {
        PLANET_LOCATIONS.clear();
    }

    public static ChunkPos getPlanetChunkCoordinates(ResourceKey<Level> planet) {
        Vec3 coord = getPlanetLocation(planet);
        if (coord == null) {
            CelestialLib.LOGGER.error("Can't find planet location for " + planet + ". Probably missing spawn_chunk_coordinates from structures/planets file.");
            return null;
        }
        int galaxyRatio = Planet.getPlanet(planet).getGalaxy().getGalaxyRatio();
        return new ChunkPos((int) coord.x * galaxyRatio, (int) coord.z * galaxyRatio);
    }

    public static BlockPos getPlanetBlockCoordinates(ResourceKey<Level> planet) {
        Vec3 coord = getPlanetLocation(planet);
        if (coord == null) {
            CelestialLib.LOGGER.error("Can't find planet location for " + planet + ". Probably missing spawn_chunk_coordinates from structures/planets file.");
            return null;
        }
        int galaxyRatio = Planet.getPlanet(planet).getGalaxy().getGalaxyRatio();
        return new BlockPos(coord.x * galaxyRatio * 16, coord.y, coord.z * galaxyRatio * 16);
    }

    public static ResourceKey<Level> getDimensionFromString(String dimensionKey) {
        String[] resource = dimensionKey.split(":");
        String namespace = resource[0];
        String path = resource[1];

        return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(namespace, path));
    }

    public static Planet getPlanetFromString(String string) {
        return getPlanetFromResourceLocation(new ResourceLocation(string));
    }

    public static Planet getPlanetFromResourceLocation(ResourceLocation loc) {
        ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, loc);
        return Planet.getPlanet(dimension);
    }

    public static Galaxy getGalaxyFromString(String string) {
        return getGalaxyFromResourceLocation(new ResourceLocation(string));
    }

    public static Galaxy getGalaxyFromResourceLocation(ResourceLocation loc) {
        ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, loc);
        return Galaxy.getGalaxy(dimension);
    }


    public static Map<ResourceLocation, List<ICelestial>> LOCKED_CELESTIALS = new HashMap<>();
    public static Map<ResourceLocation, List<ICelestial>> LOCKED_CELESTIALS_LIGHT_SPEED = new HashMap<>();

    public static void clearLockedCelestials() {
        LOCKED_CELESTIALS.clear();
    }

    public static void clearLockedLightSpeedCelestials() {
        LOCKED_CELESTIALS_LIGHT_SPEED.clear();
    }

    public static void addLockedCelestial(ResourceLocation advancement, ICelestial celestial) {

        if (LOCKED_CELESTIALS.containsKey(advancement)) {
            ArrayList<ICelestial> list = new ArrayList<> (LOCKED_CELESTIALS.get(advancement));
            list.add(celestial);
            LOCKED_CELESTIALS.put(advancement, list);
        } else {
            LOCKED_CELESTIALS.put(advancement, Collections.singletonList(celestial));
        }
    }

    public static void addLockedLightSpeedCelestial(ResourceLocation advancement, ICelestial celestial) {


        if (LOCKED_CELESTIALS_LIGHT_SPEED.containsKey(advancement)) {
            ArrayList<ICelestial> list = new ArrayList<> (LOCKED_CELESTIALS_LIGHT_SPEED.get(advancement));
            list.add(celestial);
            LOCKED_CELESTIALS_LIGHT_SPEED.put(advancement, list);
            CelestialLib.LOGGER.debug("locked celestials: " + LOCKED_CELESTIALS_LIGHT_SPEED.values());
            CelestialLib.LOGGER.debug("celestial locked: " + LOCKED_CELESTIALS_LIGHT_SPEED.get(advancement).get(0).isLocked());

        } else {
            LOCKED_CELESTIALS_LIGHT_SPEED.put(advancement, Collections.singletonList(celestial));
        }
    }

    public static List<ICelestial> getLockedCelestials(ResourceLocation advancement) {
        return LOCKED_CELESTIALS.getOrDefault(advancement, null);
    }

    public static List<ICelestial> getLockedLightSpeedCelestials(ResourceLocation advancement) {
        return LOCKED_CELESTIALS_LIGHT_SPEED.getOrDefault(advancement, null);
    }


//    protected static Map<UnlockCondition, ICelestial> UNLOCKABLE_CELESTIALS = new HashMap<>();
//
//    public static void clearUnlockableCelestials() {
//        UNLOCKABLE_CELESTIALS.clear();
//    }
//
//    public static ICelestial getUnlockableCelestial(UnlockCondition condition) {
//        return UNLOCKABLE_CELESTIALS.get(condition);
//    }
//
//    public static void addUnlockableCelestial(UnlockCondition condition, ICelestial celestial) {
//        UNLOCKABLE_CELESTIALS.put(condition, celestial);
//    }
//
//    public static void addUnlockableCelestial(List<UnlockCondition> conditions, ICelestial celestial) {
//        for (UnlockCondition condition : conditions) {
//            UNLOCKABLE_CELESTIALS.put(condition, celestial);
//        }
//    }
//
//    public static List<ICelestial> getCelestialsWithMatchingCondition(UnlockCondition condition) {
//        ArrayList<ICelestial> list = new ArrayList<>();
//        for (UnlockCondition c : UNLOCKABLE_CELESTIALS.keySet()) {
//            if (c.equals(condition)) {
//                list.add(UNLOCKABLE_CELESTIALS.get(c));
//            }
//        }
//        return list;
//    }

}