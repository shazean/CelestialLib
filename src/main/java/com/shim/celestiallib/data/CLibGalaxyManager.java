package com.shim.celestiallib.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.util.CelestialUtil;
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

//            String dimName = GsonHelper.getAsString(json, "galaxy");
//            ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimName));
            Galaxy galaxy = CelestialUtil.getGalaxyFromResourceLocation(dimensionPath);

            CelestialLib.LOGGER.debug("dimensionPath: " + dimensionPath + ", galaxy: " + galaxy.getDimension());

            int scale = GsonHelper.getAsInt(json, "scale_ratio", 1);

            ItemStack cost = null;
            boolean lightSpeedLocked = false;
            boolean lightSpeedHidden = false;
            ResourceLocation lightSpeedUnlockable = null;

            if (json.has("light_speed_travel")) {
                JsonObject lightSpeedTravel = GsonHelper.getAsJsonObject(json, "light_speed_travel");

                if (!lightSpeedTravel.isJsonNull()) {

                    if (lightSpeedTravel.has("base_cost")) {
                        JsonObject baseCost = GsonHelper.getAsJsonObject(lightSpeedTravel, "base_cost");
                        if (!baseCost.isJsonNull()) {
                            if (baseCost.has("item")) {
                                int count = GsonHelper.getAsInt(baseCost, "count", 1);

                                cost = new ItemStack(GsonHelper.getAsItem(baseCost, "item"), count);

                            }
                        }
                    }

                    if (lightSpeedTravel.has("locked")) {
                        lightSpeedLocked = true;
                        JsonObject lockedJson = GsonHelper.getAsJsonObject(lightSpeedTravel, "locked");

                        if (!lockedJson.isJsonNull()) {
                            lightSpeedHidden = GsonHelper.getAsBoolean(lockedJson, "hidden", false);
                            if (lockedJson.has("unlock_advancement"))
                                lightSpeedUnlockable = new ResourceLocation(GsonHelper.getAsString(lockedJson, "unlock_advancement"));

                            if (lightSpeedUnlockable == null) {
                                CelestialLib.LOGGER.warn("galaxy {} is locked for light speed travel but missing unlock advancement", galaxy);
                            }
                        }
                    }
                }
            }


            if (cost != null)
                galaxy.lightSpeedCost(cost);

            galaxy.setGalaxyRatio(scale);

            if (lightSpeedLocked) {
                galaxy.lightSpeedLockedAndMaybeHidden(lightSpeedHidden);
                CelestialUtil.addLockedLightSpeedCelestial(lightSpeedUnlockable, galaxy);

            }
        });
    }
}