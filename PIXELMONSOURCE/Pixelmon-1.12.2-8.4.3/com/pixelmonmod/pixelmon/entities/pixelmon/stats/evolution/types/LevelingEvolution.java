package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions.EvoCondition;

public class LevelingEvolution extends Evolution {
   public Integer level = null;

   public LevelingEvolution() {
      super("leveling");
   }

   public LevelingEvolution(PokemonSpec to, Integer level, EvoCondition... conditions) {
      super("leveling", to, conditions);
      this.level = level;
      if (level == 1) {
         this.level = null;
      }

   }

   public int getLevel() {
      return this.level == null ? -1 : this.level;
   }

   public boolean canEvolve(EntityPixelmon pokemon, int level) {
      return level >= this.getLevel() && super.canEvolve(pokemon);
   }
}
