package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class GigantamaxFactorRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"gmaxfactor"});

   public GigantamaxFactorRequirement() {
      super(KEYS);
   }

   public GigantamaxFactorRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new GigantamaxFactorRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return pokemon.canGigantamax() == this.value;
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setGigantamaxFactor(this.value);
   }
}
