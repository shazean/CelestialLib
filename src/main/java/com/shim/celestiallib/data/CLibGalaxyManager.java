package com.shim.celestiallib.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

public class CLibGalaxyManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public CLibGalaxyManager() {
        super(GSON, "celestial/galaxy");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager p_10794_, ProfilerFiller p_10795_) {


        elements.forEach((dimensionPath, element) -> {

            JsonObject json = element.getAsJsonObject();

            String dimName = GsonHelper.getAsString(json, "galaxy");
            ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimName));
            Galaxy galaxy = Galaxy.getGalaxy(dimension);

            int scale = GsonHelper.getAsInt(json, "scale_ratio", 1);

            ItemStack cost = null;
            boolean locked = false;
            boolean hidden = false;
            String unlockable = null; //FIXME change to criteria

            if (json.has("light_speed_travel")) {
                JsonObject lightSpeedTravel = GsonHelper.getAsJsonObject(json, "light_speed_travel");

                if (!lightSpeedTravel.isJsonNull()) {

                    if (lightSpeedTravel.has("base_cost")) {
                        JsonObject baseCost = GsonHelper.getAsJsonObject(json, "base_cost");
                        if (!baseCost.isJsonNull()) {
                            if (baseCost.has("item")) {
                                int count = GsonHelper.getAsInt(baseCost, "count", 1);

                                cost = new ItemStack(GsonHelper.getAsItem(baseCost, "item"), count);

                            }
                        }
                    }

                    if (lightSpeedTravel.has("locked")) {
                        locked = true;
                        JsonObject lockedJson = GsonHelper.getAsJsonObject(lightSpeedTravel, "locked");

                        if (!lockedJson.isJsonNull()) {
                            hidden = GsonHelper.getAsBoolean(lockedJson, "hidden", false);
                            unlockable = GsonHelper.getAsString(lockedJson, "unlock_criteria");

                            if (unlockable == null) {
                                throw new IllegalStateException("planet " + dimension + " missing unlock criteria");
                            }
                        }
                    }
                }
            }

            if (cost != null)
                galaxy.lightSpeedCost(cost);

            galaxy.setGalaxyRatio(scale);

            if (locked) {
                galaxy.lockedAndMaybeHidden(hidden); //TODO add criteria
            }
        });
    }
}