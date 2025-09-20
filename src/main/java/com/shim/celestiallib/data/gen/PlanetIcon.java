package com.shim.celestiallib.data.gen;

import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Function;

public class PlanetIcon {
    private final ResourceLocation id;
    private final ResourceLocation texture;
    private final int size;

    public PlanetIcon(ResourceLocation id, ResourceLocation texture, int size) {
        this.id = id;
        this.texture = texture;
        this.size = size;
    }

    public PlanetIcon.Builder deconstruct() {
        return new PlanetIcon.Builder(this.texture, this.size);
    }

    public static Builder builder() {
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

        private Builder() {
        }

        public PlanetIcon.Builder texture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public PlanetIcon.Builder size(int size) {
            this.size = size;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, PlanetIcon> p_138393_) {
            return texture != null;
        }

        public PlanetIcon build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete planet icon!");
            } else {
                return new PlanetIcon(resourceLocation, this.texture, this.size);
            }
        }

        public PlanetIcon save(Consumer<PlanetIcon> consumer, Planet planet) {
            return save(consumer, planet.getDimension());
        }

        public PlanetIcon save(Consumer<PlanetIcon> consumer, ResourceKey<Level> dimension) {
            return save(consumer, dimension.location());
        }

        public PlanetIcon save(Consumer<PlanetIcon> consumer, ResourceLocation name) {
            PlanetIcon dimension = this.build(name);
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