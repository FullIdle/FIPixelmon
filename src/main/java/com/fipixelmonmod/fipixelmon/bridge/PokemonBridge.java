package com.fipixelmonmod.fipixelmon.bridge;

import com.fipixelmonmod.fipixelmon.enums.EnumTeraType;

public interface PokemonBridge {
    /**
     * 需要标记给服务器发送更新哦~
     */
    void fIPixelmon$setTeraType(EnumTeraType teraType);
    EnumTeraType fIPixelmon$getTeraType();
}
