package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class UntradeableRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"untradeable"});

   public UntradeableRequirement() {
      super(KEYS);
   }

   public UntradeableRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new UntradeableRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return pixelmon.hasSpecFlag("untradeable") == this.value;
   }

   public void applyData(Pokemon pixelmon) {
      if (this.value) {
         pixelmon.addSpecFlag("untradeable");
      } else {
         pixelmon.removeSpecFlag("untradeable");
      }

   }
}
