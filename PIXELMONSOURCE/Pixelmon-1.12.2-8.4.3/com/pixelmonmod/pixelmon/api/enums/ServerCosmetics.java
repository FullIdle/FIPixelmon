package com.pixelmonmod.pixelmon.api.enums;

public enum ServerCosmetics {
   DROWNED_ROBE,
   ESPEON_SCARF,
   FLAREON_SCARF,
   GLACEON_SCARF,
   JOLTEON_SCARF,
   LEAFEON_SCARF,
   SYLVEON_SCARF,
   UMBREON_SCARF,
   VAPOREON_SCARF;

   public static ServerCosmetics get(String name) {
      ServerCosmetics[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ServerCosmetics cosmetic = var1[var3];
         if (cosmetic.name().equalsIgnoreCase(name)) {
            return cosmetic;
         }
      }

      return null;
   }
}
