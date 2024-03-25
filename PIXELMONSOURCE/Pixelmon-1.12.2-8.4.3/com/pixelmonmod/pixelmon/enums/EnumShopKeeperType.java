package com.pixelmonmod.pixelmon.enums;

public enum EnumShopKeeperType {
   PokemartMain,
   PokemartSecond,
   Spawn;

   public static EnumShopKeeperType getFromString(String string) {
      EnumShopKeeperType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumShopKeeperType type = var1[var3];
         if (type.toString().equalsIgnoreCase(string)) {
            return type;
         }
      }

      return null;
   }
}
