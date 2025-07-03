package com.shim.celestiallib.inventory.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.inventory.menus.LightSpeedTravelMenu;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class LightSpeedTravelScreen extends AbstractContainerScreen<LightSpeedTravelMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/light_speed_travel.png");

    private static final ResourceLocation DEFAULT_BG = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/default_galaxy_background.png");
    private static final ResourceLocation DEFAULT_GALAXY = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/galaxy_icons/default_galaxy.png");
    private static final ResourceLocation DEFAULT_PLANET = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/planets/default_planet_icon.png");

    private static Component TITLE = new TranslatableComponent("menu.celestiallib.light_speed_travel");


    private List<Component> tooltip = Lists.newArrayList();

    public LightSpeedTravelScreen(LightSpeedTravelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.imageWidth = 230;
        this.imageHeight = 219;

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight, 512, 256);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        int i = this.leftPos;
        int j = this.topPos;

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.renderBg(poseStack, delta, mouseX, mouseY);
//        super.render(poseStack, mouseX, mouseY, delta);

        font.draw(poseStack, TITLE, x + 121, y + 8, 4210752);

        this.renderGalaxies(poseStack);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    public void renderGalaxies(PoseStack poseStack) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //FIXME simplified for testing placement
        //TODO scrollbar

        //TODO check if galaxy selected or not
        RenderSystem.setShaderTexture(0, TEXTURE);

        //placement, X; placement Y, grab starting at, X; grab starting at, Y; width?; height?;

        //bar
        this.blit(poseStack, x + 10, y + 7, 230, 27, 98, 18, 512, 256);

        //galaxy icon
        //TODO grab galaxy icon & pass in size
        RenderSystem.setShaderTexture(0, DEFAULT_GALAXY);
        blit(poseStack, x + 11, y + 8, 0, 0, 16, 16, 16, 16);
        this.font.draw(poseStack, "Test Galaxy", x + 12 + 2 + 16, y + 9 + 5, 0);



    }

    public void renderGalaxyMap(PoseStack poseStack) {

    }

}
