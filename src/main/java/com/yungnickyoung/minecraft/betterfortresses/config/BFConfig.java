package com.yungnickyoung.minecraft.betterfortresses.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BFConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> blacklistedBiomes;
    public static final ForgeConfigSpec.ConfigValue<Boolean> isBlacklist;

    static {
        BUILDER.push("YUNG's Better Fortresses");

        blacklistedBiomes = BUILDER
            .comment(
                " List of biomes that will NOT have Better Fortresses.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:plains, byg:alps]\"\n" +
                " Default: \"[]\"")
            .worldRestart()
            .define("Blacklisted Biomes", "[]");

        isBlacklist = BUILDER
                .comment(
                    " Whether or not the Blacklisted Biomes setting will be a blacklist or whitelist.\n" +
                    " By default, the Blacklisted Biomes setting operates as a blacklist (hence the name).\n" +
                    " But if you set this to false, it will operate as a whitelist instead, so fortresses will only spawn\n" +
                    " in the specified biomes.\n" +
                    " Default: true")
                .worldRestart()
                .define("Is Blacklist", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
