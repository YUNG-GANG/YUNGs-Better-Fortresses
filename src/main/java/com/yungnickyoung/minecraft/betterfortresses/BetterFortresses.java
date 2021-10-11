package com.yungnickyoung.minecraft.betterfortresses;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterfortresses.init.BFModConfig;
import com.yungnickyoung.minecraft.betterfortresses.init.BFModProcessors;
import com.yungnickyoung.minecraft.betterfortresses.init.BFModStructures;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(BetterFortresses.MOD_ID)
public class BetterFortresses {
    public static final String MOD_ID = "betterfortresses";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    /**
     * Lists of blacklisted biomes.
     * Will be reinitialized later w/ values from config.
     */
    public static List<String> blacklistedBiomes = Lists.newArrayList();

    public BetterFortresses() {
        init();
    }

    private void init() {
        BFModProcessors.init();
        BFModStructures.init();
        BFModConfig.init();
    }
}