package com.yungnickyoung.minecraft.betterfortresses.module;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

public class TagModule {
    public static final ResourceKey<Structure> BETTER_NETHER_FORTRESS = ResourceKey.create(Registry.STRUCTURE_REGISTRY,
            new ResourceLocation(BetterFortressesCommon.MOD_ID, "fortress"));
}
