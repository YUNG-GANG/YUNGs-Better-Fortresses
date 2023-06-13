package com.yungnickyoung.minecraft.betterfortresses.world.processor;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterfortresses.module.StructureProcessorTypeModule;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StairPillarProcessor extends StructureProcessor {
    public static final StairPillarProcessor INSTANCE = new StairPillarProcessor();
    public static final Codec<StairPillarProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().is(Blocks.STONE_BRICK_STAIRS) || blockInfoGlobal.state().is(Blocks.PURPUR_STAIRS)) {
            Direction facing = blockInfoGlobal.state().getValue(StairBlock.FACING);
            Half half = blockInfoGlobal.state().getValue(StairBlock.HALF);
            StairsShape shape = blockInfoGlobal.state().getValue(StairBlock.SHAPE);

            // Modify the block itself
            BlockState output;
            if (blockInfoGlobal.state().is(Blocks.PURPUR_STAIRS)) {
                output = Blocks.RED_NETHER_BRICKS.defaultBlockState();
            } else {
                output = Blocks.RED_NETHER_BRICK_STAIRS.defaultBlockState()
                        .setValue(StairBlock.FACING, facing)
                        .setValue(StairBlock.HALF, half)
                        .setValue(StairBlock.SHAPE, shape);
            }

            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), output, blockInfoGlobal.nbt());

            // Begin generating offset pillar
            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos().mutable().move(facing);
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(mutable))) {
                return blockInfoGlobal;
            }

            // Generate top block (stairs)
            levelReader.getChunk(mutable).setBlockState(mutable,
                    Blocks.RED_NETHER_BRICK_STAIRS.defaultBlockState()
                            .setValue(StairBlock.FACING, facing.getOpposite())
                            .setValue(StairBlock.HALF, half)
                            .setValue(StairBlock.SHAPE, shape),
                    false);

            // Generate rest of pillar
            mutable.move(Direction.DOWN);
            BlockState currBlockState = levelReader.getBlockState(mutable);
            while (mutable.getY() > levelReader.getMinBuildHeight()
                    && mutable.getY() < levelReader.getMaxBuildHeight()
                    && (currBlockState.isAir() || !levelReader.getFluidState(mutable).isEmpty())) {
                levelReader.getChunk(mutable).setBlockState(mutable, Blocks.RED_NETHER_BRICKS.defaultBlockState(), false);

                // Update to next position
                mutable.move(Direction.DOWN);
                currBlockState = levelReader.getBlockState(mutable);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.STAIR_PILLAR_PROCESSOR;
    }
}
