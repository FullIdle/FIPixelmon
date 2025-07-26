package com.fipixelmonmod.fipixelmon.helper;

import com.fipixelmonmod.fipixelmon.bridge.EnumMegaItemBridge;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import net.minecraftforge.common.util.EnumHelper;

public class EnumMegaItemHelper {
    public static EnumMegaItem TERASTAL;

    public static void inject(){
        TERASTAL = EnumHelper.addEnum(EnumMegaItem.class, "Terastal", new Class[]{int.class}, 2);
    }

    public static EnumMegaItemBridge castBridge(EnumMegaItem enumMegaItem) {
        return (EnumMegaItemBridge) (Object) enumMegaItem;
    }

    public static boolean isTerastal(EnumMegaItem enumMegaItem) {
        return castBridge(enumMegaItem).fIPixelmon$isTerastal();
    }
}
