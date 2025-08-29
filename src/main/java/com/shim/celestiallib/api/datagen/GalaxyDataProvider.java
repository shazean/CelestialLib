package com.shim.celestiallib.api.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.shim.celestiallib.data.gen.GalaxyData;
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

public class GalaxyDataProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;
    protected ExistingFileHelper fileHelper;
    protected static String modid;

    public GalaxyDataProvider(DataGenerator generatorIn, String modid, ExistingFileHelper fileHelperIn) {
        this.generator = generatorIn;
        this.modid = modid;
        this.fileHelper = fileHelperIn;
    }

    public void add() {

    }

    public void run(HashCache cache) {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<GalaxyData> consumer = (galaxyData) -> {
            if (!set.add(galaxyData.getId())) {
                throw new IllegalStateException("Duplicate galaxy data " + galaxyData.getId());
            } else {
                Path path1 = createPath(path, galaxyData);

                try {
                    DataProvider.save(GSON, cache, galaxyData.deconstruct().serializeToJson(), path1);
                } catch (IOException ioexception) {
                    LOGGER.error("Couldn't save galaxy data {}", path1, ioexception);
                }

            }
        };

        register(consumer, fileHelper);
    }

    protected void register(Consumer<GalaxyData> consumer, ExistingFileHelper fileHelper) {
    }

    private static Path createPath(Path path, GalaxyData galaxyData) {
        return path.resolve("data/" + modid + "/celestial/galaxy/" + galaxyData.getId().getPath() + ".json");
    }

    public String getName() {
        return "Galaxy Data";
    }

}
