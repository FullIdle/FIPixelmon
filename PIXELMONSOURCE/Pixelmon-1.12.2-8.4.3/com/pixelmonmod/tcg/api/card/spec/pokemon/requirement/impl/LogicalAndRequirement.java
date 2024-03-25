package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.impl;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.tcg.api.card.spec.pokemon.requirement.AbstractRecursivePokemonRequirement;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LogicalAndRequirement extends AbstractRecursivePokemonRequirement {
   private static final Set KEYS = Sets.newHashSet(new String[]{"\b&&\b", "\band\b"});

   public LogicalAndRequirement() {
      super(KEYS);
   }

   public LogicalAndRequirement(List requirements) {
      super(KEYS, requirements);
   }

   public Requirement createInstance(List value) {
      return new LogicalAndRequirement(value);
   }

   public boolean isDataMatch(Pokemon pokemon) {
      Iterator var2 = this.requirements.iterator();

      Requirement requirement;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         requirement = (Requirement)var2.next();
      } while(requirement.isDataMatch(pokemon));

      return false;
   }

   public boolean isMinecraftMatch(EntityPixelmon entityPixelmon) {
      Iterator var2 = this.requirements.iterator();

      Requirement requirement;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         requirement = (Requirement)var2.next();
      } while(requirement.isMinecraftMatch(entityPixelmon));

      return false;
   }

   public void applyData(Pokemon pokemon) {
   }

   public void applyMinecraft(EntityPixelmon entityPixelmon) {
   }
}
