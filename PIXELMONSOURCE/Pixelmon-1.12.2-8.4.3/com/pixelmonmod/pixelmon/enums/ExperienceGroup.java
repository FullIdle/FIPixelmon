package com.pixelmonmod.pixelmon.enums;

public enum ExperienceGroup {
   Erratic,
   Fast,
   MediumFast,
   MediumSlow,
   Slow,
   Fluctuating;

   private static final ExperienceGroup[] VALUES = values();

   public static boolean hasExperienceGroup(String group) {
      ExperienceGroup[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ExperienceGroup g = var1[var3];
         if (g.name().equalsIgnoreCase(group)) {
            return true;
         }
      }

      return false;
   }

   public static ExperienceGroup getExperienceGroupFromString(String group) {
      ExperienceGroup[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ExperienceGroup g = var1[var3];
         if (g.name().equalsIgnoreCase(group)) {
            return g;
         }
      }

      return null;
   }
}
