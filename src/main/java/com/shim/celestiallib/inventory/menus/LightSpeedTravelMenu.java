package com.shim.celestiallib.inventory.menus;

import com.shim.celestiallib.inventory.CLibMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;

public class LightSpeedTravelMenu extends AbstractContainerMenu {
    private final Level level;

    public LightSpeedTravelMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv);
    }

    public LightSpeedTravelMenu(int containerId, Inventory inv) {
        super(CLibMenus.LIGHT_SPEED_TRAVEL_MENU.get(), containerId);
        this.level = inv.player.level;

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
//        return stillValid(ContainerLevelAccess.NULL, player, CelestialBlocks.PLANET_CHART.get());
    }
}


