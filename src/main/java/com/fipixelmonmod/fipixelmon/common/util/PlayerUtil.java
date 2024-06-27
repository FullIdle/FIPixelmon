package com.fipixelmonmod.fipixelmon.common.util;

import com.fipixelmonmod.fipixelmon.common.data.Cache;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;

import java.util.UUID;

public class PlayerUtil {
    public static EnumGuiScreen getNowGuiScreen(UUID uuid){
        return Cache.playerNowScreen.get(uuid);
    }
}
