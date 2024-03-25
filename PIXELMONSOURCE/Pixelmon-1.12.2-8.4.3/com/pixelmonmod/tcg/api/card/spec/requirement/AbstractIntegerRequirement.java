package com.pixelmonmod.tcg.api.card.spec.requirement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractIntegerRequirement extends AbstractRequirement {
   protected int value;
   protected int defaultValue;

   public AbstractIntegerRequirement(Set keys, int defaultValue) {
      super(keys);
      this.defaultValue = defaultValue;
   }

   public AbstractIntegerRequirement(Set keys, int defaultValue, int value) {
      this(keys, defaultValue);
      this.value = value;
   }

   public List createSimple(String key, String spec) {
      if (spec.startsWith(key + ":")) {
         String[] args = spec.split(key + ":");
         int value = args.length > 0 ? this.parseInt(args[1]) : this.defaultValue;
         return Collections.singletonList(this.createInstance(value));
      } else {
         return Collections.emptyList();
      }
   }

   private int parseInt(String s) {
      try {
         return Integer.parseInt(s);
      } catch (NumberFormatException var3) {
         return this.defaultValue;
      }
   }

   public Integer getValue() {
      return this.value;
   }
}
