package com.shim.celestiallib.inventory;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.inventory.menus.LightSpeedTravelMenu;
import com.shim.celestiallib.inventory.menus.SingleGalaxyLightSpeedMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CLibMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CelestialLib.MODID);

    public static final RegistryObject<MenuType<LightSpeedTravelMenu>> LIGHT_SPEED_TRAVEL_MENU = MENUS.register("light_speed_travel",
            () -> IForgeMenuType.create(LightSpeedTravelMenu::new));

    public static final RegistryObject<MenuType<SingleGalaxyLightSpeedMenu>> SINGLE_GALAXY_LIGHT_SPEED_TRAVEL_MENU = MENUS.register("single_galaxy_light_speed_travel",
            () -> IForgeMenuType.create(SingleGalaxyLightSpeedMenu::new));


}
