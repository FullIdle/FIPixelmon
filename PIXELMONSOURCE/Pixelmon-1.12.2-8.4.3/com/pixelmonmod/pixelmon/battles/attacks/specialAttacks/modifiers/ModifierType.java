package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.modifiers;

public enum ModifierType {
   Chance,
   User,
   Awake;

   public static ModifierType getModifierType(String string) {
      ModifierType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ModifierType t = var1[var3];
         if (t.toString().equalsIgnoreCase(string)) {
            return t;
         }
      }

      return null;
   }
}
