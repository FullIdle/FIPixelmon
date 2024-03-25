package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class LevelRequirement extends AbstractIntegerPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"pokemonLevel", "l", "level", "lvl"});
   private static final int DEFAULT_VALUE = 1;

   public LevelRequirement() {
      super(KEYS, 1);
   }

   public LevelRequirement(int value) {
      super(KEYS, 1, value);
   }

   public Requirement createInstance(Integer value) {
      return new LevelRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return pokemon.getLevel() == this.value;
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setLevel(this.value);
   }
}
