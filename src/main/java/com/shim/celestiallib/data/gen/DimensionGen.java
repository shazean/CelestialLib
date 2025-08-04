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
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

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

    public DimensionGen(ResourceLocation id, boolean useForgeSeed, long seed, ResourceLocation type,
                        ResourceLocation settings, ResourceLocation preset) {
        this.id = id;
        this.useForgeSeed = useForgeSeed;
        this.seed = seed;
        this.type = type;
        this.settings = settings;
        this.preset = preset;
    }

    public DimensionGen.Builder deconstruct() {
        return new DimensionGen.Builder(this.useForgeSeed, this.seed, this.type, this.settings, this.preset);
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

        private Builder() {}

        public Builder(boolean useForgeSeed, long seed, ResourceLocation type, ResourceLocation settings, ResourceLocation preset) {
            this.useForgeSeed = useForgeSeed;
            this.seed = seed;
            this.type = type;
            this.settings = settings;
            this.preset = preset;
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

        public DimensionGen.Builder preset(ResourceLocation preset) {
            this.preset = preset;
            return this;
        }

        public DimensionGen.Builder preset(ResourceKey<Level> dimension) {
            this.preset = dimension.location();
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, DimensionGen> p_138393_) {
            return type != null && preset != null;
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
                        this.preset);
            }
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

            biomeSourceJson.addProperty("type", "minecraft:multi_noise");
            biomeSourceJson.addProperty("preset", this.preset.toString());

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
        }
    }

    public static class Provider implements DataProvider {
        private static final Logger LOGGER = LogUtils.getLogger();
        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
        private final DataGenerator generator;
        protected ExistingFileHelper fileHelper;
        protected static String modid;

        public Provider(DataGenerator generatorIn, String modid, ExistingFileHelper fileHelperIn) {
            this.generator = generatorIn;
            this.modid = modid;
            this.fileHelper = fileHelperIn;
        }

        public void run(HashCache cache) {
            Path path = this.generator.getOutputFolder();
            Set<ResourceLocation> set = Sets.newHashSet();
            Consumer<DimensionGen> consumer = (dimensionType) -> {
                if (!set.add(dimensionType.getId())) {
                    throw new IllegalStateException("Duplicate dimension " + dimensionType.getId());
                } else {
                    Path path1 = createPath(path, dimensionType);

                    try {
                        DataProvider.save(GSON, cache, dimensionType.deconstruct().serializeToJson(), path1);
                    } catch (IOException ioexception) {
                        LOGGER.error("Couldn't save dimension {}", path1, ioexception);
                    }

                }
            };

            register(consumer, fileHelper);
        }

        protected void register(Consumer<DimensionGen> consumer, ExistingFileHelper fileHelper) {
        }

        private static Path createPath(Path path, DimensionGen dimensionGen) {
            return path.resolve("data/" + modid + "/dimension" + dimensionGen.getId().getPath() + ".json");
        }

        public String getName() {
            return "Dimensions";
        }
    }
}