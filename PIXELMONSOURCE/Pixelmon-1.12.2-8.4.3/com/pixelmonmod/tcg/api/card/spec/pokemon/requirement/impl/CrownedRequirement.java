package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumHeroDuo;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class CrownedRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"cancrowned"});

   public CrownedRequirement() {
      super(KEYS);
   }

   public CrownedRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new CrownedRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return pokemon.getFormEnum() == EnumHeroDuo.HERO && (pokemon.getSpecies() == EnumSpecies.Zacian && pokemon.getHeldItemAsItemHeld() == PixelmonItemsHeld.rustedSword || pokemon.getSpecies() == EnumSpecies.Zamazenta && pokemon.getHeldItemAsItemHeld() == PixelmonItemsHeld.rustedShield);
   }

   public void applyData(Pokemon pokemon) {
   }
}
