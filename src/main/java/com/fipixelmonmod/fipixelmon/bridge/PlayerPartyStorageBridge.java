package com.fipixelmonmod.fipixelmon.bridge;

import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;

public interface PlayerPartyStorageBridge {
    /**
     * <p>
     * 如果只有mega解锁了，{@link com.pixelmonmod.pixelmon.storage.PlayerPartyStorage#setMegaItemsUnlocked(EnumMegaItemsUnlocked)}
     * 会设置成 {@link com.fipixelmonmod.fipixelmon.helper.EnumMegaItemsUnlockedHelper#BOTH_MEGA}
     * </p>
     * <p>
     * 如果只有Dynamax解锁了，{@link com.pixelmonmod.pixelmon.storage.PlayerPartyStorage#setMegaItemsUnlocked(EnumMegaItemsUnlocked)}
     * 会设置成 {@link com.fipixelmonmod.fipixelmon.helper.EnumMegaItemsUnlockedHelper#BOTH_DYN}
     * </p>
     * <p>
     * 如果两个都解锁了，{@link com.pixelmonmod.pixelmon.storage.PlayerPartyStorage#setMegaItemsUnlocked(EnumMegaItemsUnlocked)}
     * 会设置成 {@link com.fipixelmonmod.fipixelmon.helper.EnumMegaItemsUnlockedHelper#THREE}
     * </p>
     * 都没有解锁时，则是 {@link com.fipixelmonmod.fipixelmon.helper.EnumMegaItemsUnlockedHelper#TERASTAL}
     */
    void fIPixelmon$unlockTerastal();
}
