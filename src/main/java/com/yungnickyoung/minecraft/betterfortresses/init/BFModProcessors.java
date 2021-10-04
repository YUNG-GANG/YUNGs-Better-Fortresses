package com.yungnickyoung.minecraft.betterfortresses.init;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BFModProcessors {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BFModProcessors::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }
}
