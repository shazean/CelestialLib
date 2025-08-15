package com.shim.celestiallib.capabilities;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.packets.CLibPacketHandler;
import com.shim.celestiallib.packets.CooldownDataPacket;
import com.shim.celestiallib.util.CelestialUtil;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class PlanetCoolDownHandler implements ICoolDown {

    public final Map<Planet, PlanetCooldown> COOLDOWNS = Util.make((new HashMap<>()), (map) -> {
        for (Planet planet : Planet.DIMENSIONS.values()) {
            if (planet.getGalaxy().areCooldownsEnabled() && planet.areCooldownsEnabled()) {
                map.put(planet, new PlanetCooldown(planet));
            }
        }
    });


    @Override
    public PlanetCooldown getCooldown(Planet planet) {
        return COOLDOWNS.get(planet);
    }

    @Override
    public void decrementCooldowns() {
        for (PlanetCooldown cooldown : COOLDOWNS.values()) {
            if (cooldown != null)
                cooldown.decrementCooldown();
        }
    }

    public void setVisited(Planet planet) {
        COOLDOWNS.get(planet).setHasVisited();
    }

    @Override
    public void resetCooldown(Planet planet) {
        COOLDOWNS.get(planet).resetCooldown();
    }

    @Override
    public void sync(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ICoolDown travelCap = CelestialLib.getCapability(serverPlayer, CLibCapabilities.COOLDOWN_CAPABILITY);

            if (travelCap != null) {
                PacketDistributor.PacketTarget targetPlayer = PacketDistributor.PLAYER.with(() -> serverPlayer);
                CLibPacketHandler.INSTANCE.send(targetPlayer, new CooldownDataPacket(serverPlayer.getId(), travelCap.getData()));

            }
        }
    }

    @Override
    public CompoundTag getData() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("size", this.COOLDOWNS.size());
        int i = 0;
        for (PlanetCooldown cooldown : this.COOLDOWNS.values()) {
            if (cooldown != null) {
                nbt.putString("planet_" + i, cooldown.planet.getDimension().location().toString());
                nbt.put("planet_" + i + "_cooldown", cooldown.save());
                i++;
            }
        }

        return nbt;
    }

    @Override
    public void setData(CompoundTag nbt) {
        if (nbt.contains("size")) {
            int size = nbt.getInt("size");

            for (int i = 0; i < size; i++) {
                Planet planet = CelestialUtil.getPlanetFromString(nbt.getString("planet_" + i));

                PlanetCooldown cooldown = PlanetCooldown.createFromTag(nbt.getCompound("planet_" + i + "_cooldown"), planet);

                if (cooldown != null)
                    COOLDOWNS.put(planet, cooldown);
            }
        }

    }
}
