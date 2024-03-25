package com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.conditions;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class AbsenceOfHealthCondition extends EvoCondition {
   private int health;

   public AbsenceOfHealthCondition() {
      super("healthAbsence");
   }

   public boolean passes(EntityPixelmon pixelmon) {
      Pokemon pokemon = pixelmon.getPokemonData();
      int absence = pokemon.getMaxHealth() - pokemon.getHealth();
      return absence >= this.health;
   }

   public int getHealth() {
      return this.health;
   }
}
