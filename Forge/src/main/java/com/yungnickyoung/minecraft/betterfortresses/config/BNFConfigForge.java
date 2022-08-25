package com.yungnickyoung.minecraft.betterfortresses.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BNFConfigForge {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ConfigGeneralForge general;

    static {
        BUILDER.push("YUNG's Better Nether Fortresses");

        general = new ConfigGeneralForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}