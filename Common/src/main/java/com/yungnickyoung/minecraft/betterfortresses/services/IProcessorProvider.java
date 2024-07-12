package com.yungnickyoung.minecraft.betterfortresses.services;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public interface IProcessorProvider {
    MapCodec<StructureProcessor> itemFrameProcessorCodec();
}
