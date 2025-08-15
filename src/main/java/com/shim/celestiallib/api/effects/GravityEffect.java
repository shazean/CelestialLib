package com.shim.celestiallib.api.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;

public class GravityEffect extends MobEffect {
    public static final ArrayList<GravityEffect> GRAVITY_EFFECTS = new ArrayList<>();

    public GravityEffect(int color, double gravityModifier) {
        super(MobEffectCategory.NEUTRAL, color);
        this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "503FE595-3498-8478-234A-3EC09BF892EF", gravityModifier, AttributeModifier.Operation.ADDITION);

        GRAVITY_EFFECTS.add(this);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}