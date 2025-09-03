package com.shim.celestiallib.data.gen;

import com.google.gson.JsonObject;
import com.shim.celestiallib.api.effects.GravityEffect;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlanetData {
    private final ResourceLocation id;
    private final ResourceLocation gravity;
    private final boolean locked;
    private final ResourceLocation unlockable;
    private final boolean lightSpeedLocked;
    private final boolean lightSpeedHidden;
    private final ResourceLocation lightSpeedUnlockable;
    private final ItemStack lightSpeedCost;
    private final int multiplier;

    public PlanetData(ResourceLocation id, ResourceLocation gravity, boolean locked, ResourceLocation unlockable, boolean lightSpeedLocked, boolean lightSpeedHidden, ResourceLocation lightSpeedUnlockable, ItemStack lightSpeedCost, int multiplier) {
        this.id = id;
        this.gravity = gravity;
        this.locked = locked;
       this.unlockable = unlockable;
        this.lightSpeedLocked = lightSpeedLocked;
        this.lightSpeedHidden = lightSpeedHidden;
        this.lightSpeedUnlockable = lightSpeedUnlockable;
        this.lightSpeedCost = lightSpeedCost;
        this.multiplier = multiplier;
    }

    public PlanetData.Builder deconstruct() {
        return new PlanetData.Builder(this.gravity, this.locked, this.unlockable, this.lightSpeedLocked, this.lightSpeedHidden, this.lightSpeedUnlockable, this.lightSpeedCost, this.multiplier);
    }

    public static PlanetData.Builder builder() {
        return new PlanetData.Builder();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        ResourceLocation gravity;
        boolean locked;
        ResourceLocation unlockable;
        boolean lightSpeedLocked;
        boolean lightSpeedHidden;
        ResourceLocation lightSpeedUnlockable;
        ItemStack lightSpeedCost;
        int multiplier;

        private Builder() {}

        public Builder(ResourceLocation gravity, boolean locked, ResourceLocation unlockable, boolean lightSpeedLocked, boolean lightSpeedHidden, ResourceLocation lightSpeedUnlockable, ItemStack lightSpeedCost, int multiplier) {
            this.gravity = gravity;
            this.locked = locked;
            this.unlockable = unlockable;
            this.lightSpeedLocked = lightSpeedLocked;
            this.lightSpeedHidden = lightSpeedHidden;
            this.lightSpeedUnlockable = lightSpeedUnlockable;
            this.lightSpeedCost = lightSpeedCost;
            this.multiplier = multiplier;
        }

        public PlanetData.Builder gravity(GravityEffect gravity) {
            this.gravity = gravity.getRegistryName();
            return this;
        }

        public PlanetData.Builder unlockBy(ResourceLocation unlockBy) {
            this.locked = true;
            this.unlockable = unlockBy;
            return this;
        }

        public PlanetData.Builder lightSpeedLocked(boolean locked, boolean hidden, ResourceLocation unlockAdvancement) {
            this.lightSpeedLocked = locked;
            this.lightSpeedHidden = hidden;
            this.lightSpeedUnlockable = unlockAdvancement;
            return this;
        }

        public PlanetData.Builder lightSpeedCost(ItemStack cost, int multiplier) {
            this.lightSpeedCost = cost;
            this.multiplier = multiplier;
            return this;
        }

        public PlanetData.Builder lightSpeedCost(ItemStack cost) {
            this.lightSpeedCost = cost;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, PlanetData> dimension) {
            return true;
        }

        public PlanetData build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialLib.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete planet!");
            } else {
                return new PlanetData(resourceLocation, this.gravity, this.locked, this.unlockable, this.lightSpeedLocked, this.lightSpeedHidden, this.lightSpeedUnlockable, this.lightSpeedCost, this.multiplier);
            }
        }

        public PlanetData save(Consumer<PlanetData> consumer, Planet planet) {
            return save(consumer, planet.getDimension());
        }

        public PlanetData save(Consumer<PlanetData> consumer, ResourceKey<Level> dimension) {
            return save(consumer, dimension.location().getPath());
        }

        public PlanetData save(Consumer<PlanetData> consumer, String name) {
            PlanetData dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            if (this.gravity != null)
                json.addProperty("gravity", this.gravity.toString());

            if (this.unlockable != null) {
                JsonObject lockedJson = new JsonObject();
                lockedJson.addProperty("unlock_advancement", this.unlockable.toString());
                json.add("locked", lockedJson);
            }


            JsonObject lightSpeedJson = new JsonObject();
            JsonObject costJson = new JsonObject();

            if (this.lightSpeedCost != null) {
                costJson.addProperty("item", this.lightSpeedCost.getItem().getRegistryName().toString());
                costJson.addProperty("count", this.lightSpeedCost.getCount());

                lightSpeedJson.add("base_cost", costJson);
            }

            if (this.multiplier != -1)
                lightSpeedJson.addProperty("cost_multiplier", this.multiplier);

            JsonObject lockedLightSpeedJson = new JsonObject();

            if (this.lightSpeedLocked) {
                lockedLightSpeedJson.addProperty("hidden", this.lightSpeedHidden);
                if (this.lightSpeedUnlockable != null)
                    lockedLightSpeedJson.addProperty("unlock_advancement", this.lightSpeedUnlockable.toString());
            }

            lightSpeedJson.add("locked", lockedLightSpeedJson);
            json.add("light_speed_travel", lightSpeedJson);

            return json;
        }


        public void serializeToNetwork(FriendlyByteBuf byteBuf) {

            if (this.gravity != null) {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.gravity);
            } else {
                byteBuf.writeBoolean(false);
            }

           byteBuf.writeBoolean(this.locked);
            if (this.unlockable != null) {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.unlockable);
            } else {
                byteBuf.writeBoolean(false);
            }

            if (this.lightSpeedCost != null) {
                byteBuf.writeBoolean(true);
                byteBuf.writeItem(this.lightSpeedCost);
            }

            if (this.multiplier != -1) {
                byteBuf.writeBoolean(true);
                byteBuf.writeInt(this.multiplier);
            } else {
                byteBuf.writeBoolean(false);
            }

            byteBuf.writeBoolean(this.lightSpeedLocked);
            byteBuf.writeBoolean(this.lightSpeedHidden);

            if (this.lightSpeedUnlockable != null) {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.lightSpeedUnlockable);
            } else {
                byteBuf.writeBoolean(false);
            }
        }
    }
}