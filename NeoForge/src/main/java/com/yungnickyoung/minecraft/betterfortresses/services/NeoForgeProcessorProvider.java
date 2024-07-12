package com.yungnickyoung.minecraft.betterfortresses.services;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.betterfortresses.world.ItemFrameProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public class NeoForgeProcessorProvider implements IProcessorProvider {
    @Override
    public MapCodec<StructureProcessor> itemFrameProcessorCodec() {
        return ItemFrameProcessor.CODEC;
    }
}
