package com.shim.celestiallib.inventory.menus;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.ISpaceFlight;
import com.shim.celestiallib.inventory.CLibMenus;
import com.shim.celestiallib.packets.CLibPacketHandler;
import com.shim.celestiallib.packets.DoLightTravelPacket;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.world.galaxy.Galaxy;
import com.shim.celestiallib.world.planet.Planet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class LightSpeedTravelMenu extends AbstractContainerMenu {
    private final Level level;
    private final Inventory playerInventory;

    public LightSpeedTravelMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv);
    }

    public LightSpeedTravelMenu(int containerId, Inventory inv) {
        super(CLibMenus.LIGHT_SPEED_TRAVEL_MENU.get(), containerId);
        this.level = inv.player.level;
        this.playerInventory = inv;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public int checkPlayerInventory(ItemStack itemStack) {


        if (this.playerInventory.contains(itemStack)) {
            int count = 0;
            for (ItemStack item : this.playerInventory.items) {
                if (item.is(itemStack.getItem())) {
                    count += item.getCount();
//                    CelestialLib.LOGGER.debug("item: " + item.getCount() + " " + item.getDisplayName().getString());
                }
            }
//            CelestialLib.LOGGER.debug("checking player inventory… player has " + count + " " + itemStack.getDisplayName() + " " + item);

            return count;
        }
        return 0;
    }

    public void handleLightSpeedTravel(Galaxy galaxy, Planet planet) {
        CelestialLib.LOGGER.debug("should be doing light speed travel now…");

        Player player = CelestialLib.PROXY.getPlayer();
        Entity spaceVehicle = null;

        if (player != null) {

            ISpaceFlight flightCap = CelestialLib.getCapability(player, CLibCapabilities.SPACE_FLIGHT_CAPABILITY);
            if (flightCap != null) spaceVehicle = player;
            else {
                if (player.getVehicle() != null) {
                    flightCap = CelestialLib.getCapability(player.getVehicle(), CLibCapabilities.SPACE_FLIGHT_CAPABILITY);
                    if (flightCap != null) spaceVehicle = player.getVehicle();
                }
            }

            if (spaceVehicle != null) {
                if (flightCap.canLightSpeedTravel(spaceVehicle)) {

                    BlockPos planetPos = CelestialUtil.getPlanetBlockCoordinates(planet.getDimension());

                    ArrayList<Integer> entityIds = null;
                    if (flightCap.getAdditionalEntitiesToTeleport(spaceVehicle) != null && !flightCap.getAdditionalEntitiesToTeleport(spaceVehicle).isEmpty()) {
                        entityIds = new ArrayList<>();
                        for (Entity entity : flightCap.getAdditionalEntitiesToTeleport(spaceVehicle)) {
                            entityIds.add(entity.getId());
                        }
                    }

                    CelestialLib.LOGGER.debug("should be sending packet next…");

                    if (spaceVehicle instanceof Player || spaceVehicle.getControllingPassenger() == player) {

                        //TODO
//                        CLibPacketHandler.INSTANCE.sendToServer(new ServerResetLightTravelPacket(spaceVehicle.getFirstPassenger().getId(), galaxy));


                    }

                    int galaxyCost = galaxy.getLightSpeedCost().getCount();
                    int planetCost = planet.getLightSpeedCost().getCount();

                    while (galaxyCost > 0) {
                        for (ItemStack item : this.playerInventory.items) {
                            if (item.is(galaxy.getLightSpeedCost().getItem())) {
                                int i = Math.min(item.getCount(), galaxyCost);
                                galaxyCost -= i;
                                item.shrink(i);
                                if (galaxyCost == 0) break;
                            }
                        }
                    }

                    while (planetCost > 0) {
                        CelestialLib.LOGGER.debug("running remove planet cost items…");
                        for (ItemStack item : this.playerInventory.items) {
                            if (item.is(planet.getLightSpeedCost().getItem())) {
                                int i = Math.min(item.getCount(), planetCost);
                                planetCost -= i;
                                item.shrink(i);
                                if (planetCost == 0) break;
                            }
                        }
                    }

                    CLibPacketHandler.INSTANCE.sendToServer(new DoLightTravelPacket(spaceVehicle.getId(), entityIds, galaxy.getDimension(), planetPos));

                }
            }
        }
    }
}


