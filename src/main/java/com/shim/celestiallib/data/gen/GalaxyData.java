package com.shim.celestiallib.data.gen;

import com.google.common.escape.Escaper;
import com.google.gson.JsonObject;
import com.shim.celestiallib.api.effects.GravityEffect;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Function;

public class GalaxyData {
    private final ResourceLocation id;
    private final int scaleRatio;
    private final boolean lightSpeedLocked;
    private final boolean lightSpeedHidden;
    private final ResourceLocation lightSpeedUnlockable;
    private final ItemStack lightSpeedCost;

    public GalaxyData(ResourceLocation id, int scaleRatio, boolean lightSpeedLocked, boolean lightSpeedHidden, ResourceLocation lightSpeedUnlockable, ItemStack lightSpeedCost) {
        this.id = id;
        this.scaleRatio = scaleRatio;
        this.lightSpeedLocked = lightSpeedLocked;
        this.lightSpeedHidden = lightSpeedHidden;
        this.lightSpeedUnlockable = lightSpeedUnlockable;
        this.lightSpeedCost = lightSpeedCost;
    }

    public GalaxyData.Builder deconstruct() {
        return new GalaxyData.Builder(this.scaleRatio, this.lightSpeedLocked, this.lightSpeedHidden, this.lightSpeedUnlockable, this.lightSpeedCost);
    }

    public static GalaxyData.Builder builder() {
        return new GalaxyData.Builder();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        int scaleRatio;
        boolean lightSpeedLocked;
        boolean lightSpeedHidden;
        ResourceLocation lightSpeedUnlockable;
        ItemStack lightSpeedCost;

        private Builder() {}

        public Builder(int scaleRatio, boolean lightSpeedLocked, boolean lightSpeedHidden, ResourceLocation lightSpeedUnlockable, ItemStack lightSpeedCost) {
            this.scaleRatio = scaleRatio;
            this.lightSpeedLocked = lightSpeedLocked;
            this.lightSpeedHidden = lightSpeedHidden;
            this.lightSpeedUnlockable = lightSpeedUnlockable;
            this.lightSpeedCost = lightSpeedCost;
        }

        public GalaxyData.Builder scaleRatio(int scaleRatio) {
            this.scaleRatio = scaleRatio;
            return this;
        }

        public GalaxyData.Builder lightSpeedLocked(boolean locked, boolean hidden) {
            this.lightSpeedLocked = locked;
            this.lightSpeedHidden = hidden;
            return this;
        }

        public GalaxyData.Builder unlockLightSpeedBy(ResourceLocation unlockBy) {
            this.lightSpeedUnlockable = unlockBy;
            return this;
        }

        public GalaxyData.Builder lightSpeedCost(ItemStack cost) {
            this.lightSpeedCost = cost;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, GalaxyData> dimension) {
            return true;
        }

        public GalaxyData build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete planet!");
            } else {
                return new GalaxyData(resourceLocation, this.scaleRatio, this.lightSpeedLocked, this.lightSpeedHidden, this.lightSpeedUnlockable, this.lightSpeedCost);
            }
        }

        public GalaxyData save(Consumer<GalaxyData> consumer, Galaxy galaxy) {
            return save(consumer, galaxy.getDimension());
        }

        public GalaxyData save(Consumer<GalaxyData> consumer, ResourceKey<Level> dimension) {
            return save(consumer, dimension.location().getPath());
        }

        public GalaxyData save(Consumer<GalaxyData> consumer, String name) {
            GalaxyData dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            if (this.scaleRatio != 0)
                json.addProperty("scale_ratio", this.scaleRatio);

            JsonObject lightSpeedJson = new JsonObject();
            JsonObject costJson = new JsonObject();

            if (this.lightSpeedCost != null) {
                costJson.addProperty("item", this.lightSpeedCost.getItem().getRegistryName().toString());
                costJson.addProperty("count", this.lightSpeedCost.getCount());

                lightSpeedJson.add("base_cost", costJson);
            }

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

            if (this.scaleRatio != -1) {
                byteBuf.writeBoolean(true);
                byteBuf.writeInt(this.scaleRatio);
            } else {
                byteBuf.writeBoolean(false);
            }

            if (this.lightSpeedCost != null) {
                byteBuf.writeBoolean(true);
                byteBuf.writeItem(this.lightSpeedCost);
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