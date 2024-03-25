package com.pixelmonmod.tcg.duel.attack.enums;

public enum EffectLocation {
   All,
   Active,
   Bench;

   public static EffectLocation getFromDbString(String parameter) {
      switch (parameter.toUpperCase()) {
         case "ACTIVE":
            return Active;
         case "BENCH":
            return Bench;
         case "*":
         case "ALL":
            return All;
         default:
            return null;
      }
   }
}
