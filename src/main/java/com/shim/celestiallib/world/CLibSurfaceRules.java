package com.shim.celestiallib.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class CLibSurfaceRules {
    private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }


    public static SurfaceRules.RuleSource space() {
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("air", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), AIR));

        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }
}
