package com.pixelmonmod.pixelmon.enums;

public enum EnumMegaItem {
   Disabled(-1),
   None(-1),
   BraceletORAS(0),
   BoostNecklace(0),
   MegaGlasses(0),
   MegaAnchor(0),
   DynamaxBand(1),
   MegaTiara(0);

   private final int type;
   private static final EnumMegaItem[] VALUES = values();

   private EnumMegaItem(int type) {
      this.type = type;
   }

   public static EnumMegaItem getFromString(String megaItemString) {
      EnumMegaItem[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumMegaItem item = var1[var3];
         if (item.toString().equalsIgnoreCase(megaItemString)) {
            return item;
         }
      }

      return Disabled;
   }

   public static EnumMegaItem fromOrdinal(int ordinal) {
      return ordinal >= 0 && ordinal < VALUES.length ? VALUES[ordinal] : null;
   }

   /** @deprecated */
   @Deprecated
   public boolean canEvolve() {
      return this.canMega();
   }

   public boolean canMega() {
      return this.type == 0;
   }

   public boolean canDynamax() {
      return this.type == 1;
   }

   public boolean canSpecialEvolve() {
      return this.type != -1;
   }
}
