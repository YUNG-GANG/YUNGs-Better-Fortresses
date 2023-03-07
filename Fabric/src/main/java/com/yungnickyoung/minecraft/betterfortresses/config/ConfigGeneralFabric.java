package com.yungnickyoung.minecraft.betterfortresses.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigGeneralFabric {
    @ConfigEntry.Gui.Tooltip
    public boolean disableVanillaFortresses = true;

    @ConfigEntry.Gui.Tooltip
    public int startMinY = 62;

    @ConfigEntry.Gui.Tooltip
    public int startMaxY = 82;
}
