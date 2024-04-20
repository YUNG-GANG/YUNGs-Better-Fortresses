package com.yungnickyoung.minecraft.betterfortresses.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigGeneralNeoForge {
    public final ModConfigSpec.ConfigValue<Boolean> disableVanillaFortresses;

    public ConfigGeneralNeoForge(final ModConfigSpec.Builder BUILDER) {
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

