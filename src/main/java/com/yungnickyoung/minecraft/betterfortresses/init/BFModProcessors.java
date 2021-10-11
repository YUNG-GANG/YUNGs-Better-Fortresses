package com.yungnickyoung.minecraft.betterfortresses.init;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortresses;
import com.yungnickyoung.minecraft.betterfortresses.world.BridgeLegProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BFModProcessors {
    public static IStructureProcessorType<BridgeLegProcessor> BRIDGE_LEG_PROCESSOR = () -> BridgeLegProcessor.CODEC;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BFModProcessors::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            register("bridge_leg_processor", BRIDGE_LEG_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterFortresses.MOD_ID, "bridge_leg_processor"), BRIDGE_LEG_PROCESSOR);
        });
    }

    private static IStructureProcessorType<?> register(String name, IStructureProcessorType<?> processor) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterFortresses.MOD_ID, name), processor);
    }
}
