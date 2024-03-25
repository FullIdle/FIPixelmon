package com.pixelmonmod.pixelmon.quests.util;

public enum PosAlignment {
   Relative,
   Random,
   Rotational,
   Absolute;

   public static PosAlignment getForName(String s) {
      PosAlignment[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PosAlignment alignment = var1[var3];
         if (alignment.name().equalsIgnoreCase(s)) {
            return alignment;
         }
      }

      return Relative;
   }
}
