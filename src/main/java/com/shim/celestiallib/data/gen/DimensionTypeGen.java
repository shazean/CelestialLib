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

public class DimensionTypeGen {
    private final ResourceLocation id;
    private final boolean ultrawarm;
    private final boolean natural;
    private final boolean piglinSafe;
    private final boolean bedWorks;
    private final boolean respawnAnchorWorks;
    private final boolean hasSkylight;
    private final boolean hasCeiling;
    private final boolean hasRaids;

    private final int logicalHeight;
    private final float ambientLight;
    private final int minY;
    private final int height;
    private final float coordinateScale;
    private final int fixedTime;

    private final TagKey<Block> infiniburn;
    private final ResourceLocation effects;

    public DimensionTypeGen(ResourceLocation id, boolean ultrawarm, boolean natural, boolean piglinSafe, boolean bedWorks,
                            boolean respawnAnchorWorks, boolean hasSkylight, boolean hasCeiling,
                            boolean hasRaids, int logicalHeight, float ambientLight, int minY,
                            int height, float coordinateScale, int fixedTime, TagKey<Block> infiniburn, ResourceLocation effects) {
        this.id = id;
        this.ultrawarm = ultrawarm;
        this.natural = natural;
        this.piglinSafe = piglinSafe;
        this.bedWorks = bedWorks;
        this.respawnAnchorWorks = respawnAnchorWorks;
        this.hasSkylight = hasSkylight;
        this.hasCeiling = hasCeiling;
        this.hasRaids = hasRaids;
        this.logicalHeight = logicalHeight;
        this.ambientLight = ambientLight;
        this.minY = minY;
        this.height = height;
        this.coordinateScale = coordinateScale;
        this.fixedTime = fixedTime;
        this.infiniburn = infiniburn;
        this.effects = effects;
    }

    public static Builder builder() {
        return new Builder();
    }

    public DimensionTypeGen.Builder deconstruct() {
        return new DimensionTypeGen.Builder(this.ultrawarm, this.natural, this.piglinSafe, this.bedWorks,
                this.respawnAnchorWorks, this.hasSkylight, this.hasCeiling,
                this.hasRaids, this.logicalHeight, this.ambientLight, this.minY,
                this.height, this.coordinateScale, this.fixedTime, this.infiniburn, this.effects);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        boolean ultrawarm;
        boolean natural;
        boolean piglinSafe;
        boolean bedWorks;
        boolean respawnAnchorWorks;
        boolean hasSkylight;
        boolean hasCeiling;
        boolean hasRaids;

        int logicalHeight;
        float ambientLight;
        int minY;
        int height;
        float coordinateScale;
        int fixedTime;

        TagKey<Block> infiniburn;
        ResourceLocation effects;

        private Builder() {
        }

        public Builder(boolean ultrawarm, boolean natural, boolean piglinSafe, boolean bedWorks,
                       boolean respawnAnchorWorks, boolean hasSkylight, boolean hasCeiling,
                       boolean hasRaids, int logicalHeight, float ambientLight, int minY,
                       int height, float coordinateScale, int fixedTime, TagKey<Block> infiniburn, ResourceLocation effects) {
            this.ultrawarm = ultrawarm;
            this.natural = natural;
            this.piglinSafe = piglinSafe;
            this.bedWorks = bedWorks;
            this.respawnAnchorWorks = respawnAnchorWorks;
            this.hasSkylight = hasSkylight;
            this.hasCeiling = hasCeiling;
            this.hasRaids = hasRaids;
            this.logicalHeight = logicalHeight;
            this.ambientLight = ambientLight;
            this.minY = minY;
            this.height = height;
            this.coordinateScale = coordinateScale;
            this.fixedTime = fixedTime;
            this.infiniburn = infiniburn;
            this.effects = effects;
        }

        public DimensionTypeGen.Builder height(int logicalHeight, int height, int minY) {
            this.logicalHeight = logicalHeight;
            this.minY = minY;
            this.height = height;
            return this;
        }

        public DimensionTypeGen.Builder coordinateScale(float coordinateScale) {
            this.coordinateScale = coordinateScale;
            return this;
        }

        public DimensionTypeGen.Builder infiniburn(TagKey<Block> infiniburn) {
            this.infiniburn = infiniburn;
            return this;
        }

        public DimensionTypeGen.Builder effects(ResourceLocation effects) {
            this.effects = effects;
            return this;
        }

        public DimensionTypeGen.Builder fixedTime(int fixedTime) {
            this.fixedTime = fixedTime;
            return this;
        }

        public DimensionTypeGen.Builder ultrawarm(boolean isUltrwarm) {
            this.ultrawarm = isUltrwarm;
            return this;
        }

        public DimensionTypeGen.Builder natural(boolean natural) {
            this.natural = natural;
            return this;
        }

        public DimensionTypeGen.Builder respawn(boolean bedWorks, boolean respawnAnchorWorks) {
            this.bedWorks = bedWorks;
            this.respawnAnchorWorks = respawnAnchorWorks;
            return this;
        }

        public DimensionTypeGen.Builder sky(boolean hasSkylight, boolean hasCeiling, float ambientLight) {
            this.hasSkylight = hasSkylight;
            this.hasCeiling = hasCeiling;
            this.ambientLight = ambientLight;
            return this;
        }

        public DimensionTypeGen.Builder mobs(boolean hasRaids, boolean piglinSafe) {
            this.hasRaids = hasRaids;
            this.piglinSafe = piglinSafe;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, DimensionTypeGen> p_138393_) {
            return true;// galaxy != null && texture != null;
        }

        public DimensionTypeGen build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialLib.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete dimension type!");
            } else {
                return new DimensionTypeGen(resourceLocation, this.ultrawarm, this.natural, this.piglinSafe, this.bedWorks,
                        this.respawnAnchorWorks, this.hasSkylight, this.hasCeiling,
                        this.hasRaids, this.logicalHeight, this.ambientLight, this.minY,
                        this.height, this.coordinateScale, this.fixedTime, this.infiniburn, this.effects);
            }
        }

        public DimensionTypeGen save(Consumer<DimensionTypeGen> consumer, ResourceKey<Level> dimension) {
            return save(consumer, dimension.location().getPath());
        }

        public DimensionTypeGen save(Consumer<DimensionTypeGen> consumer, String name) {
            DimensionTypeGen dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("ultrawarm", this.ultrawarm);
            json.addProperty("natural", this.natural);
            json.addProperty("piglin_safe", this.piglinSafe);
            json.addProperty("bed_works", this.bedWorks);
            json.addProperty("respawn_anchor_works", this.respawnAnchorWorks);
            json.addProperty("has_skylight", this.hasSkylight);
            json.addProperty("has_ceiling", this.hasCeiling);
            json.addProperty("has_raids", this.hasRaids);

            json.addProperty("logical_height", this.logicalHeight);
            json.addProperty("ambient_light", this.ambientLight);
            json.addProperty("min_y", this.minY);
            json.addProperty("height", this.height);
            json.addProperty("coordinate_scale", this.coordinateScale);
            json.addProperty("fixed_time", this.fixedTime);

            json.addProperty("infiniburn", "#" + this.infiniburn.location());

            if (this.effects != null)
                json.addProperty("effects", this.effects.toString());

            return json;
        }


        public void serializeToNetwork(FriendlyByteBuf byteBuf) {

            byteBuf.writeBoolean(this.ultrawarm);
            byteBuf.writeBoolean(this.natural);
            byteBuf.writeBoolean(this.piglinSafe);
            byteBuf.writeBoolean(this.bedWorks);
            byteBuf.writeBoolean(this.respawnAnchorWorks);
            byteBuf.writeBoolean(this.hasSkylight);
            byteBuf.writeBoolean(this.hasCeiling);
            byteBuf.writeBoolean(this.hasRaids);

            byteBuf.writeInt(this.logicalHeight);
            byteBuf.writeFloat(this.ambientLight);
            byteBuf.writeInt(this.minY );
            byteBuf.writeInt(this.height);
            byteBuf.writeFloat(this.coordinateScale);
            byteBuf.writeInt(this.fixedTime);

            if (this.infiniburn == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.infiniburn.location());
            }

            if (this.effects == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.effects);
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
            Consumer<DimensionTypeGen> consumer = (dimensionType) -> {
                if (!set.add(dimensionType.getId())) {
                    throw new IllegalStateException("Duplicate dimension type " + dimensionType.getId());
                } else {
                    Path path1 = createPath(path, dimensionType);

                    try {
                        DataProvider.save(GSON, cache, dimensionType.deconstruct().serializeToJson(), path1);
                    } catch (IOException ioexception) {
                        LOGGER.error("Couldn't save dimension type {}", path1, ioexception);
                    }

                }
            };

            register(consumer, fileHelper);
        }

        protected void register(Consumer<DimensionTypeGen> consumer, ExistingFileHelper fileHelper) {
        }

        private static Path createPath(Path path, DimensionTypeGen dimensionTypeGen) {
            return path.resolve("data/" + modid + "/dimension_type/" + dimensionTypeGen.getId().getPath() + ".json");
        }

        public String getName() {
            return "Dimension Types";
        }
    }
}