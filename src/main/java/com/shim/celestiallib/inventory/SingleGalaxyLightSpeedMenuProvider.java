package com.shim.celestiallib.inventory;

import com.shim.celestiallib.inventory.menus.LightSpeedTravelMenu;
import com.shim.celestiallib.inventory.menus.SingleGalaxyLightSpeedMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class SingleGalaxyLightSpeedMenuProvider implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("menu.celestiallib.light_speed_travel_menu");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new SingleGalaxyLightSpeedMenu(containerId, inventory);
    }
}
