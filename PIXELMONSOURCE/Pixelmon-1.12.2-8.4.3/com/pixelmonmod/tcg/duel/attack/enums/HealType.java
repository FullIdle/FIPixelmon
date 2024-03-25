package com.pixelmonmod.tcg.duel.attack.enums;

public enum HealType {
   All,
   Damage,
   Conditions;

   public static HealType getFromDbString(String type) {
      switch (type.toUpperCase()) {
         case "*":
         case "ALL":
            return All;
         case "DAMAGE":
            return Damage;
         case "CONDITION":
         case "STATUS":
         case "CONDITIONS":
         case "STATUSES":
            return Conditions;
         default:
            return null;
      }
   }
}
