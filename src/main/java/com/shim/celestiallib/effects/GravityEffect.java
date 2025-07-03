package com.shim.celestiallib.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.ArrayList;

public class GravityEffect extends MobEffect {
    public static final ArrayList<GravityEffect> GRAVITY_EFFECTS = new ArrayList<>();

    public GravityEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
        GRAVITY_EFFECTS.add(this);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}