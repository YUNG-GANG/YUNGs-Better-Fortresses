package com.yungnickyoung.minecraft.betterfortresses.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneralForge {
    public final ForgeConfigSpec.ConfigValue<Boolean> disableVanillaFortresses;
    public final ForgeConfigSpec.ConfigValue<Integer> startMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> startMaxY;

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

        startMinY = BUILDER
                .comment(
                        """
                        The minimum y-value at which the starting point of the fortress (the keep) can spawn.
                        Default: 62""".indent(1))
                .worldRestart()
                .define("Minimum Starting Height", 62);

        startMaxY = BUILDER
                .comment(
                        """
                        The maximum y-value at which the starting point of the fortress (the keep) can spawn.
                        Default: 82""".indent(1))
                .worldRestart()
                .define("Maximum Starting Height", 82);

        BUILDER.pop();
    }
}

