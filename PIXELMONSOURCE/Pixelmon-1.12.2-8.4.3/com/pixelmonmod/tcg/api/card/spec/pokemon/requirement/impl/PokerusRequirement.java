package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Pokerus;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PokerusRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"pokerus", "pkrs"});
   private EnumPokerusType pokerus;

   public PokerusRequirement() {
      super(KEYS);
   }

   public PokerusRequirement(EnumPokerusType pokerus) {
      this();
      this.pokerus = pokerus;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         return args.length == 0 ? Collections.singletonList(this.createInstance(EnumPokerusType.UNINFECTED)) : Collections.singletonList(this.createInstance(EnumPokerusType.valueOf(args[1])));
      }
   }

   public Requirement createInstance(EnumPokerusType value) {
      return new PokerusRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return Objects.equals(pixelmon.getPokerus().type, this.pokerus);
   }

   public void applyData(Pokemon pixelmon) {
      pixelmon.setPokerus(new Pokerus(this.pokerus));
   }

   public EnumPokerusType getValue() {
      return this.pokerus;
   }
}
