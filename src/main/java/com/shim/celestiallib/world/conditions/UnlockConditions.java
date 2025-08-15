package com.shim.celestiallib.world.conditions;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.conditions.IUnlockCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class UnlockConditions {

    public static final DeferredRegister<IUnlockCondition> CONDITION_REGISTRY = DeferredRegister.create(new ResourceLocation(CelestialLib.MODID, "unlock_conditions"), CelestialLib.MODID);
    public static final DeferredRegister<IUnlockCondition> CONDITIONS = DeferredRegister.create(CONDITION_REGISTRY.getRegistryName(), CelestialLib.MODID);
    public static final Supplier<IForgeRegistry<IUnlockCondition>> CONDITIONERS_SUPPLIER = CONDITIONS.makeRegistry(IUnlockCondition.class, RegistryBuilder::new);



}