package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class SpeciesRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"species", "ndex"});
   private EnumSpecies species;

   public SpeciesRequirement() {
      super(KEYS);
   }

   public SpeciesRequirement(EnumSpecies species) {
      this();
      this.species = species;
   }

   public int getPriority() {
      return Integer.MAX_VALUE;
   }

   public List create(String spec) {
      String[] args = spec.split(" ");
      if (args != null && args.length >= 1) {
         Optional species = EnumSpecies.getFromName(args[0]);
         if (args[0].equalsIgnoreCase("random")) {
            return Lists.newArrayList(new Requirement[]{this.createInstance(EnumSpecies.randomPoke())});
         } else {
            return (List)(!species.isPresent() ? super.create(spec) : Lists.newArrayList(new Requirement[]{this.createInstance((EnumSpecies)species.get())}));
         }
      } else {
         return super.create(spec);
      }
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         if (args.length == 0) {
            return Collections.singletonList(this.createInstance(EnumSpecies.MissingNo));
         } else if (EnumSpecies.getFromNameAnyCaseNoTranslate(args[1]) != null) {
            return Collections.singletonList(this.createInstance(EnumSpecies.getFromNameAnyCaseNoTranslate(args[1])));
         } else {
            return (List)(args[0].equalsIgnoreCase("random") ? Lists.newArrayList(new Requirement[]{this.createInstance(EnumSpecies.randomPoke())}) : Collections.emptyList());
         }
      }
   }

   public Requirement createInstance(EnumSpecies value) {
      return new SpeciesRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return Objects.equals(pixelmon.getSpecies(), this.species);
   }

   public void applyData(Pokemon pixelmon) {
      pixelmon.setSpecies(this.species, true);
   }

   public EnumSpecies getValue() {
      return this.species;
   }

   public boolean fits(String spec) {
      String[] args = spec.split(" ");
      return !EnumSpecies.hasPokemon(args[0]) && !args[0].equalsIgnoreCase("random") ? super.fits(spec) : true;
   }

   public static SpeciesRequirement tryCreate(String spec) {
      return new SpeciesRequirement(EnumSpecies.getFromNameAnyCaseNoTranslate(spec));
   }
}
