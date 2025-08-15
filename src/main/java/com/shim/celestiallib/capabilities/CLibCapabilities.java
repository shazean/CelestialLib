package com.shim.celestiallib.capabilities;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.capabilities.ISpaceFlight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class CLibCapabilities {

    public static final Capability<ISpaceFlight> SPACE_FLIGHT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<ICoolDown> COOLDOWN_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilities(RegisterCapabilitiesEvent eventIn) {
        eventIn.register(ISpaceFlight.class);
        eventIn.register(ICoolDown.class);
    }

    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> eventIn) {
        if (eventIn.getObject() instanceof Player) {
            if (!eventIn.getObject().getCapability(COOLDOWN_CAPABILITY).isPresent()) {
                eventIn.addCapability(new ResourceLocation(CelestialLib.MODID, "planet_cooldown"), new PlanetCoolDownProvider());
            }
        }
    }
}