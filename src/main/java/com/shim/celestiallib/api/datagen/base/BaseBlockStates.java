package com.shim.celestiallib.api.datagen.base;

import com.shim.celestiallib.api.blocks.AbstractPortalBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class BaseBlockStates extends BlockStateProvider {

    public BaseBlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    public void buttonBlock(ButtonBlock block, ResourceLocation texture, ResourceLocation texturePressed) {
        ModelFile button = models().button(name(block), texture);
        ModelFile buttonPressed = models().buttonPressed(name(block) + "_pressed", texturePressed);
        buttonBlock(block, button, buttonPressed);
    }

    private String name(Block block) {
        return block.getRegistryName().getPath();
    }

    public void varietyBlock(Block block) {
        ModelFile var = models().cubeAll(name(block), blockTexture(block));
        ModelFile mirror = cubeMirroredAll(name(block), blockTexture(block));

        getVariantBuilder(block)
                .partialState().addModels(new ConfiguredModel(var), new ConfiguredModel(var, 0, 180, false),
                        new ConfiguredModel(mirror), new ConfiguredModel(mirror, 0, 180, false));
    }

    public void sandPathBlock(Block block, Block bottomTexture) {
        ModelFile model = models().withExistingParent(name(block), modLoc("template_sand_path"))
                .texture("side", blockTexture(block) + "_side").texture("top", blockTexture(block) + "_top")
                .texture("bottom", blockTexture(bottomTexture)).texture("particle", blockTexture(bottomTexture));

        getVariantBuilder(block)
                .partialState().addModels(new ConfiguredModel(model), new ConfiguredModel(model, 0, 90, false),
                        new ConfiguredModel(model, 0, 180, false), new ConfiguredModel(model, 0, 270, false));
    }

    public void sandBlock(Block block) {
        ModelFile model = models().cubeAll(name(block), blockTexture(block));

        getVariantBuilder(block)
                .partialState().addModels(new ConfiguredModel(model), new ConfiguredModel(model, 0, 90, false),
                        new ConfiguredModel(model, 0, 180, false), new ConfiguredModel(model, 0, 270, false));
    }

    public void portalBlock(Block block) {
        ModelFile ew_model = models().withExistingParent(block.getRegistryName().getPath() + "_ew", modLoc("template_portal_ew"))
                .texture("portal", blockTexture(block)).texture("particle", blockTexture(block));
        ModelFile ns_model = models().withExistingParent(block.getRegistryName().getPath() + "_ns", modLoc("template_portal_ns"))
                .texture("portal", blockTexture(block)).texture("particle", blockTexture(block));

        getVariantBuilder(block)
                .partialState().with(AbstractPortalBlock.AXIS, Direction.Axis.Z).modelForState().modelFile(ew_model).addModel()
                .partialState().with(AbstractPortalBlock.AXIS, Direction.Axis.X).modelForState().modelFile(ns_model).addModel();
    }

    public void smoothStoneSlabBlock(Block block, String prefix) {
        ModelFile bottomSlab = models().withExistingParent(block.getRegistryName().getPath(), mcLoc("slab"))
                .texture("bottom", modLoc("block/" + prefix + "_smooth_stone"))
                .texture("top", modLoc("block/" + prefix + "_smooth_stone"))
                .texture("side", modLoc("block/" + prefix + "_smooth_stone_slab_side"));

        ModelFile doubleSlab = models().withExistingParent(block.getRegistryName().getPath() + "_double", mcLoc("cube_column"))
                .texture("end", modLoc("block/" + prefix + "_smooth_stone"))
                .texture("side", modLoc("block/" + prefix + "_smooth_stone_slab_side"));

        ModelFile topSlab = models().withExistingParent(block.getRegistryName().getPath() + "_top", mcLoc("slab_top"))
                .texture("bottom", modLoc("block/" + prefix + "_smooth_stone"))
                .texture("top", modLoc("block/" + prefix + "_smooth_stone"))
                .texture("side", modLoc("block/" + prefix + "_smooth_stone_slab_side"));

        getVariantBuilder(block)
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).modelForState().modelFile(bottomSlab).addModel()
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).modelForState().modelFile(doubleSlab).addModel()
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).modelForState().modelFile(topSlab).addModel();
    }

    public BlockModelBuilder cubeMirroredAll(String name, ResourceLocation texture) {
        return models().withExistingParent(name + "_mirrored", mcLoc("block/cube_mirrored_all")).texture("all", texture);
    }
}
