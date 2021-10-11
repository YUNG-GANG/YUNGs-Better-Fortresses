package com.yungnickyoung.minecraft.betterfortresses.init;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterfortresses.BetterFortresses;
import com.yungnickyoung.minecraft.betterfortresses.config.BFConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BFModConfig {
    public static void init() {
        // Register mod config with Forge
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, BFConfig.SPEC, "betterfortresses-forge-1_16.toml");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BFModConfig::configChanged);
    }

    /**
     * Parses the whitelisted dimensions & blacklisted biomes strings and updates the stored values.
     */
    public static void configChanged(ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();

        if (config.getSpec() == BFConfig.SPEC) {
            // Biome blacklisting
            String rawStringofList = BFConfig.blacklistedBiomes.get();
            int strLen = rawStringofList.length();

            // Validate the string's format
            if (strLen < 2 || rawStringofList.charAt(0) != '[' || rawStringofList.charAt(strLen - 1) != ']') {
                BetterFortresses.LOGGER.error("INVALID VALUE FOR SETTING 'Blacklisted Biomes'. Using default instead...");
                BetterFortresses.blacklistedBiomes = Lists.newArrayList();
                return;
            }

            // Parse string to list
            BetterFortresses.blacklistedBiomes = Lists.newArrayList(rawStringofList.substring(1, strLen - 1).split(",\\s*"));
        }
    }
}
