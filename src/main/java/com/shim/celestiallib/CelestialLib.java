package com.shim.celestiallib;

import com.mojang.logging.LogUtils;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.data.CLibDimensionEffectsManager;
import com.shim.celestiallib.data.CLibSpaceTravelManager;
import com.shim.celestiallib.data.CLibPlanetStructureTravelManager;
import com.shim.celestiallib.effects.CelestialLibEffects;
import com.shim.celestiallib.events.CLibCommonEventSetup;
import com.shim.celestiallib.inventory.CLibMenus;
import com.shim.celestiallib.util.ClientProxy;
import com.shim.celestiallib.util.IProxy;
import com.shim.celestiallib.util.ServerProxy;
import com.shim.celestiallib.world.conditions.UnlockConditions;
import com.shim.celestiallib.world.galaxy.Galaxies;
import com.shim.celestiallib.world.planet.Planets;
import com.shim.celestiallib.world.structures.CLibStructures;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import javax.annotation.Nullable;

@Mod(CelestialLib.MODID)
public class CelestialLib {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "celestiallib";

    public CelestialLib() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(CLibCommonEventSetup::commonSetup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);


        CelestialLibEffects.MOB_EFFECTS.register(modEventBus);
        CLibStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
        CLibMenus.MENUS.register(modEventBus);

        Galaxies.GALAXIES.register(modEventBus);
        Planets.PLANETS.register(modEventBus);
        UnlockConditions.CONDITIONS.register(modEventBus);

        modEventBus.addListener(CLibCapabilities::registerCapabilities);

        MinecraftForge.EVENT_BUS.addListener(this::reloadResources);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final IProxy PROXY = DistExecutor.unsafeRunForDist(()-> ClientProxy::new, ()-> ServerProxy::new);

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
//        InterModComms.sendTo(MODID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.messageSupplier().get()).
//                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
    }

    @Nullable
    public static <T> T getCapability(Entity entityIn, Capability<T> capability) {
        if (entityIn == null) return null;
        return entityIn.getCapability(capability).isPresent() ? entityIn.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }

    private void reloadResources(final AddReloadListenerEvent event) {
        event.addListener(new CLibSpaceTravelManager());
        event.addListener(new CLibPlanetStructureTravelManager());
        event.addListener(new CLibDimensionEffectsManager());

    }
}