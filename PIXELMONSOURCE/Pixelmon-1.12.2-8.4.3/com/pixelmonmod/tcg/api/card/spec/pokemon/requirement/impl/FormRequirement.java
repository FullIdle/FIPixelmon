package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class FormRequirement extends AbstractIntegerPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"form", "f"});
   private static final int DEFAULT_VALUE = -1;

   public FormRequirement() {
      super(KEYS, -1);
   }

   public FormRequirement(int value) {
      super(KEYS, -1, value);
   }

   public Requirement createInstance(Integer value) {
      return new FormRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return pokemon.getForm() == this.value;
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setForm(pokemon.getSpecies().getFormEnum(this.value));
   }
}
