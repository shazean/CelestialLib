package com.shim.celestiallib.inventory.menus;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.api.capabilities.ISpaceFlight;
import com.shim.celestiallib.inventory.CLibMenus;
import com.shim.celestiallib.packets.CLibPacketHandler;
import com.shim.celestiallib.packets.DoLightTravelPacket;
import com.shim.celestiallib.packets.ServerResetCooldownPacket;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class LightSpeedTravelMenu extends AbstractContainerMenu {
    final Level level;
    final Inventory playerInventory;

    public LightSpeedTravelMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv);
    }

    public LightSpeedTravelMenu(int containerId, Inventory inv) {
        this(CLibMenus.LIGHT_SPEED_TRAVEL_MENU.get(), containerId, inv);
    }

    public LightSpeedTravelMenu(MenuType<? extends AbstractContainerMenu> menu, int containerID, Inventory inv) {
        super(menu, containerID);
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
                }
            }
            return count;
        }
        return 0;
    }

    public void handleLightSpeedTravel(Galaxy galaxy, Planet planet) {
        Player player = this.playerInventory.player;
        Entity spaceVehicle = null;

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

                ArrayList<Integer> entityIds = null;
                if (flightCap.getAdditionalEntitiesToTeleport(spaceVehicle) != null && !flightCap.getAdditionalEntitiesToTeleport(spaceVehicle).isEmpty()) {
                    entityIds = new ArrayList<>();
                    for (Entity entity : flightCap.getAdditionalEntitiesToTeleport(spaceVehicle)) {
                        entityIds.add(entity.getId());
                    }
                }

                if (spaceVehicle instanceof Player) {
                    CLibPacketHandler.INSTANCE.sendToServer(new ServerResetCooldownPacket(spaceVehicle.getId(), planet.getDimension()));
                } else if (spaceVehicle.getControllingPassenger() == player) {
                    CLibPacketHandler.INSTANCE.sendToServer(new ServerResetCooldownPacket(player.getId(), planet.getDimension()));
                }

                CLibPacketHandler.INSTANCE.sendToServer(new DoLightTravelPacket(spaceVehicle.getId(), entityIds, galaxy.getDimension(), planet.getDimension(), getTravelDistance(planet)));
            }
        }
    }

    public float getTravelDistance(Planet planetTravelingTo) {

        Player player = this.playerInventory.player;

        ChunkPos startPos = new ChunkPos(player.blockPosition());

        ChunkPos endPos = CelestialUtil.getPlanetChunkCoordinates(planetTravelingTo.getDimension());

        float x = endPos.x - startPos.x;
        float z = endPos.z - startPos.z;

        return Mth.sqrt(x * x + z * z);
    }

    public Galaxy getCurrentGalaxy() {
        return Galaxy.getGalaxy(this.playerInventory.player.level.dimension());
    }

}