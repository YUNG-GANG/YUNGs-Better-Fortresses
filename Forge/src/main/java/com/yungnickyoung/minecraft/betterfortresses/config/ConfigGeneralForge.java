package com.yungnickyoung.minecraft.betterfortresses.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneralForge {
    public final ForgeConfigSpec.ConfigValue<Boolean> disableVanillaFortresses;

    public ConfigGeneralForge(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # General settings.
                                ##########################################################################################################""")
                .push("General");

        disableVanillaFortresses = BUILDER
                .comment(
                        """
                        Whether or not vanilla Nether Fortresses should be disabled.
                        Default: true""".indent(1))
                .worldRestart()
                .define("Disable Vanilla Nether Fortresses", true);

        BUILDER.pop();
    }
}

