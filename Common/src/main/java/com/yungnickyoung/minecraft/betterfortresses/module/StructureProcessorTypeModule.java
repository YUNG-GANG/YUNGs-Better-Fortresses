package com.yungnickyoung.minecraft.betterfortresses.module;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.services.Services;
import com.yungnickyoung.minecraft.betterfortresses.world.processor.BridgeArchProcessor;
import com.yungnickyoung.minecraft.betterfortresses.world.processor.LiquidBlockProcessor;
import com.yungnickyoung.minecraft.betterfortresses.world.processor.NetherWartProcessor;
import com.yungnickyoung.minecraft.betterfortresses.world.processor.PillarProcessor;
import com.yungnickyoung.minecraft.betterfortresses.world.processor.StairPillarProcessor;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

@AutoRegister(BetterFortressesCommon.MOD_ID)
public class StructureProcessorTypeModule {
    @AutoRegister("pillar_processor")
    public static StructureProcessorType<PillarProcessor> PILLAR_PROCESSOR = () -> PillarProcessor.CODEC;

    @AutoRegister("stair_pillar_processor")
    public static StructureProcessorType<StairPillarProcessor> STAIR_PILLAR_PROCESSOR = () -> StairPillarProcessor.CODEC;

    @AutoRegister("bridge_arch_processor")
    public static StructureProcessorType<BridgeArchProcessor> BRIDGE_ARCH_PROCESSOR = () -> BridgeArchProcessor.CODEC;

    @AutoRegister("liquid_block_processor")
    public static StructureProcessorType<LiquidBlockProcessor> LIQUID_BLOCK_PROCESSOR = () -> LiquidBlockProcessor.CODEC;

    @AutoRegister("nether_wart_processor")
    public static StructureProcessorType<NetherWartProcessor> NETHER_WART_PROCESSOR = () -> NetherWartProcessor.CODEC;

    @AutoRegister("item_frame_processor")
    public static StructureProcessorType<StructureProcessor> ITEM_FRAME_PROCESSOR = Services.PROCESSORS::itemFrameProcessorCodec;

    @AutoRegister("piglin_processor")
    public static StructureProcessorType<StructureProcessor> PIGLIN_PROCESSOR = Services.PROCESSORS::itemFrameProcessorCodec;
}
