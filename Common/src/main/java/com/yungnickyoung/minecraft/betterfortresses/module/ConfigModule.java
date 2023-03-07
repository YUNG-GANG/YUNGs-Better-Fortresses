package com.yungnickyoung.minecraft.betterfortresses.module;

public class ConfigModule {
    public General general = new General();

    public static class General {
        public boolean disableVanillaFortresses = true;
        public int startMinY = 62;
        public int startMaxY = 82;
    }
}
