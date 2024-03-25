package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;

public class TickingEvolution extends Evolution {
   public TickingEvolution() {
      super("ticking");
   }

   public boolean canEvolve(EntityPixelmon pokemon) {
      return super.canEvolve(pokemon);
   }
}
