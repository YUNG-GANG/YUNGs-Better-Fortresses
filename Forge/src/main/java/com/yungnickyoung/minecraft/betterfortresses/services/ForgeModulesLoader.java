package com.yungnickyoung.minecraft.betterfortresses.services;

import com.yungnickyoung.minecraft.betterfortresses.module.*;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules(); // Load common modules
        ConfigModuleForge.init();
    }
}
