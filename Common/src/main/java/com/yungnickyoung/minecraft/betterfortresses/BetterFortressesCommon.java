package com.yungnickyoung.minecraft.betterfortresses;

import com.yungnickyoung.minecraft.betterfortresses.module.ConfigModule;
import com.yungnickyoung.minecraft.betterfortresses.services.Services;
import com.yungnickyoung.minecraft.yungsapi.api.YungAutoRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterFortressesCommon {
    public static final String MOD_ID = "betterfortresses";
    public static final String MOD_NAME = "YUNG's Better Nether Fortresses";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ConfigModule CONFIG = new ConfigModule();

    public static void init() {
        YungAutoRegister.scanPackageForAnnotations("com.yungnickyoung.minecraft.betterfortresses.module");
        Services.MODULES.loadModules();
    }
}
