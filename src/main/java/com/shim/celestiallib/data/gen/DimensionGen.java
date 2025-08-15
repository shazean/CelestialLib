package com.shim.celestiallib.data.gen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class DimensionGen {
    private final ResourceLocation id;
    private final boolean useForgeSeed;
    private final long seed;
    private final ResourceLocation type;
    private final ResourceLocation settings;
    private final ResourceLocation preset;
    private final ResourceKey<Biome> fixedBiome;

    public DimensionGen(ResourceLocation id, boolean useForgeSeed, long seed, ResourceLocation type,
                        ResourceLocation settings, ResourceLocation preset, ResourceKey<Biome> fixedBiome) {
        this.id = id;
        this.useForgeSeed = useForgeSeed;
        this.seed = seed;
        this.type = type;
        this.settings = settings;
        this.preset = preset;
        this.fixedBiome = fixedBiome;
    }

    public DimensionGen.Builder deconstruct() {
        return new DimensionGen.Builder(this.useForgeSeed, this.seed, this.type, this.settings, this.preset, this.fixedBiome);
    }

    public static Builder builder() {
        return new Builder();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        boolean useForgeSeed;
        long seed = 1;
        ResourceLocation type;
        ResourceLocation settings;
        ResourceLocation preset;
        ResourceKey<Biome> fixedBiome;

        private Builder() {}

        public Builder(boolean useForgeSeed, long seed, ResourceLocation type, ResourceLocation settings, ResourceLocation preset, ResourceKey<Biome> fixedbiome) {
            this.useForgeSeed = useForgeSeed;
            this.seed = seed;
            this.type = type;
            this.settings = settings;
            this.preset = preset;
            this.fixedBiome = fixedbiome;
        }

        public DimensionGen.Builder useForgeSeed(boolean useForgeSeed) {
            this.useForgeSeed = useForgeSeed;
            return this;
        }

        public DimensionGen.Builder seed(long seed) {
            this.seed = seed;
            return this;
        }

        public DimensionGen.Builder type(ResourceLocation type) {
            this.type = type;
            return this;
        }

        public DimensionGen.Builder type(ResourceKey<Level> dimension) {
            this.type = dimension.location();
            return this;
        }

        public DimensionGen.Builder settings(ResourceLocation settings) {
            this.settings = settings;
            return this;
        }

        public DimensionGen.Builder settings(ResourceKey<Level> dimension) {
            this.settings = dimension.location();
            return this;
        }

        public DimensionGen.Builder presetOrFixedBiome(@Nullable ResourceLocation preset, @Nullable ResourceKey<Biome> biome) {
            if (preset != null) {
                this.preset = preset;
            } else if (biome != null) {
                this.fixedBiome = biome;
            } else {
                throw new IllegalStateException("preset and biome can not both be null!");
            }
            return this;
        }

        public DimensionGen.Builder preset(ResourceKey<Level> preset) {
            return presetOrFixedBiome(preset.location(), null);
        }


        public boolean canBuild(Function<ResourceLocation, DimensionGen> dimension) {
            return type != null && (preset != null || this.fixedBiome != null);
        }

        public DimensionGen build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialLib.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete dimension!");
            } else {
                return new DimensionGen(resourceLocation, this.useForgeSeed, this.seed,
                        this.type, this.settings,
                        this.preset, this.fixedBiome);
            }
        }

        public DimensionGen save(Consumer<DimensionGen> consumer, ResourceKey<Level> dimension) {
            return save(consumer, dimension.location().getPath());
        }

        public DimensionGen save(Consumer<DimensionGen> consumer, String name) {
            DimensionGen dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("type", this.type.toString());
            json.addProperty("forge:use_server_seed", this.useForgeSeed);

            JsonObject generatorJson = new JsonObject();

            generatorJson.addProperty("type", "minecraft:noise");
            generatorJson.addProperty("seed", this.seed);
            generatorJson.addProperty("settings", this.settings.toString());

            JsonObject biomeSourceJson = new JsonObject();

            if (this.fixedBiome != null) {
                biomeSourceJson.addProperty("type", "minecraft:fixed");
                biomeSourceJson.addProperty("biome", this.fixedBiome.location().toString());
            } else {
                biomeSourceJson.addProperty("type", "minecraft:multi_noise");
                biomeSourceJson.addProperty("preset", this.preset.toString());
            }

            generatorJson.add("biome_source", biomeSourceJson);

            json.add("generator", generatorJson);


            return json;
        }


        public void serializeToNetwork(FriendlyByteBuf byteBuf) {

            byteBuf.writeBoolean(this.useForgeSeed);
            byteBuf.writeLong(this.seed);

            if (this.type == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.type);
            }

            if (this.settings == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.settings);
            }

            if (this.preset == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.preset);
            }

            if (this.fixedBiome == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.fixedBiome.location());
            }
        }
    }
}