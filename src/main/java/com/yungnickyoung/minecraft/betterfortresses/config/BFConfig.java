package com.yungnickyoung.minecraft.betterfortresses.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BFConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> whitelistedDimensions;
    public static final ForgeConfigSpec.ConfigValue<String> blacklistedBiomes;

    static {
        BUILDER.push("YUNG's Better Fortresses");

        whitelistedDimensions = BUILDER
            .comment(
                " List of dimensions that will have Better Fortresses.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]\"\n" +
                " Default: \"[minecraft:the_nether]\"")
            .worldRestart()
            .define("Whitelisted Dimensions", "[minecraft:the_nether]");

        blacklistedBiomes = BUILDER
            .comment(
                " List of biomes that will NOT have Better Fortresses.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:plains, byg:alps]\"\n" +
                " Default: \"[]\"")
            .worldRestart()
            .define("Blacklisted Biomes", "[]");


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
