package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumPrimal;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class PrimalEvolutionRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"canprimalevo", "canprimal", "canprimalrevert", "canprimalevolve"});

   public PrimalEvolutionRequirement() {
      super(KEYS);
   }

   public PrimalEvolutionRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new PrimalEvolutionRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      if (pokemon.getSpecies() == EnumSpecies.Groudon) {
         return pokemon.getFormEnum() == EnumPrimal.PRIMAL || pokemon.getHeldItemAsItemHeld() == PixelmonItemsHeld.redOrb;
      } else if (pokemon.getSpecies() != EnumSpecies.Kyogre) {
         return false;
      } else {
         return pokemon.getFormEnum() == EnumPrimal.PRIMAL || pokemon.getHeldItemAsItemHeld() == PixelmonItemsHeld.blueOrb;
      }
   }

   public void applyData(Pokemon pokemon) {
   }
}
