package com.pixelmonmod.pixelmon.entities.pokeballs;

public enum EnumPokeBallMode {
   empty,
   full,
   battle;

   public static EnumPokeBallMode getFromOrdinal(Integer integer) {
      EnumPokeBallMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumPokeBallMode m = var1[var3];
         if (m.ordinal() == integer) {
            return m;
         }
      }

      return null;
   }
}
