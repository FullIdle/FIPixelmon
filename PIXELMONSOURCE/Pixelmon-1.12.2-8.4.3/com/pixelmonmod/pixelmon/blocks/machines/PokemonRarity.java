package com.pixelmonmod.pixelmon.blocks.machines;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;

public class PokemonRarity {
   public PokemonSpec pokemon;
   public int rarity;

   public PokemonRarity(PokemonSpec pokemon, int rarity) {
      this.pokemon = pokemon;
      this.rarity = rarity;
   }
}
