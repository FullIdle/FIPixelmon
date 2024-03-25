package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Assurance extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.damageTakenThisTurn > 0) {
         user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() * 2);
      }

      return AttackResult.proceed;
   }
}
