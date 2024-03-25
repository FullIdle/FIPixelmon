package com.pixelmonmod.tcg.api.card;

import java.util.Arrays;
import java.util.List;

public enum CardCondition {
   POISONED("Poisoned", new String[]{"POISON"}),
   PARALYZED("Paralyzed", new String[]{"PARALYSED", "PARALYSE", "PARALYZE"}),
   ASLEEP("Asleep", new String[]{"SLEEP"}),
   BURNT("Burnt", new String[]{"BURN"}),
   CONFUSED("Confused", new String[]{"CONFUSE"});

   private final String asString;
   private final List dbAliases;

   private CardCondition(String name, String[] dbAliases) {
      this.asString = name;
      this.dbAliases = Arrays.asList(dbAliases);
   }

   public String getName() {
      return this.asString;
   }

   public static CardCondition getFromDbString(String effect) {
      CardCondition[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CardCondition cardCondition = var1[var3];
         if (cardCondition.toString().equals(effect) || cardCondition.dbAliases.contains(effect)) {
            return cardCondition;
         }
      }

      return null;
   }
}
