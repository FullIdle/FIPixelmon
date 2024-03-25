package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.EvoCondition;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class TradeEvolution extends Evolution {
   public EnumSpecies with = null;

   public TradeEvolution(PokemonSpec to, EnumSpecies with, EvoCondition... conditions) {
      super("trade", to, conditions);
      this.with = with;
   }

   public boolean canEvolve(EntityPixelmon pokemon, EnumSpecies with) {
      return (this.with == null || this.with == with) && super.canEvolve(pokemon);
   }
}
