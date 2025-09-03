package com.shim.celestiallib.api.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.shim.celestiallib.data.gen.SpaceTravel;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class SpaceTravelProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;
    protected net.minecraftforge.common.data.ExistingFileHelper fileHelper;
    protected static String modid;

    public SpaceTravelProvider(DataGenerator generatorIn, String modid, net.minecraftforge.common.data.ExistingFileHelper fileHelperIn) {
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

    private static Path createPath(Path path, SpaceTravel spaceTravel) {
        return path.resolve("data/" + spaceTravel.getId().getNamespace() + "/celestial/space_travel/" + spaceTravel.getId().getPath() + ".json");
    }

    public String getName() {
        return "Space Travel Data";
    }
}
