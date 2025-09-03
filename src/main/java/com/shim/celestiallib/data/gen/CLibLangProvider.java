package com.shim.celestiallib.data.gen;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.effects.CLibEffects;
import com.shim.celestiallib.world.biome.PresetBiomeKeys;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.LanguageProvider;

public class CLibLangProvider extends LanguageProvider {

    public CLibLangProvider(DataGenerator gen, String locale) {
        super(gen, CelestialLib.MODID, locale);
    }

    @Override
    protected void addTranslations() {

        //---- EFFECTS -------------------------------------------------------------------------------
        add(CLibEffects.LOW_GRAVITY.get(), "Low Gravity");
        add(CLibEffects.EXTRA_LOW_GRAVITY.get(), "Extra Low Gravity");
        add(CLibEffects.HIGH_GRAVITY.get(), "High Gravity");

        add("celestial.teleport.message_1", "Teleporting to ");
        add("celestial.teleport.message_2", " in… ");

        add("menu.celestiallib.light_speed_travel.title", "Light Speed Travel");
        add("menu.celestiallib.light_speed_travel.cost", "Cost:");
        add("menu.celestiallib.light_speed_travel.cooldown", "Cooldown: ");
        add("menu.celestiallib.light_speed_travel.travel", "Travel");
        add("menu.celestiallib.light_speed_travel.have", "Have: ");
        add("menu.celestiallib.light_speed_travel.need", "Need: ");
        add("menu.celestiallib.light_speed_travel.invalid", "Invalid location for light speed travel");
        add("menu.celestiallib.locked", "Locked");

        add("key.categories.celestiallib.light_speed.light_speed_travel", "Open Light Speed Travel Menu");

        addBiome(PresetBiomeKeys.HIGH_DESERT, "High Desert");
        addBiome(PresetBiomeKeys.LOW_DESERT, "Low Desert");
        addBiome(PresetBiomeKeys.ICY_DESERT, "Icy Desert");
        addBiome(PresetBiomeKeys.MONSOON_DESERT, "Monsoon Desert");
        addBiome(PresetBiomeKeys.DUNES, "Dunes");
        addBiome(PresetBiomeKeys.SHALLOW_OCEAN, "Shallow Ocean");
        addBiome(PresetBiomeKeys.ISLANDS, "Islands");

    }

    public void addBiome(ResourceKey<Biome> biome, String localization) {
        add("biome." + CelestialLib.MODID + "." + biome.location().getPath(), localization);
    }
}