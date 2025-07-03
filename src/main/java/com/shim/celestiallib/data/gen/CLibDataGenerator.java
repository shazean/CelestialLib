package com.shim.celestiallib.data.gen;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = CelestialLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CLibDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        if (event.includeServer()) {

//            generator.addProvider(new DimensionTeleports(generator, CelestialLib.MODID, helper));
//            generator.addProvider(new PlanetTeleports(generator, CelestialLib.MODID, helper));

        }
        if (event.includeClient()) {

            generator.addProvider(new CLibLangProvider(generator, "en_us"));

        }
    }

}