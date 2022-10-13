package com.yungnickyoung.minecraft.betterfortresses.mixin;

import com.google.common.collect.ImmutableList;
import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.mixin.accessor.StructureManagerAccessor;
import com.yungnickyoung.minecraft.betterfortresses.mixin.accessor.WorldGenRegionAccessor;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.BasaltColumnsFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author TelepathicGrunt
 * <a href="https://github.com/TelepathicGrunt/RepurposedStructures">https://github.com/TelepathicGrunt/RepurposedStructures</a>
 */
@Mixin(BasaltColumnsFeature.class)
public class NoBasaltColumnsInStructuresMixin {
    @Inject(
            method = "canPlaceAt(Lnet/minecraft/world/level/LevelAccessor;ILnet/minecraft/core/BlockPos$MutableBlockPos;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void betterfortresses_noBasaltColumnsInStructures(LevelAccessor levelAccessor, int seaLevel, BlockPos.MutableBlockPos mutableBlockPos, CallbackInfoReturnable<Boolean> cir) {
        if (!(levelAccessor instanceof WorldGenRegion)) {
            return;
        }

        SectionPos sectionPos = SectionPos.of(mutableBlockPos);
        if (levelAccessor.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES, false) == null) {
            BetterFortressesCommon.LOGGER.warn("Better Fortresses: Detected a mod with a broken basalt columns configuredfeature that is trying to place blocks outside the 3x3 safe chunk area for features. Find the broken mod and report to them to fix the placement of their basalt columns feature.");
            return;
        }

        Registry<Structure> configuredStructureFeatureRegistry = levelAccessor.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
        StructureManager structureManager = ((WorldGenRegionAccessor) levelAccessor).getStructureManager();
        Structure fortressStructure = configuredStructureFeatureRegistry.get(new ResourceLocation(BetterFortressesCommon.MOD_ID, "fortress"));
        if (fortressStructure == null) {
            return;
        }

        if (getStructureAt(structureManager, mutableBlockPos, fortressStructure).isValid()) {
            cir.setReturnValue(false);
        }
    }

    /*
     * The following methods are taken from vanilla, with slight tweaks to prevent log spam.
     */

    private static StructureStart getStructureAt(StructureManager structureManager, BlockPos pos, Structure structure) {
        for (StructureStart structurestart : startsForStructure(structureManager, SectionPos.of(pos), structure)) {
            if (structurestart.getBoundingBox().isInside(pos)) {
                return structurestart;
            }
        }

        return StructureStart.INVALID_START;
    }

    private static List<StructureStart> startsForStructure(StructureManager structureManager, SectionPos sectionPos, Structure structure) {
        LongSet longset = ((StructureManagerAccessor) structureManager).getLevel().getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(structure);
        ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
        fillStartsForStructure(structureManager, structure, longset, builder::add);
        return builder.build();
    }

    private static void fillStartsForStructure(StructureManager structureManager, Structure structure, LongSet longSet, Consumer<StructureStart> consumer) {
        for (long i : longSet) {
            SectionPos sectionpos = SectionPos.of(new ChunkPos(i), ((StructureManagerAccessor) structureManager).getLevel().getMinSection());
            Optional<ChunkAccess> structureAccess = getChunk((WorldGenRegion) ((StructureManagerAccessor) structureManager).getLevel(), sectionpos.x(), sectionpos.z());
            if (structureAccess.isPresent()) {
                StructureStart structurestart = structureManager.getStartForStructure(sectionpos, structure, structureAccess.get());
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
