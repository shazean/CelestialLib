package com.shim.celestiallib.util;

import com.shim.celestiallib.CelestialLib;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class CLibKeybinds {

    public static KeyMapping OPEN_LIGHT_SPEED_TRAVEL;

    public static void register(FMLClientSetupEvent event) {
        OPEN_LIGHT_SPEED_TRAVEL = keymap("light_speed_travel", GLFW.GLFW_KEY_Y, "key.categories.celestiallib.light_speed");
    }

    private static KeyMapping keymap(String name, int defaultMapping, String category) {
        var keymap = new KeyMapping(String.format("key.%s.%s", CelestialLib.MODID, name), defaultMapping, category);
        ClientRegistry.registerKeyBinding(keymap);
        return keymap;
    }
}