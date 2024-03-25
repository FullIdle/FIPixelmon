package com.pixelmonmod.pixelmon.comm.packetHandlers;

public enum EnumMovementType {
   Riding,
   Custom;

   public static EnumMovementType getFromOrdinal(int ordinal) {
      EnumMovementType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumMovementType m = var1[var3];
         if (m.ordinal() == ordinal) {
            return m;
         }
      }

      return null;
   }
}
