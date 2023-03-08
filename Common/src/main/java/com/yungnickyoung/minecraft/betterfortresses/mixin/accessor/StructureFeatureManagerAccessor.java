package com.yungnickyoung.minecraft.betterfortresses.mixin.accessor;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureFeatureManager.class)
public interface StructureFeatureManagerAccessor {
    @Accessor
    LevelAccessor getLevel();
}
