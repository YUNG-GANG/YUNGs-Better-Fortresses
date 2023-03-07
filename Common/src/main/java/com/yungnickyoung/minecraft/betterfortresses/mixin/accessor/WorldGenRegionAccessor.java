package com.yungnickyoung.minecraft.betterfortresses.mixin.accessor;

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(WorldGenRegion.class)
public interface WorldGenRegionAccessor {
    @Accessor
    List<ChunkAccess> getCache();

    @Accessor
    ChunkPos getFirstPos();

    @Accessor
    int getSize();

    @Accessor
    StructureFeatureManager getStructureFeatureManager();
}