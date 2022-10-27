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
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BridgeArchProcessor extends StructureProcessor {
    public static final BridgeArchProcessor INSTANCE = new BridgeArchProcessor();
    public static final Codec<BridgeArchProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.is(Blocks.PRISMARINE_STAIRS) || blockInfoGlobal.state.is(Blocks.PRISMARINE_BRICK_STAIRS)) {
            Direction facing = structurePlacementData.getRotation().rotate(blockInfoGlobal.state.getValue(StairBlock.FACING));
            boolean isJunction = blockInfoGlobal.state.is(Blocks.PRISMARINE_STAIRS);

            // Modify the block itself
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.NETHER_BRICKS.defaultBlockState(), null);

            if (!(levelReader instanceof WorldGenRegion worldGenRegion)) {
                return blockInfoGlobal;
            }

            if (levelReader.getChunk(blockInfoGlobal.pos.above(3)).getBlockState(blockInfoGlobal.pos.above(3)).isAir()) {
                return blockInfoGlobal;
            }

            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos.mutable();

            // Generate first pillar
            mutable.move(Direction.UP);
            placeBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICKS.defaultBlockState());
            mutable.move(Direction.UP);
            placeWallBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICK_WALL.defaultBlockState()
                    .setValue(WallBlock.EAST_WALL, facing == Direction.EAST || (isJunction && facing == Direction.NORTH) ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.SOUTH_WALL, facing == Direction.SOUTH || (isJunction && facing == Direction.EAST) ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.WEST_WALL, facing == Direction.WEST || (isJunction && facing == Direction.SOUTH) ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.NORTH_WALL, facing == Direction.NORTH || (isJunction && facing == Direction.WEST) ? WallSide.LOW : WallSide.NONE)
            );

            // Second pillar
            mutable.move(facing);
            placeBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICK_STAIRS.defaultBlockState()
                    .setValue(StairBlock.HALF, Half.TOP)
                    .setValue(StairBlock.FACING, facing.getOpposite()));
            mutable.move(Direction.UP);
            placeWallBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICK_WALL.defaultBlockState()
                            .setValue(WallBlock.EAST_WALL, facing == Direction.EAST ? WallSide.LOW : WallSide.NONE)
                            .setValue(WallBlock.SOUTH_WALL, facing == Direction.SOUTH ? WallSide.LOW : WallSide.NONE)
                            .setValue(WallBlock.WEST_WALL, facing == Direction.WEST ? WallSide.LOW : WallSide.NONE)
                            .setValue(WallBlock.NORTH_WALL, facing == Direction.NORTH ? WallSide.LOW : WallSide.NONE)
            );

            // Middle
            mutable.move(facing);
            placeBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICKS.defaultBlockState());
            mutable.move(Direction.UP);
            placeWallBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICK_WALL.defaultBlockState());

            // Fourth pillar
            mutable.move(facing);
            mutable.move(Direction.DOWN);
            placeWallBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICK_WALL.defaultBlockState()
                    .setValue(WallBlock.EAST_WALL, facing == Direction.WEST ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.SOUTH_WALL, facing == Direction.NORTH ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.WEST_WALL, facing == Direction.EAST ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.NORTH_WALL, facing == Direction.SOUTH ? WallSide.LOW : WallSide.NONE)
            );
            mutable.move(Direction.DOWN);
            placeBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICK_STAIRS.defaultBlockState()
                    .setValue(StairBlock.HALF, Half.TOP)
                    .setValue(StairBlock.FACING, facing));

            // Fifth pillar
            mutable.move(facing);
            placeWallBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICK_WALL.defaultBlockState()
                    .setValue(WallBlock.EAST_WALL, facing == Direction.WEST || (isJunction && facing == Direction.NORTH) ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.SOUTH_WALL, facing == Direction.NORTH || (isJunction && facing == Direction.EAST) ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.WEST_WALL, facing == Direction.EAST || (isJunction && facing == Direction.SOUTH) ? WallSide.LOW : WallSide.NONE)
                    .setValue(WallBlock.NORTH_WALL, facing == Direction.SOUTH || (isJunction && facing == Direction.WEST) ? WallSide.LOW : WallSide.NONE)
            );
            mutable.move(Direction.DOWN);
            placeBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICKS.defaultBlockState());
            mutable.move(Direction.DOWN);
            placeBlock(worldGenRegion, levelReader, mutable, Blocks.NETHER_BRICKS.defaultBlockState());
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.BRIDGE_ARCH_PROCESSOR;
    }

    private void placeBlock(WorldGenRegion worldGenRegion, LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        if (worldGenRegion.getCenter().equals(new ChunkPos(blockPos))) {
            levelReader.getChunk(blockPos).setBlockState(blockPos, blockState, false);
        }
    }

    private void placeWallBlock(WorldGenRegion worldGenRegion, LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        if (worldGenRegion.getCenter().equals(new ChunkPos(blockPos))) {
            BlockPos.MutableBlockPos mutable = blockPos.mutable();
            mutable.move(Direction.NORTH);
            boolean north = levelReader.getChunk(mutable).getBlockState(mutable).isFaceSturdy(levelReader, mutable, Direction.SOUTH);
            mutable.set(blockPos).move(Direction.EAST);
            boolean east = levelReader.getChunk(mutable).getBlockState(mutable).isFaceSturdy(levelReader, mutable, Direction.WEST);
            mutable.set(blockPos).move(Direction.SOUTH);
            boolean south = levelReader.getChunk(mutable).getBlockState(mutable).isFaceSturdy(levelReader, mutable, Direction.NORTH);
            mutable.set(blockPos).move(Direction.WEST);
            boolean west = levelReader.getChunk(mutable).getBlockState(mutable).isFaceSturdy(levelReader, mutable, Direction.EAST);
            levelReader.getChunk(blockPos).setBlockState(blockPos, blockState
                    .setValue(WallBlock.NORTH_WALL, north ? WallSide.LOW : blockState.getValue(WallBlock.NORTH_WALL))
                    .setValue(WallBlock.EAST_WALL, east ? WallSide.LOW : blockState.getValue(WallBlock.EAST_WALL))
                    .setValue(WallBlock.SOUTH_WALL, south ? WallSide.LOW : blockState.getValue(WallBlock.SOUTH_WALL))
                    .setValue(WallBlock.WEST_WALL, west ? WallSide.LOW : blockState.getValue(WallBlock.WEST_WALL))
                    .setValue(WallBlock.UP, true),
                    false);
        }
    }
}
