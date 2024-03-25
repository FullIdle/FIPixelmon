package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.enums.items.EnumApricorns;

public enum EnumApricornTrees {
   Black(0, EnumApricorns.Black),
   White(1, EnumApricorns.White),
   Pink(2, EnumApricorns.Pink),
   Green(3, EnumApricorns.Green),
   Blue(4, EnumApricorns.Blue),
   Yellow(5, EnumApricorns.Yellow),
   Red(6, EnumApricorns.Red);

   public EnumApricorns apricorn;
   public int id;

   private EnumApricornTrees(int id, EnumApricorns apricorn) {
      this.id = id;
      this.apricorn = apricorn;
   }

   public static EnumApricornTrees getFromID(int id) {
      EnumApricornTrees[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumApricornTrees e = var1[var3];
         if (e.id == id) {
            return e;
         }
      }

      return null;
   }

   public static boolean hasApricornTree(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
