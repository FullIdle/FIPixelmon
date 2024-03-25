package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractStringPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class OriginalTrainerNameRequirement extends AbstractStringPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"originaltrainername", "otn"});
   private static final String DEFAULT_VALUE = "none";

   public OriginalTrainerNameRequirement() {
      super(KEYS, "none");
   }

   public OriginalTrainerNameRequirement(String value) {
      super(KEYS, "none", value);
   }

   public Requirement createInstance(String value) {
      return new OriginalTrainerNameRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return pixelmon.getOriginalTrainer() != null && pixelmon.getOriginalTrainer().equals(this.value);
   }

   public void applyData(Pokemon pixelmon) {
   }
}
