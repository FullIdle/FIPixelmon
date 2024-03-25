package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GenderRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"gender", "g"});
   private Gender gender;

   public GenderRequirement() {
      super(KEYS);
   }

   public GenderRequirement(Gender gender) {
      this();
      this.gender = gender;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         return args.length == 0 ? Collections.singletonList(this.createInstance(Gender.Male)) : Collections.singletonList(this.createInstance(Gender.valueOf(args[1].toUpperCase())));
      }
   }

   public Requirement createInstance(Gender value) {
      return new GenderRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return Objects.equals(pokemon.getGender(), this.gender);
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setGender(this.gender);
   }

   public Gender getValue() {
      return this.gender;
   }
}
