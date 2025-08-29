package com.shim.celestiallib.api.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.shim.celestiallib.data.gen.PlanetData;
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

public class PlanetDataProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;
    protected ExistingFileHelper fileHelper;
    protected static String modid;

    public PlanetDataProvider(DataGenerator generatorIn, String modid, ExistingFileHelper fileHelperIn) {
        this.generator = generatorIn;
        this.modid = modid;
        this.fileHelper = fileHelperIn;
    }

    public void add() {

    }

    public void run(HashCache cache) {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<PlanetData> consumer = (planetData) -> {
            if (!set.add(planetData.getId())) {
                throw new IllegalStateException("Duplicate planet data " + planetData.getId());
            } else {
                Path path1 = createPath(path, planetData);

                try {
                    DataProvider.save(GSON, cache, planetData.deconstruct().serializeToJson(), path1);
                } catch (IOException ioexception) {
                    LOGGER.error("Couldn't save planet data {}", path1, ioexception);
                }

            }
        };

        register(consumer, fileHelper);
    }

    protected void register(Consumer<PlanetData> consumer, ExistingFileHelper fileHelper) {
    }

    private static Path createPath(Path path, PlanetData planetData) {
        return path.resolve("data/" + modid + "/celestial/planet/" + planetData.getId().getPath() + ".json");
    }

    public String getName() {
        return "Planet Data";
    }

}
