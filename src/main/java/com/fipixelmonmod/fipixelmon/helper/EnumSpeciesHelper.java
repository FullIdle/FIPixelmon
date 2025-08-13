package com.fipixelmonmod.fipixelmon.helper;

import com.fipixelmonmod.fipixelmon.bridge.EnumSpeciesBridge;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class EnumSpeciesHelper {
    public static EnumSpeciesBridge castBridge(EnumSpecies species) {
        return (EnumSpeciesBridge) (Object) species;
    }

    public static void setName(EnumSpecies species, String name) {
        castBridge(species).fIPixelmon$setName(name);
    }

    public static void setDex(EnumSpecies species, int dex) {
        castBridge(species).fIPixelmon$setDex(dex);
    }

    public static boolean isCreatedFIP(EnumSpecies species) {
        return castBridge(species).fIPixelmon$isCreatedInFIP();
    }
}
