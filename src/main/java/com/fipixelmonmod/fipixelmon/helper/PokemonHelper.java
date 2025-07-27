package com.fipixelmonmod.fipixelmon.helper;

import com.fipixelmonmod.fipixelmon.bridge.PokemonBridge;
import com.fipixelmonmod.fipixelmon.enums.EnumTeraType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class PokemonHelper {
    public static PokemonBridge castBridge(Pokemon pokemon){
        return (PokemonBridge) pokemon;
    }

    public static EnumTeraType getTeraType(Pokemon pokemon) {
        return castBridge(pokemon).fIPixelmon$getTeraType();
    }

    public static void setTeraType(Pokemon pokemon, EnumTeraType teraType) {
        castBridge(pokemon).fIPixelmon$setTeraType(teraType);
    }
}
