package com.yungnickyoung.minecraft.betterfortresses.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BNFConfigNeoForge {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ConfigGeneralNeoForge general;

    static {
        BUILDER.push("YUNG's Better Nether Fortresses");

        general = new ConfigGeneralNeoForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}