package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractBooleanPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class UncatchableRequirement extends AbstractBooleanPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"uncatchable"});

   public UncatchableRequirement() {
      super(KEYS);
   }

   public UncatchableRequirement(boolean value) {
      super(KEYS, value);
   }

   public Requirement createInstance(Boolean value) {
      return new UncatchableRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return pokemon.getBonusStats().preventsCapture() == this.value;
   }

   public void applyData(Pokemon pokemon) {
   }

   public void applyMinecraft(EntityPixelmon entityPixelmon) {
      entityPixelmon.getPokemonData().getBonusStats().setPreventsCapture(this.value);
   }
}
