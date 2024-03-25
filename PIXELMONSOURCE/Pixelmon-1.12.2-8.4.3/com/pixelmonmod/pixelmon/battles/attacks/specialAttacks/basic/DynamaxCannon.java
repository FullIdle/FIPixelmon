package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class DynamaxCannon extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.isDynamax > 0) {
         user.attack.overridePower = user.attack.getMove().getBasePower() * 2;
      }

      return AttackResult.proceed;
   }
}
