package com.pixelmonmod.tcg.api.card.spec.requirement;

import com.google.common.collect.Lists;
import com.pixelmonmod.tcg.api.card.spec.SpecificationFactory;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractRecursiveRequirement implements Requirement {
   private final Set keys;
   private final Class clazz;
   protected List requirements;

   public AbstractRecursiveRequirement(Class clazz, Set keys) {
      this.clazz = clazz;
      this.keys = keys;
   }

   public AbstractRecursiveRequirement(Class clazz, Set keys, List requirements) {
      this(clazz, keys);
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
            this.requirements.addAll(SpecificationFactory.requirements(this.clazz, arg));
         }

         return Collections.singletonList(this.createInstance(requirements));
      }
   }

   public List getValue() {
      return this.requirements;
   }
}
