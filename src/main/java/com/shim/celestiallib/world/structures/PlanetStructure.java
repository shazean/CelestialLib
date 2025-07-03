package com.shim.celestiallib.world.structures;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.world.galaxy.Galaxy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlanetStructure extends StructureFeature<CLibConfiguration> {

    public PlanetStructure() {
        super(CLibConfiguration.CODEC, PlanetStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<CLibConfiguration> context) {
        ChunkPos chunkpos = context.chunkPos();

        Galaxy galaxy = Galaxy.getGalaxy(context.config().getDimensionFromString());
        if (galaxy == null) {
            CelestialLib.LOGGER.error("isFeatureChunk could not find galaxy for dimension {}, returning null", context.config().getDimensionFromString());
            return false;
        }

        int ratio = galaxy.getGalaxyRatio();

        return chunkpos.x == (context.config().x() * ratio) && chunkpos.z == (context.config().z() * ratio);
    }

    public static @NotNull Optional<PieceGenerator<CLibConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<CLibConfiguration> context) {
        if (!PlanetStructure.isFeatureChunk(context)) {
            return Optional.empty();
        }

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);

        blockpos = new BlockPos(blockpos.getX(), 64, blockpos.getZ());

        Optional<PieceGenerator<CLibConfiguration>> structurePiecesGenerator =
                CLibPlacement.addPieces(context, PoolElementStructurePiece::new, blockpos, false, false);

        if(structurePiecesGenerator.isPresent()) {
            CelestialLib.LOGGER.debug("Planet at {}", blockpos);
        }

        return structurePiecesGenerator;
    }
}