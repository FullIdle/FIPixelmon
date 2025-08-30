package com.fipixelmonmod.fipixelmon.bridge;

import com.fipixelmonmod.fipixelmon.data.PokemonConfig;

/**
 * @see com.fipixelmonmod.fipixelmon.mixin.pixelmon.MixinEnumSpecies
 * @see com.pixelmonmod.pixelmon.enums.EnumSpecies
 */
public interface EnumSpeciesBridge {
    void fIPixelmon$setName(String name);
    void fIPixelmon$setDex(int dex);
    boolean fIPixelmon$isCreatedInFIP();
    PokemonConfig fIPixelmon$getPokemonConfig();
}
