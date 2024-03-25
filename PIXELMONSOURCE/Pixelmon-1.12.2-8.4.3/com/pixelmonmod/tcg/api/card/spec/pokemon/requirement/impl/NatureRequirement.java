package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NatureRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"nature", "n"});
   private EnumNature nature;

   public NatureRequirement() {
      super(KEYS);
   }

   public NatureRequirement(EnumNature nature) {
      this();
      this.nature = nature;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         return args.length != 0 && args.length != 1 ? Collections.singletonList(this.createInstance(EnumNature.getNatureFromIndex(Integer.parseInt(args[1])))) : Collections.singletonList(this.createInstance(EnumNature.Adamant));
      }
   }

   public Requirement createInstance(EnumNature value) {
      return new NatureRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return Objects.equals(pokemon.getNature(), this.nature);
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setNature(this.nature);
   }

   public EnumNature getValue() {
      return this.nature;
   }
}
