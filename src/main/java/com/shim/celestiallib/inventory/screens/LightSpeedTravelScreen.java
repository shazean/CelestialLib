package com.shim.celestiallib.inventory.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.capabilities.PlanetCoolDownHandler;
import com.shim.celestiallib.inventory.menus.LightSpeedTravelMenu;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightSpeedTravelScreen extends AbstractContainerScreen<LightSpeedTravelMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/light_speed_travel.png");
    private static final ResourceLocation DEFAULT_BG = new ResourceLocation(CelestialLib.MODID, "textures/gui/light_speed_travel/default_galaxy_background.png");

    private static final Component TITLE = new TranslatableComponent("menu.celestiallib.light_speed_travel.title");
    private static final Component COST = new TranslatableComponent("menu.celestiallib.light_speed_travel.cost");
    private static final Component TRAVEL = new TranslatableComponent("menu.celestiallib.light_speed_travel.travel");

    private Galaxy selectedGalaxy;
    private Planet selectedPlanet;
    double xDrag = 0.0;
    double yDrag = 0.0;

    int scrollbarY = 7;
    boolean isScrolling;
    float scrollPercent = 0.0f;

    boolean isDragging;

    private final Galaxy[] galaxiesOnScreen = new Galaxy[5];
    private final ArrayList<PlanetWidget> planetsOnScreen = new ArrayList<>();

    private final Map<Planet, PlanetWidget> planetWidgets = new HashMap<>();

    private List<Component> tooltip = Lists.newArrayList();

    boolean canTravel = false;
    boolean travelClicked = false;

    public LightSpeedTravelScreen(LightSpeedTravelMenu menu, Inventory playerInventory, Component title) {
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

        font.draw(poseStack, TITLE, x + 124, y + 8, 4210752);

        this.renderGalaxyList(poseStack, x, y);
        this.renderGalaxyMap(poseStack, x, y);
        this.renderPlanetDetails(poseStack, x, y, mouseX, mouseY);
        this.renderPlanetTooltips(poseStack, x, y, mouseX, mouseY);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    protected boolean isScrollBarVisible() {
        return Galaxy.getVisibleGalaxies().size() > 5;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int p_97754_, double dragX, double dragY) {
        if (isScrollBarVisible() && this.isScrolling) {
            this.scrollbarY = (int) Mth.clamp(this.scrollbarY + dragY, 7, 98 - 27);
            this.scrollPercent = (this.scrollbarY - 7) / (float) 64;
            return true;
        }

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

        this.isScrolling = false;
        if (isScrollBarVisible()) {
            if (mouseY <= y + 27 + scrollbarY && mouseY >= y + scrollbarY && mouseX >= x + 109 && mouseX <= x + 109 + 6) { //mouse clicked on scrollbar
                this.isScrolling = true;
            }
        }

        if (isHovering(x - this.leftPos + 11, y - this.topPos + 8, 96, 91, mouseX, mouseY)) { //mouse clicked somewhere in galaxy list
            int yPos = Mth.clamp((int) ((mouseY - y - 7) / 18), 0, 5);

            Galaxy galaxy = this.galaxiesOnScreen[yPos];

            if (galaxy != null && !galaxy.isLocked()) {
                this.selectedGalaxy = galaxy;
                this.selectedPlanet = null;
                this.planetsOnScreen.clear();
                this.yDrag = 0;
                this.xDrag = 0;
            }
        }

        this.isDragging = false;
        if (isHovering(x - this.leftPos + 10, y - this.topPos + 107, 210, 100, mouseX, mouseY)) { //mouse clicked in galaxy map
            this.isDragging = true;
        }

        if (isHovering(x - this.leftPos + 124, y - this.topPos + 75, 98, 26, mouseX, mouseY)) {
            if (this.canTravel) {
                this.menu.handleLightSpeedTravel(this.selectedGalaxy, this.selectedPlanet);
                this.travelClicked = true;
                this.onClose();
            }
        }

        return super.mouseClicked(mouseX, mouseY, p_97750_);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        //TODO allow for inverted scrolling
        if (this.isScrollBarVisible()) {
            this.scrollbarY = (int) Mth.clamp(this.scrollbarY + amount, 7, 98 - 27);
            this.scrollPercent = (this.scrollbarY - 7) / (float) 64;
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int p_97814_) {
        this.isScrolling = false;
        if (this.isDragging) {
            this.isDragging = false;
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        for (PlanetWidget widget : this.planetsOnScreen) {

            if (this.isHovering(x - this.leftPos + widget.getX(), y - this.topPos + widget.getY(), widget.getWidth(), widget.getHeight(), mouseX, mouseY)) {
                if (widget.getPlanet().getGalaxy().areCooldownsEnabled() && widget.getPlanet().areCooldownsEnabled()) {
                    PlanetCoolDownHandler coolDownCap = (PlanetCoolDownHandler) CelestialLib.getCapability(CelestialLib.PROXY.getPlayer(), CLibCapabilities.COOLDOWN_CAPABILITY);
                    if (coolDownCap != null) {
                        if (coolDownCap.getCooldown(widget.getPlanet()).getCurrentCooldown() == 0) {
                            this.selectedPlanet = widget.getPlanet();
                        }
                        break;
                    }

                } else {
                    this.selectedPlanet = widget.getPlanet();
                    break;
                }
            }
        }

        this.travelClicked = false;

        return super.mouseReleased(mouseX, mouseY, p_97814_);
    }

    public void renderGalaxyList(PoseStack poseStack, int x, int y) {
        int i = 0;
        int j = 0;
        int k = 0;
        RenderSystem.setShaderTexture(0, TEXTURE);

        if (isScrollBarVisible()) {
            blit(poseStack, x + 109, y + scrollbarY, 230, 0, 6, 27, 512, 256);
        }

        for (Galaxy galaxy : Galaxy.getAlphabetizedList()) {

            boolean locked = galaxy.isLocked();
            boolean hidden = galaxy.isHidden();
            j++;

            if (hidden) continue;
            if (i >= 5 * 18) break;
            if (this.scrollPercent > ((float) j / Galaxy.DIMENSIONS.size())) continue;

            this.galaxiesOnScreen[k] = galaxy;
            k++;

            RenderSystem.setShaderTexture(0, TEXTURE);

            //bar
            boolean selected = this.selectedGalaxy == galaxy;
            int barYPos = locked ? 63 : selected ? 45 : 27;
            blit(poseStack, x + 10, y + 7 + i, 230, barYPos, 98, 18, 512, 256);

            //name
            String galaxyName = CelestialUtil.getDisplayName(galaxy.getDimension()).getString();

            ItemStack galaxyCost = galaxy.getLightSpeedCost(this.menu.getCurrentGalaxy());
            int maxLength = (galaxyCost == null) ? 96 - 19 : 96 - 18 - 21;

            boolean trim = false;
            while (this.font.width(galaxyName) > maxLength) {
                trim = true;
                galaxyName = StringUtils.truncate(galaxyName, galaxyName.length() - 1);
            }
            if (trim) {
                galaxyName = StringUtils.truncate(galaxyName, galaxyName.length() - 1);
                galaxyName = StringUtils.trim(galaxyName);
                galaxyName = galaxyName + "…";
            }

            this.font.draw(poseStack, galaxyName, x + 12 + 2 + 16, y + 9 + 4 + i, (locked ? 9474192 : 14737632));

            //travel resource requirements
            if (galaxyCost != null) {
                this.itemRenderer.renderAndDecorateFakeItem(galaxyCost, x + 108 - 19, y + 8 + i);
                this.itemRenderer.renderGuiItemDecorations(this.font, galaxyCost, x + 108 - 19, y + 8 + i);
            }

            //icon
            RenderSystem.setShaderTexture(0, galaxy.getIcon());
            blit(poseStack, x + 11, y + 8 + i, 0, 0, 16, 16, 16, 16);

            if (locked) {
                //fade
                RenderSystem.disableDepthTest();
                RenderSystem.colorMask(true, true, true, false);
                fillGradient(poseStack, x + 10, y + 7 + i, x + 10 + 98, y + 7 + 18 + i, slotColor, slotColor, 400);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();

                //lock
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, TEXTURE);
                blit(poseStack, x + 13, y + 8 + 2 + i, 236, 6, 12, 12, 512, 256);
            }

            i += 18;

        }
    }

    public void renderGalaxyMap(PoseStack poseStack, int x, int y) {

        if (this.selectedGalaxy == null) return;

        //background
        ResourceLocation bgImage = this.selectedGalaxy.getBackgroundImage() != null ? this.selectedGalaxy.getBackgroundImage() : DEFAULT_BG;
        int size = this.selectedGalaxy.getBackgroundImage() != null ? this.selectedGalaxy.getBackgroundImageSize() : 256;

        RenderSystem.setShaderTexture(0, bgImage);

        //default center the background… so line up the center with the center
        int centerOfImg = size / 2;

        int xPos = (int) Mth.clamp(centerOfImg - 105 - this.xDrag, 0, size - 210);
        int yPos = (int) Mth.clamp(centerOfImg - 50 - this.yDrag, 0, size - 100);

        blit(poseStack, x + 10, y + 107, xPos, yPos, 210, 100, size, size);

        //planets
        for (Planet planet : Planet.DIMENSIONS.values()) {

            if (CelestialUtil.getPlanetLocation(planet) == null) continue;
            if (!planet.getGalaxy().equals(this.selectedGalaxy)) continue;
            if (planet.isHidden()) continue;

            if (!planetWidgets.containsKey(planet)) {
                planetWidgets.put(planet, new PlanetWidget(planet, this.menu.getTravelDistance(planet)));
            }

            renderPlanet(poseStack, planet, x, y, size, xPos, yPos);
        }
    }

    public void renderPlanet(PoseStack poseStack, Planet planet, int x, int y, int galaxySize, int galaxyX, int galaxyY) {

        PlanetWidget widget = planetWidgets.get(planet);

        RenderSystem.setShaderTexture(0, widget.getTexture());

        //TODO somehow display a planet is locked/unavailable?

        Vec3 planetLoc = CelestialUtil.getPlanetLocation(planet.getDimension());
        if (planetLoc == null) {
            return;
        }
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

        if (this.selectedGalaxy == null) return;
        if (this.selectedPlanet == null) return;
        if (CelestialUtil.getPlanetLocation(this.selectedPlanet) == null) return;

        this.font.draw(poseStack, CelestialUtil.getDisplayName(this.selectedPlanet.getDimension()), x + 124, y + 20, 4210752);

        ItemStack galaxyCost = this.selectedGalaxy.getLightSpeedCost(this.menu.getCurrentGalaxy());
        ItemStack planetCost = this.selectedPlanet.getLightSpeedCost(this.menu.getTravelDistance(this.selectedPlanet));

        if (galaxyCost != null || planetCost != null) {
            this.font.draw(poseStack, COST, x + 124, y + 42, 4210752);
            int need;
            int have;

            int xPos;
            int yPos = 52;

            if (planetCost != null) {

                need = planetCost.getCount();
                have = this.menu.checkPlayerInventory(planetCost);
                xPos = (galaxyCost != null) ? 124 + 20 : 124;

                this.itemRenderer.renderAndDecorateFakeItem(planetCost, x + xPos, y + yPos);
//                this.itemRenderer.renderGuiItemDecorations(this.font, planetCost, x + xPos, y + yPos);
                this.renderItemStackText(poseStack, have, need, x + xPos, y + yPos);
                this.renderCostTooltip(poseStack, x + xPos, y + yPos, mouseX, mouseY, planetCost, have, need);

                if (have >= need)
                    this.canTravel = true;
            }

            if (galaxyCost != null) {
                need = galaxyCost.getCount();
                have = this.menu.checkPlayerInventory(galaxyCost);
                xPos = 124;

                this.itemRenderer.renderAndDecorateFakeItem(galaxyCost, x + xPos, y + yPos);
                this.renderItemStackText(poseStack, have, need, x + xPos, y + yPos);
                this.renderCostTooltip(poseStack, x + xPos, y + yPos, mouseX, mouseY, galaxyCost, have, need);

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