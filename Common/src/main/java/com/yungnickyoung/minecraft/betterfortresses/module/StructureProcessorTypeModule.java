package com.yungnickyoung.minecraft.betterfortresses.module;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.world.processor.LiquidBlockProcessor;
import com.yungnickyoung.minecraft.betterfortresses.world.processor.PillarProcessor;
import com.yungnickyoung.minecraft.betterfortresses.world.processor.StairPillarProcessor;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

@AutoRegister(BetterFortressesCommon.MOD_ID)
public class StructureProcessorTypeModule {
    @AutoRegister("pillar_processor")
    public static StructureProcessorType<PillarProcessor> PILLAR_PROCESSOR = () -> PillarProcessor.CODEC;

    @AutoRegister("stair_pillar_processor")
    public static StructureProcessorType<StairPillarProcessor> STAIR_PILLAR_PROCESSOR = () -> StairPillarProcessor.CODEC;

    @AutoRegister("liquid_block_processor")
    public static StructureProcessorType<LiquidBlockProcessor> LIQUID_BLOCK_PROCESSOR = () -> LiquidBlockProcessor.CODEC;
}
