package com.shim.celestiallib.data.gen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.shim.celestiallib.world.planet.Planet;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlanetIcon {
    private final ResourceLocation id;
    private final ResourceLocation planet;
    private final ResourceLocation texture;
    private final int size;

    public PlanetIcon(ResourceLocation id, ResourceLocation planet, ResourceLocation texture, int size) {
        this.id = id;
        this.planet = planet;
        this.texture = texture;
        this.size = size;
    }

    public PlanetIcon.Builder deconstruct() {
        return new PlanetIcon.Builder(this.planet, this.texture, this.size);
    }

    public static Builder builder() {
        return new Builder();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        ResourceLocation planet;
        ResourceLocation texture;
        int size;

        public Builder(ResourceLocation planet, ResourceLocation texture, int size) {
            this.planet = planet;
            this.texture = texture;
            this.size = size;
        }

        private Builder() {
        }

        public PlanetIcon.Builder planet(Planet planet) {
            this.planet = planet.getDimension().location();
            return this;
        }

        public PlanetIcon.Builder texture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }

        public PlanetIcon.Builder block(int size) {
            this.size = size;
            return this;
        }

        public boolean canBuild(Function<ResourceLocation, PlanetIcon> p_138393_) {
            return planet != null && texture != null;
        }

        public PlanetIcon build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialLib.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete planet icon!");
            } else {
                return new PlanetIcon(resourceLocation, this.planet, this.texture, this.size);
            }
        }

        public PlanetIcon save(Consumer<PlanetIcon> consumer, String name) {
            PlanetIcon dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("planet", this.planet.toString());
            json.addProperty("texture", this.texture.toString());

            json.addProperty("size", this.size);

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {
            if (this.planet == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.planet);
            }

            if (this.texture == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.texture);
            }


            if (this.size == -1) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeInt(this.size);
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
            Consumer<PlanetIcon> consumer = (planet) -> {
                if (!set.add(planet.getId())) {
                    throw new IllegalStateException("Duplicate planet icon " + planet.getId());
                } else {
                    Path path1 = createPath(path, planet);

                    try {
                        DataProvider.save(GSON, cache, planet.deconstruct().serializeToJson(), path1);
                    } catch (IOException ioexception) {
                        LOGGER.error("Couldn't save planet icon {}", path1, ioexception);
                    }

                }
            };

            register(consumer, fileHelper);
        }

        protected void register(Consumer<PlanetIcon> consumer, ExistingFileHelper fileHelper) {
        }

        private static Path createPath(Path path, PlanetIcon planetIcon) {
            return path.resolve("assets/" + modid + "/models/celestial/planet/" + planetIcon.getId().getPath() + ".json");
        }

        public String getName() {
            return "Planet Icons";
        }
    }
}