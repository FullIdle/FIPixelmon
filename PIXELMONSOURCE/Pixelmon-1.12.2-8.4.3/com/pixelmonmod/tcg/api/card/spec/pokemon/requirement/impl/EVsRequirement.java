package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class EVsRequirement extends AbstractIntegerPokemonRequirement {
   public EVsRequirement(Set keys, int defaultValue) {
      super(keys, defaultValue);
   }

   public EVsRequirement(Set keys, int defaultValue, int value) {
      super(keys, defaultValue, value);
   }

   public Requirement createInstance(Integer value) {
      return null;
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return false;
   }

   public void applyData(Pokemon object) {
   }
}
