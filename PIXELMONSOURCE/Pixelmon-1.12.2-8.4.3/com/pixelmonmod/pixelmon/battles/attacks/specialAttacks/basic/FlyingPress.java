package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.List;

public class FlyingPress extends SpecialAttackBase {
   public double modifyTypeEffectiveness(List effectiveTypes, EnumType moveType, double baseEffectiveness) {
      double effectiveness = baseEffectiveness * (double)EnumType.getTotalEffectiveness(effectiveTypes, EnumType.Flying);
      return effectiveness;
   }
}
