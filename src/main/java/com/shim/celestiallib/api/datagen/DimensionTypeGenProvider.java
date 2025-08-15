package com.shim.celestiallib.api.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.shim.celestiallib.data.gen.DimensionTypeGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class DimensionTypeGenProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;
    protected ExistingFileHelper fileHelper;
    protected static String modid;

    public DimensionTypeGenProvider(DataGenerator generatorIn, String modid, ExistingFileHelper fileHelperIn) {
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
