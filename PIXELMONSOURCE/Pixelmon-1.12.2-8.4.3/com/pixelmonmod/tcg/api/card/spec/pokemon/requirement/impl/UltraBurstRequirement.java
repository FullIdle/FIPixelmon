package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class UltraBurstRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"canultraburst", "canultra"});

   public UltraBurstRequirement() {
      super(KEYS);
   }

   public UltraBurstRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new UltraBurstRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      if (pokemon.getSpecies() != EnumSpecies.Necrozma) {
         return false;
      } else {
         return (pokemon.getFormEnum() == EnumNecrozma.DUSK || pokemon.getFormEnum() == EnumNecrozma.DAWN) && pokemon.getHeldItem().func_77973_b() == PixelmonItemsHeld.ultranecrozium_z;
      }
   }

   public void applyData(Pokemon pokemon) {
   }
}
