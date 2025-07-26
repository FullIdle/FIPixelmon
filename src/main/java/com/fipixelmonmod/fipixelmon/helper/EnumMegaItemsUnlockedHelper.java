package com.fipixelmonmod.fipixelmon.helper;

import com.fipixelmonmod.fipixelmon.bridge.EnumMegaItemsUnlockedBridge;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import net.minecraftforge.common.util.EnumHelper;

public class EnumMegaItemsUnlockedHelper {
    public static EnumMegaItemsUnlocked TERASTAL;
    public static EnumMegaItemsUnlocked BOTH_MEGA;
    public static EnumMegaItemsUnlocked BOTH_DYN;
    public static EnumMegaItemsUnlocked THREE;

    public static void inject() {
        TERASTAL = EnumHelper.addEnum(EnumMegaItemsUnlocked.class, "TERASTAL", new Class[0]);
        BOTH_MEGA = EnumHelper.addEnum(EnumMegaItemsUnlocked.class, "BOTH_MEGA", new Class[0]);
        BOTH_DYN = EnumHelper.addEnum(EnumMegaItemsUnlocked.class, "BOTH_DYN", new Class[0]);
        THREE = EnumHelper.addEnum(EnumMegaItemsUnlocked.class, "THREE", new Class[0]);
    }

    public static EnumMegaItemsUnlockedBridge castBridge(EnumMegaItemsUnlocked value) {
        return (EnumMegaItemsUnlockedBridge) (Object) value;
    }

    public static boolean canTerastal(EnumMegaItemsUnlocked value) {
        return castBridge(value).fIPixelmon$canTerastal();
    }
}
