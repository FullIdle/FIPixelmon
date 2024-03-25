package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;

public interface IMedicine {
   boolean useMedicine(PokemonLink var1, double var2);

   default boolean useMedicine(PokemonLink target) {
      return this.useMedicine(target, 1.0);
   }
}
