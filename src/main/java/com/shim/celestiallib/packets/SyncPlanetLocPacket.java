package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.ICoolDown;
import com.shim.celestiallib.util.CelestialUtil;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class SyncPlanetLocPacket {

    Map<ResourceKey<Level>, Vec3> data;

    public SyncPlanetLocPacket() {
        this(CelestialUtil.getPlanetLocData());
    }

    public SyncPlanetLocPacket(Map<ResourceKey<Level>, Vec3> data) {
        this.data = data;
    }

    public static void encoder(SyncPlanetLocPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.data, (buf, dimension) -> buf.writeResourceLocation(dimension.location()),
                (buf, loc) -> {
            buf.writeDouble(loc.x());
            buf.writeDouble(loc.y());
            buf.writeDouble(loc.z());
        });
    }

    public static SyncPlanetLocPacket decoder(FriendlyByteBuf buffer) {
        return new SyncPlanetLocPacket(buffer.readMap(
                (buf) -> ResourceKey.create(Registry.DIMENSION_REGISTRY, buf.readResourceLocation()),
                (buf) -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())));
    }

    public static void handle(SyncPlanetLocPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            CelestialUtil.syncPlanetLocData(message.data);

        });
        context.setPacketHandled(true);
    }
}