package com.shim.celestiallib.inventory.screens;

import com.google.common.collect.Lists;
import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.ICoolDown;
import com.shim.celestiallib.capabilities.IUnlock;
import com.shim.celestiallib.capabilities.PlanetCooldown;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PlanetWidget extends GuiComponent {
    private final Planet planet;
    private final int size;
    private final ResourceLocation planetTexture;
    private int x;
    private int y;
    private int width;
    private int height;
    private float distance;

    private static final ResourceLocation DEFAULT_PLANET = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/planets/default_planet.png");

    public PlanetWidget(Planet planet, float distance) {
        this.planet = planet;
        this.planetTexture = planet.getTexture() != null ? planet.getTexture() : DEFAULT_PLANET;
        this.size = planet.getTextureSize();
        this.distance = distance;
    }

    public ResourceLocation getTexture() {
        return this.planetTexture;
    }

    public int getSize() {
        return this.size;
    }

    public List<Component> getTooltip() {
        List<Component> tooltip = Lists.newArrayList();

        tooltip.add(CelestialUtil.getDisplayName(planet.getDimension()));

        BlockPos planetLoc = CelestialUtil.getPlanetBlockCoordinates(planet.getDimension());
        if (planetLoc != null)
            tooltip.add(new TextComponent("X: " + planetLoc.getX() + " / Z: " + planetLoc.getZ()));

        ItemStack cost = planet.getLightSpeedCost(this.distance);

        if (cost != null && !cost.isEmpty()) {
            tooltip.add(new TranslatableComponent("menu.celestiallib.light_speed_travel.cost")
                    .append(new TextComponent(" " + cost.getCount() + " ")
                    .append(cost.getHoverName())));
        }

        IUnlock travelCap = CelestialLib.getCapability(CelestialLib.PROXY.getPlayer(), CLibCapabilities.UNLOCK_CAPABILITY);

        if (travelCap != null) {
            //only display cooldown if NOT light speed locked
            if (!travelCap.isCelestialLightSpeedLocked(planet)) {
                Player player = CelestialLib.PROXY.getPlayer();
                ICoolDown cooldownCap = CelestialLib.getCapability(player, CLibCapabilities.COOLDOWN_CAPABILITY);
                if (cooldownCap != null) {
                    PlanetCooldown cooldown = cooldownCap.getCooldown(planet);
                    if (cooldown != null)
                        tooltip.add(cooldown.getCooldownComponent());
                }
            } else {
                tooltip.add(new TranslatableComponent("menu.celestiallib.locked").withStyle(ChatFormatting.RED));
            }
        }

        return tooltip;

    }

    public Planet getPlanet() {
        return this.planet;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDisplaySize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
