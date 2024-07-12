package com.yungnickyoung.minecraft.betterfortresses;

import com.yungnickyoung.minecraft.betterfortresses.module.ConfigModuleNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(BetterFortressesCommon.MOD_ID)
public class BetterFortressesNeoForge {
    public static IEventBus loadingContextEventBus;

    public BetterFortressesNeoForge(IEventBus eventBus, ModContainer container) {
        BetterFortressesNeoForge.loadingContextEventBus = eventBus;

        BetterFortressesCommon.init();
        ConfigModuleNeoForge.init(container);
    }
}