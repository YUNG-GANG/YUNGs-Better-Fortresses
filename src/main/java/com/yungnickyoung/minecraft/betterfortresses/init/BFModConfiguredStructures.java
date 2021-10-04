package com.yungnickyoung.minecraft.betterfortresses.init;

import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class BFModConfiguredStructures {
    public static StructureFeature<?, ?> CONFIGURED_BETTER_FORTRESS = BFModStructures.BETTER_FORTRESS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
}
