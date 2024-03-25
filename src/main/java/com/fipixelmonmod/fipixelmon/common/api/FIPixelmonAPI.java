package com.fipixelmonmod.fipixelmon.common.api;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.ArrayList;

public class FIPixelmonAPI {
    public static ArrayList<EnumSpecies> getExtraEnumSpecies(){
        return Cache.extraEnumSpecies;
    }
    public static ArrayList<EnumSpecies> getExtraLegendEnumSpecies(){
        return Cache.extraLegendEnumSpecies;
    }
}
