package com.shim.celestiallib.capabilities;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.packets.CLibPacketHandler;
import com.shim.celestiallib.packets.CooldownDataPacket;
import com.shim.celestiallib.packets.ServerDidLightTravelPacket;
import com.shim.celestiallib.packets.ServerUnlockedCelestialPacket;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.world.celestials.ICelestial;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class UnlockCelestialsHandler implements IUnlock {

    Map<ICelestial, Boolean> LOCKED_CELESTIALS = new HashMap<>();
    Map<ICelestial, Boolean> LOCKED_LIGHT_SPEED_CELESTIALS = new HashMap<>();

    @Override
    public boolean isCelestialLocked(ICelestial celestial) {
        if (!LOCKED_CELESTIALS.containsKey(celestial))
            LOCKED_CELESTIALS.put(celestial, celestial.isLightSpeedLocked());
        return LOCKED_CELESTIALS.get(celestial);
    }

    @Override
    public void unlockCelestial(ICelestial celestial) {
        LOCKED_CELESTIALS.put(celestial, false);
    }

    @Override
    public boolean isCelestialLightSpeedLocked(ICelestial celestial) {
        if (!LOCKED_LIGHT_SPEED_CELESTIALS.containsKey(celestial))
            LOCKED_LIGHT_SPEED_CELESTIALS.put(celestial, celestial.isLightSpeedLocked());

        return LOCKED_LIGHT_SPEED_CELESTIALS.get(celestial);
    }

    @Override
    public void unlockCelestialLightSpeed(ICelestial celestial) {
        LOCKED_LIGHT_SPEED_CELESTIALS.put(celestial, false);
    }

    @Override
    public void sync(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            IUnlock cap = CelestialLib.getCapability(serverPlayer, CLibCapabilities.UNLOCK_CAPABILITY);

            if (cap != null) {
                for (ICelestial celestial : LOCKED_CELESTIALS.keySet())
                    CLibPacketHandler.INSTANCE.sendTo(new ServerUnlockedCelestialPacket(player.getId(), celestial.getDimension(), false, celestial.isGalaxy()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);

                for (ICelestial celestial : LOCKED_LIGHT_SPEED_CELESTIALS.keySet())
                    CLibPacketHandler.INSTANCE.sendTo(new ServerUnlockedCelestialPacket(player.getId(), celestial.getDimension(), true, celestial.isGalaxy()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);

            }
        }
    }

    @Override
    public CompoundTag getData() {
        CompoundTag nbt = new CompoundTag();

        int lockedSize = LOCKED_CELESTIALS.size();
        nbt.putInt("locked_size", lockedSize);

        int i = 0;
        for (ICelestial celestial : LOCKED_CELESTIALS.keySet()) {
            String type = (celestial instanceof Galaxy) ? "galaxy" : "planet";
            nbt.putString("celestial" + i + "_type", type);
            nbt.putString("celestial_" + i, celestial.getDimension().location().toString());
            nbt.putBoolean("celestial_" + i + "_is_locked", LOCKED_CELESTIALS.get(celestial));
            i++;
        }

        lockedSize = LOCKED_LIGHT_SPEED_CELESTIALS.size();
        nbt.putInt("light_speed_size", lockedSize);

        i = 0;
        for (ICelestial celestial : LOCKED_LIGHT_SPEED_CELESTIALS.keySet()) {
            String type = (celestial instanceof Galaxy) ? "galaxy" : "planet";
            nbt.putString("light_speed_celestial_" + i + "_type", type);
            nbt.putString("light_speed_celestial_" + i, celestial.getDimension().location().toString());
            nbt.putBoolean("light_speed_celestial_" + i + "_is_locked", LOCKED_LIGHT_SPEED_CELESTIALS.get(celestial));

            CelestialLib.LOGGER.debug("saving: type: " + type + " dim: " + celestial.getDimension().location().toString() + " locked: " + LOCKED_LIGHT_SPEED_CELESTIALS.get(celestial) + " i: " + i);

            i++;
        }

        return nbt;
    }

    @Override
    public void setData(CompoundTag nbt) {

        if (nbt.contains("locked_size")) {
            int size = nbt.getInt("locked_size");

            for (int i = 0; i < size; i++) {
                String type = nbt.getString("celestial_" + i + "_type");

                ICelestial celestial = null;
                if (type.equalsIgnoreCase("galaxy"))
                    celestial = CelestialUtil.getGalaxyFromString(nbt.getString("celestial_" + i));
                else if (type.equalsIgnoreCase("planet"))
                    celestial = CelestialUtil.getPlanetFromString(nbt.getString("celestial" + i));

                boolean isLocked = nbt.getBoolean("celestial_" + i + "_is_locked");
                if (celestial != null) {
                    LOCKED_CELESTIALS.put(celestial, isLocked);
                }
            }
        }

        if (nbt.contains("light_speed_size")) {
            int size = nbt.getInt("light_speed_size");

            for (int i = 0; i < size; i++) {
                String type = nbt.getString("light_speed_celestial_" + i + "_type");

                ICelestial celestial = null;
                if (type.equalsIgnoreCase("galaxy"))
                    celestial = CelestialUtil.getGalaxyFromString(nbt.getString("light_speed_celestial_" + i));
                else if (type.equalsIgnoreCase("planet"))
                    celestial = CelestialUtil.getPlanetFromString(nbt.getString("light_speed_celestial_" + i));

                boolean isLocked = nbt.getBoolean("light_speed_celestial_" + i + "_is_locked");
                if (celestial != null) {
                    LOCKED_LIGHT_SPEED_CELESTIALS.put(celestial, isLocked);

                    CelestialLib.LOGGER.debug("loading: type: " + type + " dim: " + celestial.getDimension().location().toString() + " locked: " + isLocked + " i: " + i);


                }
            }
        }
    }
}