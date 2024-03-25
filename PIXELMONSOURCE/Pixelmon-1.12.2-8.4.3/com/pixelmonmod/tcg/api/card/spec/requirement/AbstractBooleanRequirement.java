package com.pixelmonmod.tcg.api.card.spec.requirement;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class AbstractBooleanRequirement extends AbstractRequirement {
   private static final Set FALSE_IDENTIFIERS = Sets.newHashSet(new String[]{"0", "false"});
   private static final Set TRUE_IDENTIFIERS = Sets.newHashSet(new String[]{"1", "true"});
   protected boolean value;

   public AbstractBooleanRequirement(Set keys) {
      super(keys);
   }

   public AbstractBooleanRequirement(Set keys, boolean value) {
      super(keys);
      this.value = value;
   }

   public List createSimple(String key, String spec) {
      if (spec.equalsIgnoreCase(key)) {
         return Collections.singletonList(this.createInstance(true));
      } else if (!spec.startsWith(key + ":")) {
         return Collections.emptyList();
      } else {
         String[] args = spec.split(key + ":");
         boolean invert = args.length > 0 && args[0].startsWith("!") || spec.startsWith("!");
         boolean value = args.length > 0 && this.parseBoolean(args[1]);
         return Collections.singletonList(this.createInstance(invert != value));
      }
   }

   protected boolean parseBoolean(String s) {
      return s != null && !FALSE_IDENTIFIERS.contains(s.toLowerCase()) ? TRUE_IDENTIFIERS.contains(s.toLowerCase()) : false;
   }

   public Boolean getValue() {
      return this.value;
   }
}
