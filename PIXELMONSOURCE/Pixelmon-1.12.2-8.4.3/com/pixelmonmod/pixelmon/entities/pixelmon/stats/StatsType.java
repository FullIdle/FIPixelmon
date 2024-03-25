package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.util.ITranslatable;

public enum StatsType implements ITranslatable {
   None,
   HP,
   Attack,
   Defence,
   SpecialAttack,
   SpecialDefence,
   Speed,
   Accuracy,
   Evasion;

   private static final StatsType[] STATS = new StatsType[]{HP, Attack, Defence, SpecialAttack, SpecialDefence, Speed};

   public static boolean isStatsEffect(String effectTypeString) {
      StatsType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         StatsType t = var1[var3];
         if (effectTypeString.equalsIgnoreCase(t.toString())) {
            return true;
         }
      }

      return false;
   }

   public static StatsType getStatsEffect(String effectTypeString) {
      StatsType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         StatsType t = var1[var3];
         if (effectTypeString.equalsIgnoreCase(t.toString())) {
            return t;
         }
      }

      return null;
   }

   public int getStatIndex() {
      switch (this) {
         case Accuracy:
            return 0;
         case Evasion:
            return 1;
         case Attack:
            return 2;
         case Defence:
            return 3;
         case SpecialAttack:
            return 4;
         case SpecialDefence:
            return 5;
         case Speed:
            return 6;
         default:
            return -1;
      }
   }

   public String getUnlocalizedName() {
      return this == None ? "" : "enum.stat." + this.name().toLowerCase();
   }

   public static StatsType[] getStatValues() {
      return STATS;
   }

   public static StatsType getRandomBattleMutableStat() {
      return STATS[RandomHelper.getRandomNumberBetween(1, 5)];
   }
}
