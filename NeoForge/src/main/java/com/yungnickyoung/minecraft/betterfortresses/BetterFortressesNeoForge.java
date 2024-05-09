package com.yungnickyoung.minecraft.betterfortresses;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(BetterFortressesCommon.MOD_ID)
public class BetterFortressesNeoForge {
    public static IEventBus loadingContextEventBus;

    public BetterFortressesNeoForge(IEventBus eventBus) {
        BetterFortressesNeoForge.loadingContextEventBus = eventBus;

        BetterFortressesCommon.init();
    }
}