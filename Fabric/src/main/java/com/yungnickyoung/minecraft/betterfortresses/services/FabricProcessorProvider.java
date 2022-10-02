package com.yungnickyoung.minecraft.betterfortresses.services;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterfortresses.world.ItemFrameProcessor;
import com.yungnickyoung.minecraft.betterfortresses.world.PiglinProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public class FabricProcessorProvider implements IProcessorProvider {
    @Override
    public Codec<StructureProcessor> itemFrameProcessorCodec() {
        return ItemFrameProcessor.CODEC;
    }

    @Override
    public Codec<StructureProcessor> piglinProcessorCodec() {
        return PiglinProcessor.CODEC;
    }
}
