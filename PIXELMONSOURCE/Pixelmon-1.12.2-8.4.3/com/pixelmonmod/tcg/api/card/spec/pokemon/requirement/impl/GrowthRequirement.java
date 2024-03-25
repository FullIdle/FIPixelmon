package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GrowthRequirement extends AbstractPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"growth", "gr"});
   private EnumGrowth growth;

   public GrowthRequirement() {
      super(KEYS);
   }

   public GrowthRequirement(EnumGrowth growth) {
      this();
      this.growth = growth;
   }

   public List createSimple(String key, String spec) {
      if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         if (args.length == 0) {
            return Collections.singletonList(this.createInstance(EnumGrowth.Ordinary));
         } else {
            EnumGrowth growth = this.parseGrowth(args[1]);
            return growth == null ? Collections.emptyList() : Collections.singletonList(this.createInstance(growth));
         }
      }
   }

   private EnumGrowth parseGrowth(String s) {
      try {
         return EnumGrowth.valueOf(s);
      } catch (Exception var5) {
         try {
            return EnumGrowth.values()[Integer.parseInt(s)];
         } catch (Exception var4) {
            return null;
         }
      }
   }

   public Requirement createInstance(EnumGrowth value) {
      return new GrowthRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return Objects.equals(pokemon.getGrowth(), this.growth);
   }

   public void applyData(Pokemon pokemon) {
      pokemon.setGrowth(this.growth);
   }

   public EnumGrowth getValue() {
      return this.growth;
   }
}
