package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class UltraBeastRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"ultrabeast", "isultrabeast", "ub"});

   public UltraBeastRequirement() {
      super(KEYS);
   }

   public UltraBeastRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new UltraBeastRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return pixelmon.getSpecies().isUltraBeast() == this.value;
   }

   public void applyData(Pokemon pixelmon) {
   }
}