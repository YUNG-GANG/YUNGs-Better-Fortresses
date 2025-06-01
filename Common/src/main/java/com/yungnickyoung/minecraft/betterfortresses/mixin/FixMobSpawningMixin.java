package com.yungnickyoung.minecraft.betterfortresses.mixin;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.class)
public class FixMobSpawningMixin {
    @Unique
    private static final ResourceKey<Structure> BETTER_FORTRESS = ResourceKey.create(
            Registries.STRUCTURE,
            BetterFortressesCommon.id("fortress"));

    /**
     * Vanilla Nether Fortresses have a hard-coded check for spawning monsters on Nether Brick blocks.
     * This mixin copies that behavior for Better Fortresses.
     */
    @Inject(method = "isInNetherFortressBounds", at = @At("HEAD"), cancellable = true)
    private static void betterfortresses_fixMobSpawning(BlockPos pos, ServerLevel level, MobCategory category,
                                                        StructureManager structureManager, CallbackInfoReturnable<Boolean> cir) {
        if (category == MobCategory.MONSTER && level.getBlockState(pos.below()).is(Blocks.NETHER_BRICKS)) {
            Structure betterFortress = level.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(BETTER_FORTRESS);
            if (betterFortress != null && structureManager.getStructureAt(pos, betterFortress).isValid()) {
                cir.setReturnValue(true);
            }
        }
    }
}
