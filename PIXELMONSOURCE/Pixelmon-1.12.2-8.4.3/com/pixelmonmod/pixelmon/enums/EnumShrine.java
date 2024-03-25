package com.pixelmonmod.pixelmon.enums;

public enum EnumShrine {
   Articuno,
   Zapdos,
   Moltres;

   public static boolean hasShrine(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
