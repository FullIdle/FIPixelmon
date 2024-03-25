package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.tcg.api.card.spec.SpecificationFactory;
import com.pixelmonmod.tcg.api.card.spec.pokemon.PokemonSpecification;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractStringPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.List;
import java.util.Set;

public class HasSpecFlagRequirement extends AbstractStringPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"specflag", "flag"});
   private static final String DEFAULT_VALUE = "untradeable";

   public HasSpecFlagRequirement() {
      super(KEYS, "untradeable");
   }

   public HasSpecFlagRequirement(String value) {
      super(KEYS, "untradeable", value);
   }

   public List createSimple(String key, String spec) {
      return SpecificationFactory.requirements(PokemonSpecification.class, spec);
   }

   public Requirement createInstance(String value) {
      return new HasSpecFlagRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return pokemon.hasSpecFlag(this.value);
   }

   public void applyData(Pokemon pokemon) {
      pokemon.addSpecFlag(this.value);
   }
}
