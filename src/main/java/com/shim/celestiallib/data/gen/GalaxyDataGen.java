//package com.shim.celestiallib.data.gen;
//
//
//import com.google.common.collect.Sets;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import com.mojang.logging.LogUtils;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.DataProvider;
//import net.minecraft.data.HashCache;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.block.Block;
//import net.minecraftforge.common.data.ExistingFileHelper;
//import org.slf4j.Logger;
//
//import javax.annotation.Nullable;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.Set;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//public class GalaxyDataGen {
//    private final ResourceLocation id;
//    private final ResourceLocation galaxy;
//    private final int scale_ratio;
//    private final Item lightSpeedCost;
//    private final int count;
//
//
//    public GalaxyDataGen(ResourceLocation id, boolean useForgeSeed, long seed, ResourceLocation type,
//                        ResourceLocation settings, ResourceLocation preset, ResourceKey<Biome> fixedBiome) {
//        this.id = id;
//        this.useForgeSeed = useForgeSeed;
//        this.seed = seed;
//        this.type = type;
//        this.settings = settings;
//        this.preset = preset;
//        this.fixedBiome = fixedBiome;
//    }
//
//    public GalaxyDataGen.Builder deconstruct() {
//        return new GalaxyDataGen.Builder(this.useForgeSeed, this.seed, this.type, this.settings, this.preset, this.fixedBiome);
//    }
//
//    public static Builder builder() {
//        return new Builder();
//    }
//
//    public ResourceLocation getId() {
//        return this.id;
//    }
//
//    public static class Builder {
//        boolean useForgeSeed;
//        long seed = 1;
//        ResourceLocation type;
//        ResourceLocation settings;
//        ResourceLocation preset;
//        ResourceKey<Biome> fixedBiome;
//
//        private Builder() {}
//
//        public Builder(boolean useForgeSeed, long seed, ResourceLocation type, ResourceLocation settings, ResourceLocation preset, ResourceKey<Biome> fixedbiome) {
//            this.useForgeSeed = useForgeSeed;
//            this.seed = seed;
//            this.type = type;
//            this.settings = settings;
//            this.preset = preset;
//            this.fixedBiome = fixedbiome;
//        }
//
//        public GalaxyDataGen.Builder useForgeSeed(boolean useForgeSeed) {
//            this.useForgeSeed = useForgeSeed;
//            return this;
//        }
//
//        public GalaxyDataGen.Builder seed(long seed) {
//            this.seed = seed;
//            return this;
//        }
//
//        public GalaxyDataGen.Builder type(ResourceLocation type) {
//            this.type = type;
//            return this;
//        }
//
//        public GalaxyDataGen.Builder type(ResourceKey<Level> dimension) {
//            this.type = dimension.location();
//            return this;
//        }
//
//        public GalaxyDataGen.Builder settings(ResourceLocation settings) {
//            this.settings = settings;
//            return this;
//        }
//
//        public GalaxyDataGen.Builder settings(ResourceKey<Level> dimension) {
//            this.settings = dimension.location();
//            return this;
//        }
//
//        public GalaxyDataGen.Builder presetOrFixedBiome(@Nullable ResourceLocation preset, @Nullable ResourceKey<Biome> biome) {
//            if (preset != null) {
//                this.preset = preset;
//            } else if (biome != null) {
//                this.fixedBiome = biome;
//            } else {
//                throw new IllegalStateException("preset and biome can not both be null!");
//            }
//            return this;
//        }
//
//        public GalaxyDataGen.Builder preset(ResourceKey<Level> preset) {
//            return presetOrFixedBiome(preset.location(), null);
//        }
//
//
//        public boolean canBuild(Function<ResourceLocation, GalaxyDataGen> dimension) {
//            return type != null && (preset != null || this.fixedBiome != null);
//        }
//
//        public GalaxyDataGen build(ResourceLocation resourceLocation) {
//            if (!this.canBuild((loc) -> {
////                CelestialLib.LOGGER.debug("loc: " + loc);
//                return null;
//            })) {
//                throw new IllegalStateException("Tried to build incomplete dimension!");
//            } else {
//                return new GalaxyDataGen(resourceLocation, this.useForgeSeed, this.seed,
//                        this.type, this.settings,
//                        this.preset, this.fixedBiome);
//            }
//        }
//
//        public GalaxyDataGen save(Consumer<GalaxyDataGen> consumer, ResourceKey<Level> dimension) {
//            return save(consumer, dimension.location().getPath());
//        }
//
//        public GalaxyDataGen save(Consumer<GalaxyDataGen> consumer, String name) {
//            GalaxyDataGen dimension = this.build(new ResourceLocation(name));
//            consumer.accept(dimension);
//            return dimension;
//        }
//
//        public JsonObject serializeToJson() {
//            JsonObject json = new JsonObject();
//
//            json.addProperty("type", this.type.toString());
//            json.addProperty("forge:use_server_seed", this.useForgeSeed);
//
//            JsonObject generatorJson = new JsonObject();
//
//            generatorJson.addProperty("type", "minecraft:noise");
//            generatorJson.addProperty("seed", this.seed);
//            generatorJson.addProperty("settings", this.settings.toString());
//
//            JsonObject biomeSourceJson = new JsonObject();
//
//            if (this.fixedBiome != null) {
//                biomeSourceJson.addProperty("type", "minecraft:fixed");
//                biomeSourceJson.addProperty("biome", this.fixedBiome.location().toString());
//            } else {
//                biomeSourceJson.addProperty("type", "minecraft:multi_noise");
//                biomeSourceJson.addProperty("preset", this.preset.toString());
//            }
//
//            generatorJson.add("biome_source", biomeSourceJson);
//
//            json.add("generator", generatorJson);
//
//
//            return json;
//        }
//
//
//        public void serializeToNetwork(FriendlyByteBuf byteBuf) {
//
//            byteBuf.writeBoolean(this.useForgeSeed);
//            byteBuf.writeLong(this.seed);
//
//            if (this.type == null) {
//                byteBuf.writeBoolean(false);
//            } else {
//                byteBuf.writeBoolean(true);
//                byteBuf.writeResourceLocation(this.type);
//            }
//
//            if (this.settings == null) {
//                byteBuf.writeBoolean(false);
//            } else {
//                byteBuf.writeBoolean(true);
//                byteBuf.writeResourceLocation(this.settings);
//            }
//
//            if (this.preset == null) {
//                byteBuf.writeBoolean(false);
//            } else {
//                byteBuf.writeBoolean(true);
//                byteBuf.writeResourceLocation(this.preset);
//            }
//
//            if (this.fixedBiome == null) {
//                byteBuf.writeBoolean(false);
//            } else {
//                byteBuf.writeBoolean(true);
//                byteBuf.writeResourceLocation(this.fixedBiome.location());
//            }
//        }
//    }
//
//    public static class Provider implements DataProvider {
//        private static final Logger LOGGER = LogUtils.getLogger();
//        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
//        private final DataGenerator generator;
//        protected ExistingFileHelper fileHelper;
//        protected static String modid;
//
//        public Provider(DataGenerator generatorIn, String modid, ExistingFileHelper fileHelperIn) {
//            this.generator = generatorIn;
//            this.modid = modid;
//            this.fileHelper = fileHelperIn;
//        }
//
//        public void add() {
//
//        }
//
//        public void run(HashCache cache) {
//            Path path = this.generator.getOutputFolder();
//            Set<ResourceLocation> set = Sets.newHashSet();
//            Consumer<GalaxyDataGen> consumer = (dimensionType) -> {
//                if (!set.add(dimensionType.getId())) {
//                    throw new IllegalStateException("Duplicate dimension " + dimensionType.getId());
//                } else {
//                    Path path1 = createPath(path, dimensionType);
//
//                    try {
//                        DataProvider.save(GSON, cache, dimensionType.deconstruct().serializeToJson(), path1);
//                    } catch (IOException ioexception) {
//                        LOGGER.error("Couldn't save dimension {}", path1, ioexception);
//                    }
//
//                }
//            };
//
//            register(consumer, fileHelper);
//        }
//
//        protected void register(Consumer<GalaxyDataGen> consumer, ExistingFileHelper fileHelper) {
//        }
//
//        private static Path createPath(Path path, GalaxyDataGen dimensionGen) {
//            return path.resolve("data/" + modid + "/dimension/" + dimensionGen.getId().getPath() + ".json");
//        }
//
//        public String getName() {
//            return "Dimensions";
//        }
//    }
//}