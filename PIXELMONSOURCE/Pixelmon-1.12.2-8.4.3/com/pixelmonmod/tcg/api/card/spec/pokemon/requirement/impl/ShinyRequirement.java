package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class ShinyRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"shiny", "isshiny", "s"});

   public ShinyRequirement() {
      super(KEYS);
   }

   public ShinyRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new ShinyRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return pixelmon.isShiny() == this.value;
   }

   public void applyData(Pokemon pixelmon) {
      pixelmon.setShiny(this.value);
   }
}
