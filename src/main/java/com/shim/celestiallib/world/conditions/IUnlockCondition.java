package com.shim.celestiallib.world.conditions;

import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IUnlockCondition extends IForgeRegistryEntry<IUnlockCondition> {

    void trigger();
}
