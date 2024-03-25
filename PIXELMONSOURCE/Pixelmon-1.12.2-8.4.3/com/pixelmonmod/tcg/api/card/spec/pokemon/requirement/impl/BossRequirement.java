package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractIntegerPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class BossRequirement extends AbstractIntegerPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"boss", "isboss", "b"});
   private static final int DEFAULT_VALUE = -1;

   public BossRequirement() {
      super(KEYS, -1);
   }

   public BossRequirement(int value) {
      super(KEYS, -1, value);
   }

   public Requirement createInstance(Integer value) {
      return new BossRequirement(value > 8 ? 0 : value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      return false;
   }

   public void applyData(Pokemon pokemon) {
   }

   public boolean isMinecraftMatch(EntityPixelmon entityPixelmon) {
      if (this.value == -1) {
         return entityPixelmon.getBossMode() != null && entityPixelmon.getBossMode().isBossPokemon();
      } else {
         return entityPixelmon.getBossMode() != null && entityPixelmon.getBossMode().ordinal() == this.value;
      }
   }

   public void applyMinecraft(EntityPixelmon entityPixelmon) {
      if (this.value != -1) {
         entityPixelmon.setBoss(EnumBossMode.values()[this.value]);
      }
   }
}
