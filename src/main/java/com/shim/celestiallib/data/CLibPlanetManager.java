package com.shim.celestiallib.data;

import com.google.gson.*;
import com.shim.celestiallib.world.planet.Planet;
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
        super(GSON, "celestial/planets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager p_10794_, ProfilerFiller p_10795_) {


        elements.forEach((dimensionPath, element) -> {

            JsonObject json = element.getAsJsonObject();

            String dimName = GsonHelper.getAsString(json, "planet");
            ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimName));
            Planet planet = Planet.getPlanet(dimension);

            ResourceLocation gravity = ResourceLocation.parse(GsonHelper.getAsString(json, "gravity"));

            MobEffect gravityEffect = ForgeRegistries.MOB_EFFECTS.getValue(gravity);

            //TODO?

            ItemStack cost = null;
            float multiplier = 1.0f;
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

                    if (lightSpeedTravel.has("cost_multiplier")) {
                        multiplier = GsonHelper.getAsFloat(lightSpeedTravel, "cost_multiplier", 1.0f);
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
              planet.lightSpeedCost(cost, multiplier);

            if (gravity != null)
                planet.gravity(gravityEffect);

            if (locked) {
                planet.lockedAndMaybeHidden(hidden); //TODO add criteria
            }
        });
    }
}