package com.shim.celestiallib.data;

import com.google.gson.*;
import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.effects.GravityEffect;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.util.CelestialUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class CLibPlanetManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public CLibPlanetManager() {
        super(GSON, "celestial/planet");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager p_10794_, ProfilerFiller p_10795_) {


        elements.forEach((dimensionPath, element) -> {

            JsonObject json = element.getAsJsonObject();

//            String dimName = GsonHelper.getAsString(json, "planet");
//            ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimName));
            Planet planet = CelestialUtil.getPlanetFromResourceLocation(dimensionPath);

            ResourceLocation gravity = null;
            GravityEffect gravityEffect = null;
            if (json.has("gravity")) {
                gravity = ResourceLocation.parse(GsonHelper.getAsString(json, "gravity"));
                gravityEffect = (GravityEffect) ForgeRegistries.MOB_EFFECTS.getValue(gravity);
            }

            ItemStack cost = null;
            int multiplier;
            boolean locked = false;
            boolean lightSpeedLocked = false;
            boolean lightSpeedHidden = false;
            ResourceLocation unlockable = null;
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

                    multiplier = GsonHelper.getAsInt(lightSpeedTravel, "cost_multiplier", 0);

                    if (lightSpeedTravel.has("locked")) {
                        lightSpeedLocked = true;
                        JsonObject lockedJson = GsonHelper.getAsJsonObject(lightSpeedTravel, "locked");

                        if (!lockedJson.isJsonNull()) {
                            lightSpeedHidden = GsonHelper.getAsBoolean(lockedJson, "hidden", false);
                            if (lockedJson.has("unlock_advancement"))
                                lightSpeedUnlockable = new ResourceLocation(GsonHelper.getAsString(lockedJson, "unlock_advancement"));

                            if (lightSpeedUnlockable == null) {
                                CelestialLib.LOGGER.warn("planet {} is locked for light speed travel but missing unlock advancement", planet);
                            }
                        }
                    }
                } else {
                    multiplier = 0;
                }
            } else {
                multiplier = 0;
            }

            if (json.has("locked")) {
                locked = true;
                JsonObject lockedJson = GsonHelper.getAsJsonObject(json, "locked");

                unlockable = new ResourceLocation(GsonHelper.getAsString(lockedJson, "unlock_advancement"));
            }

            if (cost != null)
              planet.lightSpeedCost(cost, () -> multiplier);

            if (locked) {
                planet.locked();
                CelestialUtil.addLockedCelestial(unlockable, planet);
            }

            if (gravity != null && gravityEffect != null)
                planet.gravity(gravityEffect);

            if (lightSpeedLocked) {
                planet.lightSpeedLockedAndMaybeHidden(lightSpeedHidden);
                CelestialUtil.addLockedLightSpeedCelestial(lightSpeedUnlockable, planet);

            }
        });
    }
}