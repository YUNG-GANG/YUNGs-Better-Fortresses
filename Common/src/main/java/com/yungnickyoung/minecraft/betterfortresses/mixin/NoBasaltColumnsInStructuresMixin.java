package com.yungnickyoung.minecraft.betterfortresses.mixin;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.BasaltColumnsFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
            return;
        }

        Registry<Structure> configuredStructureFeatureRegistry = levelAccessor.registryAccess().registryOrThrow(Registries.STRUCTURE);
        StructureManager structureManager = ((WorldGenRegion) levelAccessor).getLevel().structureManager();
        Structure fortressStructure = configuredStructureFeatureRegistry.get(ResourceLocation.fromNamespaceAndPath(BetterFortressesCommon.MOD_ID, "fortress"));
        if (fortressStructure == null) {
            return;
        }

        if (structureManager.getStructureAt(mutableBlockPos, fortressStructure).isValid()) {
            cir.setReturnValue(false);
        }
    }
}
