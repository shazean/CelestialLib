package com.shim.celestiallib.events;

import com.shim.celestiallib.packets.CLibPacketHandler;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CLibCommonEventSetup {

    public static void commonSetup(final FMLCommonSetupEvent event) {

        CLibPacketHandler.init();

    }
}
