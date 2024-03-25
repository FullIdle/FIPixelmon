package com.pixelmonmod.tcg.api.card.spec.pokemon.requirement;

import com.google.common.collect.Lists;
import com.pixelmonmod.tcg.api.card.spec.SpecificationFactory;
import com.pixelmonmod.tcg.api.card.spec.pokemon.PokemonSpecification;
import com.pixelmonmod.tcg.api.card.spec.requirement.Requirement;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractRecursivePokemonRequirement implements Requirement {
   private final Set keys;
   protected List requirements;

   public AbstractRecursivePokemonRequirement(Set keys) {
      this.keys = keys;
   }

   public AbstractRecursivePokemonRequirement(Set keys, List requirements) {
      this(keys);
      this.requirements = requirements;
   }

   public List getAliases() {
      return Lists.newArrayList(this.keys);
   }

   public int getPriority() {
      return 10;
   }

   public boolean shouldContinue() {
      return false;
   }

   public boolean fits(String spec) {
      Iterator var2 = this.keys.iterator();

      String key;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         key = (String)var2.next();
      } while(!spec.contains(key));

      return true;
   }

   public List create(String spec) {
      Iterator var2 = this.keys.iterator();

      String key;
      do {
         if (!var2.hasNext()) {
            return Collections.emptyList();
         }

         key = (String)var2.next();
      } while(!spec.contains(key));

      String[] args = spec.split(key);
      if (args.length == 0) {
         return Collections.emptyList();
      } else {
         List requirements = Lists.newArrayList();
         String[] var6 = args;
         int var7 = args.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String arg = var6[var8];
            this.requirements.addAll(SpecificationFactory.requirements(PokemonSpecification.class, arg));
         }

         return Collections.singletonList(this.createInstance(requirements));
      }
   }

   public List getValue() {
      return this.requirements;
   }
}
