package com.shim.celestiallib.data.gen;

import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Function;

public class GalaxyIcon {
    private final ResourceLocation id;
    private final ResourceLocation texture;

    public GalaxyIcon(ResourceLocation id, ResourceLocation texture) {
        this.id = id;
        this.texture = texture;
    }

    public GalaxyIcon.Builder deconstruct() {
        return new GalaxyIcon.Builder(this.texture);
    }

    public static Builder builder() {
        return new Builder();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        ResourceLocation texture;

        public Builder(ResourceLocation texture) {
            this.texture = texture;
        }

        private Builder() {
        }

        public GalaxyIcon.Builder texture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, GalaxyIcon> p_138393_) {
            return texture != null;
        }

        public GalaxyIcon build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete galaxy background!");
            } else {
                return new GalaxyIcon(resourceLocation, this.texture);
            }
        }

        public GalaxyIcon save(Consumer<GalaxyIcon> consumer, String name) {
            GalaxyIcon dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("texture", this.texture.toString());

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {
            if (this.texture == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.texture);
            }
        }
    }
}