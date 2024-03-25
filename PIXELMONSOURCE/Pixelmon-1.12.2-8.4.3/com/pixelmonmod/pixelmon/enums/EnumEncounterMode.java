package com.pixelmonmod.pixelmon.enums;

import net.minecraft.util.text.translation.I18n;

public enum EnumEncounterMode {
   Once,
   OncePerPlayer,
   OncePerMCDay,
   OncePerDay,
   Unlimited;

   public boolean isTimedAccess() {
      return this == OncePerDay || this == OncePerMCDay;
   }

   public static EnumEncounterMode getFromIndex(int i) {
      EnumEncounterMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumEncounterMode e = var1[var3];
         if (e.ordinal() == i) {
            return e;
         }
      }

      return null;
   }

   public static EnumEncounterMode getNextMode(EnumEncounterMode mode) {
      int index = mode.ordinal();
      if (index == values().length - 1) {
         index = 0;
      } else {
         ++index;
      }

      EnumEncounterMode[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumEncounterMode e = var2[var4];
         if (e.ordinal() == index) {
            return e;
         }
      }

      return null;
   }

   public static boolean hasEncounterMode(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("enum.trainerEncounter." + this.name().toLowerCase());
   }
}
