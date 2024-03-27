package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractRecursivePokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LogicalOrRequirement extends AbstractRecursivePokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"\b||\b", "\bor\b"});

   public LogicalOrRequirement() {
      super(KEYS);
   }

   public LogicalOrRequirement(List requirements) {
      super(KEYS, requirements);
   }

   public Requirement createInstance(List value) {
      return new LogicalOrRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      Iterator var2 = this.requirements.iterator();

      Requirement requirement;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         requirement = (Requirement)var2.next();
      } while(requirement.isDataMatch(pokemon));

      return true;
   }

   public boolean isMinecraftMatch(EntityPixelmon entityPixelmon) {
      Iterator var2 = this.requirements.iterator();

      Requirement requirement;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         requirement = (Requirement)var2.next();
      } while(requirement.isMinecraftMatch(entityPixelmon));

      return true;
   }

   public void applyData(Pokemon pokemon) {
   }

   public void applyMinecraft(EntityPixelmon entityPixelmon) {
   }
}