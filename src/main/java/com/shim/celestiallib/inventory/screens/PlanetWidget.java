package com.shim.celestiallib.inventory.screens;

import com.google.common.collect.Lists;
import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.world.planet.Planet;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PlanetWidget extends GuiComponent {
    public final Planet planet;
    private final int size;
    private final ResourceLocation planetTexture;
    private int x;
    private int y;
    private int width;
    private int height;

    private static final ResourceLocation DEFAULT_PLANET = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/planets/default_planet.png");

    public PlanetWidget(Planet planet) {
        this.planet = planet;
        this.planetTexture = planet.getTexture() != null ? planet.getTexture() : DEFAULT_PLANET;
        this.size = planet.getTextureSize();
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
        tooltip.add(new TextComponent("X: " + planetLoc.getX() + " / Z: " + planetLoc.getZ()));

        if (planet.getLightSpeedCost() != null && !planet.getLightSpeedCost().isEmpty()) {
            tooltip.add(new TranslatableComponent("menu.celestiallib.light_speed_travel.cost")
                    .append(new TextComponent(" " + planet.getLightSpeedCost().getCount() + " ")
                    .append(planet.getLightSpeedCost().getHoverName())));
        }

        //TODO add cooldown

        return tooltip;

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
