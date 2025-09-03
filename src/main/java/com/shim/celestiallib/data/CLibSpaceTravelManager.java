package com.shim.celestiallib.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.util.teleportation.CelestialCoordinateTeleport;
import com.shim.celestiallib.util.teleportation.CelestialScaledTeleport;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;

import java.util.Map;

public class CLibSpaceTravelManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public CLibSpaceTravelManager() {
        super(GSON, "celestial/space_travel");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager p_10794_, ProfilerFiller p_10795_) {

        CelestialUtil.clearDimensionLocations();

        elements.forEach((dimensionPath, element) -> {

            JsonObject json = element.getAsJsonObject();

//            String dimName = GsonHelper.getAsString(json, "dimension");
            ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimensionPath);

            String galaxyName = GsonHelper.getAsString(json, "galaxy");
            Galaxy galaxy = Galaxy.getGalaxy(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(galaxyName)));

            if (json.has("space_chunk_coordinates")) {
                JsonObject coordinates = GsonHelper.getAsJsonObject(json, "space_chunk_coordinates");

                if (!coordinates.isJsonNull()) {
                    if (coordinates.has("x") && coordinates.has("z")) {
                        int x = GsonHelper.getAsInt(coordinates, "x");
                        int z = GsonHelper.getAsInt(coordinates, "z");
                        CelestialUtil.setDimensionLocation(dimension, new CelestialCoordinateTeleport(galaxy, x, z));
                    }
                }
            } else if (json.has("coordinate_scale")) {
                double scale = GsonHelper.getAsDouble(json, "coordinate_scale");
                if (scale <= 0.0D) {
                    throw new IllegalStateException("Coordinate scale of " + dimension + " must be greater than 0!");
                }
                CelestialUtil.setDimensionLocation(dimension, new CelestialScaledTeleport(scale));
            }
        });
    }
}