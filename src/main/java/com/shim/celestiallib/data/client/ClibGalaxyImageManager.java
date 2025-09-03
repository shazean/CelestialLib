package com.shim.celestiallib.data.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.util.CelestialUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class ClibGalaxyImageManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final int MIN = 210;
    private static final int MAX = 1024;


    public ClibGalaxyImageManager() {
        super(GSON, "models/celestial/galaxy_background");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager p_10794_, ProfilerFiller p_10795_) {

        elements.forEach((dimensionPath, element) -> {

            JsonObject json = element.getAsJsonObject();

            String[] resource = GsonHelper.getAsString(json, "texture").split(":");
            String namespace = resource[0];
            String path = resource[1];

            ResourceLocation texture = new ResourceLocation(namespace, "textures/" + path);

            int size = GsonHelper.getAsInt(json, "size");

            if (size < MIN || size > MAX) {
                throw new IllegalStateException(String.format("Galaxy background image size is out of bounds. Must be within sizes %d and %d, inclusive", MIN, MAX));
            }

            Galaxy galaxy = CelestialUtil.getGalaxyFromResourceLocation(dimensionPath);

            galaxy.setBackgroundImage(texture, size);

        });
    }
}