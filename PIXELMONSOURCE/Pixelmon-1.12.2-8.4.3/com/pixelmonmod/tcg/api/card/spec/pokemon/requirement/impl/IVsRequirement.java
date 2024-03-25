package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class IVsRequirement extends AbstractIntegerPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{""});
   private static final int DEFAULT_VALUE = 0;

   public IVsRequirement() {
      super(KEYS, 0);
   }

   public IVsRequirement(int value) {
      super(KEYS, 0, value);
   }

   public Requirement createInstance(Integer value) {
      return new IVsRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return false;
   }

   public void applyData(Pokemon pokemon) {
   }
}
