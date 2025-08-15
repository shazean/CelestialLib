package com.shim.celestiallib.data.gen;

import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Function;

public class PlanetIcon {
    private final ResourceLocation id;
    private final ResourceLocation planet;
    private final ResourceLocation texture;
    private final int size;

    public PlanetIcon(ResourceLocation id, ResourceLocation planet, ResourceLocation texture, int size) {
        this.id = id;
        this.planet = planet;
        this.texture = texture;
        this.size = size;
    }

    public PlanetIcon.Builder deconstruct() {
        return new PlanetIcon.Builder(this.planet, this.texture, this.size);
    }

    public static Builder builder() {
        return new Builder();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        ResourceLocation planet;
        ResourceLocation texture;
        int size;

        public Builder(ResourceLocation planet, ResourceLocation texture, int size) {
            this.planet = planet;
            this.texture = texture;
            this.size = size;
        }

        private Builder() {
        }

        public PlanetIcon.Builder planet(Planet planet) {
            this.planet = planet.getDimension().location();
            return this;
        }

        public PlanetIcon.Builder texture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public PlanetIcon.Builder block(int size) {
            this.size = size;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, PlanetIcon> p_138393_) {
            return planet != null && texture != null;
        }

        public PlanetIcon build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialLib.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete planet icon!");
            } else {
                return new PlanetIcon(resourceLocation, this.planet, this.texture, this.size);
            }
        }

        public PlanetIcon save(Consumer<PlanetIcon> consumer, String name) {
            PlanetIcon dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("planet", this.planet.toString());
            json.addProperty("texture", this.texture.toString());

            json.addProperty("size", this.size);

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {
            if (this.planet == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.planet);
            }

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