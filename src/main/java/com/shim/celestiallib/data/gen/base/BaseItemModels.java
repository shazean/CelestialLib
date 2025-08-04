package com.shim.celestiallib.data.gen.base;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class BaseItemModels extends ItemModelProvider {

    public BaseItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    private String name(ItemLike block) {
        return block.asItem().getRegistryName().getPath();
    }

    protected void self(Block block) {
        this.withExistingParent(name(block), this.modLoc("block/" + name(block)));
    }

    protected void spawnEgg(Item item) {
        this.withExistingParent(name(item), this.mcLoc("item/template_spawn_egg"));
    }

    public void generatedBlockItem(Block item) {
        this.singleTexture(name(item), new ResourceLocation("item/generated"), "layer0", modLoc("block/" + name(item)));
    }

    public void generatedBlockItem(Block item, String path) {
        this.singleTexture(name(item), new ResourceLocation("item/generated"), "layer0", modLoc(path));
    }

    //for all non hand-held single-texture items
    public void generatedItem(Item item) {
        this.singleTexture(name(item), new ResourceLocation("item/generated"), "layer0", modLoc("item/" + name(item)));
    }

    //for weapons, tools, sticks, etc.
    public void handheldItem(Item item) {
        this.singleTexture(name(item), new ResourceLocation("item/handheld"), "layer0", modLoc("item/" + name(item)));
    }

    public void stairsItem(Block stairs, String textureName) {
        stairs(name(stairs), modLoc("block/" + textureName), modLoc("block/" + textureName), modLoc("block/" + textureName));
    }

    public void slabItem(Block slab, String textureName) {
        slab(name(slab), modLoc("block/" + textureName), modLoc("block/" + textureName), modLoc("block/" + textureName));
    }

    public void wallItem(Block wall, String textureName) {
        wallInventory(name(wall), modLoc("block/" + textureName));
    }
}