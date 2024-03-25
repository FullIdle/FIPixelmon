package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class DoubleOnHalf extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.getHealthPercent() <= 50.0F) {
         user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() * 2);
      }

      return AttackResult.proceed;
   }
}
