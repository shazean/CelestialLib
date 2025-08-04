package com.shim.celestiallib.data.gen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.shim.celestiallib.world.galaxy.Galaxy;
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

public class GalaxyIcon {
    private final ResourceLocation id;
    private final ResourceLocation galaxy;
    private final ResourceLocation texture;

    public GalaxyIcon(ResourceLocation id, ResourceLocation galaxy, ResourceLocation texture) {
        this.id = id;
        this.galaxy = galaxy;
        this.texture = texture;
    }

    public GalaxyIcon.Builder deconstruct() {
        return new GalaxyIcon.Builder(this.galaxy, this.texture);
    }

    public static Builder builder() {
        return new Builder();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public static class Builder {
        ResourceLocation galaxy;
        ResourceLocation texture;

        public Builder(ResourceLocation galaxy, ResourceLocation texture) {
            this.galaxy = galaxy;
            this.texture = texture;
        }

        private Builder() {
        }

        public GalaxyIcon.Builder galaxy(Galaxy galaxy) {
            this.galaxy = galaxy.getDimension().location();
            return this;
        }

        public GalaxyIcon.Builder texture(ResourceLocation texture) {
            this.texture = texture;
            return this;
        }


        public boolean canBuild(Function<ResourceLocation, GalaxyIcon> p_138393_) {
            return galaxy != null && texture != null;
        }

        public GalaxyIcon build(ResourceLocation resourceLocation) {
            if (!this.canBuild((loc) -> {
//                CelestialLib.LOGGER.debug("loc: " + loc);
                return null;
            })) {
                throw new IllegalStateException("Tried to build incomplete galaxy background!");
            } else {
                return new GalaxyIcon(resourceLocation, this.galaxy, this.texture);
            }
        }

        public GalaxyIcon save(Consumer<GalaxyIcon> consumer, String name) {
            GalaxyIcon dimension = this.build(new ResourceLocation(name));
            consumer.accept(dimension);
            return dimension;
        }

        public JsonObject serializeToJson() {
            JsonObject json = new JsonObject();

            json.addProperty("galaxy", this.galaxy.toString());
            json.addProperty("texture", this.texture.toString());

            return json;
        }

        public void serializeToNetwork(FriendlyByteBuf byteBuf) {
            if (this.galaxy == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.galaxy);
            }

            if (this.texture == null) {
                byteBuf.writeBoolean(false);
            } else {
                byteBuf.writeBoolean(true);
                byteBuf.writeResourceLocation(this.texture);
            }


        }
    }

    public class Provider implements DataProvider {
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
            Consumer<GalaxyIcon> consumer = (galaxy) -> {
                if (!set.add(galaxy.getId())) {
                    throw new IllegalStateException("Duplicate galaxy icon " + galaxy.getId());
                } else {
                    Path path1 = createPath(path, galaxy);

                    try {
                        DataProvider.save(GSON, cache, galaxy.deconstruct().serializeToJson(), path1);
                    } catch (IOException ioexception) {
                        LOGGER.error("Couldn't save galaxy icon {}", path1, ioexception);
                    }

                }
            };

            register(consumer, fileHelper);
        }

        protected void register(Consumer<GalaxyIcon> consumer, ExistingFileHelper fileHelper) {
        }

        private static Path createPath(Path path, GalaxyIcon galaxyIcon) {
            return path.resolve("assets/" + modid + "/models/celestial/galaxy/" + galaxyIcon.getId().getPath() + ".json");
        }

        public String getName() {
            return "Galaxy Icons";
        }
    }
}