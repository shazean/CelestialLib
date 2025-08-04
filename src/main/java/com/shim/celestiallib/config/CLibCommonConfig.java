package com.shim.celestiallib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CLibCommonConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<String> DEFAULT_OVERWORLD_GALAXY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GRAVITY_EFFECTS;

    static {
        BUILDER.push("Configs for CelestialLib");
        DEFAULT_OVERWORLD_GALAXY = BUILDER.comment("Default galaxy associated with the Overworld")
                .define("Overworld galaxy", "");
        GRAVITY_EFFECTS = BUILDER.comment("Should gravity effects be enabled?")
                .define("Gravity effects", true);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}