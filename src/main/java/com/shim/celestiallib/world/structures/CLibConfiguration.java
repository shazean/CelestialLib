package com.shim.celestiallib.world.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class CLibConfiguration implements FeatureConfiguration {
    public static final Codec<CLibConfiguration> CODEC = RecordCodecBuilder.create((p_67764_) -> {
        return p_67764_.group(StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(CLibConfiguration::startPool),
                        Codec.intRange(0, 7).fieldOf("size").forGetter(CLibConfiguration::maxDepth),
                        Codec.INT.fieldOf("x").forGetter(CLibConfiguration::x),
                        Codec.INT.fieldOf("z").forGetter(CLibConfiguration::z),
                        Codec.STRING.fieldOf("scale").forGetter(CLibConfiguration::dimension))
                .apply(p_67764_, CLibConfiguration::new);
    });
    private final Holder<StructureTemplatePool> startPool;
    private final int maxDepth;
    private final int x;
    private final int z;
    private final String dimension;

    public CLibConfiguration(Holder<StructureTemplatePool> pool, int maxDepth, int x, int z, String dimension) {
        this.startPool = pool;
        this.maxDepth = maxDepth;
        this.x = x;
        this.z = z;
        this.dimension = dimension;
    }

    public int maxDepth() {
        return this.maxDepth;
    }

    public Holder<StructureTemplatePool> startPool() {
        return this.startPool;
    }

    public int x() {
        return this.x;
    }

    public int z() {
        return this.z;
    }

    public ResourceKey<Level> getDimensionFromString() {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension()));
    }

    public String dimension() {
        return this.dimension;
    }
}
