package com.pixelmonmod.pixelmon.enums;

import net.minecraft.util.text.translation.I18n;

public enum EnumOldGenMode {
   World,
   Dynamax,
   Mega,
   None,
   Both;

   public static EnumOldGenMode getFromIndex(int i) {
      EnumOldGenMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumOldGenMode e = var1[var3];
         if (e.ordinal() == i) {
            return e;
         }
      }

      return null;
   }

   public static EnumOldGenMode getNextMode(EnumOldGenMode mode) {
      int index = mode.ordinal();
      if (index == values().length - 1) {
         index = 0;
      } else {
         ++index;
      }

      EnumOldGenMode[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumOldGenMode e = var2[var4];
         if (e.ordinal() == index) {
            return e;
         }
      }

      return null;
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("enum.oldGen." + this.name().toLowerCase());
   }
}
