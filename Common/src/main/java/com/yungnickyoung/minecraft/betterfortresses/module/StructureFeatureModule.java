package com.yungnickyoung.minecraft.betterfortresses.module;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.world.structure.BetterNetherFortressStructure;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

@AutoRegister(BetterFortressesCommon.MOD_ID)
public class StructureFeatureModule {
    @AutoRegister("fortress")
    public static StructureFeature<YungJigsawConfig> NETHER_FORTRESS = new BetterNetherFortressStructure();
}
