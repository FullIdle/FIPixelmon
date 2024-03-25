package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;

public class HealFixed implements IHealHP {
   private int healAmount;

   public HealFixed(int healAmount) {
      this.healAmount = healAmount;
   }

   public int getHealAmount(PokemonLink pokemon) {
      return this.healAmount;
   }
}
