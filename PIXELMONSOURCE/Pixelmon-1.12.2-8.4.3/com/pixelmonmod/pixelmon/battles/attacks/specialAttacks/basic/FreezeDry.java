package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.List;

public class FreezeDry extends SpecialAttackBase {
   private double modifyEffectivenessInternal(List effectiveTypes, EnumType moveType, double baseEffectiveness, boolean inverse) {
      double effectiveness = baseEffectiveness;
      double waterEffectiveness = (double)EnumType.getEffectiveness(moveType, EnumType.Water, inverse);
      if (waterEffectiveness <= 1.0 && effectiveTypes.contains(EnumType.Water)) {
         effectiveness = baseEffectiveness * 2.0;
         if (waterEffectiveness <= 0.5) {
            effectiveness *= 2.0;
         }
      }

      return effectiveness;
   }

   public double modifyTypeEffectiveness(List effectiveTypes, EnumType moveType, double baseEffectiveness, BattleControllerBase bc) {
      return this.modifyEffectivenessInternal(effectiveTypes, moveType, baseEffectiveness, bc != null ? bc.rules.hasClause("inverse") : false);
   }

   public double modifyTypeEffectiveness(List effectiveTypes, EnumType moveType, double baseEffectiveness) {
      return this.modifyEffectivenessInternal(effectiveTypes, moveType, baseEffectiveness, false);
   }
}
