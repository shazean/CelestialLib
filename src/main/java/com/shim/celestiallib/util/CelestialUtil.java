package com.shim.celestiallib.util;

import com.shim.celestiallib.util.teleportation.AbstractCelestialTeleportData;
import com.shim.celestiallib.util.teleportation.CelestialCoordinateTeleport;
import com.shim.celestiallib.world.planet.Planet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class CelestialUtil {

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


    public static Vec3 getDimensionToSpaceCoordinates(ResourceKey<Level> dimension, ChunkPos pos) {
        Vec3 coord = getDimensionLocation(dimension).getOutputCoordinates(pos.x, pos.z); //new Vec3(CE_DIMENSION_LOCATION.get(dimension).x() * CelestialUtil.getSpaceRatio(), 0, CE_DIMENSION_LOCATION.get(dimension).z() * CelestialUtil.getSpaceRatio()); //getDimensionLocation(dimension).getOutputCoordinates(pos.x, pos.z); //FIXME
        if (coord == null) coord = defaultDimensionLocation.getOutputCoordinates(pos.x, pos.z);
        coord = new Vec3(coord.x * 16, 145.0, coord.z * 16); //convert from chunk to block pos
        return coord;
    }

    protected static final Map<ResourceKey<Level>, Vec3> PLANET_LOCATIONS = new HashMap<>();

    public static Vec3 getPlanetLocation(ResourceKey<Level> dimension) {
        return PLANET_LOCATIONS.get(dimension);
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

    public static Vec3 getPlanetaryChunkCoordinates(ResourceKey<Level> planet) {
        Vec3 coord = getPlanetLocation(planet);
        int galaxyRatio = Planet.getPlanet(planet).getGalaxy().getGalaxyRatio();
        coord = new Vec3(coord.x * galaxyRatio, coord.y, coord.z * galaxyRatio);
        return coord;
    }
}