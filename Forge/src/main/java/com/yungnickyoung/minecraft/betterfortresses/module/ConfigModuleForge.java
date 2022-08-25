package com.yungnickyoung.minecraft.betterfortresses.module;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.config.BNFConfigForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ConfigModuleForge {
    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BNFConfigForge.SPEC, "betterfortresses-forge-1_19.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BNFConfigForge.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        BetterFortressesCommon.CONFIG.general.disableVanillaFortresses = BNFConfigForge.general.disableVanillaFortresses.get();
    }
}
