package com.yungnickyoung.minecraft.betterfortresses.util;

import com.google.common.collect.ImmutableList;
import com.yungnickyoung.minecraft.betterfortresses.mixin.accessor.StructureFeatureManagerAccessor;
import com.yungnickyoung.minecraft.betterfortresses.mixin.accessor.WorldGenRegionAccessor;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MixinUtil {
    public static StructureStart getStructureAt(StructureFeatureManager structureManager, BlockPos pos, ConfiguredStructureFeature<?, ?> structure) {
        for(StructureStart structureStart : startsForStructure(structureManager, SectionPos.of(pos), structure)) {
            if (structureStart.getBoundingBox().isInside(pos)) {
                return structureStart;
            }
        }

        return StructureStart.INVALID_START;
    }

    private static List<StructureStart> startsForStructure(StructureFeatureManager structureManager, SectionPos sectionPos, ConfiguredStructureFeature<?, ?> structure) {
        LongSet longset = ((StructureFeatureManagerAccessor) structureManager).getLevel().getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForFeature(structure);
        ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
        fillStartsForStructure(structureManager, structure, longset, builder::add);
        return builder.build();
    }

    private static void fillStartsForStructure(StructureFeatureManager structureManager, ConfiguredStructureFeature<?, ?> structure, LongSet longSet, Consumer<StructureStart> consumer) {
        for (long i : longSet) {
            SectionPos sectionpos = SectionPos.of(new ChunkPos(i), ((StructureFeatureManagerAccessor) structureManager).getLevel().getMinSection());
            Optional<ChunkAccess> chunkAccess = getChunk((WorldGenRegion) ((StructureFeatureManagerAccessor) structureManager).getLevel(), sectionpos.x(), sectionpos.z());
            if (chunkAccess.isPresent()) {
                StructureStart structurestart = structureManager.getStartForFeature(sectionpos, structure, chunkAccess.get());
                if (structurestart != null && structurestart.isValid()) {
                    consumer.accept(structurestart);
                }
            }
        }
    }

    private static Optional<ChunkAccess> getChunk(WorldGenRegion worldGenRegion, int chunkX, int chunkZ) {
        WorldGenRegionAccessor accessor = ((WorldGenRegionAccessor) worldGenRegion);
        if (worldGenRegion.hasChunk(chunkX, chunkZ)) {
            int i = chunkX - accessor.getFirstPos().x;
            int j = chunkZ - accessor.getFirstPos().z;
            ChunkAccess chunkAccess = accessor.getCache().get(i + j * accessor.getSize());
            if (chunkAccess.getStatus().isOrAfter(ChunkStatus.STRUCTURE_STARTS)) {
                return Optional.of(chunkAccess);
            }
        }

        return Optional.empty();
    }
}
