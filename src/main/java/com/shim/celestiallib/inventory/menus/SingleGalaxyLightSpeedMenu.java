package com.shim.celestiallib.inventory.menus;

import com.shim.celestiallib.inventory.CLibMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class SingleGalaxyLightSpeedMenu extends LightSpeedTravelMenu {
    public SingleGalaxyLightSpeedMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        super(containerId, inv);
    }

    public SingleGalaxyLightSpeedMenu(int containerId, Inventory inv) {
        super(CLibMenus.SINGLE_GALAXY_LIGHT_SPEED_TRAVEL_MENU.get(), containerId, inv);
    }
}