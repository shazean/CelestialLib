package com.shim.celestiallib.data.gen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class SpaceTravel {
    private final ResourceLocation id;
    private final Galaxy galaxy;

    @Nullable
    private final SpaceCoordinates coordinates;
    @Nullable
    private final double coordinateScale;

    public SpaceTravel(ResourceLocation id, Galaxy galaxy, SpaceCoordinates coordinates) {
        this(id, galaxy, coordinates, -1);
    }

    public SpaceTravel(ResourceLocation id, Galaxy galaxy, double coordinateScale) {
        this(id, galaxy, null, coordinateScale);
    }

    public SpaceTravel(ResourceLocation id, Galaxy galaxy, SpaceCoordinates coordinates, double coordinateScale) {
        this.id = id;
        this.galaxy = galaxy;
        this.coordinates = coordinates;
        this.coordinateScale = coordinateScale;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder deconstruct() {
        return new Builder(this.galaxy, this.coordinates, this.coordinateScale);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public record SpaceCoordinates(int x, int z) {

        public JsonElement serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("x", this.x());
            json.addProperty("z", this.z());

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {
            byteBuf.writeInt(this.x());
            byteBuf.writeInt(this.z());
        }
    }

    public static class Builder {
        Galaxy galaxy;
        @Nullable
        SpaceCoordinates coordinates;
        double coordinateScale;

        public Builder(Galaxy galaxy, @Nullable SpaceCoordinates coordinates) {
            this(galaxy, coordinates, -1);
        }

        public Builder(Galaxy galaxy, double coordinateScale) {
            this(galaxy, null, coordinateScale);
        }

        public Builder(Galaxy galaxy, @Nullable SpaceCoordinates coordinates, double coordinateScale) {
            this.galaxy = galaxy;
            this.coordinates = coordinates;
            this.coordinateScale = coordinateScale;
        }

        private Builder() {
            this.coordinates = null;
            this.coordinateScale = -1;
        }

        public Builder galaxy(Galaxy galaxy) {
            this.galaxy = galaxy;
            return this;
        }

        public Builder coordinates(SpaceCoordinates coord) {
            this.coordinates = coord;
            return this;
        }

        public Builder coordinateScale(double scale) {
            this.coordinateScale = scale;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, SpaceTravel> p_138393_) {
            if (galaxy == null) {
                throw new IllegalStateException("Missing galaxy");
            }
            if (coordinates == null && coordinateScale == -1) {
                throw new IllegalStateException("Either coordinates or coordinate scale must be set!");
            }
            if (coordinateScale != -1 && coordinateScale < 0) {
                throw new IllegalStateException("Coordinate scale must be greater than 0!");
            }

            return true;
        }

        public SpaceTravel build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> null)) {
                throw new IllegalStateException("Tried to build incomplete teleport!");
            } else {
                return new SpaceTravel(resourceLocation, this.galaxy, this.coordinates, this.coordinateScale);
            }
        }

        public SpaceTravel save(Consumer<SpaceTravel> consumer, Planet planet) {
            return save(consumer, planet.getDimension());
        }

        public SpaceTravel save(Consumer<SpaceTravel> consumer, ResourceKey<Level> dimension) {
            return save(consumer, dimension.location());
        }

        public SpaceTravel save(Consumer<SpaceTravel> consumer, ResourceLocation name) {
            SpaceTravel dimension = this.build(name);
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            if (this.galaxy != null) {
                json.addProperty("galaxy", this.galaxy.location().toString());
            }

            if (this.coordinates != null) {
                json.add("space_chunk_coordinates", this.coordinates.serializeToJson());
            }

            if (this.coordinateScale != -1) {
                json.addProperty("coordinate_scale", this.coordinateScale);
            }

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {
            if (this.galaxy == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.galaxy.location());
            }

            if (this.coordinates == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                this.coordinates.serializeToNetwork(byteBuf);
            }

            if (this.coordinateScale == -1) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeDouble(this.coordinateScale);
            }
        }
    }
}