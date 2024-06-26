package com.fipixelmonmod.fipixelmon.common.data;

import com.fipixelmonmod.fipixelmon.common.data.pokemon.EnumForm;
import com.fipixelmonmod.fipixelmon.common.data.pokemon.PokemonConfig;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cache {
    public static final ArrayList<EnumSpecies> extraEnumSpecies = new ArrayList<>();
    public static final ArrayList<EnumSpecies> extraLegendEnumSpecies = new ArrayList<>();
    public static final Map<EnumSpecies, PokemonConfig> extraPokemonConfig = new HashMap<>();
    public static final Map<EnumSpecies, EnumForm> extraForm = new HashMap<>();
    public static final Map<UUID, EnumGuiScreen> playerNowScreen = new HashMap<>();
}
