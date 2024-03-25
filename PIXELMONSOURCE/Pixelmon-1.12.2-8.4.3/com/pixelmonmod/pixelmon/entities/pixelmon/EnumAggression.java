package com.pixelmonmod.pixelmon.entities.pixelmon;

public enum EnumAggression {
   timid(0),
   passive(1),
   aggressive(2);

   public int index;

   private EnumAggression(int i) {
      this.index = i;
   }

   public static EnumAggression getAggression(int index) {
      EnumAggression[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumAggression a = var1[var3];
         if (a.index == index) {
            return a;
         }
      }

      return passive;
   }
}
