package com.shim.celestiallib.data.gen;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
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

public class PlanetStructureTravel {
    private final ResourceLocation id;
    private final ResourceKey<Level> dimension;
    private final PlanetStructureTravel.SpaceCoordinates coordinates;
    private final List<Block> blocksList;


    public PlanetStructureTravel(ResourceLocation id, ResourceKey<Level> dimension, PlanetStructureTravel.SpaceCoordinates coordinates, List<Block> blocksList) {
        this.id = id;
        this.dimension = dimension;
        this.coordinates = coordinates;
        this.blocksList = blocksList;
    }

    public PlanetStructureTravel.Builder deconstruct() {
        return new PlanetStructureTravel.Builder(this.dimension, this.coordinates, this.blocksList);
    }

    public static Builder builder() {
        return new Builder();
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
        PlanetStructureTravel.SpaceCoordinates coordinates;
        List<Block> blocksList;

        public Builder(ResourceKey<Level> dimension, PlanetStructureTravel.SpaceCoordinates coordinates, List<Block> blocksList) {
            this.dimension = dimension;
            this.coordinates = coordinates;
            this.blocksList = blocksList;
        }

        private Builder() {
            this.blocksList = new ArrayList<>();
        }

        public PlanetStructureTravel.Builder dimension(ResourceKey<Level> dimension) {
            this.dimension = dimension;
            return this;
        }

        public PlanetStructureTravel.Builder coordinates(PlanetStructureTravel.SpaceCoordinates coord) {
            this.coordinates = coord;
            return this;
        }

        public PlanetStructureTravel.Builder block(Block block) {
            this.blocksList.add(block);
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, PlanetStructureTravel> p_138393_) {
            return dimension != null && coordinates != null && blocksList != null;
        }

        public PlanetStructureTravel build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialExploration.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete teleport!");
            } else {
                return new PlanetStructureTravel(resourceLocation, this.dimension, this.coordinates, this.blocksList);
            }
        }

        public PlanetStructureTravel save(Consumer<PlanetStructureTravel> consumer, String name) {
            PlanetStructureTravel dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("target_dimension", this.dimension.location().toString());

            json.add("spawn_chunk_coordinates", this.coordinates.serializeToJson());

            JsonArray jsonArray = new JsonArray();

            for (Block block : this.blocksList) {
                jsonArray.add(block.getRegistryName().toString());
            }

            json.add("blocks", jsonArray);

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

            if (this.blocksList == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                for (Block block : this.blocksList)
                    byteBuf.writeResourceLocation(block.getRegistryName());
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
            Consumer<PlanetStructureTravel> consumer = (teleport) -> {
                if (!set.add(teleport.getId())) {
                    throw new IllegalStateException("Duplicate planet " + teleport.getId());
                } else {
                    Path path1 = createPath(path, teleport);

                    try {
                        DataProvider.save(GSON, cache, teleport.deconstruct().serializeToJson(), path1);
                    } catch (IOException ioexception) {
                        LOGGER.error("Couldn't save planet {}", path1, ioexception);
                    }

                }
            };

            register(consumer, fileHelper);
        }

        protected void register(Consumer<PlanetStructureTravel> consumer, net.minecraftforge.common.data.ExistingFileHelper fileHelper) {
        }

        public PlanetStructureTravel.SpaceCoordinates coord(int x, int z) {
            return new PlanetStructureTravel.SpaceCoordinates(x, z);
        }

        private static Path createPath(Path path, PlanetStructureTravel travel) {
            return path.resolve("data/" + modid + "/" + modid + "/structures/planets/" + travel.getId().getPath() + ".json");
        }

        public String getName() {
            return "Planet Structure Data";
        }
    }
}