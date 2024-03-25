package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;

public class HealFraction implements IHealHP {
   private float healFraction;

   public HealFraction(float healFraction) {
      this.healFraction = healFraction;
   }

   public int getHealAmount(PokemonLink pokemon) {
      return (int)((float)pokemon.getMaxHealth(true) * this.healFraction);
   }
}
