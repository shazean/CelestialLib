package com.shim.celestiallib;

import com.mojang.logging.LogUtils;
import com.shim.celestiallib.api.world.CLibNoiseSettings;
import com.shim.celestiallib.api.world.biome.CLibBiomePresets;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.config.CLibCommonConfig;
import com.shim.celestiallib.data.*;
import com.shim.celestiallib.api.effects.CLibEffects;
import com.shim.celestiallib.events.CLibCommonEventSetup;
import com.shim.celestiallib.inventory.CLibMenus;
import com.shim.celestiallib.util.ClientProxy;
import com.shim.celestiallib.util.IProxy;
import com.shim.celestiallib.util.ServerProxy;
import com.shim.celestiallib.world.celestials.galaxy.Galaxies;
import com.shim.celestiallib.world.celestials.planet.Planets;
import com.shim.celestiallib.world.structures.CLibStructures;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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

        CLibEffects.MOB_EFFECTS.register(modEventBus);
        CLibStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);
        CLibMenus.MENUS.register(modEventBus);

        Galaxies.GALAXIES.register(modEventBus);
        Planets.PLANETS.register(modEventBus);

//        CLibB.BIOMES.register(modEventBus);
        CLibBiomePresets.BIOME_SOURCE.register(modEventBus);
        CLibNoiseSettings.NOISES.register(modEventBus);


        modEventBus.addListener(CLibCapabilities::registerCapabilities);

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CLibCapabilities::attachEntityCapabilities);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CLibCommonConfig.SPEC, "celestiallib-common.toml");

        MinecraftForge.EVENT_BUS.addListener(this::reloadResources);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final IProxy PROXY = DistExecutor.unsafeRunForDist(()-> ClientProxy::new, ()-> ServerProxy::new);

    @Nullable
    public static <T> T getCapability(Entity entityIn, Capability<T> capability) {
        if (entityIn == null) return null;
        return entityIn.getCapability(capability).isPresent() ? entityIn.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }

    private void reloadResources(final AddReloadListenerEvent event) {
        event.addListener(new CLibSpaceTravelManager());
        event.addListener(new CLibPlanetStructureTravelManager());
        event.addListener(new CLibDimensionEffectsManager());

        event.addListener(new CLibPlanetDataManager());
        event.addListener(new CLibGalaxyDataManager());

    }
}