package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class LegendaryRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"legendary", "islegendary", "legend", "leg"});

   public LegendaryRequirement() {
      super(KEYS);
   }

   public LegendaryRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new LegendaryRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return pokemon.isLegendary();
   }

   public void applyData(Pokemon pokemon) {
   }
}
