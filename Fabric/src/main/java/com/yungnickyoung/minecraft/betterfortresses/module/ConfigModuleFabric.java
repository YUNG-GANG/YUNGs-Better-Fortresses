package com.yungnickyoung.minecraft.betterfortresses.module;

import com.yungnickyoung.minecraft.betterfortresses.BetterFortressesCommon;
import com.yungnickyoung.minecraft.betterfortresses.config.BNFConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.world.InteractionResult;

public class ConfigModuleFabric {
    public static void init() {
        AutoConfig.register(BNFConfigFabric.class, Toml4jConfigSerializer::new);
        AutoConfig.getConfigHolder(BNFConfigFabric.class).registerSaveListener(ConfigModuleFabric::bakeConfig);
        AutoConfig.getConfigHolder(BNFConfigFabric.class).registerLoadListener(ConfigModuleFabric::bakeConfig);
        bakeConfig(AutoConfig.getConfigHolder(BNFConfigFabric.class).get());
    }

    private static InteractionResult bakeConfig(ConfigHolder<BNFConfigFabric> configHolder, BNFConfigFabric configFabric) {
        bakeConfig(configFabric);
        return InteractionResult.SUCCESS;
    }

    private static void bakeConfig(BNFConfigFabric configFabric) {
        BetterFortressesCommon.CONFIG.general.disableVanillaFortresses = configFabric.general.disableVanillaFortresses;
    }
}
