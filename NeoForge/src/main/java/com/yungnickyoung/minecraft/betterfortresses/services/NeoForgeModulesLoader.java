package com.yungnickyoung.minecraft.betterfortresses.services;

import com.yungnickyoung.minecraft.betterfortresses.module.*;

public class NeoForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules(); // Load common modules
        ConfigModuleNeoForge.init();
    }
}
