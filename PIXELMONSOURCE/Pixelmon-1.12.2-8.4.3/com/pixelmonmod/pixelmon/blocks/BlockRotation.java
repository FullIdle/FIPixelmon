package com.pixelmonmod.pixelmon.blocks;

public enum BlockRotation {
   Normal(0),
   CW(1),
   CCW(3),
   Rotate180(2);

   public int metadata;

   private BlockRotation(int metadata) {
      this.metadata = metadata;
   }

   public static BlockRotation getRotationFromMetadata(int metadata) {
      BlockRotation[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BlockRotation b = var1[var3];
         if (b.metadata == metadata) {
            return b;
         }
      }

      return tryForCloningMetadata(metadata);
   }

   /** @deprecated */
   @Deprecated
   private static BlockRotation tryForCloningMetadata(int metadata) {
      switch (metadata) {
         case 8:
            return Rotate180;
         case 9:
            return CW;
         case 10:
            return Normal;
         case 11:
            return CCW;
         default:
            return null;
      }
   }
}
