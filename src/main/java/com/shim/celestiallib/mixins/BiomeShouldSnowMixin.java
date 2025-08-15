package com.shim.celestiallib.mixins;

import com.shim.celestiallib.api.world.tags.CLibTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeShouldSnowMixin {

    @Inject(method = "shouldSnow", at = @At("HEAD"), cancellable = true)
    private void injected(LevelReader levelReader, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        if (levelReader.getBiome(pos).is(CLibTags.Biomes.NO_SNOW_BIOMES)) {
            cir.setReturnValue(false);
        }
    }
}