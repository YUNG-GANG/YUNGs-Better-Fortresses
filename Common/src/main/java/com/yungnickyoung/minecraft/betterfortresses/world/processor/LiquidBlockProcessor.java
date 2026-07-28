package com.yungnickyoung.minecraft.betterfortresses.world.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Fluids;





public class LiquidBlockProcessor implements StructureProcessor {
    public static final MapCodec<LiquidBlockProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    BlockState.CODEC.fieldOf("target_block").forGetter(config -> config.targetBlock),
                    BlockState.CODEC.fieldOf("target_block_output").forGetter(config -> config.targetBlockOutput))
            .apply(instance, instance.stable(LiquidBlockProcessor::new)));

    public final BlockState targetBlock;
    public final BlockState targetBlockOutput;

    private LiquidBlockProcessor(BlockState targetBlock, BlockState targetBlockOutput) {
        this.targetBlock = targetBlock;
        this.targetBlockOutput = targetBlockOutput;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             BlockPos blockPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().is(this.targetBlock.getBlock())) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(ChunkPos.containing(blockInfoGlobal.pos()))) {
                return blockInfoGlobal;
            }
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), targetBlockOutput, blockInfoGlobal.nbt());
            if (levelReader instanceof WorldGenRegion worldGenRegion) {
                worldGenRegion.scheduleTick(blockInfoGlobal.pos(), Fluids.LAVA, 0);
            }
        }
        return blockInfoGlobal;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
