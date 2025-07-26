package com.fipixelmonmod.fipixelmon.helper;

import com.fipixelmonmod.fipixelmon.bridge.PlayerPartyStorageBridge;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;

public class PlayerPartyStorageHelper {
    public static PlayerPartyStorageBridge castBridge(PlayerPartyStorage storage) {
        return (PlayerPartyStorageBridge) storage;
    }

    public static void unlockTerastal(PlayerPartyStorage party) {
        castBridge(party).fIPixelmon$unlockTerastal();
    }
}
