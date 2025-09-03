package com.shim.celestiallib.data.gen;

import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Function;

public class GalaxyBackground {
    private final ResourceLocation id;
    private final ResourceLocation texture;
    private final int size;

    public GalaxyBackground(ResourceLocation id, ResourceLocation texture, int size) {
        this.id = id;
        this.texture = texture;
        this.size = size;
    }

    public GalaxyBackground.Builder deconstruct() {
        return new GalaxyBackground.Builder(this.texture, this.size);
    }

    public static Builder add() {
        return new Builder();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        ResourceLocation texture;
        int size;

        public Builder(ResourceLocation texture, int size) {
            this.texture = texture;
            this.size = size;
        }

        private Builder() {}

        public GalaxyBackground.Builder texture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public GalaxyBackground.Builder size(int size) {
            this.size = size;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, GalaxyBackground> p_138393_) {
            return texture != null;
        }

        public GalaxyBackground build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialLib.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete galaxy background!");
            } else {
                return new GalaxyBackground(resourceLocation, this.texture, this.size);
            }
        }

        public GalaxyBackground save(Consumer<GalaxyBackground> consumer, ResourceKey<Level> dimension) {
            return save(consumer, dimension.location());
        }


        public GalaxyBackground save(Consumer<GalaxyBackground> consumer, ResourceLocation name) {
            GalaxyBackground dimension = this.build(name);
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("texture", this.texture.toString());

            json.addProperty("size", this.size);

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {

            if (this.texture == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.texture);
            }

            if (this.size == -1) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeInt(this.size);
            }
        }
    }
}