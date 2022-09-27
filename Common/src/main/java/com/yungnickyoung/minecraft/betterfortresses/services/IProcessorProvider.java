package com.yungnickyoung.minecraft.betterfortresses.services;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public interface IProcessorProvider {
    Codec<StructureProcessor> itemFrameProcessorCodec();
}
