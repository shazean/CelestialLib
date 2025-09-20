package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.IUnlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPlayerLockedCelestialsPacket {

    private final int playerId;
    private CompoundTag data;

    public SyncPlayerLockedCelestialsPacket(int playerId, CompoundTag data) {
        this.playerId = playerId;
        this.data = data;
    }

    public static void encoder(SyncPlayerLockedCelestialsPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.playerId);
        buffer.writeNbt(packet.data);
    }

    public static SyncPlayerLockedCelestialsPacket decoder(FriendlyByteBuf buffer) {
        return new SyncPlayerLockedCelestialsPacket(buffer.readInt(), buffer.readNbt());
    }

    public static void handle(SyncPlayerLockedCelestialsPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {

            Player player = CelestialLib.PROXY.getPlayer();
            IUnlock cap = CelestialLib.getCapability(player, CLibCapabilities.UNLOCK_CAPABILITY);

            if (cap != null) {

                cap.setData(message.data);

            }
        });
        context.setPacketHandled(true);
    }
}