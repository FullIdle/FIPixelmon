package com.pixelmonmod.tcg.api.card.spec.requirement;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractRequirement implements Requirement {
   protected final Set keys;

   public AbstractRequirement(Set keys) {
      this.keys = keys;
   }

   public List getAliases() {
      return Lists.newArrayList(this.keys);
   }

   public boolean fits(String spec) {
      String[] var2 = spec.split(" ");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String subSpec = var2[var4];
         Iterator var6 = this.keys.iterator();

         while(var6.hasNext()) {
            String key = (String)var6.next();
            if (subSpec.startsWith(key + ":") || subSpec.equalsIgnoreCase(key)) {
               return true;
            }
         }
      }

      return false;
   }

   public List create(String spec) {
      String[] var2 = spec.split(" ");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String subSpec = var2[var4];
         Iterator var6 = this.keys.iterator();

         while(var6.hasNext()) {
            String key = (String)var6.next();
            if (subSpec.startsWith(key + ":") || subSpec.equalsIgnoreCase(key)) {
               return this.createSimple(key, subSpec);
            }
         }
      }

      return Collections.emptyList();
   }

   public abstract List createSimple(String var1, String var2);
}
