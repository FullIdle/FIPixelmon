package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import net.minecraft.util.text.translation.I18n;

public enum EnumCurryRating {
   KOFFING(0.25, false, false, 200, 10, ExperienceGainType.CURRY_KOFFING),
   WOBBUFFET(0.5, false, false, 480, 25, ExperienceGainType.CURRY_WOBBUFFET),
   MILCERY(1.0, false, true, 1050, 40, ExperienceGainType.CURRY_MILCERY),
   COPPERAJAH(1.0, true, true, 1890, 50, ExperienceGainType.CURRY_COPPERAJAH),
   CHARIZARD(1.0, true, true, 2980, 75, ExperienceGainType.CURRY_CHARIZARD);

   public final double hpHeal;
   public final boolean ppRestore;
   public final boolean statusCure;
   public final int expBoost;
   public final int happinessBoost;
   private final ExperienceGainType gainType;

   private EnumCurryRating(double hpHeal, boolean ppRestore, boolean statusCure, int expBoost, int happinessBoost, ExperienceGainType gainType) {
      this.hpHeal = hpHeal;
      this.ppRestore = ppRestore;
      this.statusCure = statusCure;
      this.expBoost = expBoost;
      this.happinessBoost = happinessBoost;
      this.gainType = gainType;
   }

   public static EnumCurryRating ratingFromQuality(int quality) {
      if (quality < 15) {
         return KOFFING;
      } else if (quality < 40) {
         return WOBBUFFET;
      } else if (quality < 75) {
         return MILCERY;
      } else {
         return quality < 125 ? COPPERAJAH : CHARIZARD;
      }
   }

   public String getLocalizedName() {
      switch (this) {
         case WOBBUFFET:
            return I18n.func_74838_a("pixelmon.wobbuffet.name");
         case MILCERY:
            return I18n.func_74838_a("pixelmon.milcery.name");
         case COPPERAJAH:
            return I18n.func_74838_a("pixelmon.copperajah.name");
         case CHARIZARD:
            return I18n.func_74838_a("pixelmon.charizard.name");
         default:
            return I18n.func_74838_a("pixelmon.koffing.name");
      }
   }

   public ExperienceGainType getGainType() {
      return this.gainType;
   }
}
