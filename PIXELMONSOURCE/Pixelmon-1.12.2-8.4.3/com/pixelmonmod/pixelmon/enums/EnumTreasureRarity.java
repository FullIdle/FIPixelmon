package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

public enum EnumTreasureRarity {
   COMMON(0.8F),
   UNCOMMON(0.6F),
   UNLIKELY(0.4F),
   SEMI_RARE(0.2F),
   RARE(0.1F),
   VERY_RARE(0.05F),
   SUPER_RARE(0.01F),
   ULTRA_RARE(0.005F),
   ALWAYS(1.0F),
   NEVER(0.0F);

   public static final EnumTreasureRarity[] STANDARD_RARITIES = new EnumTreasureRarity[]{COMMON, UNCOMMON, UNLIKELY, SEMI_RARE, RARE, VERY_RARE, SUPER_RARE, ULTRA_RARE};
   public static final float[] midPoints = new float[]{UNCOMMON.average(COMMON), UNLIKELY.average(UNCOMMON), SEMI_RARE.average(UNLIKELY), RARE.average(SEMI_RARE), VERY_RARE.average(RARE), SUPER_RARE.average(VERY_RARE), ULTRA_RARE.average(SUPER_RARE)};
   public final float chance;

   private EnumTreasureRarity(float chance) {
      this.chance = chance;
   }

   public static EnumTreasureRarity round(float chance) {
      for(int i = 0; i < midPoints.length; ++i) {
         if (chance >= midPoints[i]) {
            return values()[i];
         }
      }

      return ULTRA_RARE;
   }

   public static float estimateRarity(EnumPokeballs ballType) {
      switch (ballType) {
         case PokeBall:
            return COMMON.chance;
         case GreatBall:
            return UNCOMMON.chance;
         case UltraBall:
            return UNLIKELY.chance;
         case MasterBall:
            return ULTRA_RARE.chance;
         case PremierBall:
            return COMMON.average(UNCOMMON);
         default:
            return UNLIKELY.chance;
      }
   }

   public float average(EnumTreasureRarity other) {
      return (this.chance + other.chance) / 2.0F;
   }

   public float average(float other) {
      return (this.chance + other) / 2.0F;
   }

   public static boolean hasTreasureRarity(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
