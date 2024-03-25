package com.pixelmonmod.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import java.util.UUID;

public class ReforgedPokemonFactory extends PokemonFactory {
   public Pokemon create(UUID pokemonUUID) {
      return this.createDefault(pokemonUUID);
   }
}
