package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.status.Burn;
import com.pixelmonmod.pixelmon.battles.status.Freeze;
import com.pixelmonmod.pixelmon.battles.status.NoStatus;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;
import com.pixelmonmod.pixelmon.battles.status.Poison;
import com.pixelmonmod.pixelmon.battles.status.PoisonBadly;
import com.pixelmonmod.pixelmon.battles.status.Sleep;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractStringPokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Set;

public class StatusRequirement extends AbstractStringPokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"status"});
   private static final String DEFAULT_VALUE = "none";

   public StatusRequirement() {
      super(KEYS, "none");
   }

   public StatusRequirement(String value) {
      super(KEYS, "none", value);
   }

   public Requirement createInstance(String value) {
      return new StatusRequirement(value);
   }

   public boolean isDataMatch(Pokemon pixelmon) {
      return getStatus(this.value).type == pixelmon.getStatus().type;
   }

   public void applyData(Pokemon pixelmon) {
      pixelmon.setStatus(getStatus(this.value));
   }

   private static StatusPersist getStatus(String name) {
      switch (name.toLowerCase()) {
         case "sleep":
            return new Sleep();
         case "burn":
            return new Burn();
         case "paralyzed":
         case "paralysis":
            return new Paralysis();
         case "frozen":
         case "freeze":
            return new Freeze();
         case "poison":
            return new Poison();
         case "poisonbadly":
         case "poison badly":
            return new PoisonBadly();
         case "drowsy":
         case "frostbite":
         case "frostbitten":
         default:
            return NoStatus.noStatus;
      }
   }
}
