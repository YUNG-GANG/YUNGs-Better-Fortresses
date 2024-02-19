package com.yungnickyoung.minecraft.betterfortresses.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name="betterfortresses-fabric-1_20_4")
public class BNFConfigFabric implements ConfigData {
    @ConfigEntry.Category("Better Nether Fortresses")
    @ConfigEntry.Gui.TransitiveObject
    public ConfigGeneralFabric general = new ConfigGeneralFabric();
}
