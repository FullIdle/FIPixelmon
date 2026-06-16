package com.fipixelmonmod.fipixelmon;

public class Config {
    public static Config INSTANCE;

    public Config() {
        INSTANCE = this;
    }

    public boolean shieldingTCGOverlay = false;

    public boolean shieldingOverlay = false;
}
