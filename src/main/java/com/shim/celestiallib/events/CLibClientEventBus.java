package com.shim.celestiallib.events;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.data.client.CLibGalaxyIconManager;
import com.shim.celestiallib.data.client.CLibPlanetIconManager;
import com.shim.celestiallib.data.client.ClibGalaxyImageManager;
import com.shim.celestiallib.inventory.CLibMenus;
import com.shim.celestiallib.inventory.screens.LightSpeedTravelScreen;
import com.shim.celestiallib.inventory.screens.SingleGalaxyLightSpeedScreen;
import com.shim.celestiallib.util.CLibKeybinds;
import com.shim.celestiallib.world.renderer.DimensionRenderers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = CelestialLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CLibClientEventBus {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {

        CLibKeybinds.register(event);

        event.enqueueWork(DimensionRenderers::setDimensionEffects);

        MenuScreens.register(CLibMenus.LIGHT_SPEED_TRAVEL_MENU.get(), LightSpeedTravelScreen::new);
        MenuScreens.register(CLibMenus.SINGLE_GALAXY_LIGHT_SPEED_TRAVEL_MENU.get(), SingleGalaxyLightSpeedScreen::new);

    }

    @SubscribeEvent
    public static void reloadClientResource(final RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new CLibGalaxyIconManager());
        event.registerReloadListener(new CLibPlanetIconManager());
        event.registerReloadListener(new ClibGalaxyImageManager());
    }
}