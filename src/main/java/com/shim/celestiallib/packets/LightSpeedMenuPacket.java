package com.shim.celestiallib.packets;

import com.shim.celestiallib.inventory.LightSpeedMenuProvider;
import com.shim.celestiallib.inventory.SingleGalaxyLightSpeedMenuProvider;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class LightSpeedMenuPacket {

    public LightSpeedMenuPacket() {}

    public static void encoder(LightSpeedMenuPacket packet, FriendlyByteBuf buffer) {}

    public static LightSpeedMenuPacket decoder(FriendlyByteBuf buffer) {
        return new LightSpeedMenuPacket();
    }

    public static void handle(LightSpeedMenuPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {

                if (Galaxy.DIMENSIONS.size() == 1)
                    NetworkHooks.openGui(player, new SingleGalaxyLightSpeedMenuProvider());
                else
                    NetworkHooks.openGui(player, new LightSpeedMenuProvider());
            }
        });
        context.setPacketHandled(true);
    }
}