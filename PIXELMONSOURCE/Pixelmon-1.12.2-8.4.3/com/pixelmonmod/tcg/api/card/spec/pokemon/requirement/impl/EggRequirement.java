package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class EggRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"egg"});

   public EggRequirement() {
      super(KEYS);
   }

   public EggRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new EggRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return pokemon.isEgg() == this.value;
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setEggCycles(3);
      pokemon.setEggSteps(0);
   }
}
