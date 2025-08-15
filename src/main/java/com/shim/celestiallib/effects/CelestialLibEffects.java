package com.shim.celestiallib.effects;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.effects.GravityEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CelestialLibEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CelestialLib.MODID);

    public static final RegistryObject<MobEffect> LOW_GRAVITY = MOB_EFFECTS.register("low_gravity", () -> new GravityEffect(5926017, -0.045D));
    public static final RegistryObject<MobEffect> HIGH_GRAVITY = MOB_EFFECTS.register("high_gravity", () -> new GravityEffect(5926017, 0.0286D));
    public static final RegistryObject<MobEffect> EXTRA_LOW_GRAVITY = MOB_EFFECTS.register("extra_low_gravity", () -> new GravityEffect(5926017, -0.065D));

}
