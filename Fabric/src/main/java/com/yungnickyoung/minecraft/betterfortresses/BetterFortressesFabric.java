package com.yungnickyoung.minecraft.betterfortresses;

import net.fabricmc.api.ModInitializer;

public class BetterFortressesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterFortressesCommon.init();
    }
}
