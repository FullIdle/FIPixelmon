package com.pixelmonmod.pixelmon.enums;

import net.minecraft.util.text.translation.I18n;

public enum EnumTrainerAI {
   StandStill,
   StillAndEngage,
   Wander,
   WanderAndEngage;

   public static EnumTrainerAI getFromOrdinal(int ordinal) {
      EnumTrainerAI[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumTrainerAI v = var1[var3];
         if (v.ordinal() == ordinal) {
            return v;
         }
      }

      return null;
   }

   public static EnumTrainerAI getNextMode(EnumTrainerAI mode) {
      int index = mode.ordinal();
      if (index == values().length - 1) {
         index = 0;
      } else {
         ++index;
      }

      return getFromOrdinal(index);
   }

   public static boolean hasTrainerAI(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }

   public boolean doesEngage() {
      return this == StillAndEngage || this == WanderAndEngage;
   }

   public String getLocalizedName() {
      return I18n.func_74838_a("enum.trainerAI." + this.name().toLowerCase());
   }
}
