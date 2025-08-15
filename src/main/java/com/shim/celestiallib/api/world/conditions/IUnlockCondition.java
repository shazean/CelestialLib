package com.shim.celestiallib.api.world.conditions;

import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IUnlockCondition extends IForgeRegistryEntry<IUnlockCondition> {

    void trigger();
}
