package com.shim.celestiallib.data.gen;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.effects.CelestialLibEffects;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class CLibLangProvider extends LanguageProvider {

    public CLibLangProvider(DataGenerator gen, String locale) {
        super(gen, CelestialLib.MODID, locale);
    }

    @Override
    protected void addTranslations() {

        //---- EFFECTS -------------------------------------------------------------------------------
        add(CelestialLibEffects.LOW_GRAVITY.get(), "Low Gravity");
        add(CelestialLibEffects.EXTRA_LOW_GRAVITY.get(), "Extra Low Gravity");
        add(CelestialLibEffects.HIGH_GRAVITY.get(), "High Gravity");


        add("celestial.teleport.message_1", "Teleporting to ");
        add("celestial.teleport.message_2", " in… ");


        add("menu.celestiallib.light_speed_travel.title", "Light Speed Travel");
        add("menu.celestiallib.light_speed_travel.cost", "Cost:");
        add("menu.celestiallib.light_speed_travel.travel", "Travel");
        add("menu.celestiallib.light_speed_travel.have", "Have: ");
        add("menu.celestiallib.light_speed_travel.need", "Need: ");



        add("key.categories.celestiallib.light_speed.light_speed_travel", "Open Light Speed Travel Menu");

    }
}