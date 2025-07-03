package com.shim.celestiallib.armor;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;

public interface ISpacesuit {
    boolean shouldNegateGravity(MobEffect effect, ItemStack item);

}