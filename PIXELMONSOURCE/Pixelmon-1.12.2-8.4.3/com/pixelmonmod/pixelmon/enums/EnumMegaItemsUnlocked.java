package com.pixelmonmod.pixelmon.enums;

import net.minecraft.util.text.translation.I18n;

public enum EnumMegaItemsUnlocked {
   None,
   Mega,
   Dynamax,
   Both;

   private static final EnumMegaItemsUnlocked[] VALUES = values();

   public static EnumMegaItemsUnlocked getFromString(String str) {
      EnumMegaItemsUnlocked[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumMegaItemsUnlocked item = var1[var3];
         if (item.toString().equalsIgnoreCase(str)) {
            return item;
         }
      }

      return None;
   }

   public static EnumMegaItemsUnlocked fromOrdinal(int ordinal) {
      return ordinal >= 0 && ordinal < VALUES.length ? VALUES[ordinal] : null;
   }

   public boolean isNone() {
      return this == None;
   }

   public boolean canMega() {
      return this == Mega || this == Both;
   }

   public boolean canDynamax() {
      return this == Dynamax || this == Both;
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("enum.megaItems." + this.name().toLowerCase());
   }
}
