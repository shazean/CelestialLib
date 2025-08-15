package com.shim.celestiallib.packets;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.ICoolDown;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CooldownDataPacket {
    private final int playerId;
    private final CompoundTag travelData;

    public CooldownDataPacket(int playerId, CompoundTag travelData) {
        this.playerId = playerId;
        this.travelData = travelData;
    }

    public static void encoder(CooldownDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.playerId);
        buffer.writeNbt(packet.travelData);
    }

    public static CooldownDataPacket decoder(FriendlyByteBuf buffer) {
        return new CooldownDataPacket(buffer.readInt(), buffer.readNbt());
    }

    public static void handle(CooldownDataPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
//            ServerPlayer player = context.getSender();
            Player player;

            player = CelestialLib.PROXY.getPlayer();

            if (player != null) {
                ICoolDown travelCap = CelestialLib.getCapability(player, CLibCapabilities.COOLDOWN_CAPABILITY);

                if (travelCap != null) {
                    travelCap.setData(message.travelData);
                }
            }
        });
        context.setPacketHandled(true);
    }
}