package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class RevelationDance extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      EnumType type = !user.type.isEmpty() && user.type.get(0) != null ? (EnumType)user.type.get(0) : EnumType.Mystery;
      user.attack.overrideType(type);
      return AttackResult.proceed;
   }
}
