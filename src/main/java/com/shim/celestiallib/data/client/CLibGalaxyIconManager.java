package com.shim.celestiallib.data.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shim.celestiallib.world.galaxy.Galaxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class CLibGalaxyIconManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public CLibGalaxyIconManager() {
        super(GSON, "models/celestial/galaxy");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager p_10794_, ProfilerFiller p_10795_) {


        elements.forEach((dimensionPath, element) -> {

            JsonObject json = element.getAsJsonObject();

            ResourceLocation dimension = new ResourceLocation(GsonHelper.getAsString(json, "galaxy"));
            ResourceLocation texture = new ResourceLocation(GsonHelper.getAsString(json, "texture"));

            Galaxy galaxy = Galaxy.getGalaxy(ResourceKey.create(Registry.DIMENSION_REGISTRY, dimension));

            galaxy.setIcon(texture);

        });
    }
}