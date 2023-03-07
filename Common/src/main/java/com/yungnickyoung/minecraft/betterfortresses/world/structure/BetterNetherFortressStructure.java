package com.yungnickyoung.minecraft.betterfortresses.world.structure;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.Optional;

public class BetterNetherFortressStructure extends StructureFeature<YungJigsawConfig> {
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public BetterNetherFortressStructure() {
        super(YungJigsawConfig.CODEC, ctx -> {
            WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
            worldgenRandom.setLargeFeatureSeed(ctx.seed(), ctx.chunkPos().x, ctx.chunkPos().z);

            // Only generate if location is valid
            if (!checkLocation(ctx)) {
                return Optional.empty();
            }

            // Determine start pos
            int minY = BetterFortressesCommon.CONFIG.general.startMinY;
            int maxY = BetterFortressesCommon.CONFIG.general.startMaxY;
            int y = Mth.randomBetweenInclusive(worldgenRandom, minY, maxY);
            BlockPos startPos = new BlockPos(ctx.chunkPos().getMiddleBlockX(), y, ctx.chunkPos().getMiddleBlockZ());

            return YungJigsawManager.assembleJigsawStructure(
                    ctx,
                    PoolElementStructurePiece::new,
                    startPos,
                    false,
                    false,
                    116);
        });
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<YungJigsawConfig> ctx) {
        // Check for any avoided structures nearby
        for (ResourceKey<StructureSet> structureSetToAvoid : ctx.config().getStructureSetAvoid()) {
            if (ctx.chunkGenerator().hasFeatureChunkInRange(structureSetToAvoid, ctx.seed(), ctx.chunkPos().x, ctx.chunkPos().z, ctx.config().getStructureAvoidRadius())) {
                return false;
            }
        }

        // Check for valid biome
        return ctx.validBiome().test(ctx.chunkGenerator().getNoiseBiome(
                QuartPos.fromBlock(ctx.chunkPos().getMiddleBlockX()),
                QuartPos.fromBlock(64),
                QuartPos.fromBlock(ctx.chunkPos().getMiddleBlockZ())));
    }
}
