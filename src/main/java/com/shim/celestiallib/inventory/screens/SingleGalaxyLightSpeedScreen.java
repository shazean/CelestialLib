package com.shim.celestiallib.inventory.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.inventory.menus.SingleGalaxyLightSpeedMenu;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.world.galaxy.Galaxy;
import com.shim.celestiallib.world.planet.Planet;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleGalaxyLightSpeedScreen extends AbstractContainerScreen<SingleGalaxyLightSpeedMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/light_speed_travel.png");
    private static final ResourceLocation DEFAULT_BG = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/default_galaxy_background.png");

    private static final Component TITLE = new TranslatableComponent("menu.celestiallib.light_speed_travel.title");
    private static final Component COST = new TranslatableComponent("menu.celestiallib.light_speed_travel.cost");
    private static final Component TRAVEL = new TranslatableComponent("menu.celestiallib.light_speed_travel.travel");

    private final Galaxy galaxy = Galaxy.getFirstGalaxy();
    private Planet selectedPlanet;
    double xDrag = 0.0;
    double yDrag = 0.0;
    boolean isDragging;

    private final ArrayList<PlanetWidget> planetsOnScreen = new ArrayList<>();
    private final Map<Planet, PlanetWidget> planetWidgets = new HashMap<>();

    private List<Component> tooltip = Lists.newArrayList();

    boolean canTravel = false;
    boolean travelClicked = false;

    public SingleGalaxyLightSpeedScreen(SingleGalaxyLightSpeedMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
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

        font.draw(poseStack, TITLE, x + 124, y + 136, 4210752);

//        this.renderGalaxyList(poseStack, x, y);
        this.renderGalaxyMap(poseStack, x, y);
        this.renderPlanetDetails(poseStack, x, y, mouseX, mouseY);
        this.renderPlanetTooltips(poseStack, x, y, mouseX, mouseY);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int p_97754_, double dragX, double dragY) {
        if (this.isDragging) {
            this.xDrag += dragX;
            this.yDrag += dragY;
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, p_97754_, dragY, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_97750_) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.isDragging = false;
        if (isHovering(x - this.leftPos + 10, y - this.topPos + 107, 210, 100, mouseX, mouseY)) { //mouse clicked in galaxy map
            this.isDragging = true;
        }

        if (isHovering(x - this.leftPos + 124, y - this.topPos + 75, 98, 26, mouseX, mouseY)) {
            if (this.canTravel) {
                this.menu.handleLightSpeedTravel(this.galaxy, this.selectedPlanet);
                this.travelClicked = true;
                this.onClose();
            }
        }

        return super.mouseClicked(mouseX, mouseY, p_97750_);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int p_97814_) {
        if (this.isDragging) {
            this.isDragging = false;
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        for (PlanetWidget planet : this.planetsOnScreen) {

            if (this.isHovering(x - this.leftPos + planet.getX(), y - this.topPos + planet.getY(), planet.getWidth(), planet.getHeight(), mouseX, mouseY)) {
                this.selectedPlanet = planet.planet;
                break;
            }
        }

        this.travelClicked = false;

        return super.mouseReleased(mouseX, mouseY, p_97814_);
    }

    public void renderGalaxyMap(PoseStack poseStack, int x, int y) {

        //background
        ResourceLocation bgImage = this.galaxy.getBackgroundImage() != null ? this.galaxy.getBackgroundImage() : DEFAULT_BG;
        int size = this.galaxy.getBackgroundImage() != null ? this.galaxy.getBackgroundImageSize() : 256;

        RenderSystem.setShaderTexture(0, bgImage);

        //default center the background… so line up the center with the center
        int centerOfImg = size / 2;

        int xPos = (int) Mth.clamp(centerOfImg - 105 - this.xDrag, 0, size - 210);
        int yPos = (int) Mth.clamp(centerOfImg - 50 - this.yDrag, 0, size - 100);

        blit(poseStack, x + 10, y + 10, xPos, yPos, 210, 100, size, size);

        //planets
        for (Planet planet : Planet.DIMENSIONS.values()) {
            if (!planetWidgets.containsKey(planet)) {
                planetWidgets.put(planet, new PlanetWidget(planet));
            }

//            if (!planet.getGalaxy().equals(this.selectedGalaxy)) continue;
            if (planet.isHidden()) continue;

            renderPlanet(poseStack, planet, x, y, size, xPos, yPos);
        }
    }

    public void renderPlanet(PoseStack poseStack, Planet planet, int x, int y, int galaxySize, int galaxyX, int galaxyY) {

        PlanetWidget widget = planetWidgets.get(planet);

        RenderSystem.setShaderTexture(0, widget.getTexture());

        //TODO somehow display a planet is locked/unavailable?

        Vec3 planetLoc = CelestialUtil.getPlanetLocation(planet.getDimension());
        int scale = planet.getGalaxy().getGuiScale();

        int size = widget.getSize();
        int xOffset = 0;
        int yOffset = 0;

        int xMovement = (galaxyX >= (galaxySize - 210)) ? -((galaxySize - 210) / 2) : ((galaxyX <= 0) ? ((galaxySize - 210) / 2) : (int) xDrag);
        int yMovement = (galaxyY >= (galaxySize - 100)) ? -((galaxySize - 100) / 2) : ((galaxyY <= 0) ? ((galaxySize - 100) / 2) : (int) yDrag);

        int xLoc = x + 10 + 105 - (size / 2) + xMovement + (int) (planetLoc.x() / scale);
        int yLoc = y + 107 + 50 - (size / 2) + yMovement + (int) (planetLoc.y() / scale);

//        CelestialLib.LOGGER.debug("yLoc: " + yLoc + ", galaxyY: " + galaxyY + ", yDrag: " + yDrag + ", yMovement: " + yMovement);
        int yStarting = 0;
        int xStarting = 0;

        if (yLoc < y + 107) {
            yOffset = Mth.clamp(y + 107 - yLoc, 0, size);
            yStarting = yOffset;
        }
        if (yLoc > y + 207 - size) {
            yOffset = -Mth.clamp(y + 207 - size - yLoc, -size, 0);
            yStarting = 0;
        }

        if (xLoc < x + 10) {
            xOffset = Mth.clamp(x + 10 - xLoc, 0, size);
            xStarting = xOffset;
        }
        if (xLoc > x + 219 - size) {
            xOffset = -Mth.clamp(x + 219 - size - xLoc, -size, 0);
            xStarting = 0;
        }

        yLoc = Mth.clamp(yLoc, y + 107, y + 207);
        xLoc = Mth.clamp(xLoc, x + 10, x + 219);

//        if (xLoc < 10 - size || xLoc > 219 + size || yLoc < 107 || yLoc > 207)
//            return;

        widget.setPos(xLoc - x, yLoc - y);
        widget.setDisplaySize(size - xOffset, size - yOffset);
        if (!this.planetsOnScreen.contains(widget))
            this.planetsOnScreen.add(widget);

        if (size - xOffset == 0 || size - yOffset == 0) {
            this.planetsOnScreen.remove(widget);
            return;
        }

        blit(poseStack, xLoc, yLoc, xStarting, yStarting, size - xOffset, size - yOffset, size, size);

    }

    public void renderPlanetDetails(PoseStack poseStack, int x, int y, int mouseX, int mouseY) {

        if (this.selectedPlanet == null) return;

        this.font.draw(poseStack, CelestialUtil.getDisplayName(this.selectedPlanet.getDimension()), x + 124, y + 20, 4210752);

        if (this.galaxy.getLightSpeedCost() != null || selectedPlanet.getLightSpeedCost() != null) {
            this.font.draw(poseStack, COST, x + 124, y + 42, 4210752);
            int need;
            int have;
            ItemStack cost;

            int xPos;
            int yPos = 52;

            if (selectedPlanet.getLightSpeedCost() != null) {

                need = this.selectedPlanet.getLightSpeedCost().getCount();
                have = this.menu.checkPlayerInventory(this.selectedPlanet.getLightSpeedCost());
                cost = this.selectedPlanet.getLightSpeedCost();
                xPos = (this.galaxy.getLightSpeedCost() != null) ? 124 + 20 : 124;

                this.itemRenderer.renderAndDecorateFakeItem(cost, x + xPos, y + yPos);
//                this.itemRenderer.renderGuiItemDecorations(this.font, cost, x + xPos, y + yPos);
                this.renderItemStackText(poseStack, have, need, x + xPos, y + yPos);
                this.renderCostTooltip(poseStack, x + xPos, y + yPos, mouseX, mouseY, cost, have, need);

                if (have >= need)
                    this.canTravel = true;
            }

            if (this.galaxy.getLightSpeedCost() != null) {
                need = this.galaxy.getLightSpeedCost().getCount();
                have = this.menu.checkPlayerInventory(this.galaxy.getLightSpeedCost());
                cost = this.galaxy.getLightSpeedCost();
                xPos = 124;

                this.itemRenderer.renderAndDecorateFakeItem(cost, x + xPos, y + yPos);
                this.renderItemStackText(poseStack, have, need, x + xPos, y + yPos);
                this.renderCostTooltip(poseStack, x + xPos, y + yPos, mouseX, mouseY, cost, have, need);

                if (have < need)
                    this.canTravel = false;

            }
        } else {
            this.canTravel = true;
        }

        int yPos = this.travelClicked ? 107 : 81;
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, x + 124, y + 75, 230, yPos, 98, 26, 512, 256);

        if (this.canTravel) {
            int width = this.font.width(TRAVEL.getString());
            this.font.draw(poseStack, TRAVEL, x + 121 + 49 - ((float) width / 2), y + 20 + 22 + 14 + 28, 4210752);
        }
    }

    public void renderPlanetTooltips(PoseStack poseStack, int x, int y, int mouseX, int mouseY) {

        if (this.isDragging) return;

        int i = this.leftPos;
        int j = this.topPos;

        for (PlanetWidget planet : this.planetsOnScreen) {

            if (this.isHovering(x - i + planet.getX(), y - j + planet.getY(), planet.getWidth(), planet.getHeight(), mouseX, mouseY)) {

                //render highlight
                RenderSystem.disableDepthTest();
                RenderSystem.colorMask(true, true, true, false);
                fillGradient(poseStack, x + planet.getX(), y + planet.getY(), x + planet.getX() + planet.getWidth(), y + planet.getY() + planet.getHeight(), slotColor, slotColor, 400);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();

                //render tooltip
                tooltip = planet.getTooltip();
                this.renderComponentTooltip(poseStack, this.tooltip, mouseX, mouseY);
            }
        }
    }

    public static int getColor(int have, int need) {
        return have >= need ? 458496 : 16711680;
    }

    public void renderItemStackText(PoseStack poseStack, int haveCount, int needCount, int x, int y) {
        poseStack.pushPose();
        String need = String.valueOf(needCount);
        double z = 200.0D;
        poseStack.translate(0.0D, 0.0D, this.getBlitOffset() + 200.0D);
        MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        this.font.drawShadow(poseStack, need, (float) (x + 19 - 2 - this.font.width(need)), (float) (y + 6 + 3), getColor(haveCount, needCount));
        multibuffersource$buffersource.endBatch();
        poseStack.popPose();
    }

    public void renderCostTooltip(PoseStack poseStack, int x, int y, int mouseX, int mouseY, ItemStack itemStack, int have, int need) {

        int i = this.leftPos;
        int j = this.topPos;

        if (isHovering(x - i, y - j, 16, 16, mouseX, mouseY)) {
            tooltip = Lists.newArrayList();
            this.tooltip.add(itemStack.getHoverName());
            this.tooltip.add(new TranslatableComponent("menu.celestiallib.light_speed_travel.have").append(new TextComponent(have + ", ").append(
                    new TranslatableComponent("menu.celestiallib.light_speed_travel.need").append(new TextComponent(need + "")))));

            this.renderComponentTooltip(poseStack, this.tooltip, mouseX, mouseY);
        }
    }
}