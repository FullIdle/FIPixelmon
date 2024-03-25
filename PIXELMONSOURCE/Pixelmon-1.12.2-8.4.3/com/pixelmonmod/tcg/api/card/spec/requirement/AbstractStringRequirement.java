package com.pixelmonmod.tcg.api.card.spec.requirement;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractStringRequirement extends AbstractRequirement {
   protected String value;
   protected String defaultValue;

   public AbstractStringRequirement(Set keys, String defaultValue) {
      super(keys);
      this.defaultValue = defaultValue;
   }

   public AbstractStringRequirement(Set keys, String defaultValue, String value) {
      this(keys, defaultValue);
      this.value = value;
   }

   public List createSimple(String key, String spec) {
      if (spec.startsWith(key + ":")) {
         String[] args = spec.split(key + ":");
         String value = args.length > 0 ? args[1] : this.defaultValue;
         return Collections.singletonList(this.createInstance(value));
      } else {
         return Collections.emptyList();
      }
   }

   public String getValue() {
      return this.value;
   }
}
