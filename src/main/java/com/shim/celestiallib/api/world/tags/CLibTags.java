package com.shim.celestiallib.api.world.tags;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;

public class CLibTags {

    public static class Blocks {

    }

    public static class Items {

    }

    public static class Fluids {

    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> LOW_GRAVITY_EXEMPT = create("low_gravity_exempt");
        public static final TagKey<EntityType<?>> HIGH_GRAVITY_EXEMPT = create("high_gravity_exempt");

        private static TagKey<EntityType<?>> create(String key) {
            return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, modLoc(key));
        }

    }


    public static class Biomes {
        public static final TagKey<Biome> NO_SNOW_BIOMES = create("no_snow");

        public static final TagKey<Biome> DUST_STORM_BIOMES = create("dust_storms");
        public static final TagKey<Biome> METEOR_SHOWER_BIOMES = create("meteor_showers");

        private static TagKey<Biome> create(String key) {
            return TagKey.create(Registry.BIOME_REGISTRY, modLoc(key));
        }

    }


    public static class Structures {

    }


    private static ResourceLocation modLoc(String location) {
        return new ResourceLocation(CelestialLib.MODID, location);
    }

}