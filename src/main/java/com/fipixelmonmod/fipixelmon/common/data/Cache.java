package com.fipixelmonmod.fipixelmon.common.data;

import com.fipixelmonmod.fipixelmon.common.data.pokemon.PokemonConfig;
import com.pixelmonmod.pixelmon.enums.EnumPokemonModel;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    public static final ArrayList<EnumSpecies> extraEnumSpecies = new ArrayList<>();
    public static final ArrayList<EnumSpecies> extraLegendEnumSpecies = new ArrayList<>();
    public static final Map<EnumSpecies, PokemonConfig> extraPokemonConfig = new HashMap<>();
}
