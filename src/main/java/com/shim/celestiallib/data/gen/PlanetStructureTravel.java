package com.shim.celestiallib.data.gen;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shim.celestiallib.api.world.planet.Planet;
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
    private final PlanetStructureTravel.SpaceCoordinates coordinates;
    private final List<Block> blocksList;


    public PlanetStructureTravel(ResourceLocation id, PlanetStructureTravel.SpaceCoordinates coordinates, List<Block> blocksList) {
        this.id = id;
        this.coordinates = coordinates;
        this.blocksList = blocksList;
    }

    public PlanetStructureTravel.Builder deconstruct() {
        return new PlanetStructureTravel.Builder(this.coordinates, this.blocksList);
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
        PlanetStructureTravel.SpaceCoordinates coordinates;
        List<Block> blocksList;

        public Builder(PlanetStructureTravel.SpaceCoordinates coordinates, List<Block> blocksList) {
            this.coordinates = coordinates;
            this.blocksList = blocksList;
        }

        private Builder() {
            this.blocksList = new ArrayList<>();
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
            return coordinates != null && blocksList != null;
        }

        public PlanetStructureTravel build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialExploration.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete teleport!");
            } else {
                return new PlanetStructureTravel(resourceLocation, this.coordinates, this.blocksList);
            }
        }

        public PlanetStructureTravel save(Consumer<PlanetStructureTravel> consumer, Planet planet) {
            return save(consumer, planet.getDimension());
        }

        public PlanetStructureTravel save(Consumer<PlanetStructureTravel> consumer, ResourceKey<Level> dimension) {
            return save(consumer, dimension.location());
        }

        public PlanetStructureTravel save(Consumer<PlanetStructureTravel> consumer, ResourceLocation name) {
            PlanetStructureTravel dimension = this.build(name);
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.add("spawn_chunk_coordinates", this.coordinates.serializeToJson());

            JsonArray jsonArray = new JsonArray();

            for (Block block : this.blocksList) {
                jsonArray.add(block.getRegistryName().toString());
            }

            json.add("blocks", jsonArray);

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {

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
}