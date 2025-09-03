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

public class CLibGalaxyIconManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public CLibGalaxyIconManager() {
        super(GSON, "models/celestial/galaxy");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager p_10794_, ProfilerFiller p_10795_) {


        elements.forEach((dimensionPath, element) -> {

            JsonObject json = element.getAsJsonObject();

            String[] resource = GsonHelper.getAsString(json, "texture").split(":");
            String namespace = resource[0];
            String path = resource[1];

            ResourceLocation texture = new ResourceLocation(namespace, "textures/" + path + ".png");

            Galaxy galaxy = CelestialUtil.getGalaxyFromResourceLocation(dimensionPath);
            if (galaxy != null)
                galaxy.setIcon(texture);

        });
    }
}