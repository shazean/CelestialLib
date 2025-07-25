package com.shim.celestiallib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public interface IProxy {
    default Player getPlayer() {
        return null;
    }
    default Minecraft getMinecraft() {
        return null;
    }
}
