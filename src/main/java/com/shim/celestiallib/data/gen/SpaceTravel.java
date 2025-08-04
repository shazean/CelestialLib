package com.shim.celestiallib.data.gen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
    private final ResourceKey<Level> dimension;
    @Nullable
    private final SpaceCoordinates coordinates;
    @Nullable
    private final double coordinateScale;

    public SpaceTravel(ResourceLocation id, ResourceKey<Level> dimension, SpaceCoordinates coordinates) {
        this(id, dimension, coordinates, -1);
    }

    public SpaceTravel(ResourceLocation id, ResourceKey<Level> dimension, double coordinateScale) {
        this(id, dimension, null, coordinateScale);
    }

    public SpaceTravel(ResourceLocation id, ResourceKey<Level> dimension, SpaceCoordinates coordinates, double coordinateScale) {
        this.id = id;
        this.dimension = dimension;
        this.coordinates = coordinates;
        this.coordinateScale = coordinateScale;
    }

    public static Builder add() {
        return new Builder();
    }

    public Builder deconstruct() {
        return new Builder(this.dimension, this.coordinates, this.coordinateScale);
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
        ResourceKey<Level> dimension;
        @Nullable
        SpaceCoordinates coordinates;
        double coordinateScale;

        public Builder(ResourceKey<Level> dimension, @Nullable SpaceCoordinates coordinates) {
            this(dimension, coordinates, -1);
        }

        public Builder(ResourceKey<Level> dimension, double coordinateScale) {
            this(dimension, null, coordinateScale);
        }

        public Builder(ResourceKey<Level> dimension, @Nullable SpaceCoordinates coordinates, double coordinateScale) {
            this.dimension = dimension;
            this.coordinates = coordinates;
            this.coordinateScale = coordinateScale;
        }

        private Builder() {
            this.coordinates = null;
            this.coordinateScale = -1;
        }

        public Builder dimension(ResourceKey<Level> dimension) {
            this.dimension = dimension;
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
            if (dimension == null) {
                return false;
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
                return new SpaceTravel(resourceLocation, this.dimension, this.coordinates, this.coordinateScale);
            }
        }

        public SpaceTravel save(Consumer<SpaceTravel> consumer, String name) {
            SpaceTravel dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("dimension", this.dimension.location().toString());

            if (this.coordinates != null) {
                json.add("space_chunk_coordinates", this.coordinates.serializeToJson());
            }

            if (this.coordinateScale != -1) {
                json.addProperty("coordinate_scale", this.coordinateScale);
            }

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {
            if (this.dimension == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.dimension.getRegistryName());
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

    public class Provider implements DataProvider {
        private static final Logger LOGGER = LogUtils.getLogger();
        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
        private final DataGenerator generator;
        protected net.minecraftforge.common.data.ExistingFileHelper fileHelper;
        protected static String modid;

        public Provider(DataGenerator generatorIn, String modid, net.minecraftforge.common.data.ExistingFileHelper fileHelperIn) {
            this.generator = generatorIn;
            this.modid = modid;
            this.fileHelper = fileHelperIn;
        }

        public void run(HashCache cache) {
            Path path = this.generator.getOutputFolder();
            Set<ResourceLocation> set = Sets.newHashSet();
            Consumer<SpaceTravel> consumer = (teleport) -> {
                if (!set.add(teleport.getId())) {
                    throw new IllegalStateException("Duplicate dimension " + teleport.getId());
                } else {
                    Path path1 = createPath(path, teleport);

                    try {
                        DataProvider.save(GSON, cache, teleport.deconstruct().serializeToJson(), path1);
                    } catch (IOException ioexception) {
                        LOGGER.error("Couldn't save dimension {}", path1, ioexception);
                    }

                }
            };

            register(consumer, fileHelper);
        }

        protected void register(Consumer<SpaceTravel> consumer, net.minecraftforge.common.data.ExistingFileHelper fileHelper) {
        }

        public SpaceTravel.SpaceCoordinates coord(int x, int z) {
            return new SpaceTravel.SpaceCoordinates(x, z);
        }

        private static Path createPath(Path p_123971_, SpaceTravel p_123972_) {
            return p_123971_.resolve("data/" + modid + "/" + modid + "/space_travel/" + p_123972_.getId().getPath() + ".json");
        }

        public String getName() {
            return "Space Travel Data";
        }
    }
}